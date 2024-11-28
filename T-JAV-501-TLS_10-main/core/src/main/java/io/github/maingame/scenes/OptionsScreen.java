package io.github.maingame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.maingame.core.Main;
import io.github.maingame.input.InputManager;

public class OptionsScreen extends ScreenAdapter {
    private final Main game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final BitmapFont titleFont;
    private final Texture backgroundTexture;
    private final Texture buttonTexture;
    private final Texture backgroundGUI;
    private final Rectangle backButtonBounds;

    private final Rectangle leftKeyBounds;
    private final Rectangle rightKeyBounds;
    private final Rectangle jumpKeyBounds;
    private final Rectangle attackKeyBounds;
    private final Rectangle rollKeyBounds;
    private final Rectangle potionKeyBounds;

    private boolean waitingForNewKey = false;
    private String keyToRemap = "";

    public OptionsScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/background_menuscreen.png"));
        buttonTexture = new Texture(Gdx.files.internal("GUI/button_basic.png"));
        backgroundGUI = new Texture(Gdx.files.internal("backgrounds/background_gamescreen_GUI.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Jacquard12-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 56;
        font = generator.generateFont(parameter);
        font.setColor(Color.BROWN);

        parameter.size = 96;
        titleFont = generator.generateFont(parameter);
        titleFont.setColor(Color.BROWN);

        generator.dispose();

        float buttonWidth = 450;
        float buttonHeight = 150;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        backButtonBounds = new Rectangle((screenWidth - buttonWidth) / 2, 100, buttonWidth, buttonHeight);

        leftKeyBounds = new Rectangle(screenWidth / 2 - 150, screenHeight - 320, 200, 100);
        rightKeyBounds = new Rectangle(screenWidth / 2 - 150, screenHeight - 400, 200, 100);
        jumpKeyBounds = new Rectangle(screenWidth / 2 - 150, screenHeight - 480, 200, 100);
        attackKeyBounds = new Rectangle(screenWidth / 2 - 150, screenHeight - 560, 200, 100);
        rollKeyBounds = new Rectangle(screenWidth / 2 - 150, screenHeight - 640, 200, 100);
        potionKeyBounds = new Rectangle(screenWidth / 2 - 150, screenHeight - 720, 200, 100);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
        batch.draw(backgroundGUI, screenWidth / 2 - backgroundGUI.getWidth() / 1.4f, screenHeight / 2 - backgroundGUI.getHeight() / 1.4f, 600, 800);
        batch.draw(buttonTexture, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height);

        titleFont.draw(batch, "Options", screenWidth / 2 - 150, screenHeight - 180);
        font.draw(batch, "Back", backButtonBounds.x + 170, backButtonBounds.y + 90);

        font.draw(batch, "Left: " + Input.Keys.toString(InputManager.getKey("leftKey")), leftKeyBounds.x, leftKeyBounds.y + 50);
        font.draw(batch, "Right: " + Input.Keys.toString(InputManager.getKey("rightKey")), rightKeyBounds.x, rightKeyBounds.y + 50);
        font.draw(batch, "Jump: " + Input.Keys.toString(InputManager.getKey("jumpKey")), jumpKeyBounds.x, jumpKeyBounds.y + 50);
        font.draw(batch, "Attack: " + Input.Keys.toString(InputManager.getKey("attackKey")), attackKeyBounds.x, attackKeyBounds.y + 50);
        font.draw(batch, "Roll: " + Input.Keys.toString(InputManager.getKey("rollKey")), rollKeyBounds.x, rollKeyBounds.y + 50);
        font.draw(batch, "Potion: " + Input.Keys.toString(InputManager.getKey("potionKey")), potionKeyBounds.x, potionKeyBounds.y + 50);

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 clickPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            if (backButtonBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                game.setScreen(new MainMenuScreen(game));
            } else if (leftKeyBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                waitingForNewKey = true;
                keyToRemap = "leftKey";
            } else if (rightKeyBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                waitingForNewKey = true;
                keyToRemap = "rightKey";
            } else if (jumpKeyBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                waitingForNewKey = true;
                keyToRemap = "jumpKey";
            } else if (attackKeyBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                waitingForNewKey = true;
                keyToRemap = "attackKey";
            } else if (rollKeyBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                waitingForNewKey = true;
                keyToRemap = "rollKey";
            } else if (potionKeyBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                waitingForNewKey = true;
                keyToRemap = "potionKey";
            }
        }
        if (waitingForNewKey) {
            for (int key = 0; key < 256; key++) {
                if (Gdx.input.isKeyJustPressed(key)) {
                    InputManager.setKey(keyToRemap, key);
                    waitingForNewKey = false;
                }
            }
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        titleFont.dispose();
        backgroundTexture.dispose();
        buttonTexture.dispose();
        backgroundGUI.dispose();
    }
}
