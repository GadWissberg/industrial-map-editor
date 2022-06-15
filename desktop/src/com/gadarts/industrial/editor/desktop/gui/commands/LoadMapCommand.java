package com.gadarts.industrial.editor.desktop.gui.commands;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.managers.PersistenceManager;
import com.industrial.editor.MapRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.gadarts.industrial.editor.desktop.gui.Gui.SETTINGS_FILE;
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
		Map<String, String> settings = getManagers().getPersistenceManager().getSettings();
		if (settings.containsKey(SETTINGS_KEY_LAST_OPENED_FOLDER)) {
			fileChooser.setCurrentDirectory(new File(settings.get(SETTINGS_KEY_LAST_OPENED_FOLDER)));
		}
		if (fileChooser.showOpenDialog(getWindowAncestor((Component) e.getSource())) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			tryOpeningFile(file, e);
		}
	}

	private void tryOpeningFile(final File file, ActionEvent e) {
		try {
			getMapRenderer().onLoadMapRequested(file.getPath());
			PersistenceManager persistenceManager = getManagers().getPersistenceManager();
			persistenceManager.updateCurrentlyOpenedFile(
					file,
					SETTINGS_FILE,
					(JFrame) SwingUtilities.windowForComponent((Component) e.getSource()));
			persistenceManager.updateLastOpenedFolder(SETTINGS_FILE, file.getParent());
		} catch (final IOException error) {
			error.printStackTrace();
			Window windowAncestor = getWindowAncestor((Component) e.getSource());
			JOptionPane.showMessageDialog(windowAncestor, "Failed to open map file!");
		}
	}

}
