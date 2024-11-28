package io.github.maingame.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.maingame.core.GameStat;
import io.github.maingame.utils.Platform;

import java.util.List;

public class EnemyFactory {
    public static Enemy createRandomEnemy(Vector2 position, List<Platform> platforms, Player player, GameStat stat) {
        int floor = stat.getFloors();

        if (floor <= 3) {
            return new Skeleton(position, platforms, player, stat);
        } else if (floor <= 6) {
            return MathUtils.randomBoolean(0.7f) ?
                new Skeleton(position, platforms, player, stat) :
                new Goblin(position, platforms, player, stat);
        } else {
            float randomValue = MathUtils.random();
            if (randomValue < 0.5f) {
                return new Skeleton(position, platforms, player, stat);
            } else if (randomValue < 0.8f) {
                return new Goblin(position, platforms, player, stat);
            } else {
                return new Mushroom(position, platforms, player, stat);
            }
        }
    }
}
