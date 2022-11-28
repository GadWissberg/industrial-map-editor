package com.industrial.editor.handlers.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.env.EnvironmentObjectDefinition;
import com.gadarts.industrial.shared.model.env.EnvironmentObjectType;
import com.gadarts.industrial.shared.model.env.ThingsDefinitions;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.map.NodeWalls;
import com.gadarts.industrial.shared.model.map.Wall;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.HandlersManager;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.handlers.action.ActionsHandler;
import com.industrial.editor.handlers.cursor.CursorHandler;
import com.industrial.editor.handlers.cursor.CursorHandlerModelData;
import com.industrial.editor.handlers.cursor.CursorSelectionModel;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.ModeType;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.model.elements.*;
import com.industrial.editor.model.node.FlatNode;
import com.industrial.editor.utils.Utils;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RenderHandler implements Disposable {
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Matrix4 auxMatrix_1 = new Matrix4();
	private static final Matrix4 auxMatrix_2 = new Matrix4();
	private static final Vector2 auxVector2_1 = new Vector2();

	private final GameAssetsManager assetsManager;
	private final HandlersManager handlersManager;
	private final Camera camera;
	private final RenderHandlerBatches renderHandlerBatches = new RenderHandlerBatches();
	private final RenderHandlerAuxModels renderHandlerAuxModels = new RenderHandlerAuxModels();
	private CameraGroupStrategy groupStrategy;

	public RenderHandler(final GameAssetsManager assetsManager,
						 final HandlersManager handlersManager,
						 final Camera camera) {
		this.assetsManager = assetsManager;
		this.handlersManager = handlersManager;
		this.camera = camera;
	}

	private static <T extends PlacedDecalElement> void renderSimpleDecals(HandlersManager handlersManager,
																		  Camera camera,
																		  Map<EditModes, Set<? extends PlacedElement>> placedObjects,
																		  EditModes mode) {
		Set<T> placed = (Set<T>) placedObjects.get(mode);
		for (T placedObject : placed) {
			handlersManager.getRenderHandler().renderDecal(placedObject.getDecal(), camera);
		}
	}

	public void renderDecal(final Decal decal, final Camera camera) {
		decal.lookAt(auxVector3_1.set(decal.getPosition()).sub(camera.direction), camera.up);
		renderHandlerBatches.getDecalBatch().add(decal);
	}


	public void draw(EditorMode mode,
					 PlacedElements placedElements,
					 Set<MapNodeData> initializedTiles,
					 ElementDefinition selectedElement,
					 EditorTool tool) {
		int sam = Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0;
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | sam);
		renderModels(initializedTiles, placedElements, mode, selectedElement, tool);
		renderDecals(handlersManager, mode, placedElements);
	}

	public void renderAux(final ModelBatch modelBatch) {
		handlersManager.getAxisModelHandler().render(modelBatch);
		modelBatch.render(renderHandlerAuxModels.getGridModelInstance());
	}

	private void renderDecals(HandlersManager handlersManager,
							  EditorMode mode,
							  PlacedElements placedElements) {
		Gdx.gl.glDepthMask(false);
		ModelInstance highlighter = handlersManager.getLogicHandlers().getCursorHandler().getHighlighter();
		if (highlighter != null && mode.getClass().equals(EditModes.class) && ((EditModes) mode).isDecalCursor()) {
			renderCursorOfDecalMode(handlersManager, mode, camera);
		}
		renderDecalPlacedElements(placedElements, handlersManager, camera);
		renderHandlerBatches.getDecalBatch().flush();
		Gdx.gl.glDepthMask(true);
	}

	private void renderDecalPlacedElements(final PlacedElements placedElements,
										   final HandlersManager handlersManager,
										   final Camera camera) {
		Map<EditModes, Set<? extends PlacedElement>> placedObjects = placedElements.getPlacedObjects();
		renderCharacters(handlersManager, camera, placedObjects);
		renderSimpleDecals(handlersManager, camera, placedObjects, EditModes.LIGHTS);
		renderSimpleDecals(handlersManager, camera, placedObjects, EditModes.TRIGGERS);
	}

	private void renderCharacters(HandlersManager handlersManager, Camera camera, Map<EditModes, Set<? extends PlacedElement>> placedObjects) {
		Set<PlacedCharacter> placedCharacters = (Set<PlacedCharacter>) placedObjects.get(EditModes.CHARACTERS);
		for (final PlacedCharacter character : placedCharacters) {
			renderCharacter(character.getCharacterDecal(), character.getCharacterDecal().getSpriteDirection(), camera, handlersManager);
		}
	}


	private void renderCursorOfDecalMode(final HandlersManager handlersManager,
										 final EditorMode mode,
										 final Camera camera) {
		CursorHandler cursorHandler = handlersManager.getLogicHandlers().getCursorHandler();
		if (mode == EditModes.CHARACTERS) {
			CharacterDecal cursorCharDecal = cursorHandler.getCursorCharacterDecal();
			Optional.ofNullable(cursorCharDecal)
					.ifPresent(c -> renderCharacter(c, cursorCharDecal.getSpriteDirection(), camera, handlersManager));
		} else {
			handlersManager.getRenderHandler().renderDecal(cursorHandler.getCursorSimpleDecal(), camera);
		}
	}

	private void renderCharacter(final CharacterDecal characterDecal,
								 final Direction facingDirection,
								 final Camera camera,
								 final HandlersManager handlersManager) {
		Utils.applyFrameSeenFromCameraForCharacterDecal(characterDecal, facingDirection, camera, assetsManager);
		handlersManager.getRenderHandler().renderDecal(characterDecal.getDecal(), camera);
	}


	private void renderModels(Set<MapNodeData> initializedTiles,
							  PlacedElements placedElements,
							  EditorMode mode,
							  ElementDefinition selectedElement,
							  EditorTool tool) {
		ModelBatch modelBatch = renderHandlerBatches.getModelBatch();
		modelBatch.begin(camera);
		renderAux(modelBatch);
		renderCursor(mode, selectedElement, tool);
		renderExistingProcess();
		renderModelPlacedElements(initializedTiles, placedElements);
		modelBatch.end();
	}

	private void renderModelPlacedElements(final Set<MapNodeData> initializedTiles, final PlacedElements placedElements) {
		renderTiles(initializedTiles);
		renderEnvObjects(placedElements);
		Set<PlacedPickup> placedPickups = (Set<PlacedPickup>) placedElements.getPlacedObjects().get(EditModes.PICKUPS);
		for (final PlacedPickup pickup : placedPickups) {
			renderPickup(pickup.getModelInstance());
		}
	}

	private void renderTiles(final Set<MapNodeData> initializedTiles) {
		for (final MapNodeData tile : initializedTiles) {
			if (tile.getModelInstance() != null) {
				ModelBatch modelBatch = renderHandlerBatches.getModelBatch();
				modelBatch.render(tile.getModelInstance());
				NodeWalls walls = tile.getWalls();
				renderWall(modelBatch, walls.getNorthWall());
				renderWall(modelBatch, walls.getEastWall());
				renderWall(modelBatch, walls.getWestWall());
				renderWall(modelBatch, walls.getSouthWall());
			}
		}
	}

	private void renderWall(final ModelBatch modelBatch, final Wall wall) {
		if (wall != null) {
			modelBatch.render(wall.getModelInstance());
		}
	}

	public void renderCursor(final EditorMode mode, final ElementDefinition selectedElement, EditorTool tool) {
		ModelInstance highlighter = handlersManager.getLogicHandlers().getCursorHandler().getHighlighter();
		SelectionHandler selectionHandler = handlersManager.getLogicHandlers().getSelectionHandler();
		if (highlighter != null && mode.getType() == ModeType.EDIT) {
			Assets.SurfaceTextures selectedTile = selectionHandler.getSelectedTile();
			boolean cursorAlwaysDisplayed = tool.isForceCursorDisplay();
			if ((cursorAlwaysDisplayed || (selectedElement != null || selectedTile != null))) {
				renderHandlerBatches.getModelBatch().render(highlighter);
			}
		}
		renderCursorObjectModel(selectedElement, mode);
	}

	private void renderCursorObjectModel(final ElementDefinition selectedElement, final EditorMode mode) {
		CursorHandler cursorHandler = handlersManager.getLogicHandlers().getCursorHandler();
		if (selectedElement != null) {
			if (mode == EditModes.ENVIRONMENT) {
				renderCursorEnvObjectModel((EnvironmentObjectDefinition) selectedElement, cursorHandler);
			} else if (mode == EditModes.PICKUPS) {
				renderPickup(cursorHandler.getCursorHandlerModelData().getCursorSelectionModel().getModelInstance());
			}
		}
	}

	private void renderCursorEnvObjectModel(EnvironmentObjectDefinition selectedElement, CursorHandler cursorHandler) {
		cursorHandler.renderModelCursorFloorGrid(selectedElement, renderHandlerBatches.getModelBatch());
		CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
		CursorSelectionModel cursorSelectionModel = cursorHandlerModelData.getCursorSelectionModel();
		ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
		Direction facingDirection = cursorSelectionModel.getFacingDirection();
		ModelInstance appendixModelInstance = cursorSelectionModel.getAppendixModelInstance();
		renderEnvObject(selectedElement, modelInstance, facingDirection, appendixModelInstance);
	}

	private void renderEnvObjects(final PlacedElements placedElements) {
		Map<EditModes, Set<? extends PlacedElement>> placedObjects = placedElements.getPlacedObjects();
		Set<PlacedEnvObject> placedEnvObjects = (Set<PlacedEnvObject>) placedObjects.get(EditModes.ENVIRONMENT);
		for (final PlacedEnvObject placedEnvObject : placedEnvObjects) {
			renderEnvObject(
					(EnvironmentObjectDefinition) placedEnvObject.getDefinition(),
					placedEnvObject.getModelInstance(),
					placedEnvObject.getFacingDirection(),
					placedEnvObject.getAppendixModelInstance());
		}
	}

	private void renderPickup(final ModelInstance modelInstance) {
		Matrix4 originalTransform = auxMatrix_1.set(modelInstance.transform);
		modelInstance.transform.translate(0.5f, 0, 0.5f);
		renderHandlerBatches.getModelBatch().render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}

	private void renderEnvObject(final EnvironmentObjectDefinition definition,
								 final ModelInstance modelInstance,
								 final Direction facingDirection,
								 ModelInstance appendixModelInstance) {
		Matrix4 originalTransform = auxMatrix_1.set(modelInstance.transform);
		rotateEnvObject(definition, modelInstance, facingDirection);
		handleSpecificEnvRender(definition, modelInstance, facingDirection, definition.getEnvironmentObjectType());
		renderHandlerBatches.getModelBatch().render(modelInstance);
		renderEnvAppendix(definition, facingDirection, appendixModelInstance);
		modelInstance.transform.set(originalTransform);
	}

	private void renderEnvAppendix(EnvironmentObjectDefinition definition,
								   Direction facingDirection,
								   ModelInstance appendixModelInstance) {
		if (appendixModelInstance != null) {
			Matrix4 originalTransform = auxMatrix_2.set(appendixModelInstance.transform);
			rotateEnvObject(definition, appendixModelInstance, facingDirection);
			renderHandlerBatches.getModelBatch().render(appendixModelInstance);
			appendixModelInstance.transform.set(originalTransform);
		}
	}

	private void rotateEnvObject(EnvironmentObjectDefinition definition, ModelInstance modelInstance, Direction facingDirection) {
		modelInstance.transform.translate(0.5f, 0, 0.5f);
		modelInstance.transform.rotate(Vector3.Y, -1 * facingDirection.getDirection(auxVector2_1).angleDeg());
		modelInstance.transform.translate(definition.getOffset(auxVector3_1));
	}

	private void handleSpecificEnvRender(EnvironmentObjectDefinition definition,
										 ModelInstance modelInstance,
										 Direction facingDirection,
										 EnvironmentObjectType environmentObjectType) {
		if (environmentObjectType == EnvironmentObjectType.THING) {
			ThingsDefinitions.handleEvenSize((ThingsDefinitions) definition, modelInstance, facingDirection);
		}
	}


	private void renderExistingProcess( ) {
		ActionsHandler actionsHandler = handlersManager.getLogicHandlers().getActionsHandler();
		MappingProcess<? extends MappingProcess.FinishProcessParameters> p = actionsHandler.getCurrentProcess();
		Optional.ofNullable(p).ifPresent(process -> {
			if (p.isRequiresRegionSelectionCursor()) {
				FlatNode srcNode = p.getSrcNode();
				handlersManager.getLogicHandlers().getCursorHandler().renderRectangleMarking(
						srcNode.getRow(),
						srcNode.getCol(),
						renderHandlerBatches.getModelBatch());
			}
		});
	}

	public void render(EditorMode mode,
					   PlacedElements placedElements,
					   ElementDefinition selectedElement,
					   EditorTool tool) {
		draw(mode, placedElements, placedElements.getPlacedTiles(), selectedElement, tool);
	}

	@Override
	public void dispose( ) {
		renderHandlerAuxModels.dispose();
		renderHandlerBatches.dispose();
		groupStrategy.dispose();
	}

	public void init(Dimension levelSize) {
		groupStrategy = new CameraGroupStrategy(camera);
		renderHandlerBatches.createBatches(groupStrategy);
		renderHandlerAuxModels.init(levelSize, handlersManager.getAxisModelHandler());
	}

	public void createModels(Dimension dimension) {
		renderHandlerAuxModels.createModels(dimension, handlersManager.getAxisModelHandler());
	}

	public Model getTileModel( ) {
		return renderHandlerAuxModels.getTileModel();
	}
}
