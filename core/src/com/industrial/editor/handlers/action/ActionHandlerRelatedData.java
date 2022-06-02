package com.industrial.editor.handlers.action;

import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElements;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record ActionHandlerRelatedData(GameMap map, PlacedElements placedElements) {
}
