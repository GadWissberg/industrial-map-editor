package com.gadarts.industrial.editor.desktop.gui.managers;

import com.industrial.editor.MapRenderer;

import javax.swing.*;
import java.io.File;

public interface Managers {
	void onApplicationStart(JPanel mainPanel, JFrame windowParent, JPanel entitiesPanel, File assetsFolderLocation);

	void onMapRendererIsReady(MapRenderer mapRenderer, JFrame windowParent);

	PersistenceManager getPersistenceManager();

	ModesManager getModesManager();

	ToolbarsManager getToolbarsManager();

	EntitiesSelectionPanelManager getEntitiesSelectionPanelManager( );

	DialogsManager getDialogsManager();
}
