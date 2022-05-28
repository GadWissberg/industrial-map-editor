package com.gadarts.industrial.editor.desktop.gui.commands.mode.edit;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.commands.mode.SetModeCommand;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;

public class SetEnvironmentModeCommand extends SetModeCommand {

	public SetEnvironmentModeCommand(MapRenderer mapRenderer,
									 Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorMode getMode( ) {
		return EditModes.ENVIRONMENT;
	}


}
