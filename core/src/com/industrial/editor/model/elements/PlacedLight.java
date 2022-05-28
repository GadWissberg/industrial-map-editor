package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.utils.Utils;
import lombok.Getter;

@Getter
public class PlacedLight extends PlacedElement {
	private static final float BULB_Y = 0.5f;
	private final Decal decal;

	public PlacedLight(final PlacedElementParameters params, final GameAssetsManager gameAssetsManager) {
		super(params);
		decal = Utils.createSimpleDecal(gameAssetsManager.getTexture(Assets.UiTextures.BULB));
		MapNodeData node = params.getNode();
		Coords coords = node.getCoords();
		decal.setPosition(coords.getCol() + 0.5f, BULB_Y, coords.getRow() + 0.5f);
	}
}
