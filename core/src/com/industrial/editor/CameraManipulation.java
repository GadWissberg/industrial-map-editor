package com.industrial.editor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface CameraManipulation {
	void run(Vector2 lastMouseTouchPosition, OrthographicCamera camera, int screenX, int screenY, Vector3 vector3);
}
