package com.gadarts.industrial.editor.desktop.gui.menu.definitions;

import com.gadarts.industrial.editor.desktop.gui.commands.LoadMapCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.NewMapCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.SaveMapCommand;
import com.gadarts.industrial.editor.desktop.gui.menu.MenuItemProperties;

public enum FileMenuItemsDefinitions implements MenuItemDefinition {
	NEW(new MenuItemProperties("New", NewMapCommand.class, "file_new")),
	SAVE(new MenuItemProperties("Save", SaveMapCommand.class, "file_save")),
	LOAD(new MenuItemProperties("Load", LoadMapCommand.class, "file_load"));

	private final MenuItemProperties menuItemProperties;

	FileMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
