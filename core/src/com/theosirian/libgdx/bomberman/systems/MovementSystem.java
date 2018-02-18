package com.theosirian.libgdx.bomberman.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.theosirian.libgdx.bomberman.components.HitboxComponent;
import com.theosirian.libgdx.bomberman.components.InputComponent;
import com.theosirian.libgdx.bomberman.components.PositionComponent;
import com.theosirian.libgdx.bomberman.components.StaticColliderComponent;
import com.theosirian.libgdx.bomberman.util.Mappers;
import com.theosirian.libgdx.bomberman.util.Pools;

public class MovementSystem extends IteratingSystem {

	private static final float DELAY = 0.1f;
	private Family collisionFamily;
	private ImmutableArray<Entity> collisionEntities;

	public MovementSystem() {
		super(Family
				.all(PositionComponent.class,
						InputComponent.class,
						HitboxComponent.class)
				.get());
		collisionFamily = Family
				.all(PositionComponent.class,
						HitboxComponent.class,
						StaticColliderComponent.class).get();
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		collisionEntities = getEngine().getEntitiesFor(collisionFamily);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		collisionEntities = null;
	}

	private boolean collides(Rectangle player) {
		Rectangle collisionRectangle = Pools.rectPool.obtain();
		for (Entity collisionEntity : collisionEntities) {
			PositionComponent colliderPosition = Mappers.positionMapper.get(collisionEntity);
			HitboxComponent colliderHitbox = Mappers.hitboxMapper.get(collisionEntity);
			collisionRectangle.set(
					colliderPosition.x + colliderHitbox.x,
					colliderPosition.y + colliderHitbox.y,
					colliderHitbox.width,
					colliderHitbox.height);
			if (collisionRectangle.overlaps(player)) {
				Pools.rectPool.free(collisionRectangle);
				return true;
			}
		}
		Pools.rectPool.free(collisionRectangle);
		return false;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Mappers.positionMapper.get(entity);
		HitboxComponent hitbox = Mappers.hitboxMapper.get(entity);
		InputComponent input = Mappers.inputMapper.get(entity);

		input.timer -= deltaTime;

		Rectangle playerHitbox = Pools.rectPool.obtain();
		playerHitbox.set(
				position.x + hitbox.x,
				position.y + hitbox.y,
				hitbox.width,
				hitbox.height);

		if (input.timer <= 0f) {
			if (input.up) {
				int py = position.y + 16;
				playerHitbox.setY(py);
				if (!collides(playerHitbox)) {
					position.y = py;
					input.timer = DELAY;
				}
			} else if (input.down) {
				int py = position.y - 16;
				playerHitbox.setY(py);
				if (!collides(playerHitbox)) {
					position.y = py;
					input.timer = DELAY;
				}
			} else if (input.left) {
				int px = position.x - 16;
				playerHitbox.setX(px);
				if (!collides(playerHitbox)) {
					position.x = px;
					input.timer = DELAY;
				}
			} else if (input.right) {
				int px = position.x + 16;
				playerHitbox.setX(px);
				if (!collides(playerHitbox)) {
					position.x = px;
					input.timer = DELAY;
				}
			}
		}

		Pools.rectPool.free(playerHitbox);
	}
}
