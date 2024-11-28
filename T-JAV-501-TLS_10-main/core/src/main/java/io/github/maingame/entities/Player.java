package io.github.maingame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.maingame.input.PlayerInputHandler;
import io.github.maingame.items.Gear;
import io.github.maingame.items.Inventory;
import io.github.maingame.items.Item;
import io.github.maingame.utils.AnimationManager;
import io.github.maingame.utils.Platform;
import io.github.maingame.utils.SoundManager;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class Player extends Entity {
    private final float maxStamina = 100;
    private Inventory inventory;
    private boolean isDead = false;
    private boolean isRolling = false;
    private float rollTimer = 0f;
    private float stamina = 100;
    private PlayerInputHandler inputHandler;

    private SoundManager soundManager;

    private final String[] playerScreams = {"sound_playerScream1", "sound_playerScream2", "sound_playerScream3", "sound_playerScream4", "sound_playerScream5"};
    private final String[] hitSwords = {"sound_hitSword1", "sound_hitSword2", "sound_hitSword3", "sound_hitSword4"};


    // Constructor
    public Player(Vector2 position, List<Platform> platforms, SoundManager soundManager) {
        super(position,
            new AnimationManager(
                "atlas/player/sprite_player_walk.png",
                "atlas/player/sprite_player_idle.png",
                "atlas/player/sprite_player_jump.png",
                "atlas/player/sprite_player_attack.png",
                "atlas/player/sprite_player_death.png",
                "atlas/player/sprite_player_roll.png",
                "atlas/player/sprite_player_hit.png",
                120, 80, 0.1f, 0.04f), 100, 100, 25);

        this.inventory = new Inventory();
        this.initialPosition = new Vector2(position);
        this.initialHealth = health;
        this.initialGold = gold;
        this.initialAttack = attackDamage;

        this.speed = 350;
        this.jumpVelocity = 1000;
        this.gravity = 50;
        this.platforms = platforms;
        this.renderWidth = 450;
        this.renderHeight = 300;
        this.attackRange = 50;

        this.inputHandler = new PlayerInputHandler(this);

        this.soundManager = soundManager;
    }

    // Update Method
    public void update(float delta, List<Enemy> enemies, float leftBoundary, float rightBoundary) {
        if (health <= 0 && !isDead) {
            handleDeath();
        }

        if (isDead) {
            animationTime += delta;
        } else if (isRolling) {
            handleRolling(delta);
        } else {
            handleInput();
            animationTime += delta;

            position.x = MathUtils.clamp(position.x, leftBoundary, rightBoundary - renderWidth);

            if (isAttacking) {
                handleAttack(enemies);
            } else {
                applyGravity();
                handlePlatformCollision();
                position.add(velocity.cpy().scl(delta));
                checkOnFloor();
            }
        }

        regenerateStamina(delta);
    }

    private void handleDeath() {
        inventory.clear(this);

        isDead = true;
        isAttacking = false;
        isRolling = false;
        animationTime = 0f;

    }

    private void handleRolling(float delta) {
        rollTimer += delta;
        animationTime += delta;
        float rollDuration = 0.5f;
        if (rollTimer >= rollDuration) {
            isRolling = false;
            rollTimer = 0f;
        } else {
            float rollSpeed = 700;
            velocity.x = isLookingRight ? rollSpeed : -rollSpeed;
            position.add(velocity.cpy().scl(delta));
        }
    }

    private void handleAttack(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (isCollidingWith(enemy, attackRange) && isEnemyInFront(enemy)) {
                enemy.receiveDamage(getAttack());
            }
        }
        checkAttackFinish();
    }

    private void handlePlatformCollision() {
        for (Platform platform : platforms) {
            if (isOnPlatform(platform) && velocity.y <= 0) {
                position.y = platform.getBounds().y + platform.getBounds().height;
                velocity.y = 0;
                isJumping = false;
                break;
            }
        }
    }

    private void regenerateStamina(float delta) {
        float staminaRegenRate = 10f;
        stamina = Math.min(maxStamina, stamina + staminaRegenRate * delta);
    }

    // Input Handling
    public void handleInput() {
        inputHandler.handleInput();
    }

    private boolean isEnemyInFront(Enemy enemy) {
        return (isLookingRight && enemy.getPosition().x > position.x) ||
            (!isLookingRight && enemy.getPosition().x < position.x);
    }

    // Override Methods
    @Override
    public TextureRegion getCurrentFrame() {
        if (isDead) {
            return flipAnimationCheck(animation.getDeathCase().getKeyFrame(animationTime, false));
        } else if (isRolling) {
            return flipAnimationCheck(animation.getRollCase().getKeyFrame(animationTime, false));
        } else if (isAttacking) {
            return flipAnimationCheck(animation.getAttackCase().getKeyFrame(animationTime, true));
        } else if (isJumping) {
            return flipAnimationCheck(animation.getJumpCase().getKeyFrame(animationTime, true));
        } else if (velocity.x != 0) {
            return flipAnimationCheck(animation.getWalkCase().getKeyFrame(animationTime, true));
        } else {
            return flipAnimationCheck(animation.getIdleCase().getKeyFrame(animationTime, true));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        batch.draw(currentFrame, position.x, position.y, renderWidth, renderHeight);
    }

    @Override
    public void receiveDamage(float damage) {
        if (isDead || isRolling) return;

        float reducedDamage = Math.max(0, damage - armor);
        health -= reducedDamage;

        if (health <= 0) {
            health = 0;
            isDead = true;
            animationTime = 0f;
        }
    }

    // Reset and Game Logic
    public void prepareForNewGame() {
        health = maxHealth;
        position.set(initialPosition);
        velocity.set(0, 0);
        isDead = false;
    }

    public void reset() {
        position.set(initialPosition);
        health = initialHealth;
        attackDamage = initialAttack;
        isDead = false;
        isAttacking = false;
        velocity.set(0, 0);
        animationTime = 0f;

        for (Item item : inventory.getItems()) {
            if (item instanceof Gear) {
                item.applyItem(this);
            }
        }
    }

    // Getters and Setters
    public float getStamina() {
        return stamina;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setVelocityX(float velocityX) {
        this.velocity.x = velocityX;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void jump() {
        this.velocity.y = jumpVelocity;
        this.isJumping = true;
    }

    public void attack() {
        if (stamina >= 15f) {
            stamina -= 15f;
            isAttacking = true;
            animationTime = 0f;

            if (soundManager != null) {
                String randomScream = playerScreams[MathUtils.random(playerScreams.length - 1)];
                soundManager.playSound(randomScream);

                String randomSwordHit = hitSwords[MathUtils.random(hitSwords.length - 1)];
                soundManager.playSound(randomSwordHit);
            }
        }
    }


    public void roll() {
        if (stamina >= 10f) {
            stamina -= 10f;
            isRolling = true;
            animationTime = 0f;
        }
    }

    public void usePotion() {
        inventory.applyConsumable(this);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isRolling() {
        return isRolling;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isDeathAnimationFinished() {
        return isDead && animationTime >= animation.getDeathCase().getAnimationDuration();
    }

    public void setInputHandler(PlayerInputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void setLookingRight(boolean b) {
        isLookingRight = b;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
