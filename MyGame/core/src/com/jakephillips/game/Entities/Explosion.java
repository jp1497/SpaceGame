package com.jakephillips.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {

    public static final float ANIMATION_SPEED = 0.1f;
    public static final int OFFSET = 8;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    private static Animation<TextureRegion> animation = null;
    float x,y;
    float statetime;
    Texture texture = new Texture("explosion.png");


    public boolean remove = false;

    public Explosion(float x, float y){
        this.x = x - OFFSET;
        this.y = y - OFFSET;
        statetime = 0;

        if (animation == null){
            animation = new Animation<TextureRegion>(ANIMATION_SPEED, TextureRegion.split(texture, WIDTH, HEIGHT)[0]);
        }
    }

    public void update (float deltatime){
        statetime += deltatime;
        if (animation.isAnimationFinished(statetime)){
            remove = true;
        }
    }

    public void render (SpriteBatch batch){
        batch.draw(animation.getKeyFrame(statetime),x,y, WIDTH, HEIGHT);
    }

}
