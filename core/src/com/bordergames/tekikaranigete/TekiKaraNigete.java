package com.bordergames.tekikaranigete;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class TekiKaraNigete extends ApplicationAdapter {

	static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private OrthographicCamera camera;
	private SpriteBatch batch;
    private Texture background;
    private float rotationSpeed = 0.5f;
    private float moveSpeed = 0.5f;
    private float aspectRatio;
	
	@Override
	public void create () {
        background = new Texture(Gdx.files.internal("bg.jpg"));
        aspectRatio = 1f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight(); // соотношение сторон

        camera = new OrthographicCamera(200, 200 * aspectRatio);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

		batch = new SpriteBatch();
	}

    private void update(float dt) {
        handleInput(dt);
    }

	@Override
	public void render () {
        update(Gdx.graphics.getDeltaTime());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
//		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        batch.draw(background, 0, 0, background.getWidth(), background.getHeight());
		batch.end();
	}

    private void handleInput(float dt) {
        /* Zooming */
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.zoom -= 0.02;
        }
        /* Moving */
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-moveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(moveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, moveSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -moveSpeed);
        }
        /* Rotation */
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.rotate(rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(-rotationSpeed, 0, 0, 1);
        }

        /* Запрет на слишком сильное увеличение и отдаление */
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, background.getHeight() / aspectRatio / camera.viewportWidth);

        /* Запрет на выход за пределы карты */
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, background.getWidth() - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, background.getHeight() - effectiveViewportHeight / 2f);

    }

    @Override
	public void dispose () {
        background.dispose();
		batch.dispose();
	}

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 200f;
        camera.viewportHeight = 200f * aspectRatio;
    }
}
