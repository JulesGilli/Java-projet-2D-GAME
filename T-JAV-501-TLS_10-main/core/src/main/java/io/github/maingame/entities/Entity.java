package io.github.maingame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.maingame.utils.AnimationManager;
import io.github.maingame.utils.Platform;

import java.util.List;

public abstract class Entity {
    protected boolean isWalking = false;
    protected boolean isJumping = false;
    protected boolean isAttacking = false;
    protected List<Platform> platforms;
    protected Vector2 position;
    protected Vector2 velocity;
    protected AnimationManager animation;
    protected boolean isLookingRight;
    protected float speed;
    protected float jumpVelocity;
    protected float gravity;
    protected float animationTime;
    protected int gold;
    protected float health;
    protected float maxHealth;
    protected float attackDamage;
    protected float armor = 0;
    protected float attackBonus = 0;
    protected float speedBonus = 0;
    protected int renderWidth = 100;
    protected int renderHeight = 100;
    protected float attackRange;
    protected Vector2 initialPosition;
    protected float initialHealth;
    protected int initialGold;
    protected float initialAttack;

    protected boolean isHit = false;
    protected float hitAnimationTime = 0f;
    protected float hitDuration = 0.3f;


    public Entity(Vector2 position, AnimationManager animation, float health, int gold, float attack) {
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.animation = animation;
        this.health = health;
        this.maxHealth = health;
        this.gold = gold;
        this.attackDamage = attack;
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentFrame();
        batch.draw(
            currentFrame,
            position.x,
            position.y,
            renderWidth,
            renderHeight
        );
    }

    public void applyGravity() {
        if (isJumping) {
            velocity.y -= gravity;
        }
    }

    protected void checkAttackFinish() {
        if (animation.getAttackCase().isAnimationFinished(animationTime)) {
            isAttacking = false;
            animationTime = 0f;
        }
    }

    protected boolean isOnPlatform(Platform platform) {
        return position.y <= platform.getBounds().y + platform.getBounds().height &&
            position.y >= platform.getBounds().y &&
            position.x + renderWidth > platform.getBounds().x &&
            position.x < platform.getBounds().x + platform.getBounds().width;
    }

    protected void applyPlatformPosition(Platform platform) {
        position.y = platform.getBounds().y + platform.getBounds().height;
        velocity.y = 0;
        isJumping = false;
    }

    protected void checkOnPlatform() {
        for (Platform platform : platforms) {
            if (isOnPlatform(platform) && velocity.y <= 0) {
                applyPlatformPosition(platform);
                break;
            }
        }
    }

    public void checkOnFloor() {
        if (position.y < 0) {
            position.y = 0;
            isJumping = false;
        }
    }

    public TextureRegion flipAnimationCheck(TextureRegion currentFrame) {
        if ((isLookingRight && currentFrame.isFlipX()) || (!isLookingRight && !currentFrame.isFlipX())) {
            currentFrame.flip(true, false);
        }
        return currentFrame;
    }

    public TextureRegion getCurrentFrame() {
        if (isAttacking) {
            return flipAnimationCheck(animation.getAttackCase().getKeyFrame(animationTime, true));
        } else if (isJumping) {
            return flipAnimationCheck(animation.getJumpCase().getKeyFrame(animationTime, true));
        } else if (velocity.x != 0) {
            return flipAnimationCheck(animation.getWalkCase().getKeyFrame(animationTime, true));
        } else {
            return flipAnimationCheck(animation.getIdleCase().getKeyFrame(animationTime, true));
        }
    }

    public void idle() {
        velocity.x = 0;
    }

    public void jump() {
        if (!isJumping) {
            velocity.y = jumpVelocity;
        }
        isJumping = true;
    }

    public void attack() {
        isAttacking = true;
        animationTime = 0f;
    }

    public void moveLaterally(float SPEED) {
        velocity.x = SPEED;
        isLookingRight = velocity.x > 0;
        isWalking = true;
    }

    public boolean isHorizontallyAligned(float thisLeft, float thisRight, float otherLeft, float otherRight, float attackRange) {
        return (isLookingRight && otherLeft <= thisRight + attackRange && otherRight >= thisRight) ||
            (!isLookingRight && otherRight >= thisLeft - attackRange && otherLeft <= thisLeft);
    }

    public boolean isVerticallyAligned(float thisBottom, float thisTop, float otherBottom, float otherTop) {
        return (thisBottom <= otherTop && thisTop >= otherBottom);
    }

    public boolean isCollidingWith(Entity other, float attackRange) {
        float thisLeft = position.x;
        float thisRight = position.x + other.renderWidth * 0.5f;
        float thisTop = position.y + other.renderHeight * 0.5f;
        float thisBottom = position.y;

        float otherLeft = other.getPosition().x;
        float otherRight = other.getPosition().x + other.renderWidth * 0.5f;
        float otherTop = other.getPosition().y + other.renderHeight * 0.5f;
        float otherBottom = other.getPosition().y;
        return isHorizontallyAligned(thisLeft,thisRight,otherLeft,otherRight, attackRange) && isVerticallyAligned(thisBottom,thisTop,otherBottom,otherTop);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAttack() {
        return attackDamage + attackBonus;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void receiveDamage(float damage) {
        float burst =Math.max(damage - armor , 0);
        this.health -= burst;
    }

    public float getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(float attackBonus) {
        this.attackBonus = attackBonus;
    }

    public float getSpeedBonus() {
        return speedBonus;
    }

    public void setSpeedBonus(float speedBonus) {
        this.speedBonus = speedBonus;
    }

    public float getSpeed() {
        return speed + speedBonus;
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getJumpVelocity() {
        return jumpVelocity;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isLookingRight() {
        return isLookingRight;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public float getGravity() {
        return gravity;
    }

    public void setJumpVelocity(float jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setLookingRight(boolean lookingRight) {
        isLookingRight = lookingRight;
    }

    public void dispose() {
        animation.getIdleCase().getKeyFrames()[0].getTexture().dispose();
        animation.getAttackCase().getKeyFrames()[0].getTexture().dispose();
        animation.getJumpCase().getKeyFrames()[0].getTexture().dispose();
        animation.getWalkCase().getKeyFrames()[0].getTexture().dispose();
        animation.getDeathCase().getKeyFrames()[0].getTexture().dispose();
    }

}

