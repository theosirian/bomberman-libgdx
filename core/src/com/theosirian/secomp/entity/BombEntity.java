package com.theosirian.secomp.entity;

import com.badlogic.ashley.core.Entity;
import com.theosirian.secomp.components.HitboxComponent;
import com.theosirian.secomp.components.PositionComponent;
import com.theosirian.secomp.components.RenderComponent;
import com.theosirian.secomp.components.StaticColliderComponent;
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
	}
}
