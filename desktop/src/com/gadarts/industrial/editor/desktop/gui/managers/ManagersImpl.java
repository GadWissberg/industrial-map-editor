package com.gadarts.industrial.editor.desktop.gui.managers;

import com.industrial.editor.MapRenderer;
import lombok.Getter;

import javax.swing.*;
import java.io.File;

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
	}


}
