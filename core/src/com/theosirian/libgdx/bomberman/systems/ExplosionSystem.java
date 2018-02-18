package com.theosirian.libgdx.bomberman.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.theosirian.libgdx.bomberman.components.*;
import com.theosirian.libgdx.bomberman.entity.FireEntity;
import com.theosirian.libgdx.bomberman.util.Constants;
import com.theosirian.libgdx.bomberman.util.Mappers;
import com.theosirian.libgdx.bomberman.util.Pools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static com.theosirian.libgdx.bomberman.systems.ExplosionSystem.Extension.HORIZONTAL;
import static com.theosirian.libgdx.bomberman.systems.ExplosionSystem.Extension.VERTICAL;
import static com.theosirian.libgdx.bomberman.systems.ExplosionSystem.Tip.*;

public class ExplosionSystem extends IteratingSystem {
	private Family destructibleFamily;
	private ImmutableArray<Entity> destructibleEntities;

	private Family collisionFamily;
	private ImmutableArray<Entity> collisionEntities;

	public ExplosionSystem() {
		super(Family.all(PositionComponent.class, ExplosionComponent.class).get());
		destructibleFamily = Family
				.all(PositionComponent.class, HitboxComponent.class,
						DestructibleComponent.class)
				.get();
		collisionFamily = Family
				.all(PositionComponent.class, HitboxComponent.class,
						StaticColliderComponent.class)
				.exclude(DestructibleComponent.class)
				.get();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		destructibleEntities = engine.getEntitiesFor(destructibleFamily);
		collisionEntities = engine.getEntitiesFor(collisionFamily);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		destructibleEntities = null;
		collisionEntities = null;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ExplosionComponent explosion = Mappers.explosionMapper.get(entity);
		explosion.timer -= deltaTime;
		if (explosion.timer <= 0f) {
			PositionComponent position = Mappers.positionMapper.get(entity);
			explodeCenter(position.x, position.y, explosion.range);
			getEngine().removeEntity(entity);
		}
	}

	public enum Tip {
		UP(0, 16),
		DOWN(0, -16),
		LEFT(-16, 0),
		RIGHT(16, 0);

		private Tip opposite;

		static {
			UP.opposite = DOWN;
			DOWN.opposite = UP;
			LEFT.opposite = RIGHT;
			RIGHT.opposite = LEFT;
		}

		private final int dx;
		private final int dy;

		Tip(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		public int dx() {
			return dx;
		}

		public int dy() {
			return dy;
		}

		public Tip opposite() {
			return opposite;
		}
	}

	public enum Extension {
		HORIZONTAL,
		VERTICAL
	}

	private void explodeCenter(int x, int y, int range) {
		HashMap<Vector2, Entity> cross = new HashMap<>();
		explodeLine(x + UP.dx(), y + UP.dy(), range, UP, VERTICAL, cross);
		explodeLine(x + DOWN.dx(), y + DOWN.dy(), range, DOWN, VERTICAL, cross);
		explodeLine(x + LEFT.dx(), y + LEFT.dy(), range, LEFT, HORIZONTAL, cross);
		explodeLine(x + RIGHT.dx(), y + RIGHT.dy(), range, RIGHT, HORIZONTAL, cross);

		final Rectangle rect = Pools.rectPool.obtain();
		rect.set(x, y, Constants.kObstacleWidth, Constants.kObstacleHeight);
		Optional<Entity> destructible = Arrays.stream(destructibleEntities.toArray()).filter((d) -> collides(rect, d)).findFirst();
		destructible.ifPresent(entity -> getEngine().removeEntity(entity));

		cross.put(new Vector2(x, y), new FireEntity(x, y, true));
		cross.forEach((k, v) -> getEngine().addEntity(v));
	}

	private void explodeLine(final int px, final int py, int range, final Tip tip, Extension extension, HashMap<Vector2, Entity> cross) {
		if (range > 0) {
			final Rectangle rect = Pools.rectPool.obtain();
			rect.set(px, py, Constants.kObstacleWidth, Constants.kObstacleHeight);
			Optional<Entity> destructible = Arrays.stream(destructibleEntities.toArray()).filter((d) -> collides(rect, d)).findFirst();
			if (destructible.isPresent()) {
				cross.put(new Vector2(px, py), new FireEntity(px, py, tip));
				getEngine().removeEntity(destructible.get());
			} else if (Arrays.stream(collisionEntities.toArray()).anyMatch((c) -> collides(rect, c))) {
				int x = px + tip.opposite().dx();
				int y = py + tip.opposite().dy();
				cross.put(new Vector2(x, y), new FireEntity(x, y, tip));
			} else {
				cross.put(new Vector2(px, py), new FireEntity(px, py, extension));
				explodeLine(px + tip.dx(), py + tip.dy(), range - 1, tip, extension, cross);
			}
			Pools.rectPool.free(rect);
		} else {
			int x = px + tip.opposite().dx();
			int y = py + tip.opposite().dy();
			cross.put(new Vector2(x, y), new FireEntity(x, y, tip));
		}
	}

	private static boolean collides(Rectangle source, Entity object) {
		PositionComponent pos = Mappers.positionMapper.get(object);
		HitboxComponent hitbox = Mappers.hitboxMapper.get(object);
		Rectangle collider = Pools.rectPool.obtain().set(pos.x + hitbox.x, pos.y + hitbox.y, hitbox.width, hitbox.height);
		boolean match = collider.overlaps(source);
		Pools.rectPool.free(collider);
		return match;
	}
}
