package com.gadarts.industrial.editor.desktop.dialogs;

import com.gadarts.industrial.editor.desktop.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class DialogPane extends JPanel {
	public static final int GENERAL_BUTTON_WIDTH = 100;
	public static final int GENERAL_BUTTON_HEIGHT = 25;
	static final int SPINNER_WIDTH = 50;
	private static final int PADDING = 10;
	private static final String BUTTON_LABEL_OK = "OK";
	private static final Color TEXT_COLOR = Color.BLACK;
	private static final String BUTTON_LABEL_CANCEL = "Cancel";

	protected DialogPane( ) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	JSpinner addSpinnerWithLabel(String label, JSpinner spinner) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new JLabel(label));
		panel.add(spinner);
		panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
		add(panel);
		return spinner;
	}

	void init( ) {
		initializeView();
	}

	abstract void initializeView( );

	protected Button addGeneralButtons(final ActionListener okClick) {
		add(new JSeparator());
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		Button okButton = addGeneralButton(BUTTON_LABEL_OK, okClick, panel);
		addGeneralButton(BUTTON_LABEL_CANCEL, e -> closeDialog(), panel);
		add(panel);
		return okButton;
	}

	public Button addGeneralButton(String buttonLabelCancel,
								   ActionListener actionListener,
								   JPanel parent) {
		Button button = createGeneralButton(buttonLabelCancel, actionListener);
		JPanel padding = new JPanel();
		padding.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
		padding.add(button);
		parent.add(padding);
		return button;
	}

	private Button createGeneralButton(String buttonLabelCancel, ActionListener actionListener) {
		Button button = new Button(buttonLabelCancel);
		button.addActionListener(actionListener);
		button.setForeground(TEXT_COLOR);
		button.setPreferredSize(new Dimension(GENERAL_BUTTON_WIDTH, GENERAL_BUTTON_HEIGHT));
		return button;
	}

	protected JSpinner addSpinner(double value,
								  int maximum,
								  float step,
								  boolean allowNegative) {
		JSpinner jSpinner = GuiUtils.createSpinner(value, -1, maximum, step, allowNegative, SPINNER_WIDTH);
		add(jSpinner);
		return jSpinner;
	}

	protected JSpinner createSpinner(double value, int maximum, float step, int minimum) {
		return createSpinner(value, maximum, step, minimum, true);
	}

	protected JSpinner createSpinner(double value, int maximum, float step, int minimum, boolean allowNegative) {
		return GuiUtils.createSpinner(value, minimum, maximum, step, allowNegative, SPINNER_WIDTH);
	}


	public abstract String getDialogTitle( );

	void closeDialog( ) {
		((Dialog) getRootPane().getParent()).dispose();
	}

	protected void addLabel(final String label) {
		add(new JLabel(label));
	}
}
