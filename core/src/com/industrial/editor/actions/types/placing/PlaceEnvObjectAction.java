package com.industrial.editor.actions.types.placing;

import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.env.EnvironmentObjectDefinition;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.actions.PlaceElementAction;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedEnvObject;
import com.industrial.editor.model.elements.PlacedModelElement.PlacedModelElementParameters;

import java.util.Set;

public class PlaceEnvObjectAction extends PlaceElementAction<PlacedEnvObject, EnvironmentObjectDefinition> {

	private final EnvironmentObjectDefinition selectedEnvObject;
	private final Set<PlacedEnvObject> placedEnvObjects;

	public PlaceEnvObjectAction(GameMap map,
								Set<PlacedEnvObject> placedEnvObjects,
								MapNodeData node,
								EnvironmentObjectDefinition definition,
								GameAssetsManager assetsManager,
								Direction selectedObjectDirection) {
		super(map, node, assetsManager, selectedObjectDirection, definition, placedEnvObjects);
		this.selectedEnvObject = definition;
		this.placedEnvObjects = placedEnvObjects;
	}

	@Override
	public void execute(final MapEditorEventsNotifier eventsNotifier) {
		super.execute(eventsNotifier);
		applyOnMap();
	}

	@Override
	protected void addElementToList(final PlacedEnvObject element) {
		placedEnvObjects.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(final PlacedEnvObject element, final MapNodeData tile) {
	}

	@Override
	protected PlacedEnvObject createElement(final MapNodeData tile) {
		PlacedModelElementParameters parameters = new PlacedModelElementParameters(
				selectedEnvObject,
				elementDirection,
				node,
				0);
		return new PlacedEnvObject(parameters, assetsManager);
	}

	private void applyOnMap( ) {
		int halfHeight = selectedEnvObject.getDepth() / 2;
		int halfWidth = selectedEnvObject.getWidth() / 2;
		for (int row = -halfHeight; row < Math.max(halfHeight, 1); row++) {
			for (int col = -halfWidth; col < Math.max(halfWidth, 1); col++) {
				applyOnNode(row, col);
			}
		}
	}

	private void applyOnNode(final int row, final int col) {
		MapNodeData[][] nodes = map.getNodes();
		Coords coords = node.getCoords();
		int currentRow = Math.min(Math.max(coords.getRow() + row, 0), nodes.length);
		int currentCol = Math.min(Math.max(coords.getCol() + col, 0), nodes[0].length);
		if (currentRow < nodes.length && currentCol < nodes[0].length) {
			if (nodes[currentRow][currentCol] == null) {
				nodes[currentRow][currentCol] = new MapNodeData(currentRow, currentCol, selectedEnvObject.getNodeType());
			} else {
				nodes[currentRow][currentCol].setMapNodeType(selectedEnvObject.getNodeType());
			}
		}
	}

	@Override
	public boolean isProcess( ) {
		return false;
	}
}
