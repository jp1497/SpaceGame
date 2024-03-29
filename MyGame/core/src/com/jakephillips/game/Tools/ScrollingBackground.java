package com.jakephillips.game.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jakephillips.game.SpaceGame;

public class ScrollingBackground {

    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 250;
    public static final int GOAL_REACH_ACCELERATION = 200;

    Texture image;
    float y1, y2;
    int speed;
    int goalSpeed;
    float imageScale;
    boolean speedFixed;

    public ScrollingBackground (){
        image = new Texture("stars_background.png");

        //2 identical images continuously flowing into each other to give the illusion it goes on forever
        y1 = 0;
        y2 = image.getHeight();
        speed = 0;
        goalSpeed = DEFAULT_SPEED;
        imageScale = 0;
        speedFixed = true;
    }

    public void updateAndRender (float deltaTime, SpriteBatch batch){
        if (speed < goalSpeed) {
            speed += GOAL_REACH_ACCELERATION * deltaTime;
            if (speed > goalSpeed) {
                speed = goalSpeed;
            }
        } else if (speed > goalSpeed) {
            speed -= GOAL_REACH_ACCELERATION * deltaTime;
            if (speed < goalSpeed) {
                speed = goalSpeed;
            }
        }

        if (!speedFixed)
            speed += ACCELERATION * deltaTime;

        y1 -= speed * deltaTime;
        y2 -= speed * deltaTime;

        //if img reached bottom of screen and not visible, put it back on top
        if (y1 + image.getHeight() * imageScale <= 0)
            y1 = y2 + image.getHeight() * imageScale;

        if (y2 + image.getHeight() * imageScale <= 0)
            y2 = y1 + image.getHeight() * imageScale;

        //render
        batch.draw(image,0,y1, SpaceGame.WIDTH, image.getHeight() * imageScale);
        batch.draw(image,0,y2, SpaceGame.WIDTH, image.getHeight() * imageScale);
    }

    public void resize(int width, int height){
        imageScale = width / image.getWidth();
    }

    public void setSpeed(int goalSpeed){
        this.goalSpeed = goalSpeed;
    }

    public void setSpeedFixed(boolean speedFixed){
        this.speedFixed = speedFixed;
    }
}
