package com.industrial.editor.handlers.cursor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CursorHandlerModelData {
	private static final Color CURSOR_COLOR = Color.valueOf("#2AFF14");

	private ModelInstance cursorTileModelInstance;
	private Model cursorTileModel;

	@Setter
	private CursorSelectionModel cursorSelectionModel;

	public void createCursors(final Model tileModel) {
		this.cursorTileModel = tileModel;
		createCursorTile();
	}

	private void createCursorTile( ) {
		cursorTileModel.materials.get(0).set(ColorAttribute.createDiffuse(CURSOR_COLOR));
		cursorTileModelInstance = new ModelInstance(cursorTileModel);
	}
}
