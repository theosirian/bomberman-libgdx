package com.theosirian.secomp.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Pools {

	public static final Pool<Vector2> vector2Pool = new Pool<Vector2>() {
		@Override
		protected void reset(Vector2 object) {
			object.set(0, 0);
		}

		@Override
		protected Vector2 newObject() {
			return new Vector2(0, 0);
		}
	};

	public static final Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected void reset(Rectangle object) {
			object.set(0, 0, 0, 0);
		}

		@Override
		protected Rectangle newObject() {
			return new Rectangle(0, 0, 0, 0);
		}
	};

}
