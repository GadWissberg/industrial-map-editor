package com.industrial.editor.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.pickups.ItemDefinition;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.tools.EditorTool;

import java.awt.*;

public interface HandlersManager extends Disposable {

	ResourcesHandler getResourcesHandler();

	MapFileHandler getMapFileHandler();

	void onCreate(OrthographicCamera camera, WallCreator wallCreator, Dimension dimension);

	RenderHandler getRenderHandler();

	void onTileSelected(Assets.SurfaceTextures texture);

	void onTreeCharacterSelected(CharacterDefinition definition);

	void onTreeEnvSelected(ElementDefinition selectedElement);

	void onTreePickupSelected(ItemDefinition definition);

	MapEditorEventsNotifier getEventsNotifier();


	void onEditModeSet(EditModes mode);

	void onSelectedObjectRotate(int direction, EditorMode mode);

	void onModeSet(EditorMode mode);

	void onToolSet(EditorTool tool);

	boolean onTouchUp(Model cursorTileModel);

	LogicHandlers getLogicHandlers();
}
