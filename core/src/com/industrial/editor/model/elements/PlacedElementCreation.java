package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.GameAssetsManager;

public interface PlacedElementCreation {
	PlacedElement create(final PlacedElement.PlacedElementParameters parameters, final GameAssetsManager assetsManager);
}
