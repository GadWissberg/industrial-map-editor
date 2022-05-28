package com.gadarts.industrial.editor.desktop.gui.commands.mode.view;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.commands.mode.SetModeCommand;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.ViewModes;

public class SetRotateModeCommand extends SetModeCommand {

	public SetRotateModeCommand(MapRenderer mapRenderer,
								Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorMode getMode( ) {
		return ViewModes.ROTATE;
	}


}
