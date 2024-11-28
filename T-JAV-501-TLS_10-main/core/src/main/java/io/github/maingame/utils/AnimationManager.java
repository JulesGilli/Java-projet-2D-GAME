package io.github.maingame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {
    private final Animation<TextureRegion> walkCase;
    private final Animation<TextureRegion> idleCase;
    private final Animation<TextureRegion> jumpCase;
    private final Animation<TextureRegion> attackCase;
    private final Animation<TextureRegion> deathCase;
    private final Animation<TextureRegion> rollCase;
    private final Animation<TextureRegion> hitCase;

    public AnimationManager(String walkAsset, String idleAsset, String jumpAsset, String attackAsset, String deathAsset, String rollAsset, String hitAsset, int frameWidth, int frameHeight, float frameDuration, float rollFrameDuration) {
        Texture walkSteps = new Texture(Gdx.files.internal(walkAsset));
        Texture idleSteps = new Texture(Gdx.files.internal(idleAsset));
        Texture jumpSteps = new Texture(Gdx.files.internal(jumpAsset));
        Texture attackSteps = new Texture(Gdx.files.internal(attackAsset));
        Texture deathSteps = new Texture(Gdx.files.internal(deathAsset));
        Texture rollSteps = new Texture(Gdx.files.internal(rollAsset));
        Texture hitSteps = new Texture(Gdx.files.internal(hitAsset));


        walkCase = createAnimation(walkSteps, frameWidth, frameHeight, frameDuration);
        idleCase = createAnimation(idleSteps, frameWidth, frameHeight, frameDuration);
        jumpCase = createAnimation(jumpSteps, frameWidth, frameHeight, frameDuration);
        attackCase = createAnimation(attackSteps, frameWidth, frameHeight, frameDuration);
        deathCase = createAnimation(deathSteps, frameWidth, frameHeight, frameDuration);
        rollCase = createAnimation(rollSteps, frameWidth, frameHeight, rollFrameDuration);
        hitCase = createAnimation(hitSteps, frameWidth, frameHeight, frameDuration);
    }

    private Animation<TextureRegion> createAnimation(Texture allSteps, int stepWidth, int stepHeight, float stepDuration) {
        TextureRegion[][] tmpSteps = TextureRegion.split(allSteps, stepWidth, stepHeight);

        int totalFrames = tmpSteps.length * tmpSteps[0].length;
        TextureRegion[] frames = new TextureRegion[totalFrames];
        int indexFrame = 0;
        for (TextureRegion[] tmpStep : tmpSteps) {
            for (TextureRegion textureRegion : tmpStep) {
                frames[indexFrame++] = textureRegion;
            }
        }
        return new Animation<>(stepDuration, frames);
    }

    public void updateFrameDurations(float speedMultiplier) {
        updateAnimationFrameDuration(attackCase, speedMultiplier);
    }

    private void updateAnimationFrameDuration(Animation<TextureRegion> animation, float speedMultiplier) {
        float originalFrameDuration = animation.getFrameDuration();
        animation.setFrameDuration(originalFrameDuration * speedMultiplier);
    }


    public Animation<TextureRegion> getAttackCase() {
        return attackCase;
    }

    public Animation<TextureRegion> getIdleCase() {
        return idleCase;
    }

    public Animation<TextureRegion> getJumpCase() {
        return jumpCase;
    }

    public Animation<TextureRegion> getWalkCase() {
        return walkCase;
    }

    public Animation<TextureRegion> getDeathCase() {
        return deathCase;
    }

    public Animation<TextureRegion> getRollCase() {
        return rollCase;
    }

    public Animation<TextureRegion> getHitCase() {
        return hitCase;
    }


    public void dispose() {
        walkCase.getKeyFrames()[0].getTexture().dispose();
        idleCase.getKeyFrames()[0].getTexture().dispose();
        jumpCase.getKeyFrames()[0].getTexture().dispose();
        attackCase.getKeyFrames()[0].getTexture().dispose();
        deathCase.getKeyFrames()[0].getTexture().dispose();
        rollCase.getKeyFrames()[0].getTexture().dispose();
        hitCase.getKeyFrames()[0].getTexture().dispose();
    }

}
