package com.badlogic.platformer.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {

    private final MyPlatformerGame game;
    private SpriteBatch batch;
    private Sound hitSound;
    private Sound damageSound;
    private Sound gameOverSound;
    private Sound jumpSound;

    // Background and floor
    private Texture background;
    private Texture floor;

    // Characters
    private Texture character1Texture;
    private Texture character1AttackTexture;
    private Rectangle character1Rect;
    private Vector2 character1Position;
    private Vector2 character1Velocity;
    public int character1Health = 100;
    private boolean character1Attacking = false;
    private float character1AttackTime = 0f;

    private Texture character2Texture;
    private Texture character2AttackTexture;
    private Rectangle character2Rect;
    private Vector2 character2Position;
    private Vector2 character2Velocity;
    public int character2Health = 100;
    private boolean character2Attacking = false;
    private float character2AttackTime = 0f;

    // Font for displaying text
    private BitmapFont font;

    // Game state
    private boolean isGameOver = false;
    private String gameOverMessage = "";

    // Constants
    private final float GRAVITY = -4500f;
    private final float MOVE_SPEED = 1200f;
    private final float JUMP_SPEED = 2000f;
    private final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private final float FLOOR_HEIGHT = 150;
    private final float ATTACK_DURATION = 0.1f; // Duration of attack animation in seconds



    // UI Scaling
    private float uiScale = 1f;

    private PauseScreen pauseScreen;

    private float alpha;

    public GameScreen(MyPlatformerGame game) {
        this.game = game;
        batch = new SpriteBatch();

        // Initialize font
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.setColor(Color.WHITE);

        // Load sound files
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit_sound.wav"));
        damageSound = Gdx.audio.newSound(Gdx.files.internal("damage_sound.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("game_over_sound.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump_sound.mp3"));

        // Load textures
        background = new Texture("game_background.png");
        floor = new Texture("floor_texture.png");
        character1Texture = new Texture("character1.png");
        character1AttackTexture = new Texture("character1_attack.png");
        character2Texture = new Texture("character2.png");
        character2AttackTexture = new Texture("character2_attack.png");

        // Initialize positions and hitboxes
        character1Position = new Vector2(SCREEN_WIDTH / 2 - 50, FLOOR_HEIGHT);
        character1Velocity = new Vector2(0, 0);
        character1Rect = new Rectangle(character1Position.x, character1Position.y, 128, 128);

        character2Position = new Vector2(SCREEN_WIDTH / 2 + 50, FLOOR_HEIGHT);
        character2Velocity = new Vector2(0, 0);
        character2Rect = new Rectangle(character2Position.x, character2Position.y, 128, 128);
        pauseScreen = new PauseScreen(game, this);

        alpha = 0f;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if (isGameOver) {
            renderGameOver();
            return;
        }

        batch.setColor(1, 1, 1, alpha);

        update(delta);

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw game elements
        batch.begin();
        batch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.draw(floor, 0, 0, SCREEN_WIDTH, FLOOR_HEIGHT);

        // Draw characters and attacks
        drawCharacters();

        // Draw health bars
        drawHealthBars();


        batch.end();

        // Update the fade effect (for transitions)
        if (alpha < 1f) {
            alpha += delta * 0.5f; // Slowly fade in
        }
    }

    private void drawCharacters() {
        // Scaling factors for characters
        float scaleFactor = 1.5f; // Increase size by 50%

        // Update character dimensions
        float character1Width = character1Rect.width * 2F;
        float character1Height = character1Rect.height * 2F;
        float character2Width = character2Rect.width * 2F;
        float character2Height = character2Rect.height * 2F;

        // Draw Character 1
        if (character1Attacking) {
            batch.draw(character1AttackTexture, character1Position.x, character1Position.y, character1Width, character1Height);
        } else {
            batch.draw(character1Texture, character1Position.x, character1Position.y, character1Width, character1Height);
        }

        // Draw Character 2
        if (character2Attacking) {
            batch.draw(character2AttackTexture, character2Position.x, character2Position.y, character2Width, character2Height);
        } else {
            batch.draw(character2Texture, character2Position.x, character2Position.y, character2Width, character2Height);
        }
    }

    private void drawHealthBars() {
        float barWidth = 200 * uiScale;
        float barHeight = 20 * uiScale;
        float padding = 20;

        // Character 1 Health Bar
        batch.draw(new Texture("health_bar_bg.png"), padding, Gdx.graphics.getHeight() - barHeight - padding, barWidth, barHeight);
        batch.draw(new Texture("health_bar_fg.png"), padding, Gdx.graphics.getHeight() - barHeight - padding, barWidth * (character1Health / 100f), barHeight);
        font.getData().setScale(1);
        font.draw(batch, "Player 1: " + character1Health, padding + barWidth / 2 - font.getRegion().getRegionWidth() / 2 + 110, Gdx.graphics.getHeight() - barHeight - padding - 15);

        // Character 2 Health Bar
        batch.draw(new Texture("health_bar_bg.png"), Gdx.graphics.getWidth() - barWidth - padding, Gdx.graphics.getHeight() - barHeight - padding, barWidth, barHeight);
        batch.draw(new Texture("health_bar_fg.png"), Gdx.graphics.getWidth() - barWidth - padding, Gdx.graphics.getHeight() - barHeight - padding, barWidth * (character2Health / 100f), barHeight);
        font.draw(batch, "Player 2: " + character2Health, Gdx.graphics.getWidth() - barWidth / 2 - padding - font.getRegion().getRegionWidth() / 2 + 110, Gdx.graphics.getHeight() - barHeight - padding - 15);
    }

    private void renderGameOver() {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin drawing with SpriteBatch
        batch.begin();

        // Draw the background (same way you would draw other textures)
        batch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Display the Game Over message at the center of the screen
        font.getData().setScale(1.5F); // Adjust font size for visibility
        font.draw(batch, gameOverMessage, SCREEN_WIDTH / 2 - font.getRegion().getRegionWidth() / 2, SCREEN_HEIGHT / 2);

        // Display the "Press ENTER to Restart" message
        font.getData().setScale(1); // Adjust font size for the restart text
        font.draw(batch, "Press ENTER to Restart", SCREEN_WIDTH / 2 - font.getRegion().getRegionWidth() / 2 + 50, SCREEN_HEIGHT / 2 - 50);

        // End drawing with SpriteBatch
        batch.end();

        // Listen for ENTER key to reset the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            resetGame();  // Reset the game when ENTER is pressed
        }
    }


    private void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(pauseScreen);
            return;
        }

        // Apply gravity
        character1Velocity.y += GRAVITY * delta;
        character2Velocity.y += GRAVITY * delta;

        // Update positions
        character1Position.add(character1Velocity.x * delta, character1Velocity.y * delta);
        character2Position.add(character2Velocity.x * delta, character2Velocity.y * delta);

        // Collision with floor for both characters
        if (character1Position.y <= FLOOR_HEIGHT) {
            character1Position.y = FLOOR_HEIGHT;
            character1Velocity.y = 0;
        }
        if (character2Position.y <= FLOOR_HEIGHT) {
            character2Position.y = FLOOR_HEIGHT;
            character2Velocity.y = 0;
        }

        // Prevent characters from moving out of bounds
        character1Position.x = Math.max(0, Math.min(SCREEN_WIDTH - character1Rect.width * 1.5F  , character1Position.x));
        character2Position.x = Math.max(0, Math.min(SCREEN_WIDTH - character2Rect.width * 1.5F , character2Position.x));

        // Update hitboxes
        character1Rect.setPosition(character1Position.x, character1Position.y);
        character2Rect.setPosition(character2Position.x, character2Position.y);

        // Input for Character 1
        handleCharacter1Input(delta);
        // Input for Character 2
        handleCharacter2Input(delta);

        // Check for attacks
        checkAttacks();

        // Check for game over
        checkGameOver();
    }

    private void handleCharacter1Input(float delta) {
        // Character 1 Movement (Left/Right)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            character1Velocity.x = -MOVE_SPEED;  // Move left
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            character1Velocity.x = MOVE_SPEED;  // Move right
        } else {
            character1Velocity.x = 0;  // Stop moving horizontally when no key is pressed
        }

        // Character 1 Jumping (Up)
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && character1Position.y == FLOOR_HEIGHT) {
            character1Velocity.y = JUMP_SPEED;  // Apply upward velocity when jump key is pressed
            jumpSound.play();
        }

        // Character 1 Attack (NUMPAD_0)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0)) {
            character1Attacking = true;
            character1AttackTime = ATTACK_DURATION;  // Start attack timer
            hitSound.play();
        }

        // Attack cooldown for Character 1
        if (character1AttackTime > 0) {
            character1AttackTime -= delta;
        } else {
            character1Attacking = false;  // End attack after cooldown
        }
    }

    private void handleCharacter2Input(float delta) {
        // Character 2 Movement (A/D)
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            character2Velocity.x = -MOVE_SPEED;  // Move left
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            character2Velocity.x = MOVE_SPEED;  // Move right
        } else {
            character2Velocity.x = 0;  // Stop moving horizontally when no key is pressed
        }

        // Character 2 Jumping (W)
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && character2Position.y == FLOOR_HEIGHT) {
            character2Velocity.y = JUMP_SPEED;  // Apply upward velocity when jump key is pressed
            jumpSound.play();
        }

        // Character 2 Attack (SPACE)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            character2Attacking = true;
            character2AttackTime = ATTACK_DURATION; // Start attack timer
            hitSound.play();
        }

        // Attack cooldown for Character 2
        if (character2AttackTime > 0) {
            character2AttackTime -= delta;
        } else {
            character2Attacking = false;  // End attack after cooldown
        }
    }

    private void checkAttacks() {
        if (character1Attacking && character1Rect.overlaps(character2Rect)) {
            character2Health = Math.max(0, character2Health - 10);
            character1Attacking = false;
            damageSound.play();

        }
        if (character2Attacking && character2Rect.overlaps(character1Rect)) {
            character1Health = Math.max(0, character1Health - 10);
            character2Attacking = false;
            damageSound.play();
        }
    }

    private void checkGameOver() {
        if (character1Health <= 0 && character2Health <= 0) {
            gameOverMessage = "DRAW";
            isGameOver = true;
            gameOverSound.play();
        } else if (character1Health <= 0) {
            gameOverMessage = "Character 2 Wins!";
            isGameOver = true;
            gameOverSound.play();
        } else if (character2Health <= 0) {
            gameOverMessage = "Character 1 Wins!";
            isGameOver = true;
            gameOverSound.play();
        }
    }

    private void resetGame() {
        character1Health = 100;
        character2Health = 100;
        character1Position.set(SCREEN_WIDTH / 2 - 50, FLOOR_HEIGHT);
        character2Position.set(SCREEN_WIDTH / 2 + 50, FLOOR_HEIGHT);
        isGameOver = false;
    }

    @Override
    public void resize(int width, int height) {
        uiScale = (float) width / 1280; // Scale UI based on screen width
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        floor.dispose();
        character1Texture.dispose();
        character1AttackTexture.dispose();
        character2Texture.dispose();
        character2AttackTexture.dispose();
        font.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
