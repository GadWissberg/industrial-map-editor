package com.industrial.editor.actions;

import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElement;

import java.util.List;
import java.util.Optional;

public abstract class PlaceElementAction<T extends PlacedElement, S extends ElementDefinition> extends MappingAction {

	protected final GameAssetsManager assetsManager;
	protected final Direction elementDirection;
	protected final S elementDefinition;
	protected final List<T> placedElements;
	protected final MapNodeData node;

	public PlaceElementAction(final GameMap map,
							  final MapNodeData node,
							  final GameAssetsManager assetsManager,
							  final Direction elementDirection,
							  final S elementDefinition,
							  final List<T> placedElements) {
		super(map);
		this.node = node;
		this.assetsManager = assetsManager;
		this.elementDirection = elementDirection;
		this.elementDefinition = elementDefinition;
		this.placedElements = placedElements;
	}

	@Override
	public void execute(final MapEditorEventsNotifier eventsNotifier) {
		Coords coords = node.getCoords();
		MapNodeData tile = map.getNodes()[coords.getRow()][coords.getCol()];
		T element = createElement(tile);
		Optional.ofNullable(element).ifPresent(e -> {
			placeElementInCorrectHeight(e, tile);
			addElementToList(e);
		});
	}

	protected abstract void addElementToList(T element);

	protected abstract void placeElementInCorrectHeight(T element, MapNodeData tile);

	protected abstract T createElement(MapNodeData tile);
}
