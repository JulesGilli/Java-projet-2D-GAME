package io.github.maingame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class TextureManager {
    private static final HashMap<String, Texture> textures = new HashMap<>();

    public static Texture getTexture(String filePath) {
        if (!textures.containsKey(filePath)) {
            textures.put(filePath, new Texture(Gdx.files.internal(filePath)));
        }
        return textures.get(filePath);
    }

    public static void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }
}
