package com.theosirian.libgdx.bomberman.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderComponent implements Component {
	public Animation<TextureRegion> animation;
	public float timer;
}
