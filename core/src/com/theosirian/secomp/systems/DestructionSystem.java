package com.theosirian.secomp.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.theosirian.secomp.components.DestroyComponent;
import com.theosirian.secomp.util.Mappers;

public class DestructionSystem extends IteratingSystem {
	public DestructionSystem() {
		super(Family.all(DestroyComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		DestroyComponent destroy = Mappers.destroyMapper.get(entity);
		destroy.timer -= deltaTime;
		if (destroy.timer <= 0f)
			getEngine().removeEntity(entity);
	}
}
