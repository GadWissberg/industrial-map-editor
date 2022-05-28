package com.industrial.editor.actions.processes;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.industrial.shared.assets.Assets;
import lombok.Getter;

@Getter
public class PlaceTilesFinishProcessParameters extends SelectTilesFinishProcessParameters {
	private final Assets.SurfaceTextures selectedTile;
	private final Model tileModel;


	public PlaceTilesFinishProcessParameters(final int dstRow,
											 final int dstCol,
											 final Assets.SurfaceTextures selectedTile,
											 final Model tileModel) {
		super(dstRow, dstCol);
		this.selectedTile = selectedTile;
		this.tileModel = tileModel;
	}
}
