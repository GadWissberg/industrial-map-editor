package com.gadarts.industrial.editor.desktop.gui.commands;

import com.gadarts.industrial.editor.desktop.dialogs.SetAmbientLightDialog;
import com.gadarts.industrial.editor.desktop.gui.managers.DialogsManager;
import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.industrial.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class OpenAmbientLightDialogCommand extends MapperCommand {


	public OpenAmbientLightDialogCommand(MapRenderer mapRenderer,
										 Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		MapRenderer mapRenderer = getMapRenderer();
		DialogsManager dialogsManager = getManagers().getDialogsManager();
		dialogsManager.openDialog(new SetAmbientLightDialog(mapRenderer.getAmbientLightValue(), mapRenderer));
	}

}
