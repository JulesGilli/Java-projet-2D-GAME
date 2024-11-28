package io.github.maingame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class GameStat {
    private int golds;
    private int kills;
    private int killStreak;
    private int waves;
    private int floors;
    private int maxFloors;
    private int deaths;
    private boolean gameOver;
    private int speedPotionUse;
    private boolean isFirstGame = true;

    public GameStat() {
        this.golds = 0;
        this.kills = 0;
        this.waves = 0;
        this.maxFloors = 0;
        this.deaths = 0;
        this.speedPotionUse = 0;
        this.gameOver = false;
    }

    public void addGolds(int amount) {
        this.golds += amount;
    }

    public void removeGolds(int amount) {
        this.golds = Math.max(0, this.golds - amount);
    }

    public void saveGame() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String saveData = json.toJson(this);
        Gdx.files.local("saveData.json").writeString(saveData, false);
    }

    public void loadGame() {
        if (Gdx.files.local("saveData.json").exists()) {
            Json json = new Json();
            GameStat loadedData = json.fromJson(GameStat.class, Gdx.files.local("saveData.json").readString());
            this.golds = loadedData.golds;
            this.kills = loadedData.kills;
            this.waves = loadedData.waves;
            this.deaths = loadedData.deaths;
            this.maxFloors = loadedData.maxFloors;
            this.gameOver = loadedData.gameOver;
            this.speedPotionUse = loadedData.speedPotionUse;
            this.isFirstGame = loadedData.isFirstGame;
        }
    }

    public boolean isFirstGame() {
        return isFirstGame;
    }

    public void setFirstGame(boolean firstGame) {
        this.isFirstGame = firstGame;
    }

    public int getSpeedPotionUse() {
        return speedPotionUse;
    }

    public void setSpeedPotionUse(int speedPotionUse) {
        this.speedPotionUse = speedPotionUse;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getGolds() {
        return golds;
    }

    public void setGolds(int golds) {
        this.golds = golds;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getWaves() {
        return waves;
    }


    public void setWaves(int waves) {
        this.waves = waves;
    }

    public int getMaxFloors() {
        return maxFloors;
    }

    public void setMaxFloors(int maxFloors) {
        this.maxFloors = maxFloors;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
