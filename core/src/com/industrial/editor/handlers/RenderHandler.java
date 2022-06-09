package com.industrial.editor.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
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
import lombok.Getter;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RenderHandler implements Disposable {
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Matrix4 auxMatrix_1 = new Matrix4();
	private static final Matrix4 auxMatrix_2 = new Matrix4();
	private static final Vector2 auxVector2_1 = new Vector2();
	private static final int DECALS_POOL_SIZE = 200;
	private static final Color GRID_COLOR = Color.GRAY;

	private final AxisModelHandler axisModelHandler = new AxisModelHandler();
	private final GameAssetsManager assetsManager;
	private final HandlersManager handlersManager;
	private final Camera camera;
	@Getter
	private final Model tileModel;
	private Model gridModel;
	private ModelInstance gridModelInstance;
	@Getter
	private ModelBatch modelBatch;

	@Getter
	private DecalBatch decalBatch;

	public RenderHandler(final GameAssetsManager assetsManager,
						 final HandlersManager handlersManager,
						 final Camera camera) {
		tileModel = createRectModel();
		this.assetsManager = assetsManager;
		this.handlersManager = handlersManager;
		this.camera = camera;
	}

	public void renderDecal(final Decal decal, final Camera camera) {
		decal.lookAt(auxVector3_1.set(decal.getPosition()).sub(camera.direction), camera.up);
		decalBatch.add(decal);
	}

	void createBatches(final Camera camera) {
		CameraGroupStrategy groupStrategy = new CameraGroupStrategy(camera);
		this.decalBatch = new DecalBatch(DECALS_POOL_SIZE, groupStrategy);
		this.modelBatch = new ModelBatch();
	}

	private Model createRectModel( ) {
		ModelBuilder builder = new ModelBuilder();
		BlendingAttribute highlightBlend = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Material material = new Material(highlightBlend);
		return builder.createRect(
				0, 0, 1,
				1, 0, 1,
				1, 0, 0,
				0, 0, 0,
				0, 1, 0,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
		);
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
		axisModelHandler.render(modelBatch);
		modelBatch.render(gridModelInstance);
	}

	public void createModels(final Dimension levelSize) {
		Model axisModelX = axisModelHandler.getAxisModelX();
		Model axisModelY = axisModelHandler.getAxisModelY();
		Model axisModelZ = axisModelHandler.getAxisModelZ();
		if (axisModelX == null && axisModelY == null && axisModelZ == null) {
			axisModelHandler.createAxis();
		}
		createGrid(levelSize);
	}

	private void createGrid(final Dimension levelSize) {
		Gdx.app.postRunnable(( ) -> {
			if (gridModel != null) {
				gridModel.dispose();
			}
			ModelBuilder builder = new ModelBuilder();
			Material material = new Material(ColorAttribute.createDiffuse(GRID_COLOR));
			int attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;
			gridModel = builder.createLineGrid(levelSize.width, levelSize.height, 1, 1, material, attributes);
			gridModelInstance = new ModelInstance(gridModel);
			gridModelInstance.transform.translate(levelSize.width / 2f, 0.01f, levelSize.height / 2f);
		});
	}

	private void renderDecals(final HandlersManager handlersManager, final EditorMode mode, final PlacedElements placedElements) {
		Gdx.gl.glDepthMask(false);
		if (handlersManager.getLogicHandlers().getCursorHandler().getHighlighter() != null && mode.getClass().equals(EditModes.class) && ((EditModes) mode).isDecalCursor()) {
			renderCursorOfDecalMode(handlersManager, mode, camera);
		}
		renderDecalPlacedElements(placedElements, handlersManager, camera);
		handlersManager.getRenderHandler().getDecalBatch().flush();
		Gdx.gl.glDepthMask(true);
	}

	private void renderDecalPlacedElements(final PlacedElements placedElements,
										   final HandlersManager handlersManager,
										   final Camera camera) {
		Map<EditModes, List<? extends PlacedElement>> placedObjects = placedElements.getPlacedObjects();
		List<PlacedCharacter> placedCharacters = (List<PlacedCharacter>) placedObjects.get(EditModes.CHARACTERS);
		for (final PlacedCharacter character : placedCharacters) {
			renderCharacter(character.getCharacterDecal(), character.getCharacterDecal().getSpriteDirection(), camera, handlersManager);
		}
		List<PlacedLight> placedLights = (List<PlacedLight>) placedObjects.get(EditModes.LIGHTS);
		for (final PlacedLight placedLight : placedLights) {
			handlersManager.getRenderHandler().renderDecal(placedLight.getDecal(), camera);
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
		ModelBatch modelBatch = handlersManager.getRenderHandler().getModelBatch();
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
		List<PlacedPickup> placedPickups = (List<PlacedPickup>) placedElements.getPlacedObjects().get(EditModes.PICKUPS);
		for (final PlacedPickup pickup : placedPickups) {
			renderPickup(pickup.getModelInstance());
		}
	}

	private void renderTiles(final Set<MapNodeData> initializedTiles) {
		for (final MapNodeData tile : initializedTiles) {
			if (tile.getModelInstance() != null) {
				ModelBatch modelBatch = handlersManager.getRenderHandler().getModelBatch();
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
			if (((!cursorAlwaysDisplayed && (selectedElement != null || selectedTile != null)) || cursorAlwaysDisplayed)) {
				handlersManager.getRenderHandler().getModelBatch().render(highlighter);
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
		EnvironmentObjectDefinition environmentDefinition = selectedElement;
		cursorHandler.renderModelCursorFloorGrid(environmentDefinition, modelBatch);
		CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
		CursorSelectionModel cursorSelectionModel = cursorHandlerModelData.getCursorSelectionModel();
		ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
		Direction facingDirection = cursorSelectionModel.getFacingDirection();
		ModelInstance appendixModelInstance = cursorSelectionModel.getAppendixModelInstance();
		renderEnvObject(environmentDefinition, modelInstance, facingDirection, appendixModelInstance);
	}

	private void renderEnvObjects(final PlacedElements placedElements) {
		Map<EditModes, List<? extends PlacedElement>> placedObjects = placedElements.getPlacedObjects();
		List<PlacedEnvObject> placedEnvObjects = (List<PlacedEnvObject>) placedObjects.get(EditModes.ENVIRONMENT);
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
		handlersManager.getRenderHandler().getModelBatch().render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}

	private void renderEnvObject(final EnvironmentObjectDefinition definition,
								 final ModelInstance modelInstance,
								 final Direction facingDirection,
								 ModelInstance appendixModelInstance) {
		Matrix4 originalTransform = auxMatrix_1.set(modelInstance.transform);
		rotateEnvObject(definition, modelInstance, facingDirection);
		handleSpecificEnvRender(definition, modelInstance, facingDirection, definition.getEnvironmentObjectType());
		modelBatch.render(modelInstance);
		renderEnvAppendix(definition, facingDirection, appendixModelInstance);
		modelInstance.transform.set(originalTransform);
	}

	private void renderEnvAppendix(EnvironmentObjectDefinition definition,
								   Direction facingDirection,
								   ModelInstance appendixModelInstance) {
		if (appendixModelInstance != null) {
			Matrix4 originalTransform = auxMatrix_2.set(appendixModelInstance.transform);
			rotateEnvObject(definition, appendixModelInstance, facingDirection);
			modelBatch.render(appendixModelInstance);
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
						handlersManager.getRenderHandler().getModelBatch());
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
		tileModel.dispose();
		modelBatch.dispose();
		decalBatch.dispose();
		axisModelHandler.dispose();
		gridModel.dispose();
	}

}
