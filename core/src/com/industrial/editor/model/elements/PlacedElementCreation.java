package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.GameAssetManager;

public interface PlacedElementCreation {
	PlacedElement create(final PlacedElement.PlacedElementParameters parameters, final GameAssetManager assetsManager);
}
