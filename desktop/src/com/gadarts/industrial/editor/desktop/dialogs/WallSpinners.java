package com.gadarts.industrial.editor.desktop.dialogs;

import com.gadarts.industrial.editor.desktop.GuiUtils;
import com.industrial.editor.model.node.WallDefinition;
import lombok.Getter;

import javax.swing.*;

import static com.gadarts.industrial.editor.desktop.dialogs.DialogPane.SPINNER_WIDTH;

@Getter
public class WallSpinners {
	static final Float DEF_V_SCALE = 1F;
	static final Float DEF_H_OFFSET = 0F;
	static final Float DEF_V_OFFSET = 0F;
	private static final int VALUE_MAX = 10;
	private static final int VALUE_MIN = -10;
	private static final float STEP_SIZE_V_SCALE = 1F;
	private static final float STEP_SIZE_H_OFFSET = 0.1F;
	private static final float STEP_SIZE_V_OFFSET = 0.1F;
	private static final String LABEL_VER_SCALE = "V. scale: ";
	private static final String LABEL_H_OFFSET = "H. offset: ";
	private static final String LABEL_V_OFFSET = "V. offset: ";
	private final JSpinner wallVScale;
	private final JSpinner wallHorOffset;
	private final JSpinner wallVerOffset;

	public WallSpinners(JPanel sec, WallDefinition initialValues) {
		Float vScale = initialValues.getVScale();
		wallVScale = addSpinnerLine(sec, LABEL_VER_SCALE, STEP_SIZE_V_SCALE, vScale != null ? vScale : DEF_V_SCALE);
		Float hOffset = initialValues.getHorizontalOffset();
		wallHorOffset = addSpinnerLine(sec, LABEL_H_OFFSET, STEP_SIZE_H_OFFSET, hOffset != null ? hOffset : DEF_H_OFFSET);
		Float vOffset = initialValues.getVerticalOffset();
		wallVerOffset = addSpinnerLine(sec, LABEL_V_OFFSET, STEP_SIZE_V_OFFSET, vOffset != null ? vOffset : DEF_V_OFFSET);
	}

	private JSpinner addSpinnerLine(JPanel spinnersPanel, String label, float step, float value) {
		spinnersPanel.add(new JLabel(label));
		JSpinner spinner = GuiUtils.createSpinner(value, VALUE_MIN, VALUE_MAX, step, true, SPINNER_WIDTH);
		JPanel paddingPanel = new JPanel();
		paddingPanel.add(spinner);
		spinnersPanel.add(paddingPanel);
		return spinner;
	}
}
