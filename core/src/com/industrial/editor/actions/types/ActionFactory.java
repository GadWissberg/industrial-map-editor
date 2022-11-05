package com.industrial.editor.actions.types;

import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedEnvObject;

/**
 * Factory for creating actions that modify the map data.
 */
public final class ActionFactory {

	/**
	 * @param map
	 * @param parameters
	 * @return An action that modifies the height of the given nodes.
	 */
	public static LiftNodesAction liftNodes(final GameMap map, final LiftNodesAction.Parameters parameters) {
		return new LiftNodesAction(map, parameters);
	}

	/**
	 * @param map
	 * @param element
	 * @param height
	 * @return An action that sets the values of a given placed environment object.
	 */
	public static DefineEnvObjectAction defineEnvObject(final GameMap map,
														final PlacedEnvObject element,
														final float height) {
		return new DefineEnvObjectAction(map, element, height);
	}
}
