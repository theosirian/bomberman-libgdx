package com.theosirian.libgdx.bomberman.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.theosirian.libgdx.bomberman.components.InputComponent;
import com.theosirian.libgdx.bomberman.components.PositionComponent;
import com.theosirian.libgdx.bomberman.entity.BombEntity;
import com.theosirian.libgdx.bomberman.util.Mappers;

public class BombSystem extends IteratingSystem {
	public BombSystem() {
		super(Family.all(PositionComponent.class,
				InputComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Mappers.positionMapper.get(entity);
		InputComponent input = Mappers.inputMapper.get(entity);
		if (input.bomb) {
			getEngine().addEntity(new BombEntity(position.x, position.y));
			input.bomb = false;
		}
	}
}
