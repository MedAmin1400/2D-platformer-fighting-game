package com.badlogic.platformer.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseScreen implements Screen {

    private final MyPlatformerGame game;
    private final GameScreen gameScreen;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture resumeButtonTextureNormal;
    private Texture resumeButtonTextureHovered;
    private Texture quitButtonTextureNormal;
    private Texture quitButtonTextureHovered;
    private Texture background;

    private boolean isResumeButtonHovered = false;
    private boolean isQuitButtonHovered = false;
    private Sound buttonClickSound;

    public PauseScreen(MyPlatformerGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

        batch = new SpriteBatch();
        font = new BitmapFont();

        // Normal and Hovered textures for the buttons
        resumeButtonTextureNormal = new Texture("resume_button_normal.png");
        resumeButtonTextureHovered = new Texture("resume_button_hovered.png");
        quitButtonTextureNormal = new Texture("quit_button_normal.png");
        quitButtonTextureHovered = new Texture("quit_button_hovered.png");
        background = new Texture("game_background.png");
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button_click.wav"));
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Render the transparent background with smooth transparency effect
        batch.setColor(1, 1, 1, 0.7f); // Set alpha for transparency
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE); // Reset color


        // Draw "Resume" button with hover effect
        float resumeButtonX = Gdx.graphics.getWidth() / 2 - resumeButtonTextureNormal.getWidth() / 2;
        float resumeButtonY = Gdx.graphics.getHeight() / 2 - resumeButtonTextureNormal.getHeight() / 2 + 50;
        float quitButtonX = Gdx.graphics.getWidth() / 2 - quitButtonTextureNormal.getWidth() / 2;
        float quitButtonY = Gdx.graphics.getHeight() / 2 - quitButtonTextureNormal.getHeight() / 2 - 50;

        // Check for hover on "Resume" button
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y-axis
        isResumeButtonHovered = mouseX > resumeButtonX && mouseX < resumeButtonX + resumeButtonTextureNormal.getWidth()
            && mouseY > resumeButtonY && mouseY < resumeButtonY + resumeButtonTextureNormal.getHeight();

        isQuitButtonHovered = mouseX > quitButtonX && mouseX < quitButtonX + quitButtonTextureNormal.getWidth()
            && mouseY > quitButtonY && mouseY < quitButtonY + quitButtonTextureNormal.getHeight();

        // Draw Resume Button with appropriate texture
        if (isResumeButtonHovered) {
            batch.draw(resumeButtonTextureHovered, resumeButtonX, resumeButtonY + 30); // Use hovered texture
        } else {
            batch.draw(resumeButtonTextureNormal, resumeButtonX, resumeButtonY + 30); // Use normal texture
        }

        // Draw Quit Button with appropriate texture
        if (isQuitButtonHovered) {
            batch.draw(quitButtonTextureHovered, quitButtonX, quitButtonY - 30); // Use hovered texture
        } else {
            batch.draw(quitButtonTextureNormal, quitButtonX, quitButtonY - 30); // Use normal texture
        }

        batch.setColor(Color.WHITE); // Reset color after highlighting

        batch.end();

        // Check for button clicks (resume or quit)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (isResumeButtonHovered) {
                buttonClickSound.play();
                game.setScreen(gameScreen); // Return to the game
            } else if (isQuitButtonHovered) {
                Gdx.app.exit(); // Exit the game
            }
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        resumeButtonTextureNormal.dispose();
        resumeButtonTextureHovered.dispose();
        quitButtonTextureNormal.dispose();
        quitButtonTextureHovered.dispose();
        background.dispose();
    }
}
