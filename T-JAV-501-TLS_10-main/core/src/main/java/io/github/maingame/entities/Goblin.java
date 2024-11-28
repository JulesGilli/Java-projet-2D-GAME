package io.github.maingame.entities;

import com.badlogic.gdx.math.Vector2;
import io.github.maingame.core.GameStat;
import io.github.maingame.utils.AnimationManager;
import io.github.maingame.utils.Platform;

import java.util.List;

public class Goblin extends Enemy {

    private float attackDelayTimer = 0f;

    public Goblin(Vector2 position, List<Platform> platforms, Player player, GameStat gameStat) {
        super(
            position,
            platforms,
            player,
            gameStat,
            new AnimationManager(
                "atlas/goblin/sprite_goblin_walk.png",
                "atlas/goblin/sprite_goblin_idle.png",
                "atlas/goblin/sprite_goblin_walk.png",
                "atlas/goblin/sprite_goblin_attack.png",
                "atlas/goblin/sprite_goblin_death.png",
                "atlas/goblin/sprite_goblin_walk.png",
                "atlas/goblin/sprite_goblin_hit.png",
                150, 101, 0.15f, 0.1f
            ),
            30,
            400,
            15,
            150,
            1f
        );
    }

    @Override
    public void makeAction(float delta) {
        if (isDead || isAttacking) return;

        if (inRange()) {
            attackDelayTimer += delta;
            if (attackDelayTimer >= attackDelay && timeSinceLastAttack >= attackCooldown) {
                attack();
                timeSinceLastAttack = 0f;
                attackDelayTimer = 0f;
                hasHitPlayer = false;
            } else {
                idle();
            }
        } else {
            attackDelayTimer = 0f;

            if (!isPlayerInFront()) {
                isLookingRight = target.getPosition().x > position.x;
            }
            walk();
        }
    }

}
