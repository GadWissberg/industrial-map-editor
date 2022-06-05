package com.gadarts.industrial.editor.desktop.gui.commands;

import com.gadarts.industrial.editor.desktop.gui.dialogs.SetMapSizeDialog;
import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.industrial.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class OpenMapSizeDialogCommand extends MapperCommand {


	public OpenMapSizeDialogCommand(MapRenderer mapRenderer,
									Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		MapRenderer mapRenderer = getMapRenderer();
		getManagers().getDialogsManager().openDialog(new SetMapSizeDialog(mapRenderer.getMapSize(), mapRenderer));
	}

}
