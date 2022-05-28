package com.gadarts.industrial.editor.desktop.gui.toolbar;

import com.gadarts.industrial.editor.desktop.gui.menu.definitions.EditMenuItemsDefinitions;
import com.gadarts.industrial.editor.desktop.gui.menu.definitions.FileMenuItemsDefinitions;
import com.gadarts.industrial.editor.desktop.gui.menu.definitions.ViewMenuItemsDefinitions;

public enum ToolbarDefinitions implements ToolbarButtonDefinition {
	NEW(new ToolbarButtonProperties(
			"file_new",
			"New Map",
			FileMenuItemsDefinitions.NEW)),

	SAVE(new ToolbarButtonProperties(
			"file_save",
			"Save Map",
			FileMenuItemsDefinitions.SAVE)),

	LOAD(new ToolbarButtonProperties(
			"file_load",
			"Load Map",
			FileMenuItemsDefinitions.LOAD)),

	SEPARATOR_1(),

	MODE_TILE(new ToolbarButtonProperties(
			"mode_tile",
			"Tiles Mode",
			EditMenuItemsDefinitions.MODE_TILE)),

	MODE_CHARACTER(new ToolbarButtonProperties(
			"mode_character",
			"Characters Mode",
			EditMenuItemsDefinitions.MODE_CHARACTER)),

	MODE_ENV(new ToolbarButtonProperties(
			"mode_env",
			"Environment Objects Mode",
			EditMenuItemsDefinitions.MODE_ENV)),

	MODE_PICKUP(new ToolbarButtonProperties(
			"mode_pickup",
			"Pick-Ups Mode",
			EditMenuItemsDefinitions.MODE_PICKUPS)),

	MODE_LIGHTS(new ToolbarButtonProperties(
			"mode_light",
			"Lights Mode",
			EditMenuItemsDefinitions.MODE_LIGHTS)),

	SEPARATOR_2(),

	CAMERA_PAN(new ToolbarButtonProperties(
			"camera_pan",
			"Pan Camera",
			ViewMenuItemsDefinitions.CAMERA_PAN)),

	CAMERA_ROTATE(new ToolbarButtonProperties(
			"camera_rotate",
			"Rotate Camera",
			ViewMenuItemsDefinitions.CAMERA_ROTATE)),

	CAMERA_ZOOM(new ToolbarButtonProperties(
			"camera_zoom",
			"Zoom Camera",
			ViewMenuItemsDefinitions.CAMERA_ZOOM)),

	SEPARATOR_3(),

	AMBIENT_LIGHT(new ToolbarButtonProperties(
			"ambient_light",
			"Set Ambient Light",
			EditMenuItemsDefinitions.SET_AMBIENT_LIGHT)),

	MAP_SIZE(new ToolbarButtonProperties(
			"size",
			"Set Map Size",
			EditMenuItemsDefinitions.SET_MAP_SIZE));
	private final ToolbarButtonProperties toolbarButtonProperties;

	ToolbarDefinitions( ) {
		this(null);
	}

	ToolbarDefinitions(final ToolbarButtonProperties toolbarButtonProperties) {
		this.toolbarButtonProperties = toolbarButtonProperties;
	}

	@Override
	public ToolbarButtonProperties getButtonProperties( ) {
		return toolbarButtonProperties;
	}


}
