package io.github.maingame.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.maingame.core.GameStat;
import io.github.maingame.entities.Enemy;
import io.github.maingame.entities.EnemyFactory;
import io.github.maingame.entities.Player;
import io.github.maingame.utils.Platform;
import io.github.maingame.utils.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyManager {
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Enemy> spawnList = new ArrayList<>();
    private final GameStat stat;
    private final Player player;
    private final OrthographicCamera camera;
    private final SoundManager soundManager;
    private float timeSinceLastSpawn;
    private float spawnDelay;

    public EnemyManager(GameStat stat, Player player, OrthographicCamera camera, SoundManager soundManager) {
        this.stat = stat;
        this.player = player;
        this.camera = camera;
        this.soundManager = soundManager;
        this.spawnDelay = 3.0f;
    }

    public void setupFloorEnemies() {
        spawnList.clear();
        int enemyCount = 3 + stat.getFloors();

        for (int i = 0; i < enemyCount; i++) {
            Vector2 spawnPosition = new Vector2(-200, 100);
            Enemy enemy = EnemyFactory.createRandomEnemy(spawnPosition, Platform.getPlatforms(), player, stat);
            enemy.updateStats(stat);
            enemy.setScaleFactor(3f);
            spawnList.add(enemy);
            System.out.println("Enemy added: " + enemy);
        }

        spawnDelay = Math.max(1.0f, spawnDelay * 0.95f);
        timeSinceLastSpawn = 0f;
    }

    public void spawnEnemies(float delta, SpriteBatch batch) {
        timeSinceLastSpawn += delta;

        if (!spawnList.isEmpty() && timeSinceLastSpawn >= spawnDelay) {
            Enemy enemyToSpawn = spawnList.remove(0);
            float spawnX;
            if (MathUtils.randomBoolean()) {
                spawnX = camera.position.x - camera.viewportWidth / 2 - 100;
            } else {
                spawnX = camera.position.x + camera.viewportWidth / 2 + 100;
            }
            float spawnY = 100;
            enemyToSpawn.getPosition().set(spawnX, spawnY);
            enemies.add(enemyToSpawn);
            timeSinceLastSpawn = 0f;

            if (MathUtils.random() < 0.3f) {
                soundManager.playSound("enemy_spawn");
            }
        }

        for (Iterator<Enemy> iterator = enemies.iterator(); iterator.hasNext(); ) {
            Enemy enemy = iterator.next();
            enemy.render(batch);
            enemy.update(delta);
            if (enemy.isDeathAnimationFinished()) {
                stat.addGolds(enemy.getGold());
                stat.setKills(stat.getKills() + 1);
                iterator.remove();
            }
        }
    }

    public boolean isSpawnListEmpty() {
        return spawnList.isEmpty();
    }


    public List<Enemy> getEnemies() {
        return enemies;
    }
}
