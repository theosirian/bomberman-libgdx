package com.theosirian.secomp.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.theosirian.secomp.components.HitboxComponent;
import com.theosirian.secomp.components.InputComponent;
import com.theosirian.secomp.components.PositionComponent;
import com.theosirian.secomp.components.RenderComponent;

public class PlayerEntity extends Entity implements InputProcessor {

	private final InputComponent input;

	public PlayerEntity(int x, int y, Animation<TextureRegion> animation) {
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
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.UP:
				input.up = true;
				break;

			case Input.Keys.DOWN:
				input.down = true;
				break;

			case Input.Keys.LEFT:
				input.left = true;
				break;

			case Input.Keys.RIGHT:
				input.right = true;
				break;

			case Input.Keys.SPACE:
				input.bomb = true;
				break;

			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.UP:
				input.up = false;
				break;

			case Input.Keys.DOWN:
				input.down = false;
				break;

			case Input.Keys.LEFT:
				input.left = false;
				break;

			case Input.Keys.RIGHT:
				input.right = false;
				break;

			case Input.Keys.SPACE:
				input.bomb = false;
				break;

			default:
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
