package com.gadarts.industrial.editor.desktop.gui.dialogs;

import com.gadarts.industrial.editor.desktop.gui.GuiUtils;
import com.gadarts.industrial.shared.model.env.LightConstants;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.model.elements.PlacedLight;
import com.industrial.editor.model.node.FlatNode;

import javax.swing.*;

import static com.gadarts.industrial.shared.model.env.LightConstants.DEFAULT_LIGHT_HEIGHT;
import static com.gadarts.industrial.shared.model.env.LightConstants.DEFAULT_LIGHT_RADIUS;

public class PlaceLightDialog extends DialogPane {
	static final float STEP = 0.1f;
	private static final String LABEL_HEIGHT = "Height: ";
	private static final String LABEL_RADIUS = "Radius: ";
	private static final String LABEL_INTENSITY = "Intensity: ";
	private static final int MAX_LIGHT_HEIGHT = 2;
	private static final int MAX_LIGHT_RADIUS = 5;
	private static final int MAX_LIGHT_INTENSITY = 1;
	private final MapRenderer mapRenderer;
	private final FlatNode node;
	private final PlacedLight lightInNode;

	public PlaceLightDialog(FlatNode node, MapRenderer mapRenderer, PlacedLight lightInNode) {
		this.node = node;
		this.mapRenderer = mapRenderer;
		this.lightInNode = lightInNode;
		init();
	}

	@Override
	void initializeView( ) {
		JSpinner heightSpinner = addHeightSpinner();
		JSpinner radiusSpinner = addRadiusSpinner();
		JSpinner intensitySpinner = addIntensitySpinner();
		addGeneralButtons(e -> {
			float height = ((Double) heightSpinner.getValue()).floatValue();
			float radius = ((Double) radiusSpinner.getValue()).floatValue();
			float intensity = ((Double) intensitySpinner.getValue()).floatValue();
			mapRenderer.onLightPlaced(node, height, radius, intensity);
			closeDialog();
		});
	}

	private JSpinner addIntensitySpinner( ) {
		return addSpinnerWithLabel(LABEL_INTENSITY, GuiUtils.createSpinner(
				lightInNode != null ? lightInNode.getIntensity() : LightConstants.DEFAULT_LIGHT_INTENSITY,
				0,
				MAX_LIGHT_INTENSITY,
				STEP,
				false,
				SPINNER_WIDTH));
	}

	private JSpinner addRadiusSpinner( ) {
		return addSpinnerWithLabel(LABEL_RADIUS, GuiUtils.createSpinner(
				lightInNode != null ? lightInNode.getRadius() : DEFAULT_LIGHT_RADIUS,
				0,
				MAX_LIGHT_RADIUS,
				STEP,
				false,
				SPINNER_WIDTH));
	}

	private JSpinner addHeightSpinner( ) {
		return addSpinnerWithLabel(LABEL_HEIGHT, GuiUtils.createSpinner(
				lightInNode != null ? lightInNode.getHeight() : DEFAULT_LIGHT_HEIGHT,
				0,
				MAX_LIGHT_HEIGHT,
				STEP,
				false,
				SPINNER_WIDTH));
	}

	@Override
	public String getDialogTitle( ) {
		return "Lift Tiles";
	}
}
