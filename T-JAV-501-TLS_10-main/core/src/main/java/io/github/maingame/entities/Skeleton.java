package io.github.maingame.entities;

import com.badlogic.gdx.math.Vector2;
import io.github.maingame.core.GameStat;
import io.github.maingame.utils.AnimationManager;
import io.github.maingame.utils.Platform;

import java.util.List;

public class Skeleton extends Enemy {

    public Skeleton(Vector2 position, List<Platform> platforms, Player player, GameStat gameStat) {
        super(
            position,
            platforms,
            player,
            gameStat,
            new AnimationManager(
                "atlas/skeleton/sprite_skeleton_walk.png",
                "atlas/skeleton/sprite_skeleton_idle.png",
                "atlas/skeleton/sprite_skeleton_walk.png",
                "atlas/skeleton/sprite_skeleton_attack.png",
                "atlas/skeleton/sprite_skeleton_death.png",
                "atlas/skeleton/sprite_skeleton_walk.png",
                "atlas/skeleton/sprite_skeleton_hit.png",
                150, 101, 0.1f, 0.1f
            ),
            50,
            300,
            20,
            200,
            1.5f
        );

        float scaleFactor = 5;

        this.renderWidth *= (int) scaleFactor;
        this.renderHeight *= (int) scaleFactor;
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

