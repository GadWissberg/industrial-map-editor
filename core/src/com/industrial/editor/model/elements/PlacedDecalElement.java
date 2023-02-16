package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.utils.Utils;
import lombok.Getter;

/**
 * A in-map placed element displayed by a decal.
 */
@Getter
public abstract class PlacedDecalElement extends PlacedElement {
	private static final float DECAL_Y = 0.5f;
	private final Decal decal;

	public PlacedDecalElement(PlacedElementParameters parameters,
							  GameAssetManager GameAssetManager,
							  Assets.UiTextures decalTexture) {
		super(parameters);
		decal = Utils.createSimpleDecal(GameAssetManager.getTexture(decalTexture));
		MapNodeData node = parameters.getNode();
		Coords coords = node.getCoords();
		float x = coords.getCol() + 0.5f;
		float y = node.getHeight() + parameters.getHeight() + DECAL_Y;
		float z = coords.getRow() + 0.5f;
		decal.setPosition(x, y, z);
	}


	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		decal.getPosition().y = getNode().getHeight() + height;
	}
}
