package com.industrial.editor.actions.types.placing;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.declarations.ElementDeclaration;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.PlaceElementAction;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedDecalElement;

import java.util.Set;

public abstract class PlaceDecalElementAction<T extends PlacedDecalElement> extends PlaceElementAction<T, ElementDeclaration> {
	public PlaceDecalElementAction(GameMap map,
								   MapNodeData node,
								   GameAssetManager assetsManager,
								   Direction elementDirection,
								   ElementDeclaration elementDefinition,
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
		decal.setPosition(position.x, decal.getPosition().y + 0.5F, position.z);
	}

}
