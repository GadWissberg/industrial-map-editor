package com.industrial.editor.mode.events;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.handlers.action.ActionsHandler;

import java.util.Set;

public class PickupsOnTouchDownEventLeft implements OnTouchDownLeftEvent {
	@Override
	public boolean run(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   final ActionsHandler actionsHandler,
					   final GameAssetManager assetsManager,
					   final Set<MapNodeData> initializedTiles, SelectionHandler selectionHandler) {
		actionsHandler.placePickup(assetsManager);
		return true;
	}
}
