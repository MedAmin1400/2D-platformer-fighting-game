package com.badlogic.platformer.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // Handles macOS/Windows JVM requirements.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MyPlatformerGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Graphics.DisplayMode primaryMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("2D Platformer"); // Set the title to match your game.
        config.useVsync(true); // Enable Vsync to prevent screen tearing.
        //config.setFullscreenMode(primaryMode); // Default to fullscreen mode.
        config.setWindowedMode(1920, 1080);

        // Optional: Set a custom window icon for branding.
        config.setWindowIcon("icon128.png", "icon64.png","icon32.png", "icon16.png");

        return config;
    }
}
