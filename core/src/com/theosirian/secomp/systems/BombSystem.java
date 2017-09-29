package com.theosirian.secomp.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.theosirian.secomp.components.InputComponent;
import com.theosirian.secomp.components.PositionComponent;
import com.theosirian.secomp.entity.BombEntity;
import com.theosirian.secomp.util.Mappers;

public class BombSystem extends IteratingSystem {
	public BombSystem() {
		super(Family.all(PositionComponent.class,
				InputComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Mappers.positionMapper.get(entity);
		if (Mappers.inputMapper.get(entity).bomb)
			getEngine().addEntity(new BombEntity(position.x, position.y));
	}
}
