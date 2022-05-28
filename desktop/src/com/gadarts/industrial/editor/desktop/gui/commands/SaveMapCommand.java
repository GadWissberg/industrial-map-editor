package com.gadarts.industrial.editor.desktop.gui.commands;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.industrial.editor.MapRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

import static com.gadarts.industrial.editor.desktop.gui.Gui.PROGRAM_TILE;
import static com.gadarts.industrial.editor.desktop.gui.Gui.SETTINGS_FILE;
import static javax.swing.SwingUtilities.getWindowAncestor;

public class SaveMapCommand extends MapperCommand {
	public SaveMapCommand(MapRenderer mapRenderer,
						  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		File file = getManagers().getPersistenceManager().getCurrentlyOpenedMap();
		Window windowAncestor = getWindowAncestor((Component) e.getSource());
		if (file == null) {
			JFileChooser chooser = new JFileChooser();
			if (chooser.showSaveDialog(windowAncestor) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			}
		}
		Optional.ofNullable(file).ifPresent(f -> {
			try {
				getMapRenderer().onSaveMapRequested(f.getPath());
				JFrame window = (JFrame) SwingUtilities.windowForComponent((Component) e.getSource());
				getManagers().getPersistenceManager().updateCurrentlyOpenedFile(f, SETTINGS_FILE, window);
				JOptionPane.showMessageDialog(
						windowAncestor,
						String.format("Map was saved successfully to: %s", f.getPath()),
						PROGRAM_TILE,
						JOptionPane.INFORMATION_MESSAGE);
			} catch (NullPointerException error) {
				JOptionPane.showMessageDialog(
						windowAncestor,
						"Failed to save map file!",
						PROGRAM_TILE,
						JOptionPane.ERROR_MESSAGE);
			}
		});

	}
}
