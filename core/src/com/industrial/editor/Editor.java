package com.industrial.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;

public abstract class Editor extends ApplicationAdapter implements InputProcessor {

	@Override
	public boolean keyDown(final int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(final int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(final char character) {
		return false;
	}

	@Override
	public boolean scrolled(final float amountX, final float amountY) {
		return false;
	}
}
