package com.theosirian.secomp.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.theosirian.secomp.components.DestroyComponent;
import com.theosirian.secomp.components.PositionComponent;
import com.theosirian.secomp.components.RenderComponent;
import com.theosirian.secomp.systems.ExplosionSystem;
import com.theosirian.secomp.util.Textures;

public class FireEntity extends Entity {
	public FireEntity(int px, int py, boolean center) {
		PositionComponent position = new PositionComponent();
		position.x = px;
		position.y = py;
		this.add(position);
		DestroyComponent destroy = new DestroyComponent();
		destroy.timer = 0.5f;
		this.add(destroy);
		if (center) {
			RenderComponent render = new RenderComponent();
			render.animation = new Animation<TextureRegion>(1f, Textures.bombCenter);
			this.add(render);
		}
	}

	public FireEntity(int px, int py, ExplosionSystem.Tip tip) {
		this(px, py, false);
		RenderComponent render = new RenderComponent();
		switch (tip) {
			case UP:
				render.animation = new Animation<TextureRegion>(1f, Textures.bombUp);
				break;
			case DOWN:
				render.animation = new Animation<TextureRegion>(1f, Textures.bombDown);
				break;
			case LEFT:
				render.animation = new Animation<TextureRegion>(1f, Textures.bombLeft);
				break;
			case RIGHT:
				render.animation = new Animation<TextureRegion>(1f, Textures.bombRight);
				break;
		}
		this.add(render);
	}

	public FireEntity(int px, int py, ExplosionSystem.Extension extension) {
		this(px, py, false);
		RenderComponent render = new RenderComponent();
		switch (extension) {
			case HORIZONTAL:
				render.animation = new Animation<TextureRegion>(1f, Textures.bombHorizontal);
				break;
			case VERTICAL:
				render.animation = new Animation<TextureRegion>(1f, Textures.bombVertical);
				break;
		}
		this.add(render);
	}
}
