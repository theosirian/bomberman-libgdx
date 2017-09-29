package com.theosirian.secomp.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.theosirian.secomp.components.*;

public class PlayerEntity extends Entity implements InputProcessor {

	public static class InputConfiguration {
		public final int up, down, left, right, bomb;

		public InputConfiguration(int up, int down, int left, int right, int bomb) {
			this.up = up;
			this.down = down;
			this.left = left;
			this.right = right;
			this.bomb = bomb;
		}
	}

	private final InputConfiguration config;
	private final InputComponent input;

	public PlayerEntity(int x, int y, Animation<TextureRegion> animation, InputConfiguration config) {
		this.config = config;
		RenderComponent render = new RenderComponent();
		render.animation = animation;
		this.add(render);

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

		input = new InputComponent();
		this.add(input);

		this.add(new DestructibleComponent());
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == config.up) {
			input.up = true;
		} else if (keycode == config.down) {
			input.down = true;
		} else if (keycode == config.left) {
			input.left = true;
		} else if (keycode == config.right) {
			input.right = true;
		} else if (keycode == config.bomb) {
			input.bomb = true;
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == config.up) {
			input.up = false;
		} else if (keycode == config.down) {
			input.down = false;
		} else if (keycode == config.left) {
			input.left = false;
		} else if (keycode == config.right) {
			input.right = false;
		} else if (keycode == config.bomb) {
			input.bomb = false;
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
