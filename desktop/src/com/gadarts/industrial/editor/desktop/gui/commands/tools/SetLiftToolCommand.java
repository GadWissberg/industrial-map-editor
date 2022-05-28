package com.gadarts.industrial.editor.desktop.gui.commands.tools;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.TilesTools;

public class SetLiftToolCommand extends SetToolCommand {

	public SetLiftToolCommand(MapRenderer mapRenderer,
							  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorTool getTool( ) {
		return TilesTools.LIFT;
	}


}
