package com.industrial.editor.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.declarations.ElementDeclaration;
import com.gadarts.industrial.shared.assets.declarations.characters.CharacterDeclaration;
import com.gadarts.industrial.shared.assets.declarations.pickups.ItemDeclaration;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.industrial.editor.handlers.cursor.CursorSelectionModel;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.MapRendererData;
import com.industrial.editor.MapRendererImpl;
import com.industrial.editor.handlers.cursor.CursorHandler;
import com.industrial.editor.handlers.cursor.CursorHandlerModelData;
import com.industrial.editor.handlers.render.RenderHandler;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.ModeType;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.model.elements.CharacterDecal;
import lombok.Getter;

import java.awt.*;
import java.util.Optional;

@Getter
public class HandlersManagerImpl implements HandlersManager, Disposable {
	private final HandlersManagerRelatedData handlersManagerRelatedData;
	private final ResourcesHandler resourcesHandler = new ResourcesHandler();
	private final MapEditorEventsNotifier eventsNotifier;
	private final LogicHandlers logicHandlers;
	private final AxisModelHandler axisModelHandler = new AxisModelHandler();
	private RenderHandler renderHandler;


	public HandlersManagerImpl(final MapRendererData data) {
		this.eventsNotifier = new MapEditorEventsNotifier();
		handlersManagerRelatedData = new HandlersManagerRelatedData(data.getMap(), data.getPlacedElements());
		this.logicHandlers = new LogicHandlers(eventsNotifier, resourcesHandler);
	}


	@Override
	public void dispose( ) {
		Optional.ofNullable(renderHandler).ifPresent(RenderHandler::dispose);
		Optional.of(resourcesHandler).ifPresent(ResourcesHandler::dispose);
		Optional.ofNullable(logicHandlers).ifPresent(LogicHandlers::dispose);
		Optional.of(axisModelHandler).ifPresent(AxisModelHandler::dispose);
	}

	@Override
	public MapFileHandler getMapFileHandler( ) {
		return resourcesHandler.getMapFileHandler();
	}

	@Override
	public void onCreate(final OrthographicCamera camera, final WallCreator wallCreator, final Dimension levelSize) {
		GameAssetManager assetsManager = resourcesHandler.getAssetsManager();
		resourcesHandler.initializeGameFiles();
		renderHandler = new RenderHandler(assetsManager, this, camera);
		renderHandler.init(levelSize);
		logicHandlers.onCreate(handlersManagerRelatedData, wallCreator, assetsManager, renderHandler, resourcesHandler);
	}

	public void onTileSelected(final Assets.SurfaceTextures texture) {
		logicHandlers.onTileSelected(texture);
	}

	@Override
	public void onTreeCharacterSelected(final CharacterDeclaration definition) {
		logicHandlers.onTreeCharacterSelected(definition);
	}

	public void onTreeEnvSelected(final ElementDeclaration selectedElement) {
		logicHandlers.onTreeEnvSelected(selectedElement);
	}

	@Override
	public void onTreePickupSelected(final ItemDeclaration definition) {
		logicHandlers.onTreePickupSelected(definition);
	}

	@Override
	public void onEditModeSet(final EditModes mode) {
		onModeSet(mode);
		if (mode == EditModes.TILES && logicHandlers.getSelectionHandler().getSelectedTile() != null) {
			onTileSelected(logicHandlers.getSelectionHandler().getSelectedTile());
		}
		logicHandlers.getCursorHandler().onEditModeSet(mode, resourcesHandler.getAssetsManager());
	}

	@Override
	public void onSelectedObjectRotate(final int direction, final EditorMode mode) {
		if (logicHandlers.getSelectionHandler().getSelectedElement() != null) {
			CursorHandler cursorHandler = logicHandlers.getCursorHandler();
			if (mode == EditModes.CHARACTERS) {
				CharacterDecal cursorCharacterDecal = cursorHandler.getCursorCharacterDecal();
				int ordinal = cursorCharacterDecal.getSpriteDirection().ordinal() + direction;
				int length = Direction.values().length;
				int index = (ordinal < 0 ? ordinal + length : ordinal) % length;
				cursorCharacterDecal.setSpriteDirection(Direction.values()[index]);
			} else {
				CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
				CursorSelectionModel cursorSelectionModel = cursorHandlerModelData.getCursorSelectionModel();
				int ordinal = cursorSelectionModel.getFacingDirection().ordinal() + direction * 2;
				int length = Direction.values().length;
				int index = (ordinal < 0 ? ordinal + length : ordinal) % length;
				cursorSelectionModel.setFacingDirection(Direction.values()[index]);
			}
		}
	}

	@Override
	public void onModeSet(final EditorMode mode) {
		logicHandlers.getSelectionHandler().setSelectedElement(null);
		if (mode.getType() != ModeType.EDIT) {
			logicHandlers.getCursorHandler().getCursorHandlerModelData().setHighlighter(null);
		}
		MapRendererImpl.mode = mode;
	}

	@Override
	public void onToolSet(final EditorTool tool) {
		logicHandlers.getSelectionHandler().setSelectedElement(null);
		CursorHandler cursorHandler = logicHandlers.getCursorHandler();
		cursorHandler.getCursorHandlerModelData().setHighlighter(cursorHandler.getCursorHandlerModelData().getCursorTileModelInstance());
		MapRendererImpl.tool = tool;
	}

	@Override
	public boolean onTouchUp(final Model cursorTileModel) {
		SelectionHandler selectionHandler = logicHandlers.getSelectionHandler();
		return logicHandlers.getActionsHandler().onTouchUp(selectionHandler.getSelectedTile(), cursorTileModel);
	}
}
