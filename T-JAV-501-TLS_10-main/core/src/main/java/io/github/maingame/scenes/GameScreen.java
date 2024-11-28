package io.github.maingame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import io.github.maingame.core.GameStat;
import io.github.maingame.core.Main;
import io.github.maingame.entities.Player;
import io.github.maingame.input.InputManager;
import io.github.maingame.input.PlayerInputHandler;
import io.github.maingame.utils.Platform;
import io.github.maingame.utils.SoundManager;
import io.github.maingame.utils.TextureManager;

public class GameScreen extends ScreenAdapter {
    final Player player;
    private final SpriteBatch batch;
    private final Texture background1, background2, background3, background4a, background4b;
    private final OrthographicCamera camera;
    private final OrthographicCamera hudCamera;
    private final GameStat stat;
    private final HUD hud;
    private final PlayerInputHandler playerInputHandler;
    private final EnemyManager enemyManager;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private boolean isWaveTransition = false;
    private boolean hasPlayedPassWaveSound = false;
    private float waveTransitionTimer = 0f;
    private boolean isTutorial = true;
    private Main game;

    public GameScreen(Main game, GameStat stat, Player player) {
        this.stat = stat;
        this.player = player;
        this.game = game;
        this.isTutorial = stat.isFirstGame();
        this.playerInputHandler = new PlayerInputHandler(player);
        this.batch = game.batch;
        this.hud = new HUD(game, stat, player);
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.enemyManager = new EnemyManager(stat, player, camera, game.getSoundManager());

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        hudCamera.position.set(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2, 0);
        hudCamera.update();

        background1 = new Texture(Gdx.files.internal("backgrounds/background_gamescreen1.png"));
        background2 = new Texture(Gdx.files.internal("backgrounds/background_gamescreen2.png"));
        background3 = new Texture(Gdx.files.internal("backgrounds/background_gamescreen3.png"));
        background4a = new Texture(Gdx.files.internal("backgrounds/background_gamescreen4a.png"));
        background4b = new Texture(Gdx.files.internal("backgrounds/background_gamescreen4b.png"));

        stat.loadGame();
        updatePlayerKeys();
        Platform.createPlatforms();
        enemyManager.setupFloorEnemies();

        game.getSoundManager().playMusic("fight", true, 0.2f);
    }

    @Override
    public void render(float delta) {
        handleInput();
        clearScreen();
        batch.begin();

        if (isTutorial) {
            game.getSoundManager().playMusic("fight", true, 0.1f);
        } else {
            game.getSoundManager().playMusic("fight", true, 0.2f);
        }

        if (isPaused) {
            game.getSoundManager().setVolume("fight",0.1f);
            renderPausedState();
            return;
        }
        if (handleWaveTransition(delta)) {
            return;
        }
        checkGameOverState();
        updateCamera();
        batch.setProjectionMatrix(camera.combined);
        renderGameElements(delta);

        if (!isTutorial) {
            enemyManager.spawnEnemies(delta, batch);
        }

        batch.setProjectionMatrix(hudCamera.combined);
        renderHUD();
        handleNextWave();
        batch.end();
    }

    public void resumeGame() {
        isPaused = false;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
        }
    }

    private void clearScreen() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void renderPausedState() {
        batch.setProjectionMatrix(hudCamera.combined);
        hud.renderPauseMenu(batch);
        batch.end();
    }

    private boolean handleWaveTransition(float delta) {
        if (isWaveTransition) {
            waveTransitionTimer += delta;
            float waveTransitionDuration = 2f;

            if (!hasPlayedPassWaveSound) {
                game.getSoundManager().playSound("passWave");
                hasPlayedPassWaveSound = true;
            }

            if (waveTransitionTimer >= waveTransitionDuration) {
                isWaveTransition = false;
                waveTransitionTimer = 0f;

                hasPlayedPassWaveSound = false;
            } else {
                float alpha = MathUtils.clamp(
                    1.0f - Math.abs(waveTransitionTimer - waveTransitionDuration / 2) / (waveTransitionDuration / 2), 0, 1
                );
                batch.setProjectionMatrix(hudCamera.combined);
                hud.renderWaveTransition(batch, stat.getFloors(), alpha);
                batch.end();
                return true;
            }
        }
        return false;
    }


    private void checkGameOverState() {
        if (player.getHealth() <= 0 && player.isDeathAnimationFinished() && !isGameOver) {
            stat.saveGame();
            stat.setDeaths(stat.getDeaths() + 1);
            isGameOver = true;
        }
    }

    private void updateCamera() {
        float targetCameraX = player.getPosition().x + 300;
        camera.position.x += (targetCameraX - camera.position.x) * 0.05f;
        float minX = 0;
        float maxX = 3000;
        camera.position.x = MathUtils.clamp(camera.position.x, minX + camera.viewportWidth / 2, maxX - camera.viewportWidth / 2);
        camera.position.y = (float) Gdx.graphics.getHeight() / 2;
        camera.update();
    }

    private void renderGameElements(float delta) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        drawBackground(screenWidth, screenHeight);
        drawPlatforms();
        player.render(batch);

        if (!isTutorial && !isGameOver) {
            float rightBoundary = 3200;
            float leftBoundary = -200;
            player.update(delta, enemyManager.getEnemies(), leftBoundary, rightBoundary);
        }
    }

    private void renderHUD() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        if (isTutorial) {
            hud.renderFirstGameInstructions(batch,
                InputManager.getLeftKey(),
                InputManager.getRightKey(),
                InputManager.getJumpKey(),
                InputManager.getAttackKey(),
                InputManager.getRollKey(),
                InputManager.getPotionKey());
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                isTutorial = false;
                stat.setFirstGame(false);
                stat.saveGame();
            }
        } else {
            hud.render(batch, player, screenWidth, screenHeight, isGameOver);
        }
    }

    private void handleNextWave() {
        if (!isTutorial
            && enemyManager.getEnemies().isEmpty()
            && !isWaveTransition
            && enemyManager.isSpawnListEmpty()) {
            stat.setFloors(stat.getFloors() + 1);
            stat.setMaxFloors(Math.max(stat.getFloors(), stat.getMaxFloors()));
            enemyManager.setupFloorEnemies();
            isWaveTransition = true;
        }
    }

    private void drawBackground(float screenWidth, float screenHeight) {
        float parallaxFactor1 = 0.95f;
        float parallaxFactor2 = 0.9f;
        float parallaxFactor3 = 0.85f;
        float parallaxFactor4a = 0.8f;
        float parallaxFactor4b = 0.75f;
        float backgroundX = camera.position.x - camera.viewportWidth / 2;
        float backgroundY = camera.position.y - camera.viewportHeight / 2;
        float backgroundWidth1 = screenWidth / parallaxFactor1;
        float backgroundWidth2 = screenWidth / parallaxFactor2;
        float backgroundWidth3 = screenWidth / parallaxFactor3;
        float backgroundWidth4a = screenWidth / parallaxFactor4a;
        float backgroundWidth4b = screenWidth / parallaxFactor4b;
        batch.draw(background1, backgroundX * parallaxFactor1, backgroundY, backgroundWidth1, screenHeight);
        batch.draw(background2, backgroundX * parallaxFactor2, backgroundY, backgroundWidth2, screenHeight);
        batch.draw(background3, backgroundX * parallaxFactor3, backgroundY, backgroundWidth3, screenHeight);
        batch.draw(background4a, backgroundX * parallaxFactor4a, backgroundY, backgroundWidth4a, screenHeight);
        batch.draw(background4b, backgroundX * parallaxFactor4b, backgroundY, backgroundWidth4b, screenHeight);
    }

    private void drawPlatforms() {
        for (Platform platform : Platform.getPlatforms()) {
            platform.render(batch);
        }
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.app.log("GameScreen", "Stopping gameplay music");
        game.getSoundManager().stopMusic("gameplay");
    }


    @Override
    public void show() {
        if (ShopScreen.comingFromShop) {
            player.prepareForNewGame();
            stat.setFloors(0);
            ShopScreen.comingFromShop = false;
            resetGame();
        }
        enemyManager.setupFloorEnemies();
    }

    private void resetGame() {
        player.reset();
        enemyManager.getEnemies().clear();
        enemyManager.setupFloorEnemies();
        isGameOver = false;
    }

    @Override
    public void dispose() {
        hud.dispose();
        if (background1 != null) background1.dispose();
        if (background2 != null) background2.dispose();
        if (background3 != null) background3.dispose();
        if (background4a != null) background4a.dispose();
        if (background4b != null) background4b.dispose();
        TextureManager.dispose();
    }

    public void updatePlayerKeys() {
        playerInputHandler.updateKeys();
    }
}
