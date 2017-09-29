package com.theosirian.secomp.entity;

import com.badlogic.ashley.core.Entity;
import com.theosirian.secomp.components.*;
import com.theosirian.secomp.util.Textures;

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
		render.animation = Textures.bombAnimation;
		this.add(render);

		ExplosionComponent explosion = new ExplosionComponent();
		explosion.range = 2;
		explosion.timer = 2f;
		this.add(explosion);
	}
}
