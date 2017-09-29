package com.theosirian.secomp.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.theosirian.secomp.components.*;

public class Mappers {
	public static final ComponentMapper<PositionComponent>
			positionMapper =
			ComponentMapper.getFor(PositionComponent.class);

	public static final ComponentMapper<InputComponent>
			inputMapper =
			ComponentMapper.getFor(InputComponent.class);

	public static final ComponentMapper<RenderComponent>
			renderMapper =
			ComponentMapper.getFor(RenderComponent.class);

	public static final ComponentMapper<HitboxComponent>
			hitboxMapper =
			ComponentMapper.getFor(HitboxComponent.class);

	public static final ComponentMapper<ExplosionComponent>
			explosionMapper =
			ComponentMapper.getFor(ExplosionComponent.class);

	public static final ComponentMapper<DestroyComponent>
			destroyMapper =
			ComponentMapper.getFor(DestroyComponent.class);
}
