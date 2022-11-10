package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.industrial.editor.actions.types.placing.PlaceLightActionParameters;
import com.industrial.editor.model.node.FlatNode;
import lombok.Getter;


@Getter
public class PlacedLight extends PlacedDecalElement {
	public static final float DEFAULT_LIGHT_HEIGHT = 1.9F;
	public static final float DEFAULT_LIGHT_RADIUS = 4;
	public static final float DEFAULT_LIGHT_INTENSITY = 0.3F;
	private float intensity;
	private float radius;

	public PlacedLight(PlacedElementParameters params,
					   GameAssetsManager gameAssetsManager) {
		super(params, gameAssetsManager, Assets.UiTextures.BULB);
	}

	public PlacedLight set(PlaceLightActionParameters parameters) {
		setHeight(parameters.height());
		intensity = parameters.intensity();
		radius = parameters.radius();
		return this;
	}
}
