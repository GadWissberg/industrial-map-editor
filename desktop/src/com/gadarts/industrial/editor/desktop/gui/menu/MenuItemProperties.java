package com.gadarts.industrial.editor.desktop.gui.menu;

import com.gadarts.industrial.editor.desktop.gui.commands.MapperCommand;
import lombok.Getter;

@Getter
public class MenuItemProperties {
	private final String label;
	private final Class<? extends MapperCommand> actionClass;
	private final boolean disabledOnStart;
	private final String icon;
	private final String buttonGroup;

	public MenuItemProperties(final String label, final Class<? extends MapperCommand> actionClass, final String icon) {
		this(label, actionClass, icon, false, null);
	}

	public MenuItemProperties(String label, Class<? extends MapperCommand> actionClass, String icon, String buttonGroup) {
		this(label, actionClass, icon, false, buttonGroup);
	}

	public MenuItemProperties(String label,
							  Class<? extends MapperCommand> actionClass,
							  String icon,
							  boolean disabledOnStart,
							  String buttonGroup) {
		this.label = label;
		this.actionClass = actionClass;
		this.disabledOnStart = disabledOnStart;
		this.icon = icon;
		this.buttonGroup = buttonGroup;
	}


}
