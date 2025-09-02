package net.jneto.taiwan49;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private final boolean oledMode;

    public Main() {
        this.oledMode = true; // false para desligado
    }

    public boolean isOledMode() {
        return oledMode;
    }

    @Override
    public void create() {
        setScreen(new FirstScreen(this, oledMode));
    }
}