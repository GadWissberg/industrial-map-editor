package com.industrial.editor.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.declarations.ElementDeclaration;
import com.gadarts.industrial.shared.assets.declarations.characters.CharacterDeclaration;
import com.gadarts.industrial.shared.assets.declarations.pickups.ItemDeclaration;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.handlers.render.RenderHandler;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.tools.EditorTool;

import java.awt.*;

public interface HandlersManager extends Disposable {
	AxisModelHandler getAxisModelHandler( );

	ResourcesHandler getResourcesHandler( );

	MapFileHandler getMapFileHandler( );

	void onCreate(OrthographicCamera camera, WallCreator wallCreator, Dimension dimension);

	RenderHandler getRenderHandler( );

	void onTileSelected(Assets.SurfaceTextures texture);

	void onTreeCharacterSelected(CharacterDeclaration definition);

	void onTreeEnvSelected(ElementDeclaration selectedElement);

	void onTreePickupSelected(ItemDeclaration definition);

	MapEditorEventsNotifier getEventsNotifier( );


	void onEditModeSet(EditModes mode);

	void onSelectedObjectRotate(int direction, EditorMode mode);

	void onModeSet(EditorMode mode);

	void onToolSet(EditorTool tool);

	boolean onTouchUp(Model cursorTileModel);

	LogicHandlers getLogicHandlers( );
}
