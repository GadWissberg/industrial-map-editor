package com.gadarts.industrial.editor.desktop.gui.toolbar;

import com.gadarts.industrial.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.industrial.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.industrial.editor.MapRenderer;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.gadarts.industrial.editor.desktop.gui.toolbar.ToolbarButtonProperties.createButtonIcon;

@Getter
public class ToolBarButton extends JButton {

	private final MenuItemDefinition menuItemDefinition;

	public ToolBarButton(ToolbarButtonProperties properties,
						 MapRenderer mapRenderer,
						 Managers managers) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
		setIcon(createButtonIcon(properties));
		menuItemDefinition = properties.getMenuItemDefinition();
		if (menuItemDefinition != null) {
			addActionFromMenuItem(mapRenderer, managers, menuItemDefinition);
		} else {
			addActionFromProperties(properties, mapRenderer, managers);
		}
	}

	private void addActionFromProperties(ToolbarButtonProperties properties,
										 MapRenderer mapRenderer,
										 Managers managers) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		addActionListener((ActionListener) properties.getMapperCommandClass().getConstructors()[0].newInstance(
				mapRenderer,
				managers));
	}

	private void addActionFromMenuItem(MapRenderer mapRenderer,
									   Managers managers,
									   MenuItemDefinition menuItemDefinition) {
		MenuItemProperties menuItemProperties = menuItemDefinition.getMenuItemProperties();
		setEnabled(!menuItemProperties.isDisabledOnStart());
		MapperCommand action;
		try {
			Constructor<?> constructor = menuItemProperties.getActionClass().getConstructors()[0];
			action = (MapperCommand) constructor.newInstance(
					mapRenderer,
					managers);
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		addActionListener(action);
	}


}
