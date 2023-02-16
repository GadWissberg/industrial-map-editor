package com.industrial.editor.actions.types;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.actions.MappingAction;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedEnvObject;

public class DefineEnvObjectAction extends MappingAction {
	private final static Vector3 auxVector = new Vector3();
	private final float height;
	private final PlacedEnvObject element;

	public DefineEnvObjectAction(final GameMap map, final PlacedEnvObject element, final float height) {
		super(map);
		this.element = element;
		this.height = height;
	}

	@Override
	public void execute(final MapEditorEventsNotifier eventsNotifier) {
		float actualHeight = Math.max(element.getNode().getHeight(), height);
		element.setHeight(actualHeight);
		Matrix4 transform = element.getModelInstance().transform;
		Vector3 position = transform.getTranslation(auxVector);
		transform.setTranslation(position.x, actualHeight, position.z);
	}

	@Override
	public boolean isProcess( ) {
		return false;
	}
}
