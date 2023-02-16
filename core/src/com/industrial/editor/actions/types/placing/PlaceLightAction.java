package com.industrial.editor.actions.types.placing;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElement.PlacedElementParameters;
import com.industrial.editor.model.elements.PlacedLight;

import java.util.Optional;
import java.util.Set;

public class PlaceLightAction extends PlaceDecalElementAction<PlacedLight> {

	private final PlaceLightActionParameters parameters;

	public PlaceLightAction(GameMap map,
							Set<PlacedLight> placedElements,
							MapNodeData node,
							GameAssetManager assetsManager,
							PlaceLightActionParameters parameters) {
		super(map, node, assetsManager, Direction.SOUTH, null, placedElements);
		this.parameters = parameters;
	}

	@Override
	protected PlacedLight createElement(final MapNodeData node) {
		if (node != null) {
			Optional<PlacedLight> lightAtNode = placedElements.stream().filter(l -> l.getNode().equals(node)).findFirst();
			if (lightAtNode.isPresent()) {
				return lightAtNode.get().set(parameters);
			} else {
				return createLight(parameters);
			}
		}
		return null;
	}

	private PlacedLight createLight(PlaceLightActionParameters parameters) {
		return new PlacedLight(new PlacedElementParameters(
				elementDefinition,
				this.node,
				this.parameters.height()),
				assetsManager).set(parameters);
	}

}
