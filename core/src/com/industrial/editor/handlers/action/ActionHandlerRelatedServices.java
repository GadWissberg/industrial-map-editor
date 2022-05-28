package com.industrial.editor.handlers.action;

import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.handlers.CursorHandler;
import com.industrial.editor.handlers.SelectionHandler;

public record ActionHandlerRelatedServices(CursorHandler cursorHandler,
										   WallCreator wallCreator,
										   MapEditorEventsNotifier eventsNotifier,
										   GameAssetsManager assetsManager,
										   SelectionHandler selectionHandler) {
}
