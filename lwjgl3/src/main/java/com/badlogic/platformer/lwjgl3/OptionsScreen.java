package com.badlogic.platformer.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class OptionsScreen implements Screen {

    private final MyPlatformerGame game;
    private final MainMenuScreen mainMenuScreen;

    private SpriteBatch batch;
    private BitmapFont font;

    private Texture backButtonNormal;
    private Texture backButtonHovered;
    private Texture background;

    private Rectangle backButtonRect;
    private boolean isBackButtonHovered = false;

    // Placeholder values for the options
    private float soundVolume = 1.0f; // Max volume by default
    private String graphicsQuality = "High"; // Default graphics setting

    public OptionsScreen(MyPlatformerGame game, MainMenuScreen mainMenuScreen) {
        this.game = game;
        this.mainMenuScreen = mainMenuScreen;

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.setColor(Color.WHITE);

        // Load textures
        backButtonNormal = new Texture("back_button_normal.png");
        backButtonHovered = new Texture("back_button_hovered.png");
        background = new Texture("background.png");

        // Initialize back button rectangle
        float backButtonX = Gdx.graphics.getWidth() / 2f - backButtonNormal.getWidth() / 2f;
        float backButtonY = Gdx.graphics.getHeight() / 4f;
        backButtonRect = new Rectangle(backButtonX, backButtonY, backButtonNormal.getWidth(), backButtonNormal.getHeight());
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Update logic
        update(delta);

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin drawing
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw options text
        font.getData().setScale(1f); // Set font scale
        //font.draw(batch, "Options Menu", Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() - 50);

        // Display options
        font.draw(batch, "Sound Volume: " + (int) (soundVolume * 100) + "%", Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2f + 50);
        font.draw(batch, "Graphics Quality: " + graphicsQuality, Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2f);

        // Draw back button with hover effect
        if (isBackButtonHovered) {
            batch.draw(backButtonHovered, backButtonRect.x, backButtonRect.y);
        } else {
            batch.draw(backButtonNormal, backButtonRect.x, backButtonRect.y);
        }

        batch.end();
    }

    private void update(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y-axis for screen coordinates

        // Check if the back button is hovered
        isBackButtonHovered = backButtonRect.contains(mouseX, mouseY);

        // Handle back button click
        if (isBackButtonHovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            game.setScreen(mainMenuScreen);
        }

        // Allow ESC to return to the main menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(mainMenuScreen);
        }

        // Simulate sound and graphics adjustment using keyboard keys (for demonstration purposes)
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            soundVolume = Math.min(1.0f, soundVolume + 0.1f); // Increase volume
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            soundVolume = Math.max(0.0f, soundVolume - 0.1f); // Decrease volume
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            graphicsQuality = "Low"; // Set graphics quality to low
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            graphicsQuality = "High"; // Set graphics quality to high
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        backButtonNormal.dispose();
        backButtonHovered.dispose();
        background.dispose();
    }
}
