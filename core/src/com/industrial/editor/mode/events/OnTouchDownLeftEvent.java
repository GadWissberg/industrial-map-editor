package com.industrial.editor.mode.events;

import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.handlers.action.ActionsHandler;

import java.util.Set;

public interface OnTouchDownLeftEvent {

	boolean run(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
				ActionsHandler actionsHandler,
				GameAssetsManager assetsManager,
				Set<MapNodeData> initializedTiles,
				SelectionHandler selectionHandler);
}
