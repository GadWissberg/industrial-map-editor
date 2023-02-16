package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import lombok.Getter;

@Getter
public class PlacedTrigger extends PlacedDecalElement {
	public PlacedTrigger(final PlacedElementParameters params, final GameAssetManager GameAssetManager) {
		super(params, GameAssetManager, Assets.UiTextures.TRIGGER);
	}

}
