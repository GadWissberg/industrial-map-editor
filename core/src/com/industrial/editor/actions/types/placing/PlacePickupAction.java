package com.industrial.editor.actions.types.placing;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.ItemDeclaration;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.PlaceElementAction;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedModelElement.PlacedModelElementParameters;
import com.industrial.editor.model.elements.PlacedPickup;

import java.util.Set;

public class PlacePickupAction extends PlaceElementAction<PlacedPickup, ItemDeclaration> {

	public PlacePickupAction(GameMap map,
							 Set<PlacedPickup> placedPickups,
							 MapNodeData node,
							 ItemDeclaration itemDefinition,
							 GameAssetManager assetsManager,
							 Direction facingDirection) {
		super(map, node, assetsManager, facingDirection, itemDefinition, placedPickups);
	}


	@Override
	protected void addElementToList(final PlacedPickup element) {
		placedElements.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(final PlacedPickup element, final MapNodeData tile) {
	}

	@Override
	protected PlacedPickup createElement(final MapNodeData node) {
		PlacedPickup result = null;
		if (node != null) {
			PlacedModelElementParameters parameters = new PlacedModelElementParameters(elementDefinition, node, 0);
			result = new PlacedPickup(parameters, assetsManager);
		}
		return result;
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
