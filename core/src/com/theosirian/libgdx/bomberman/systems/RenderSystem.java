package com.theosirian.libgdx.bomberman.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.theosirian.libgdx.bomberman.components.PositionComponent;
import com.theosirian.libgdx.bomberman.components.RenderComponent;
import com.theosirian.libgdx.bomberman.util.Mappers;

public class RenderSystem extends IteratingSystem {
	private final SpriteBatch batch;

	public RenderSystem(SpriteBatch batch) {
		super(Family
				.all(PositionComponent.class, RenderComponent.class)
				.get());
		this.batch = batch;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Mappers.positionMapper.get(entity);
		RenderComponent render = Mappers.renderMapper.get(entity);
		render.timer += deltaTime;
		batch.draw(render.animation.getKeyFrame(render.timer, true), position.x, position.y);
	}
}
