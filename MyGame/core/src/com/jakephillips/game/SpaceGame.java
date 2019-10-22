package com.jakephillips.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jakephillips.game.Screens.GameOverScreen;
import com.jakephillips.game.Screens.MainGameScreen;
import com.jakephillips.game.Screens.MainMenuScreen;
import com.jakephillips.game.Tools.ScrollingBackground;

public class SpaceGame extends Game {

	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;

	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.scrollingBackground = new ScrollingBackground();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void resize(int width, int height){
		this.scrollingBackground.resize(width,height);
		super.resize(width,height);
	}
}
