package com.gadarts.industrial.editor.desktop.gui.managers;

import com.industrial.editor.MapRenderer;
import lombok.Getter;

import javax.swing.*;
import java.io.File;
import java.util.Map;

import static com.gadarts.industrial.editor.desktop.gui.Gui.SETTINGS_KEY_LAST_OPENED_FILE;
import static com.gadarts.industrial.editor.desktop.gui.managers.PersistenceManager.SETTINGS_KEY_LAST_OPENED_FOLDER;

@Getter
public class ManagersImpl implements Managers {
	private final PersistenceManager persistenceManager;
	private final ModesManager modesManager = new ModesManager();
	private final ToolbarsManager toolbarsManager;
	private final DialogsManager dialogsManager;
	private final EntitiesSelectionPanelManager entitiesSelectionPanelManager;

	public ManagersImpl(MapRenderer mapRenderer, JFrame parentWindow) {
		this.dialogsManager = new DialogsManager(mapRenderer, parentWindow);
		this.toolbarsManager = new ToolbarsManager(mapRenderer, this);
		this.entitiesSelectionPanelManager = new EntitiesSelectionPanelManager(mapRenderer);
		this.persistenceManager = new PersistenceManager(mapRenderer);
	}

	@Override
	public void onApplicationStart(JPanel mainPanel, JFrame windowParent, JPanel entitiesPanel, File assetsFolderLocation) {
		toolbarsManager.onApplicationStart(mainPanel, windowParent);
		entitiesSelectionPanelManager.onApplicationStart(entitiesPanel, assetsFolderLocation);
	}

	@Override
	public void onMapRendererIsReady(MapRenderer mapRenderer, JFrame windowParent) {
		persistenceManager.readSettingsFile(windowParent, mapRenderer);
		openLatestFile(windowParent);
	}

	private void openLatestFile(JFrame windowParent) {
		Map<String, String> settings = persistenceManager.getSettings();
		if (settings.containsKey(SETTINGS_KEY_LAST_OPENED_FOLDER)) {
			persistenceManager.tryOpeningFile(new File(settings.get(SETTINGS_KEY_LAST_OPENED_FILE)), windowParent);
		}
	}
}
