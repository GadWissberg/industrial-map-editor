package com.gadarts.industrial.editor.desktop.gui.toolbar;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.menu.MenuItemProperties;
import com.industrial.editor.MapRenderer;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Getter
public class RadioToolBarButton extends JToggleButton {

	private final ToolbarButtonProperties properties;

	public RadioToolBarButton(ToolbarButtonProperties properties,
							  MapRenderer mapRenderer,
							  Managers managers) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
		this.properties = properties;
		setIcon(ToolbarButtonProperties.createButtonIcon(properties));
		ActionListener action = createAction(properties, mapRenderer, managers);
		addActionListener(action);
	}

	private ActionListener createAction(ToolbarButtonProperties buttonDef,
										MapRenderer mapRenderer,
										Managers managers) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		if (buttonDef.getMenuItemDefinition() != null) {
			return createActionFromMenuItem(buttonDef, mapRenderer, managers);
		} else {
			return (ActionListener) buttonDef.getMapperCommandClass().getConstructors()[0].newInstance(
					mapRenderer,
					managers);
		}
	}

	private ActionListener createActionFromMenuItem(ToolbarButtonProperties buttonDefinition,
													MapRenderer mapRenderer,
													Managers managers) {
		MenuItemProperties menuItemProperties = buttonDefinition.getMenuItemDefinition().getMenuItemProperties();
		setEnabled(!menuItemProperties.isDisabledOnStart());
		try {
			return (ActionListener) menuItemProperties.getActionClass().getConstructors()[0].newInstance(
					mapRenderer,
					managers);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


}
