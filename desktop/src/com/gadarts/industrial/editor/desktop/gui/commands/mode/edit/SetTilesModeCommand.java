package com.gadarts.industrial.editor.desktop.gui.commands.mode.edit;

import com.gadarts.industrial.editor.desktop.gui.commands.mode.SetModeCommand;
import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;

public class SetTilesModeCommand extends SetModeCommand {

	public SetTilesModeCommand(MapRenderer mapRenderer,
							   Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorMode getMode( ) {
		return EditModes.TILES;
	}


}
