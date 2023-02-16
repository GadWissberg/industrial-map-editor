package com.industrial.editor.mode.events;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.MapRendererImpl;
import com.industrial.editor.handlers.action.ActionsHandler;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.ElementTools;

import java.util.Set;

public class EnvOnTouchDownEventLeft implements OnTouchDownLeftEvent {
	@Override
	public boolean run(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   ActionsHandler actionsHandler,
					   GameAssetManager assetsManager,
					   Set<MapNodeData> initializedTiles,
					   SelectionHandler selectionHandler) {
		EditorTool tool = MapRendererImpl.getTool();
		if (tool == ElementTools.BRUSH && selectionHandler.getSelectedElement() != null) {
			actionsHandler.placeEnvObject(assetsManager);
		} else if (tool == ElementTools.DEFINE) {
			actionsHandler.defineSelectedEnvObject();
		}
		return true;
	}
}
