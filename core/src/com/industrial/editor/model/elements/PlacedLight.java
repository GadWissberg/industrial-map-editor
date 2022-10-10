package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;

public class PlacedLight extends PlacedDecalElement {

	public PlacedLight(final PlacedElementParameters params, final GameAssetsManager gameAssetsManager) {
		super(params, gameAssetsManager, Assets.UiTextures.BULB);
	}
}
