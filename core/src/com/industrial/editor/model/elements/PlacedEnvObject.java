package com.industrial.editor.model.elements;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import lombok.Getter;

@Getter
public class PlacedEnvObject extends PlacedModelElement {

	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector2 auxVector2_1 = new Vector2();

	public PlacedEnvObject(final PlacedModelElementParameters parameters, final GameAssetsManager assetsManager) {
		super(parameters, assetsManager);
	}
}
