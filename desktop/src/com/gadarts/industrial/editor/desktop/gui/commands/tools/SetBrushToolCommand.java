package com.gadarts.industrial.editor.desktop.gui.commands.tools;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.managers.ModesManager;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.ElementTools;
import com.industrial.editor.mode.tools.TilesTools;

public class SetBrushToolCommand extends SetToolCommand {

	public SetBrushToolCommand(MapRenderer mapRenderer,
							   Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorTool getTool( ) {
		EditorMode selectedMode = ModesManager.getSelectedMode();
		EditorTool result = null;
		if (selectedMode == EditModes.TILES) {
			result = TilesTools.BRUSH;
		} else if (selectedMode == EditModes.ENVIRONMENT) {
			result = ElementTools.BRUSH;
		}
		return result;
	}

}
