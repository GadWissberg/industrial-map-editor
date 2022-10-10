package com.gadarts.industrial.editor.desktop.gui.menu.definitions;

import com.gadarts.industrial.editor.desktop.gui.commands.OpenAmbientLightDialogCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.OpenMapSizeDialogCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.mode.edit.*;
import com.gadarts.industrial.editor.desktop.gui.menu.MenuItemProperties;

public enum EditMenuItemsDefinitions implements MenuItemDefinition {
	MODE_TILE(new MenuItemProperties("Tiles Mode", SetTilesModeCommand.class, "mode_tile", Menus.Constants.BUTTON_GROUP_MODES)),
	MODE_CHARACTER(new MenuItemProperties("Characters Mode", SetCharactersModeCommand.class, "mode_character", Menus.Constants.BUTTON_GROUP_MODES)),
	MODE_ENV(new MenuItemProperties("Environment Mode", SetEnvironmentModeCommand.class, "mode_env", Menus.Constants.BUTTON_GROUP_MODES)),
	MODE_PICKUPS(new MenuItemProperties("Pick-Ups Mode", SetPickupsModeCommand.class, "mode_pickup", Menus.Constants.BUTTON_GROUP_MODES)),
	MODE_LIGHTS(new MenuItemProperties("Lights Mode", SetLightsModeCommand.class, "mode_light", Menus.Constants.BUTTON_GROUP_MODES)),
	MODE_TRIGGERS(new MenuItemProperties("Triggers Mode", SetTriggersModeCommand.class, "mode_trigger", Menus.Constants.BUTTON_GROUP_MODES)),

	SEPARATOR_1(),
	SET_AMBIENT_LIGHT(new MenuItemProperties("Set Ambient Light", OpenAmbientLightDialogCommand.class, "ambient_light")),
	SET_MAP_SIZE(new MenuItemProperties("Set Map Size", OpenMapSizeDialogCommand.class, "size"));
	private final MenuItemProperties menuItemProperties;

	EditMenuItemsDefinitions(final MenuItemProperties menuItemProperties) {
		this.menuItemProperties = menuItemProperties;
	}

	EditMenuItemsDefinitions() {
		menuItemProperties = null;
	}

	@Override
	public MenuItemProperties getMenuItemProperties() {
		return menuItemProperties;
	}


}
