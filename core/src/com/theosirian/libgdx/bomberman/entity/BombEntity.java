package com.theosirian.libgdx.bomberman.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.theosirian.libgdx.bomberman.components.*;
import com.theosirian.libgdx.bomberman.util.Textures;

public class BombEntity extends Entity {

	public BombEntity(int x, int y) {
		PositionComponent position = new PositionComponent();
		position.x = x;
		position.y = y;
		this.add(position);

		HitboxComponent hitbox = new HitboxComponent();
		hitbox.x = 0;
		hitbox.y = 0;
		hitbox.width = 16;
		hitbox.height = 16;
		this.add(hitbox);

		this.add(new StaticColliderComponent());

		RenderComponent render = new RenderComponent();
		render.animation = new Animation<>(0.2f, Textures.bombRegions);
		this.add(render);

		ExplosionComponent explosion = new ExplosionComponent();
		explosion.range = 1;
		explosion.timer = 2f;
		this.add(explosion);
	}
}
