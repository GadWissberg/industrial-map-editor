package com.industrial.editor.actions.types.placing;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.PlaceElementAction;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedDecalElement;

import java.util.Set;

public abstract class PlaceDecalElementAction<T extends PlacedDecalElement> extends PlaceElementAction<T, ElementDefinition> {
	public PlaceDecalElementAction(GameMap map,
								   MapNodeData node,
								   GameAssetsManager assetsManager,
								   Direction elementDirection,
								   ElementDefinition elementDefinition,
								   Set<T> placedElements) {
		super(map, node, assetsManager, elementDirection, elementDefinition, placedElements);
	}

	@Override
	public boolean isProcess( ) {
		return false;
	}

	@Override
	protected void addElementToList(T element) {
		placedElements.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(T element, MapNodeData tile) {
		Decal decal = element.getDecal();
		Vector3 position = decal.getPosition();
		decal.setPosition(position.x, tile.getHeight() + 0.5F, position.z);
	}

}
