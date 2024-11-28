package io.github.maingame.items;

public abstract class Gear extends Item {
    protected int lvl;

    public Gear(int gold, String textureName, String textureBuyName, String textureLockName, int lvl) {
        super(gold, textureName, textureBuyName, textureLockName);
        this.lvl = lvl;
    }

    public int getLvl() {
        return lvl;
    }
}
