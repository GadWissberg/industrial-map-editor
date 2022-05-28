package com.industrial.editor.mode;

import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.handlers.action.ActionsHandler;

import java.util.Set;

public interface EditorMode {
	int ordinal( );

	void onTouchDownLeft(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
						 ActionsHandler tool,
						 GameAssetsManager actionsHandler,
						 Set<MapNodeData> initializedTiles, SelectionHandler selectionHandler);

	String getDisplayName( );

	EditorTool[] getTools( );

	ModeType getType( );

	String name( );
}
