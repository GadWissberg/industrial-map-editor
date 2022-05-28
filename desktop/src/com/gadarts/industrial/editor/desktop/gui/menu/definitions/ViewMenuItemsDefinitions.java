package com.gadarts.industrial.editor.desktop.gui.menu.definitions;

import com.gadarts.industrial.editor.desktop.gui.commands.mode.view.SetPanModeCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.mode.view.SetRotateModeCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.mode.view.SetZoomModeCommand;
import com.gadarts.industrial.editor.desktop.gui.menu.MenuItemProperties;

public enum ViewMenuItemsDefinitions implements MenuItemDefinition {
	CAMERA_PAN(new MenuItemProperties("Pan Camera", SetPanModeCommand.class, "camera_pan", Menus.Constants.BUTTON_GROUP_MODES)),
	CAMERA_ROTATE(new MenuItemProperties("Rotate Camera", SetRotateModeCommand.class, "camera_rotate", Menus.Constants.BUTTON_GROUP_MODES)),
	CAMERA_ZOOM(new MenuItemProperties("Zoom Camera", SetZoomModeCommand.class, "camera_zoom", Menus.Constants.BUTTON_GROUP_MODES));
	private final MenuItemProperties menuItemProperties;

	ViewMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}
}
