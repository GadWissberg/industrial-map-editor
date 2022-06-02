package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.GameAssetsManager;
import lombok.Getter;

@Getter
public class PlacedEnvObject extends PlacedModelElement {

	public PlacedEnvObject(final PlacedModelElementParameters parameters, final GameAssetsManager assetsManager) {
		super(parameters, assetsManager);
	}
}
