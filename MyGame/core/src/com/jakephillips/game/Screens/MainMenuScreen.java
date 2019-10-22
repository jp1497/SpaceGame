package com.jakephillips.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.jakephillips.game.SpaceGame;
import com.jakephillips.game.Tools.ScrollingBackground;

public class MainMenuScreen implements Screen {

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 125;
    private static final int EXIT_BUTTON_Y = 100;
    private static final int EXIT_BUTTON_X = (SpaceGame.WIDTH - EXIT_BUTTON_WIDTH) / 2;

    private static final int PLAY_BUTTON_WIDTH = 330;
    private static final int PLAY_BUTTON_HEIGHT = 150;
    private static final int PLAY_BUTTON_Y = 220;
    private static final int PLAY_BUTTON_X = (SpaceGame.WIDTH - PLAY_BUTTON_WIDTH) / 2;

    private static final int LOGO_WIDTH = 300;
    private static final int LOGO_HEIGHT = 150;
    private static final int LOGO_Y = 500;
    private static final int LOGO_X = (SpaceGame.WIDTH - LOGO_WIDTH) / 2;

    SpaceGame game;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture logo;


    public MainMenuScreen(SpaceGame game){
        this.game = game;
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        logo = new Texture("logo.png");

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.2f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta,game.batch);

        game.batch.draw(logo, LOGO_X, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);

        if (Gdx.input.getX() < EXIT_BUTTON_X + EXIT_BUTTON_WIDTH && Gdx.input.getX() > EXIT_BUTTON_X
        && SpaceGame.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() >= EXIT_BUTTON_Y){
            game.batch.draw(exitButtonActive, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()){
                Gdx.app.exit();
            }
        } else {
            game.batch.draw(exitButtonInactive, EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }


        if (Gdx.input.getX() < PLAY_BUTTON_X + PLAY_BUTTON_WIDTH && Gdx.input.getX() > PLAY_BUTTON_X
                && SpaceGame.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y){
            game.batch.draw(playButtonActive, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()){
                game.setScreen(new MainGameScreen(game));
                this.dispose();
            }
        } else {
            game.batch.draw(playButtonInactive, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }


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

    }
}
