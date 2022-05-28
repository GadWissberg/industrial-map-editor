package com.industrial.editor.handlers.action;

import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElements;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ActionHandlerRelatedData {
    private final GameMap map;
    private final PlacedElements placedElements;

}
