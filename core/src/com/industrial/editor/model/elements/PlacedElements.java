package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.mode.EditModes;
import lombok.Getter;

import java.util.*;

@Getter
public class PlacedElements {
	private final Set<MapNodeData> placedTiles = new HashSet<>();
	private final Map<EditModes, Set<? extends PlacedElement>> placedObjects = new HashMap<>();

	public void clear( ) {
		placedTiles.clear();
		placedObjects.forEach((key, value) -> value.clear());
	}
}
