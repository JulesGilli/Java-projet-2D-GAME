package io.github.maingame.input;

import com.badlogic.gdx.Gdx;
import io.github.maingame.entities.Player;

public class PlayerInputHandler {
    private final Player player;

    private int leftKey;
    private int rightKey;
    private int jumpKey;
    private int attackKey;
    private int rollKey;
    private int potionKey;

    public PlayerInputHandler(Player player) {
        this.player = player;
        loadKeys();
    }

    private void loadKeys() {
        this.leftKey = InputManager.getLeftKey();
        this.rightKey = InputManager.getRightKey();
        this.jumpKey = InputManager.getJumpKey();
        this.attackKey = InputManager.getAttackKey();
        this.rollKey = InputManager.getRollKey();
        this.potionKey = InputManager.getPotionKey();
    }

    public void updateKeys() {
        loadKeys();
    }

    public void handleInput() {
        if (player.isAttacking() || player.isRolling() || player.isDead()) return;

        if (Gdx.input.isKeyPressed(InputManager.getLeftKey())) {
            player.setVelocityX(-player.getSpeed());
            player.setLookingRight(false);
        } else if (Gdx.input.isKeyPressed(InputManager.getRightKey())) {
            player.setVelocityX(player.getSpeed());
            player.setLookingRight(true);
        } else {
            player.idle();
        }

        if (Gdx.input.isKeyJustPressed(InputManager.getJumpKey()) && !player.isJumping() && !player.isRolling()) {
            player.jump();
        }

        if (Gdx.input.isKeyJustPressed(InputManager.getAttackKey()) && !player.isAttacking() && !player.isJumping()) {
            player.attack();
        }

        if (Gdx.input.isKeyJustPressed(InputManager.getRollKey()) && !player.isRolling() && !player.isJumping()) {
            player.roll();
        }

        if (Gdx.input.isKeyJustPressed(InputManager.getPotionKey())) {
            player.usePotion();
        }
    }
}
