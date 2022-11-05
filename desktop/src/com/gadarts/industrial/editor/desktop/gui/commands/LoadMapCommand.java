package com.gadarts.industrial.editor.desktop.gui.commands;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.managers.PersistenceManager;
import com.industrial.editor.MapRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

import static com.gadarts.industrial.editor.desktop.gui.managers.PersistenceManager.SETTINGS_KEY_LAST_OPENED_FOLDER;
import static javax.swing.SwingUtilities.getWindowAncestor;

public class LoadMapCommand extends MapperCommand {


	public LoadMapCommand(MapRenderer mapRenderer,
						  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		PersistenceManager persistenceManager = getManagers().getPersistenceManager();
		Map<String, String> settings = persistenceManager.getSettings();
		if (settings.containsKey(SETTINGS_KEY_LAST_OPENED_FOLDER)) {
			fileChooser.setCurrentDirectory(new File(settings.get(SETTINGS_KEY_LAST_OPENED_FOLDER)));
		}
		if (fileChooser.showOpenDialog(getWindowAncestor((Component) e.getSource())) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			persistenceManager.tryOpeningFile(file, (JFrame) SwingUtilities.windowForComponent((Component) e.getSource()));
		}
	}


}
