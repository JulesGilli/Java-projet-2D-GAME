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
import io.github.maingame.core.GameStat;
import io.github.maingame.core.Main;
import io.github.maingame.entities.Player;
import io.github.maingame.input.PlayerInputHandler;
import io.github.maingame.utils.Platform;
import com.badlogic.gdx.audio.Music;
import io.github.maingame.utils.SoundManager;

public class MainMenuScreen extends ScreenAdapter {
    private final Main game;
    private final SpriteBatch batch;
    private final GameStat stat;
    private final Texture backgroundTexture;
    private final Texture buttonTexture;
    private final Rectangle playButtonBounds;
    private final Rectangle quitButtonBounds;
    private final Rectangle optionButtonBounds;
    private final Rectangle shopButtonBounds;
    private Player player = null;
    private BitmapFont font;
    private BitmapFont titleFont;

    private SoundManager soundManager;

    public MainMenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        stat = new GameStat();

        this.soundManager = game.getSoundManager();


        player = new Player(new Vector2(100, 100), Platform.getPlatforms(), soundManager);

        PlayerInputHandler inputHandler = new PlayerInputHandler(player);
        player.setInputHandler(inputHandler);

        stat.loadGame();

        backgroundTexture = new Texture(Gdx.files.internal("assets/backgrounds/background_menuscreen.png"));
        buttonTexture = new Texture(Gdx.files.internal("assets/GUI/button_basic.png"));

        initFonts();

        float buttonWidth = 600;
        float buttonHeight = 200;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        playButtonBounds = new Rectangle((screenWidth - buttonWidth) / 2, screenHeight / 2 + 150, buttonWidth, buttonHeight);
        shopButtonBounds = new Rectangle((screenWidth - buttonWidth) / 2, screenHeight / 2, buttonWidth, buttonHeight);
        optionButtonBounds = new Rectangle((screenWidth - buttonWidth) / 2, screenHeight / 2 - 150, buttonWidth, buttonHeight);
        quitButtonBounds = new Rectangle((screenWidth - buttonWidth) / 2, screenHeight / 2 - 300, buttonWidth, buttonHeight);

        System.out.println("Main Menu");

        game.getSoundManager().playMusic("menu", true, 0.3f);

    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Jacquard12-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 64;
        font = generator.generateFont(parameter);
        font.setColor(Color.BROWN);

        parameter.size = 96;
        titleFont = generator.generateFont(parameter);
        titleFont.setColor(Color.LIGHT_GRAY);

        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(buttonTexture, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height);
        batch.draw(buttonTexture, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
        batch.draw(buttonTexture, optionButtonBounds.x, optionButtonBounds.y, optionButtonBounds.width, optionButtonBounds.height);
        batch.draw(buttonTexture, shopButtonBounds.x, shopButtonBounds.y, shopButtonBounds.width, shopButtonBounds.height);
        font.draw(batch, "Play", playButtonBounds.x + 250, playButtonBounds.y + 125);
        font.draw(batch, "Shop", shopButtonBounds.x + 235, shopButtonBounds.y + 125);
        font.draw(batch, "Option", optionButtonBounds.x + 220, optionButtonBounds.y + 125);
        font.draw(batch, "Quit", quitButtonBounds.x + 240, quitButtonBounds.y + 125);
        titleFont.draw(batch, "Echoes of the Abyss", 600, 950);

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 clickPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            if (playButtonBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                game.getSoundManager().stopMusic("menu");
                game.setScreen(new GameScreen(game, stat, player));
            }


            if (optionButtonBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                game.setScreen(new OptionsScreen(game));
            }

            if (shopButtonBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                game.setScreen(new ShopScreen(game, stat, player));
            }

            if (quitButtonBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                game.getSoundManager().stopMusic("menu");
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (titleFont != null) titleFont.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
    }

}
