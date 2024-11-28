package io.github.maingame.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

public class InputManager {
    private static final String PREFS_NAME = "GamePreferences";
    private static final Preferences preferences = com.badlogic.gdx.Gdx.app.getPreferences(PREFS_NAME);

    public static int getKey(String action) {
        try {
            KeyAction keyAction = findKeyAction(action);
            return preferences.getInteger(keyAction.getPreferenceKey(), keyAction.getDefaultValue());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown action: " + action, e);
        }
    }

    public static void setKey(String action, int key) {
        try {
            KeyAction keyAction = findKeyAction(action);
            preferences.putInteger(keyAction.getPreferenceKey(), key);
            preferences.flush();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown action: " + action, e);
        }
    }

    private static KeyAction findKeyAction(String action) {
        String uppercaseAction = action.toUpperCase();
        for (KeyAction keyAction : KeyAction.values()) {
            if (keyAction.getPreferenceKey().equalsIgnoreCase(action) || keyAction.name().equals(uppercaseAction)) {
                return keyAction;
            }
        }
        throw new IllegalArgumentException("Unknown action: " + action);
    }

    public static int getLeftKey() {
        return getKey("leftKey");
    }

    public static int getRightKey() {
        return getKey("rightKey");
    }

    public static int getJumpKey() {
        return getKey("jumpKey");
    }

    public static int getAttackKey() {
        return getKey("attackKey");
    }

    public static int getRollKey() {
        return getKey("rollKey");
    }

    public static int getPotionKey() {
        return getKey("potionKey");
    }

    private enum KeyAction {
        LEFT("leftKey", Input.Keys.A),
        RIGHT("rightKey", Input.Keys.D),
        JUMP("jumpKey", Input.Keys.SPACE),
        ATTACK("attackKey", Input.Keys.F),
        ROLL("rollKey", Input.Keys.SHIFT_LEFT),
        POTION("potionKey", Input.Keys.E);

        private final String preferenceKey;
        private final int defaultValue;

        KeyAction(String preferenceKey, int defaultValue) {
            this.preferenceKey = preferenceKey;
            this.defaultValue = defaultValue;
        }

        public String getPreferenceKey() {
            return preferenceKey;
        }

        public int getDefaultValue() {
            return defaultValue;
        }
    }
}
