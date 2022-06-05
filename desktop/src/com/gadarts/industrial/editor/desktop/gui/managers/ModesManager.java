package com.gadarts.industrial.editor.desktop.gui.managers;

import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.TilesTools;
import lombok.Getter;

@Getter
public class ModesManager {

	@Getter
	private static EditorMode selectedMode = EditModes.TILES;

	@Getter
	private static EditorTool selectedTool = TilesTools.BRUSH;

	public void applyMode(final EditorMode mode) {
		if (ModesManager.selectedMode == mode) return;
		ModesManager.selectedMode = mode;
	}

	public void applyTool(final EditorTool tool) {
		if (ModesManager.selectedTool == tool) return;
		ModesManager.selectedTool = tool;
	}

}
