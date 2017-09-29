package com.theosirian.secomp.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.theosirian.secomp.components.*;
import com.theosirian.secomp.entity.FireEntity;
import com.theosirian.secomp.util.Mappers;

import java.util.Arrays;
import java.util.Optional;

import static com.theosirian.secomp.systems.ExplosionSystem.Extension.HORIZONTAL;
import static com.theosirian.secomp.systems.ExplosionSystem.Extension.VERTICAL;
import static com.theosirian.secomp.systems.ExplosionSystem.Tip.*;

public class ExplosionSystem extends IteratingSystem {
	private Family destructibleFamily;
	private ImmutableArray<Entity> destructibleEntities;

	private Family collisionFamily;
	private ImmutableArray<Entity> collisionEntities;

	public ExplosionSystem() {
		super(Family.all(
				PositionComponent.class,
				ExplosionComponent.class).get());
		destructibleFamily = Family.all(
				PositionComponent.class,
				HitboxComponent.class,
				DestructibleComponent.class).get();
		collisionFamily = Family.all(
				PositionComponent.class,
				HitboxComponent.class,
				StaticColliderComponent.class)
				.exclude(DestructibleComponent.class).get();
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
		UP, DOWN, LEFT, RIGHT
	}

	public enum Extension {
		HORIZONTAL, VERTICAL
	}

	private void explodeCenter(int x, int y, int range) {
		getEngine().addEntity(new FireEntity(x, y, false));
		explodeLine(x, y + 16, range, UP, VERTICAL);
		explodeLine(x, y - 16, range, DOWN, VERTICAL);
		explodeLine(x - 16, y, range, LEFT, HORIZONTAL);
		explodeLine(x + 16, y, range, RIGHT, HORIZONTAL);
	}

	private void explodeLine(final int px, final int py, int range, Tip tip, Extension extension) {
		Optional<Entity> first =
				Arrays.stream(destructibleEntities.toArray()).filter((d) ->
						Mappers.positionMapper.get(d).x == px &&
								Mappers.positionMapper.get(d).y == py).findFirst();
		if (first.isPresent()) {
			getEngine().addEntity(new FireEntity(px, py, tip));
			getEngine().removeEntity(first.get());
		} else {
			switch (tip) {
				case UP:
					if (Arrays.stream(collisionEntities.toArray()).noneMatch((c) ->
							Mappers.positionMapper.get(c).x == px &&
									Mappers.positionMapper.get(c).y == py + 16))
						getEngine().addEntity(new FireEntity(px, py, tip));
					else if (range == 0)
						getEngine().addEntity(new FireEntity(px, py, tip));
					else {
						getEngine().addEntity(new FireEntity(px, py, extension));
						explodeLine(px, py + 16, range - 1, tip, extension);
					}
					break;
				case DOWN:
					if (Arrays.stream(collisionEntities.toArray()).noneMatch((c) ->
							Mappers.positionMapper.get(c).x == px &&
									Mappers.positionMapper.get(c).y == py - 16))
						getEngine().addEntity(new FireEntity(px, py, tip));
					else if (range == 0)
						getEngine().addEntity(new FireEntity(px, py, tip));
					else {
						getEngine().addEntity(new FireEntity(px, py, extension));
						explodeLine(px, py - 16, range - 1, tip, extension);
					}
					break;
				case LEFT:
					if (Arrays.stream(collisionEntities.toArray()).noneMatch((c) ->
							Mappers.positionMapper.get(c).x == px - 16 &&
									Mappers.positionMapper.get(c).y == py))
						getEngine().addEntity(new FireEntity(px, py, tip));
					else if (range == 0)
						getEngine().addEntity(new FireEntity(px, py, tip));
					else {
						getEngine().addEntity(new FireEntity(px, py, extension));
						explodeLine(px - 16, py, range - 1, tip, extension);
					}
					break;
				case RIGHT:
					if (Arrays.stream(collisionEntities.toArray()).noneMatch((c) ->
							Mappers.positionMapper.get(c).x == px + 16 &&
									Mappers.positionMapper.get(c).y == py))
						getEngine().addEntity(new FireEntity(px, py, tip));
					else if (range == 0)
						getEngine().addEntity(new FireEntity(px, py, tip));
					else {
						getEngine().addEntity(new FireEntity(px, py, extension));
						explodeLine(px + 16, py, range - 1, tip, extension);
					}
					break;
			}
		}
	}
}
