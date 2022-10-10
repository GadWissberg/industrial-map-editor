package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.utils.Utils;
import lombok.Getter;

@Getter
public abstract class PlacedDecalElement extends PlacedElement {
	private static final float DECAL_Y = 0.5f;
	private final Decal decal;

	public PlacedDecalElement(PlacedElementParameters parameters,
							  GameAssetsManager gameAssetsManager,
							  Assets.UiTextures decalTexture) {
		super(parameters);
		decal = Utils.createSimpleDecal(gameAssetsManager.getTexture(decalTexture));
		MapNodeData node = parameters.getNode();
		Coords coords = node.getCoords();
		decal.setPosition(coords.getCol() + 0.5f, node.getHeight() + DECAL_Y, coords.getRow() + 0.5f);
	}
}
