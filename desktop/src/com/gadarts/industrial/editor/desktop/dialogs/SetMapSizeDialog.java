package com.gadarts.industrial.editor.desktop.dialogs;

import com.industrial.editor.MapRenderer;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;

public class SetMapSizeDialog extends DialogPane {

	public static final int MAXIMUM = 160;
	private static final String LABEL_WIDTH = "Width: ";
	private static final String LABEL_DEPTH = "Depth: ";
	private static final int STEP_SIZE = 1;
	private final Dimension current;
	private final MapRenderer mapRenderer;

	public SetMapSizeDialog(final Dimension current, final MapRenderer mapRenderer) {
		this.current = current;
		this.mapRenderer = mapRenderer;
		init();
	}

	@Override
	void initializeView( ) {
		JSpinner widthSpinner = addSpinnerWithLabel(LABEL_WIDTH, createValueSpinner(current.width));
		JSpinner heightSpinner = addSpinnerWithLabel(LABEL_DEPTH, createValueSpinner(current.height));
		addGeneralButtons((e -> {
			int width = ((Double) widthSpinner.getValue()).intValue();
			int depth = ((Double) heightSpinner.getValue()).intValue();
			mapRenderer.onMapSizeSet(width, depth);
			closeDialog();
		}));
	}


	private JSpinner createValueSpinner(final float currentValue) {
		JSpinner jSpinner = addSpinner(currentValue, MAXIMUM, STEP_SIZE, false);
		JFormattedTextField editor = ((JSpinner.NumberEditor) jSpinner.getEditor()).getTextField();
		((NumberFormatter) editor.getFormatter()).setAllowsInvalid(false);
		return jSpinner;
	}

	@Override
	public String getDialogTitle( ) {
		return "Set Map Size";
	}
}
