package com.badlogic.platformer.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MainMenuScreen implements Screen {

    private final MyPlatformerGame game;
    private SpriteBatch batch;
    private Texture background;
    private Texture playButtonTexture;
    private Texture playButtonHoverTexture;
    private Texture optionsButtonTexture;
    private Texture optionsButtonHoverTexture;
    private Texture exitButtonTexture;
    private Texture exitButtonHoverTexture;
    private BitmapFont font;

    private Rectangle playButtonBounds;
    private Rectangle optionsButtonBounds;
    private Rectangle exitButtonBounds;

    private Sound buttonClickSound;
    private Sound buttonHoverSound;
    private Music backgroundMusic;


    private float alpha; // For screen fade animation

    public MainMenuScreen(MyPlatformerGame game) {
        this.game = game;
        batch = new SpriteBatch();

        // Load assets
        background = new Texture("main_menu_background.png");
        playButtonTexture = new Texture("play_button.png");
        playButtonHoverTexture = new Texture("play_button_hover.png");
        optionsButtonTexture = new Texture("options_button.png");
        optionsButtonHoverTexture = new Texture("options_button_hover.png");
        exitButtonTexture = new Texture("exit_button.png");
        exitButtonHoverTexture = new Texture("exit_button_hover.png");
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button_click.wav"));
        buttonHoverSound = Gdx.audio.newSound(Gdx.files.internal("button_hover.mp3")); // Hover sound
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3")); // Background music

        // Set up background music to loop
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.15f);
        backgroundMusic.play();



        // Define button boundaries
        int buttonWidth = 200;
        int buttonHeight = 100;
        int centerX = Gdx.graphics.getWidth() / 2;
        playButtonBounds = new Rectangle(centerX - buttonWidth / 2, Gdx.graphics.getHeight() / 2 + 80, buttonWidth, buttonHeight);
        optionsButtonBounds = new Rectangle(centerX - buttonWidth / 2, Gdx.graphics.getHeight() / 2 - 40, buttonWidth, buttonHeight);
        exitButtonBounds = new Rectangle(centerX - buttonWidth / 2, Gdx.graphics.getHeight() / 2 - 160, buttonWidth, buttonHeight);

        alpha = 0f; // Initial alpha for fade in
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        // Draw background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw buttons with hover effect
        drawButton(playButtonTexture, playButtonHoverTexture, playButtonBounds, "Play");
        drawButton(optionsButtonTexture, optionsButtonHoverTexture, optionsButtonBounds, "Options");
        drawButton(exitButtonTexture, exitButtonHoverTexture, exitButtonBounds, "Exit");

        // Fade effect

        batch.end();

        // Handle input
        handleInput();

        // Update the fade effect (for transitions)
        if (alpha < 1f) {
            alpha += delta * 0.5f; // Slowly fade in
        }
    }

    private void drawButton(Texture normal, Texture hover, Rectangle bounds, String label) {
        boolean isHovered = bounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        Texture textureToDraw = isHovered ? hover : normal;
        // Play hover sound if the mouse enters the button area
        if (isHovered && !isButtonHovered(bounds)) {
            buttonHoverSound.play();
        }


        batch.draw(textureToDraw, bounds.x, bounds.y, bounds.width, bounds.height);

    }

    private boolean isButtonHovered(Rectangle bounds) {
        return bounds.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

    }

    private void handleInput() {
        // Check for mouse clicks on buttons
        if (Gdx.input.isTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip y-axis

            if (playButtonBounds.contains(x, y)) {
                buttonClickSound.play();
                fadeToGameScreen(); // Transition to game screen with fade
            } else if (optionsButtonBounds.contains(x, y)) {
                buttonClickSound.play();
                fadeToOptionsScreen(); // Transition to options screen with fade
            } else if (exitButtonBounds.contains(x, y)) {
                Gdx.app.exit(); // Exit the game
            }
        }

        // Handle keyboard shortcuts
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            fadeToGameScreen(); // Start game
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit(); // Exit game
        }
    }

    private void fadeToGameScreen() {
        alpha = 0f; // Reset alpha for fade out
        game.setScreen(new GameScreen(game)); // Transition to game screen
        backgroundMusic.stop();
    }

    private void fadeToOptionsScreen() {
        alpha = 0f; // Reset alpha for fade out
        MainMenuScreen mainMenuScreen = new MainMenuScreen(game);
        game.setScreen(new OptionsScreen(game, this)); // Transition to options screen
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        playButtonTexture.dispose();
        playButtonHoverTexture.dispose();
        optionsButtonTexture.dispose();
        optionsButtonHoverTexture.dispose();
        exitButtonTexture.dispose();
        exitButtonHoverTexture.dispose();
        font.dispose();
        buttonClickSound.dispose();
        buttonHoverSound.dispose();
        backgroundMusic.stop(); // Stop the music when disposing
        backgroundMusic.dispose();
    }
}
