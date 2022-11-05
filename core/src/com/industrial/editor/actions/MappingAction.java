package com.industrial.editor.actions;

import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.model.GameMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MappingAction {
	protected final GameMap map;

	public abstract void execute(MapEditorEventsNotifier eventsNotifier);

	public abstract boolean isProcess( );
}
