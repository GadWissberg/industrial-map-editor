package com.gadarts.industrial.editor.desktop.gui.menu.definitions;

import lombok.Getter;

@Getter
public enum Menus {
    FILE("File", FileMenuItemsDefinitions.values()),
    EDIT("Edit", EditMenuItemsDefinitions.values()),
    VIEW("View", ViewMenuItemsDefinitions.values());

    private final MenuItemDefinition[] definitions;
    private final String label;

    Menus(final String label, final MenuItemDefinition[] values) {
        this.label = label;
        this.definitions = values;
    }

    public static final class Constants {
        public static final String BUTTON_GROUP_MODES = "modes";

        private Constants() {
        }
    }
}
