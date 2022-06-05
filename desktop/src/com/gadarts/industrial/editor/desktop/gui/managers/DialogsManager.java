package com.gadarts.industrial.editor.desktop.gui.managers;

import com.gadarts.industrial.editor.desktop.gui.dialogs.DialogPane;
import com.gadarts.industrial.editor.desktop.gui.GuiUtils;
import com.industrial.editor.MapRenderer;

import javax.swing.*;

public class DialogsManager extends BaseManager {
	private final JFrame parentWindow;

	public DialogsManager(MapRenderer mapRenderer, JFrame parentWindow) {
		super(mapRenderer);
		this.parentWindow = parentWindow;
	}

	public void openDialog(DialogPane dialog) {
		GuiUtils.openNewDialog(parentWindow, dialog);
	}
}
