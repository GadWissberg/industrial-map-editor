package com.industrial.editor.handlers;

import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.declarations.ElementDeclaration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectionHandler {
	private Assets.SurfaceTextures selectedTile;
	private ElementDeclaration selectedElement;

	public void onTileSelected(final Assets.SurfaceTextures texture) {
		selectedTile = texture;
	}
}
