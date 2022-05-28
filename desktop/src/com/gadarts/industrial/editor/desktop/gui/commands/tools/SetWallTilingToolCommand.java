package com.gadarts.industrial.editor.desktop.gui.commands.tools;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.TilesTools;

public class SetWallTilingToolCommand extends SetToolCommand {

	public SetWallTilingToolCommand(MapRenderer mapRenderer,
									Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorTool getTool( ) {
		return TilesTools.WALL_TILING;
	}

}
