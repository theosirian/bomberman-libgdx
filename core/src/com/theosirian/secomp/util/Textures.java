package com.theosirian.secomp.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Textures {

	public static Texture playerOneTexture;
	public static TextureRegion[] playerOneRegions;
	public static Animation<TextureRegion> playerOneAnimation;

	public static Texture obstacleTexture;
	public static TextureRegion obstacleRegion;
	public static Animation<TextureRegion> obstacleAnimation;

	public static Texture bombTexture;
	public static TextureRegion[] bombRegions;
	public static Animation<TextureRegion> bombAnimation;

	public static TextureRegion bombUp, bombDown,
			bombLeft, bombRight,
			bombVertical, bombHorizontal,
			bombCenter;
}
