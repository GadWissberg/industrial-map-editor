package com.industrial.editor.actions.types;

import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedEnvObject;

public final class ActionFactory {

	public static LiftTilesAction liftTiles(final GameMap map, final LiftTilesAction.Parameters parameters) {
		return new LiftTilesAction(map, parameters);
	}

	public static DefineEnvObjectAction defineEnvObject(final GameMap map,
														final PlacedEnvObject element,
														final float height) {
		return new DefineEnvObjectAction(map, element, height);
	}
}
