package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import lombok.Getter;

@Getter
public class PlacedTrigger extends PlacedDecalElement {
	public PlacedTrigger(final PlacedElementParameters params, final GameAssetsManager gameAssetsManager) {
		super(params, gameAssetsManager, Assets.UiTextures.TRIGGER);
	}

}
