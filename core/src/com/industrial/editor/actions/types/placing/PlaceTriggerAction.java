package com.industrial.editor.actions.types.placing;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.ElementDeclaration;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElement.PlacedElementParameters;
import com.industrial.editor.model.elements.PlacedTrigger;

import java.util.Set;

public class PlaceTriggerAction extends PlaceDecalElementAction<PlacedTrigger> {

	public PlaceTriggerAction(GameMap map,
							  Set<PlacedTrigger> placedElements,
							  MapNodeData node,
							  ElementDeclaration selectedCharacter,
							  GameAssetManager assetsManager) {
		super(map, node, assetsManager, Direction.SOUTH, selectedCharacter, placedElements);
	}

	@Override
	protected PlacedTrigger createElement(final MapNodeData tile) {
		PlacedTrigger result = null;
		if (tile != null) {
			result = new PlacedTrigger(new PlacedElementParameters(elementDefinition, node, 0), assetsManager);
		}
		return result;
	}

	@Override
	public boolean isProcess( ) {
		return false;
	}
}
