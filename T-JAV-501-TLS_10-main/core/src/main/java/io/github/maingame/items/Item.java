package io.github.maingame.items;

import com.badlogic.gdx.graphics.Texture;

public abstract class Item implements ItemSolution {
    protected float increaseValue;
    protected String name;
    protected int gold;
    protected String  textureAvailable;
    protected String textureDisabled;
    protected String textureLock;
    protected int unlockFloor;

    public Item(int value, String textureName, String textureDisabledName, String textureLockName) {
        this.gold = value;
        this.textureAvailable = textureName;
        this.textureDisabled = textureDisabledName;
        this.textureLock = textureLockName;
    }

    public String getStrGold() {
        return Integer.toString(this.gold);
    }

    public int getGold() {
        return this.gold;
    }

    public String getTextureAvailable() {
        return textureAvailable;
    }

    public String  getTextureDisabled() {
        return textureDisabled;
    }

    public String getTextureLock() {
        return textureLock;
    }

    public int getUnlockFloor() {
        return unlockFloor;
    }

    public float getIncreaseValue() {
        return increaseValue;
    }

    public void setIncreaseValue(float increaseValue) {
        this.increaseValue = increaseValue;
    }
}
