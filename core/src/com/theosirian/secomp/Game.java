package com.theosirian.secomp;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.theosirian.secomp.entity.PlayerEntity;
import com.theosirian.secomp.systems.*;
import com.theosirian.secomp.util.MapLoader;
import com.theosirian.secomp.util.Textures;

public class Game extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	private ShapeRenderer shape;
	private Engine world, render;
	private PlayerEntity player;
	private OrthographicCamera camera;
	private FitViewport fitViewport;
	private MapLoader mapLoader;

	private void loadTextures() {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		Textures.playerOneTexture = new Texture("player.one.png");
		Textures.playerOneRegions = new TextureRegion[2];
		for (int i = 0; i < 2; i++)
			Textures.playerOneRegions[i] =
					new TextureRegion(Textures.playerOneTexture, i * 16, 0, 16, 16);
		Textures.playerOneAnimation =
				new Animation<TextureRegion>(0.4f, Textures.playerOneRegions);

		Textures.obstacleTexture = new Texture("obstacle.png");
		Textures.obstacleRegion =
				new TextureRegion(Textures.obstacleTexture, 0, 0, 16, 16);
		Textures.obstacleAnimation =
				new Animation<TextureRegion>(1f, Textures.obstacleRegion);

		Textures.bombTexture = new Texture("bomb.png");
		Textures.bombRegions = new TextureRegion[2];
		for (int i = 0; i < 2; i++)
			Textures.bombRegions[i] =
					new TextureRegion(Textures.bombTexture, i * 16, 0, 16, 16);
		Textures.bombAnimation =
				new Animation<TextureRegion>(0.2f, Textures.bombRegions);

		Textures.bombCenter =
				new TextureRegion(Textures.bombTexture, 2 * 16, 0, 16, 16);
		Textures.bombUp =
				new TextureRegion(Textures.bombTexture, 3 * 16, 0, 16, 16);
		Textures.bombDown =
				new TextureRegion(Textures.bombTexture, 4 * 16, 0, 16, 16);
		Textures.bombLeft =
				new TextureRegion(Textures.bombTexture, 5 * 16, 0, 16, 16);
		Textures.bombRight =
				new TextureRegion(Textures.bombTexture, 6 * 16, 0, 16, 16);
		Textures.bombVertical =
				new TextureRegion(Textures.bombTexture, 7 * 16, 0, 16, 16);
		Textures.bombHorizontal =
				new TextureRegion(Textures.bombTexture, 8 * 16, 0, 16, 16);
	}

	private void initializeWorld() {
		world = new Engine();
		world.addSystem(new MovementSystem());
		world.addSystem(new BombSystem());
		world.addSystem(new ExplosionSystem());
		world.addSystem(new DestructionSystem());
		world.addEntityListener(new EntityListener() {
			@Override
			public void entityAdded(Entity entity) {
				render.addEntity(entity);
			}

			@Override
			public void entityRemoved(Entity entity) {
				render.removeEntity(entity);
			}
		});
		render = new Engine();
		render.addSystem(new RenderSystem(batch));

		player = new PlayerEntity(16, 16, Textures.playerOneAnimation);
		world.addEntity(player);

		mapLoader.createEntities(world);

		Gdx.input.setInputProcessor(new InputMultiplexer(this, player));

		camera = new OrthographicCamera();
		fitViewport = new FitViewport(17 * 16, 17 * 16, camera);
		fitViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fitViewport.apply();

		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	}

	@Override
	public void create() {
		mapLoader = new MapLoader();
		mapLoader.loadMap("map.tmx");
		loadTextures();
		initializeWorld();
	}

	@Override
	public void resize(int width, int height) {
		fitViewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.update(Gdx.graphics.getDeltaTime());

		camera.update();
		mapLoader.render(camera);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		render.update(Gdx.graphics.getDeltaTime());
		batch.end();
	}

	@Override
	public void dispose() {
		Textures.obstacleTexture.dispose();
		Textures.playerOneTexture.dispose();
		mapLoader.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.ESCAPE:
				Gdx.app.exit();
				break;

			case Input.Keys.R:
				initializeWorld();
				break;

			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
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
