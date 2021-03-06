package com.industrial.editor.handlers;

import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.env.EnvironmentObjectDefinition;
import com.gadarts.industrial.shared.model.pickups.ItemDefinition;
import com.industrial.editor.handlers.cursor.CursorSelectionModel;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.handlers.action.ActionHandlerRelatedData;
import com.industrial.editor.handlers.action.ActionHandlerRelatedServices;
import com.industrial.editor.handlers.action.ActionsHandler;
import com.industrial.editor.handlers.action.ActionsHandlerImpl;
import com.industrial.editor.handlers.cursor.CursorHandler;
import com.industrial.editor.handlers.cursor.CursorHandlerModelData;
import com.industrial.editor.model.GameMap;
import lombok.Getter;

import java.util.Optional;

@Getter
public class LogicHandlers implements Disposable {
	private final CursorHandler cursorHandler = new CursorHandler();
	private final SelectionHandler selectionHandler = new SelectionHandler();
	private final MapEditorEventsNotifier eventsNotifier;
	private final ResourcesHandler resourcesHandler;
	private ActionsHandler actionsHandler;

	public LogicHandlers(final MapEditorEventsNotifier eventsNotifier, ResourcesHandler resourcesHandler) {
		this.eventsNotifier = eventsNotifier;
		this.resourcesHandler = resourcesHandler;
	}

	private void createActionsHandler(final HandlersManagerRelatedData handlersManagerRelatedData,
									  final WallCreator wallCreator,
									  final ResourcesHandler resourcesHandler) {
		GameMap map = handlersManagerRelatedData.getMap();
		ActionHandlerRelatedData data = new ActionHandlerRelatedData(map, handlersManagerRelatedData.getPlacedElements());
		actionsHandler = new ActionsHandlerImpl(data, new ActionHandlerRelatedServices(
				getCursorHandler(),
				wallCreator,
				eventsNotifier,
				resourcesHandler.getAssetsManager(),
				selectionHandler));
	}

	@Override
	public void dispose( ) {
		cursorHandler.dispose();
	}

	public void onCreate(final HandlersManagerRelatedData handlersManagerRelatedData,
						 final WallCreator wallCreator,
						 final GameAssetsManager assetsManager,
						 final RenderHandler renderHandler,
						 final ResourcesHandler resourcesHandler) {
		createActionsHandler(handlersManagerRelatedData, wallCreator, resourcesHandler);
		cursorHandler.createCursors(assetsManager, renderHandler.getTileModel());
	}

	public void onTileSelected(final Assets.SurfaceTextures texture) {
		cursorHandler.setHighlighter(cursorHandler.getCursorHandlerModelData().getCursorTileModelInstance());
		selectionHandler.onTileSelected(texture);
	}

	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		Optional.ofNullable(cursorHandler.getCursorCharacterDecal()).ifPresentOrElse(
				c -> cursorHandler.getCursorCharacterDecal().setCharacterDefinition(definition),
				( ) -> cursorHandler.initializeCursorCharacterDecal(
						resourcesHandler.getAssetsManager(),
						definition));
		cursorHandler.setHighlighter(cursorHandler.getCursorHandlerModelData().getCursorTileModelInstance());
		selectionHandler.setSelectedElement(definition);
	}

	public void onTreeEnvSelected(final ElementDefinition selected) {
		selectionHandler.setSelectedElement(selected);
		cursorHandler.setHighlighter(cursorHandler.getCursorHandlerModelData().getCursorTileModelInstance());
		CursorSelectionModel cursorSelectionModel = cursorHandler.getCursorHandlerModelData().getCursorSelectionModel();
		cursorSelectionModel.setSelection(selected, ((EnvironmentObjectDefinition) selected).getModelDefinition());
		cursorHandler.applyOpacity();
	}

	public void onTreePickupSelected(final ItemDefinition selectedElement) {
		selectionHandler.setSelectedElement(selectedElement);
		CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
		cursorHandler.setHighlighter(cursorHandlerModelData.getCursorTileModelInstance());
		Assets.Models modelDefinition = selectedElement.getModelDefinition();
		cursorHandlerModelData.getCursorSelectionModel().setSelection(selectedElement, modelDefinition);
		cursorHandler.applyOpacity();
	}
}
