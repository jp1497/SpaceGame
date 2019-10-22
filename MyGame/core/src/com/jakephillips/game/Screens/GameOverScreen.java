package com.jakephillips.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.jakephillips.game.SpaceGame;
import com.jakephillips.game.Tools.ScrollingBackground;

public class GameOverScreen implements Screen {

    private static final int GAME_OVER_WIDTH = 350;
    private static final int GAME_OVER_HEIGHT = 100;

    SpaceGame game;

    int score, highscore;

    Texture gameOverText;
    BitmapFont scoreFont;

    public GameOverScreen(SpaceGame game, int score){
        this.game = game;
        this.score = score;

        //get highscore from save file
        Preferences preferences = Gdx.app.getPreferences("spacegame");
        this.highscore = preferences.getInteger("highscore", 0);

        //check if score > highscore
        if (score > highscore){
            preferences.putInteger("highscore",score);
            preferences.flush();
        }

        //load textures and fonts
        gameOverText = new Texture("game_over.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta,game.batch);

        game.batch.draw(gameOverText, SpaceGame.WIDTH / 2 - GAME_OVER_WIDTH / 2,
                SpaceGame.HEIGHT - GAME_OVER_HEIGHT - 15, GAME_OVER_WIDTH, GAME_OVER_HEIGHT);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "score: \n " + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "highscore: \n   " + score, Color.WHITE, 0, Align.left, false);

        scoreFont.draw(game.batch, scoreLayout, SpaceGame.WIDTH / 2 + 40 - scoreLayout.width/2, SpaceGame.HEIGHT - GAME_OVER_HEIGHT - 15 * 2);
        scoreFont.draw(game.batch, highscoreLayout, SpaceGame.WIDTH / 2 + 40 - highscoreLayout.width/2, SpaceGame.HEIGHT - GAME_OVER_HEIGHT - scoreLayout.height - 15 * 4 );

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");

        float tryAgainX = SpaceGame.WIDTH / 2 - tryAgainLayout.width / 2;
        float tryAgainY = SpaceGame.HEIGHT / 2 - tryAgainLayout.height / 2;
        float mainMenuX = SpaceGame.WIDTH / 2 - mainMenuLayout.width / 2;
        float mainMenuY = SpaceGame.HEIGHT / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        //check if hovering over try again button
        if (touchX >= tryAgainX && touchX <= tryAgainX + tryAgainLayout.width
        && touchY >= tryAgainY  - tryAgainLayout.height  && touchY<= tryAgainY){
            tryAgainLayout.setText(scoreFont, "Try Again", Color.YELLOW, 0, Align.left, false);
        }

        //check if hovering over main menu button
        if (touchX >= mainMenuX && touchX <= mainMenuX + mainMenuLayout.width
                && touchY >= mainMenuY - mainMenuLayout.height  && touchY<= mainMenuY){
            mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0, Align.left, false);
        }

        //if try again pressed
        if (Gdx.input.isTouched()) {
            //try again
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width
                    && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainGameScreen(game));
                return;
            }
            //main menu
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width
                    && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }

        //draw buttons
        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

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
