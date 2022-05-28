package com.gadarts.industrial.editor.desktop.gui.commands;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.managers.PersistenceManager;
import com.industrial.editor.MapRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.gadarts.industrial.editor.desktop.gui.Gui.DEFAULT_MAP_NAME;
import static com.gadarts.industrial.editor.desktop.gui.Gui.PROGRAM_TILE;
import static com.gadarts.industrial.editor.desktop.gui.Gui.SETTINGS_FILE;
import static com.gadarts.industrial.editor.desktop.gui.Gui.SETTINGS_KEY_LAST_OPENED_FILE;
import static com.gadarts.industrial.editor.desktop.gui.Gui.WINDOW_HEADER;


public class NewMapCommand extends MapperCommand {

	public NewMapCommand(MapRenderer mapRenderer,
						 Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		resetCurrentlyOpenedFile(e);
		getMapRenderer().onNewMapRequested();
	}

	private void resetCurrentlyOpenedFile(ActionEvent e) {
		PersistenceManager persistenceManager = getManagers().getPersistenceManager();
		persistenceManager.setCurrentlyOpenedMap(null);
		JFrame window = (JFrame) SwingUtilities.windowForComponent((Component) e.getSource());
		window.setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		persistenceManager.getSettings().put(SETTINGS_KEY_LAST_OPENED_FILE, null);
		persistenceManager.saveSettings(persistenceManager.getSettings(), SETTINGS_FILE);
	}
}
