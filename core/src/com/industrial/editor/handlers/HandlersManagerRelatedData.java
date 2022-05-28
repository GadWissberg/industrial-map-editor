package com.industrial.editor.handlers;

import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElements;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HandlersManagerRelatedData {
	private final GameMap map;
	private final PlacedElements placedElements;


}
