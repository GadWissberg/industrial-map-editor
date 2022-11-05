package com.industrial.editor.handlers;

import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.MapRendererData;
import com.industrial.editor.handlers.cursor.CursorHandler;
import com.industrial.editor.handlers.render.RenderHandler;
import com.industrial.editor.utils.MapDeflater;
import com.industrial.editor.utils.MapInflater;

import java.io.IOException;
import java.util.Set;

public class MapFileHandler {
	private final MapDeflater deflater = new MapDeflater();
	private MapInflater inflater;

	public void init(final GameAssetsManager assetsManager,
					 final CursorHandler cursorHandler,
					 final Set<MapNodeData> placedTiles) {
		inflater = new MapInflater(assetsManager, cursorHandler, placedTiles);
	}

	public void onSaveMapRequested(final MapRendererData data, final String path) {
		deflater.deflate(data, path);
	}

	public void onLoadMapRequested(final MapRendererData data,
								   final WallCreator wallCreator,
								   final RenderHandler renderHandler,
								   final String path) throws IOException {
		inflater.inflateMap(data, wallCreator, renderHandler, path);
	}

}
