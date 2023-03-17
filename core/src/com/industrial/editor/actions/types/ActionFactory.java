package com.industrial.editor.actions.types;

import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElements;
import com.industrial.editor.model.elements.PlacedEnvObject;

public final class ActionFactory {

	public static LiftNodesAction liftNodes(GameMap map,
											LiftNodesAction.Parameters parameters,
											PlacedElements placedElements) {
		return new LiftNodesAction(map, parameters, placedElements);
	}

	public static DefineEnvObjectAction defineEnvObject(final GameMap map,
														final PlacedEnvObject element,
														final float height) {
		return new DefineEnvObjectAction(map, element, height);
	}
}
