package com.theosirian.libgdx.bomberman.util;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.theosirian.libgdx.bomberman.components.*;

import java.util.ArrayList;
import java.util.List;

public class MapLoader {
	private TiledMap tiledMap;
	private TiledMapTileLayer floorLayer, layoutLayer;
	private OrthogonalTiledMapRenderer renderer;
	private List<Rectangle> collisionList;
	private List<Vector2> requiredObstacleList, randomObstacleList;

	public List<Vector2> spawnList;

	public void loadMap(String filename) {
		tiledMap = new TmxMapLoader().load(filename);
		renderer = new OrthogonalTiledMapRenderer(tiledMap);
		floorLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Floor");
		layoutLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Layout");
		MapLayer collisionLayer = tiledMap.getLayers().get("Collision");
		collisionList = new ArrayList<>();
		for (MapObject mapObject : collisionLayer.getObjects()) {
			Rectangle obtain = Pools.rectPool.obtain();
			MapProperties properties = mapObject.getProperties();
			int x = properties.get("x", Float.class).intValue();
			int y = properties.get("y", Float.class).intValue();
			int w = properties.get("width", Float.class).intValue();
			int h = properties.get("height", Float.class).intValue();
			obtain.set(x, y, w, h);
			collisionList.add(obtain);
		}
		MapLayer obstacleLayer = tiledMap.getLayers().get("Obstacles");
		requiredObstacleList = new ArrayList<>();
		randomObstacleList = new ArrayList<>();
		for (MapObject mapObject : obstacleLayer.getObjects()) {
			MapProperties properties = mapObject.getProperties();
			int x = properties.get("x", Float.class).intValue();
			int y = properties.get("y", Float.class).intValue();
			int width = properties.get("width", Float.class).intValue();
			int height = properties.get("height", Float.class).intValue();
			String type = properties.get("type", String.class);
			for (int i = 0; i < width; i += Constants.kObstacleWidth) {
				for (int j = 0; j < height; j += Constants.kObstacleHeight) {
					final int px = i + x, py = j + y;
					final Rectangle rect = Pools.rectPool.obtain();
					rect.set(px, py, Constants.kObstacleWidth, Constants.kObstacleHeight);
					if (collisionList.stream().noneMatch((collider) -> collider.overlaps(rect))) {
						if ("required".equals(type)) {
							Vector2 obtain = Pools.vector2Pool.obtain();
							obtain.set(px, py);
							requiredObstacleList.add(obtain);
						} else if ("random".equals(type)) {
							Vector2 obtain = Pools.vector2Pool.obtain();
							obtain.set(px, py);
							randomObstacleList.add(obtain);
						}
					}
					Pools.rectPool.free(rect);
				}
			}
		}
	}

	public void createEntities(Engine world) {
		for (Rectangle rect : collisionList) {
			Entity entity = new Entity();

			PositionComponent position = new PositionComponent();
			position.x = ((int) rect.x);
			position.y = ((int) rect.y);
			entity.add(position);

			HitboxComponent hitbox = new HitboxComponent();
			hitbox.x = 0;
			hitbox.y = 0;
			hitbox.width = ((int) rect.width);
			hitbox.height = ((int) rect.height);
			entity.add(hitbox);

			entity.add(new StaticColliderComponent());

			world.addEntity(entity);
		}
		for (Vector2 vec2 : requiredObstacleList)
			world.addEntity(createObstacle(vec2));

		for (Vector2 vec2 : randomObstacleList)
			if (MathUtils.randomBoolean())
				world.addEntity(createObstacle(vec2));
	}

	private Entity createObstacle(Vector2 vec2) {
		Entity entity = new Entity();

		PositionComponent position = new PositionComponent();
		position.x = ((int) vec2.x);
		position.y = ((int) vec2.y);
		entity.add(position);

		HitboxComponent hitbox = new HitboxComponent();
		hitbox.x = 0;
		hitbox.y = 0;
		hitbox.width = Constants.kObstacleWidth;
		hitbox.height = Constants.kObstacleHeight;
		entity.add(hitbox);

		RenderComponent render = new RenderComponent();
		render.animation = new Animation<>(1f, Textures.obstacleRegion);
		entity.add(render);

		entity.add(new StaticColliderComponent());

		entity.add(new DestructibleComponent());
		return entity;
	}

	public void render(OrthographicCamera camera) {
		renderer.setView(camera);
		renderer.getBatch().begin();
		renderer.renderTileLayer(floorLayer);
		renderer.renderTileLayer(layoutLayer);
		renderer.getBatch().end();
	}

	public void dispose() {
		for (Vector2 vec2 : randomObstacleList) Pools.vector2Pool.free(vec2);
		for (Vector2 vec2 : requiredObstacleList) Pools.vector2Pool.free(vec2);
		for (Rectangle rect : collisionList) Pools.rectPool.free(rect);
		renderer.dispose();
		tiledMap.dispose();
	}
}
