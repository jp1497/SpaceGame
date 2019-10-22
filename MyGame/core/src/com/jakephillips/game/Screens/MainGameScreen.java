package com.jakephillips.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jakephillips.game.Entities.Asteroid;
import com.jakephillips.game.Entities.Bullet;
import com.jakephillips.game.Entities.Explosion;
import com.jakephillips.game.SpaceGame;
import com.jakephillips.game.Tools.CollisionRect;
import com.jakephillips.game.Tools.ScrollingBackground;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MainGameScreen implements Screen {
    public static final float SPEED = 300;
    public static final float SHIP_ANIMATION_SPEED = 0.5f;

    public static final int SHIP_WIDTH = 51;
    public static final int SHIP_HEIGHT = 96;

    public static final float ROLL_TIMER_SWITCH_TIME = 0.15f;

    public static final float MIN_ASTEROID_SPAWN_TIME = 0.1f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.5f;

    SpaceGame game;

    Animation<TextureRegion>[]  rolls;
    float shipX, shipY;
    int roll;
    float rollTimer;
    float asteroidSpawnTimer;
    float stateTime;

    Random random;

    ArrayList<Bullet> bullets;
    ArrayList<Asteroid> asteroids;
    ArrayList<Explosion> explosions;

    BitmapFont scoreFont;
    Texture blank;

    CollisionRect playerRect;

    float health = 1;

    int score;

    public MainGameScreen(SpaceGame game){
        this.game = game;
        shipY = 15;
        shipX = (SpaceGame.WIDTH - SHIP_WIDTH) / 2;
        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        explosions = new ArrayList<Explosion>();
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        playerRect = new CollisionRect(0,0,SHIP_WIDTH,SHIP_HEIGHT);
        blank = new Texture("blank.png");

        score = 0;

        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + (MIN_ASTEROID_SPAWN_TIME);

        roll = 2;
        rollTimer = 0;
        rolls = new Animation[5];

        //5 roll states in first index, 2 animation states in second index
        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), 17, 32);

        rolls[0] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]); //
        rolls[1] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]); //tilted to left
        rolls[2] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]); //centre point
        rolls[3] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]); //tilted to right
        rolls[4] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]);

        game.scrollingBackground.setSpeedFixed(false);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        //shooting
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

            int offset = 4;

            if (roll == 1 || roll == 3) //slightly tilted
                offset = 8;

            if (roll == 0 || roll == 4) //fully tilted
                offset = 16;

            bullets.add(new Bullet(shipX + offset));
            bullets.add(new Bullet(shipX + SHIP_WIDTH - offset));
        }

        //update bullets
        ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bullets) {
            bullet.update(delta);
            if (bullet.remove)
                bulletsToRemove.add(bullet);
        }
        bullets.removeAll(bulletsToRemove);

        //asteroid spawn
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer < 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + (MIN_ASTEROID_SPAWN_TIME);
            asteroids.add(new Asteroid(random.nextInt(SpaceGame.WIDTH - Asteroid.WIDTH)));
        }

        //update asteroids
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }
        asteroids.removeAll(asteroidsToRemove);

        //update explosions
        ArrayList<Explosion> explosionsToRemove =  new ArrayList<Explosion>();
        for (Explosion explosion: explosions) {
            explosion.update(delta);
            if (explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);

        //movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            shipX -= SPEED * delta;
            if (shipX <= 0) {
                shipX = 0;
            }
            // update roll
            rollTimer -= delta;
            if (rollTimer < -ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer += ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        }
        // automatically roll back to centre
        else if (roll < 2) {
            rollTimer -= delta;
            if (rollTimer < -ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer += ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            shipX += SPEED * delta;
            if (shipX + SHIP_WIDTH >= SpaceGame.WIDTH) {
                shipX = SpaceGame.WIDTH - SHIP_WIDTH;
            }
            // update roll
            rollTimer -= delta;
            if (rollTimer < -ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer += ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        }
        // automatically roll back to centre
        else if (roll > 2) {
            rollTimer -= delta;
            if (rollTimer < -ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer += ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        }

        //after player moves, update player collision rect
        playerRect.move(shipX,shipY);

        //after all updates, check for collisions
        for (Bullet bullet: bullets){
            for (Asteroid asteroid: asteroids){
                if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())){
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                    score+= 100;
                }
            }
        }

        bullets.removeAll(bulletsToRemove);

        for (Asteroid asteroid : asteroids){
            if (asteroid.getCollisionRect().collidesWith(playerRect)){
                asteroidsToRemove.add(asteroid);
                health -= 0.1;

                //if dead, go to game over screen
                if (health <= 0){
                    game.setScreen(new GameOverScreen(game,score));
                    return;
                }
            }
        }
        asteroids.removeAll(asteroidsToRemove);


        stateTime += delta;

        //begin drawing

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta,game.batch);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "" + score);
        scoreFont.draw(game.batch,scoreLayout,SpaceGame.WIDTH /2 - scoreLayout.width / 2, SpaceGame.HEIGHT - 20);

        for (Bullet bullet : bullets){
            bullet.render(game.batch);
        }

        for (Asteroid asteroid: asteroids){
            asteroid.render(game.batch);
        }

        for (Explosion explosion: explosions){
            explosion.render(game.batch);
        }

        //draw health
        if (health > 0.6f)
            game.batch.setColor(Color.GREEN);
        else if (health > 0.2)
            game.batch.setColor(Color.ORANGE);
        else
            game.batch.setColor(Color.RED);
        game.batch.draw(blank, 0,0,SpaceGame.WIDTH * health, 5);
        game.batch.setColor(Color.WHITE);

        game.batch.draw(rolls[roll].getKeyFrame(stateTime, true), shipX, shipY, SHIP_WIDTH, SHIP_HEIGHT);

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.batch.dispose();
    }
}
