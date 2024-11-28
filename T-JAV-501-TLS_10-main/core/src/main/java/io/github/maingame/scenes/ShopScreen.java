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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.maingame.core.GameStat;
import io.github.maingame.core.Main;
import io.github.maingame.entities.Player;
import io.github.maingame.items.Inventory;
import io.github.maingame.items.Item;
import io.github.maingame.items.Shop;

import java.util.ArrayList;
import java.util.List;


public class ShopScreen extends ScreenAdapter {
    public static boolean comingFromShop = false; // Flag pour indiquer un retour au jeu
    private final Main game;
    private final SpriteBatch batch;
    private final Texture backgroundTexture;
    private final Texture shopTexture;
    private final Shop shop;
    private final List<Item> items;
    private final GameStat stat;
    private final float shopWidth;
    private final float shopHeight;
    private final int screenWidth = Gdx.graphics.getWidth();
    private final int screenHeight = Gdx.graphics.getHeight();
    private final float centerShopWidth;
    private final float centerShopHeight;
    private final float buttonWidth = screenWidth * 0.30f;
    private final float buttonHeight = screenHeight * 0.20f;
    private final float itemWidth = screenWidth * 0.04f;
    private final float itemHeight = screenHeight * 0.08f;
    private final Texture buttonTexture;
    private final com.badlogic.gdx.math.Rectangle playButtonBounds;
    private final com.badlogic.gdx.math.Rectangle mainMenuButtonBounds;
    private ShapeRenderer shapeRenderer;


    private final Player player;
    private BitmapFont font;

    public ShopScreen(Main game, GameStat stat, Player player) {
        this.game = game;
        this.stat = stat;
        this.player = player;
        this.batch = new SpriteBatch();
        this.shop = new Shop(stat, player);
        this.items = shop.getItems();
        this.player.setInventory(new Inventory());
        shopWidth = 0.76f * screenWidth;
        shopHeight = 0.8f * screenHeight;
        centerShopWidth = screenWidth / 2f - shopWidth / 2f;
        centerShopHeight = screenHeight / 2f - shopHeight / 2f;
        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/background_menuscreen.png"));
        shopTexture = new Texture(Gdx.files.internal("backgrounds/background_shop.png"));
        initFonts();
        buttonTexture = new Texture(Gdx.files.internal("GUI/button_basic.png"));
        playButtonBounds = new com.badlogic.gdx.math.Rectangle((screenWidth - buttonWidth) / 2 + buttonWidth / 2, screenHeight * 0.03f, buttonWidth, buttonHeight);
        mainMenuButtonBounds = new com.badlogic.gdx.math.Rectangle((screenWidth - buttonWidth) / 2 - buttonWidth / 2, screenHeight * 0.03f, buttonWidth, buttonHeight);
        System.out.println("ShopScreen");
        shapeRenderer = new ShapeRenderer();
        game.getSoundManager().playMusic("shop", true, 0.3f);
        comingFromShop = false;
    }

    public Vector2 getItemAssetPosition(int number) {
        return new Vector2(centerShopWidth * 2.3f + number % 4 * centerShopWidth * 1.2f, centerShopHeight * 7.35f - number / 4 * centerShopHeight * 2.05f);
    }

    public Vector2 getItemGoldPosition(int number) {
        return new Vector2(centerShopWidth * 2.4f + number % 4 * centerShopWidth * 1.18f, centerShopHeight * 6.9f - number / 4 * centerShopHeight * 2f);
    }

    public Rectangle drawItem(int number) {
        Item item = items.get(number);
        if (!item.isUnlocked(stat)) {
            Texture lock = new Texture(item.getTextureLock());
            batch.draw(lock, getItemAssetPosition(number).x , getItemAssetPosition(number).y, itemWidth, itemHeight);
        } else if (shop.isAvailable(item)) {
            Texture available = new Texture(item.getTextureAvailable());
            batch.draw(available, getItemAssetPosition(number).x, getItemAssetPosition(number).y, itemWidth, itemHeight);

        } else {
            Texture disabled = new Texture(item.getTextureDisabled());
            batch.draw(disabled, getItemAssetPosition(number).x , getItemAssetPosition(number).y,itemWidth, itemHeight);
        }
        if (player.getInventory().inInventory(item)) {
            font.draw(batch, "bought", getItemGoldPosition(number).x - itemWidth/2, getItemGoldPosition(number).y);

        } else if (!item.isUnlocked(stat)){
            font.getData().setScale(0.7f);
            font.draw(batch, shop.unlockCondition(stat,item), getItemGoldPosition(number).x - itemWidth/2, getItemGoldPosition(number).y - itemHeight/10 );
            font.getData().setScale(1f);
        }
        else {
            font.draw(batch, item.getStrGold() , getItemGoldPosition(number).x, getItemGoldPosition(number).y);
        }
        float clickAreaWidth = screenWidth * 0.10f;
        float clickAreaHeight = screenHeight * 0.20f;
        return new Rectangle(getItemGoldPosition(0).x - itemWidth/0.97f + number % 4 * clickAreaWidth * 1.45f, getItemGoldPosition(0).y -itemHeight/1.8f - number / 4 * clickAreaHeight, clickAreaWidth, clickAreaHeight);
    }

    public List<Rectangle> createButtons() {
        List<Rectangle> buttons = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            buttons.add(drawItem(i));
        }
        return buttons;
    }

    public void input(List<Rectangle> listButtons) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 clickPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            if (playButtonBounds.contains(clickPosition)) {
                comingFromShop = true;
                stat.saveGame();
                game.getSoundManager().playSound("select");
                game.getSoundManager().stopMusic("menu");
                game.setScreen(new GameScreen(game, stat, player));
            }
            if (mainMenuButtonBounds.contains(clickPosition)) {
                game.getSoundManager().playSound("select");
                game.setScreen(new MainMenuScreen(game));
            }
            for (int i = 0; i < 12; i++) {
                if (listButtons.get(i).contains(clickPosition)) {
                    System.out.println(i);
                    if (shop.buyItem(stat, items.get(i))) {
                        game.getSoundManager().playSound("buy");
                        Texture disabled = new Texture(items.get(i).getTextureDisabled());
                        batch.draw(disabled, getItemAssetPosition(i).x, getItemAssetPosition(i).y , itemWidth, itemHeight);
                        font.draw(batch, "bought", getItemGoldPosition(i).x, getItemGoldPosition(i).y);
                    }
                }
            }
        }
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Jacquard12-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);
        font.setColor(Color.BROWN);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
        batch.draw(shopTexture, screenWidth / 1.85f - shopWidth/2f , screenHeight/1.65f - shopHeight/2, shopWidth, shopHeight);
        batch.draw(buttonTexture, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height);
        batch.draw(buttonTexture, mainMenuButtonBounds.x, mainMenuButtonBounds.y, mainMenuButtonBounds.width, mainMenuButtonBounds.height);
        font.getData().setScale(1.2f);
        font.draw(batch, "Play", playButtonBounds.x + buttonWidth/2.5f , playButtonBounds.y + buttonHeight/1.7f );
        font.draw(batch, "Main Menu", mainMenuButtonBounds.x + buttonWidth/3f, mainMenuButtonBounds.y + buttonHeight/1.7f);
        font.getData().setScale(1.5f);
        font.draw(batch, "Shop", screenWidth / 2.1f , screenHeight * 0.96f );
        font.getData().setScale(1f);
        font.draw(batch, Integer.toString(stat.getGolds()), centerShopWidth * 7f, centerShopHeight * 8.6f);
        List<Rectangle> listButtons = createButtons();

        input(listButtons);
        batch.end();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (shopTexture != null) {
            shopTexture.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }

}
