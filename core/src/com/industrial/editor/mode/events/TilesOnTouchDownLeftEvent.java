package com.industrial.editor.mode.events;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.MapRendererImpl;
import com.industrial.editor.handlers.action.ActionsHandler;
import com.industrial.editor.mode.tools.TilesTools;

import java.util.Set;

public class TilesOnTouchDownLeftEvent implements OnTouchDownLeftEvent {

	@Override
	public boolean run(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   final ActionsHandler actionsHandler,
					   final GameAssetManager assetsManager,
					   final Set<MapNodeData> initializedTiles, SelectionHandler selectionHandler) {
		if (currentProcess != null) return false;
		if (MapRendererImpl.getTool() == TilesTools.BRUSH) {
			actionsHandler.beginTilePlacingProcess(assetsManager, initializedTiles);
		} else if (MapRendererImpl.getTool() == TilesTools.LIFT) {
			actionsHandler.beginSelectingTileForLiftProcess(1, initializedTiles);
		} else if (MapRendererImpl.getTool() == TilesTools.WALL_TILING) {
			actionsHandler.beginSelectingTilesForWallTiling();
		}
		return true;
	}
}
