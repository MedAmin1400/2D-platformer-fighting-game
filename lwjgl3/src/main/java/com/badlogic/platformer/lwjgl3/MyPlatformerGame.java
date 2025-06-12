package com.badlogic.platformer.lwjgl3;

import com.badlogic.gdx.Game;

public class MyPlatformerGame extends Game {

    @Override
    public void create() {
        // Set the initial screen to the main menu
        this.setScreen(new MainMenuScreen(this));
    }
}
