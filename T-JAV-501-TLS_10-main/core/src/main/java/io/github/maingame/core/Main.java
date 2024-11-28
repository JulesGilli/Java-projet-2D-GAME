package io.github.maingame.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.maingame.scenes.MainMenuScreen;
import io.github.maingame.utils.SoundManager;

public class Main extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    private SoundManager soundManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        soundManager = new SoundManager();
        soundManager.initialize();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        this.setScreen(new MainMenuScreen(this));
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            Gdx.app.log("Main", "Disposing batch");
            batch.dispose();
            batch = null;
        }
        if (getScreen() != null) {
            getScreen().dispose();
            setScreen(null);
        }
    }


}
