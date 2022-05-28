package com.gadarts.industrial.editor.desktop.gui.commands.editing;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.gadarts.industrial.editor.desktop.gui.commands.MapperCommand;
import com.industrial.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class RotateSelectionCounterClockwiseCommand extends MapperCommand {


	public RotateSelectionCounterClockwiseCommand(MapRenderer mapRenderer,
												  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		getMapRenderer().onSelectedObjectRotate(MapRenderer.COUNTER_CLOCKWISE);
	}
}
