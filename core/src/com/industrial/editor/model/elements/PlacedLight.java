package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.industrial.editor.actions.types.placing.PlaceLightActionParameters;
import com.industrial.editor.model.node.FlatNode;
import lombok.Getter;


@Getter
public class PlacedLight extends PlacedDecalElement {
	private float intensity;
	private float radius;

	public PlacedLight(PlacedElementParameters params,
					   GameAssetsManager gameAssetsManager) {
		super(params, gameAssetsManager, Assets.UiTextures.BULB);
	}

	@Override
	public String toString( ) {
		return "Light";
	}

	public PlacedLight set(PlaceLightActionParameters parameters) {
		setHeight(parameters.height());
		intensity = parameters.intensity();
		radius = parameters.radius();
		return this;
	}
}
