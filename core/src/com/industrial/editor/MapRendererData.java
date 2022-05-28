package com.industrial.editor;

import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElements;
import lombok.Getter;

@Getter
public class MapRendererData {
	private final ViewportResolution viewportResolution;
	private final PlacedElements placedElements = new PlacedElements();
	private final GameMap map = new GameMap();

	public MapRendererData(ViewportResolution viewportResolution) {
		this.viewportResolution = viewportResolution;
	}

	public void reset( ) {
		placedElements.clear();
		map.reset();
	}
}
