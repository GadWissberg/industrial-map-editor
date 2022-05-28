package com.gadarts.industrial.editor.desktop.gui.commands.tools;

import com.gadarts.industrial.editor.desktop.gui.managers.EntitiesSelectionPanelManager;
import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.ModesManager;
import com.gadarts.industrial.editor.desktop.gui.commands.MapperCommand;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.mode.tools.EditorTool;

import java.awt.event.ActionEvent;

public abstract class SetToolCommand extends MapperCommand {
	private EditorTool tool;

	public SetToolCommand(MapRenderer mapRenderer,
						  Managers managers) {
		super(mapRenderer, managers);
	}

	protected abstract EditorTool getTool();

	@Override
	public void actionPerformed(final ActionEvent e) {
		applyTool();
	}

	private void applyTool() {
		EditorTool tool = getTool();
		if (ModesManager.getSelectedTool() == tool) return;

		getManagers().getToolbarsManager().setLatestSelectedToolPerMode(tool);
		getManagers().getModesManager().applyTool(tool);
		EntitiesSelectionPanelManager entitiesSelectionPanelManager = getManagers().getEntitiesSelectionPanelManager();
		entitiesSelectionPanelManager.changeEntitiesSelectionModePerTool(ModesManager.getSelectedMode(), tool);
		getMapRenderer().onToolSet(tool);
	}
}
