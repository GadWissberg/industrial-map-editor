package com.industrial.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.GeneralUtils;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.env.EnvironmentDefinitions;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.pickups.ItemDefinition;
import com.industrial.editor.handlers.cursor.CursorHandler;
import com.industrial.editor.handlers.HandlersManager;
import com.industrial.editor.handlers.HandlersManagerImpl;
import com.industrial.editor.handlers.ResourcesHandler;
import com.industrial.editor.handlers.cursor.CursorSelectionModel;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.ViewModes;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.TilesTools;
import com.industrial.editor.model.elements.PlacedElements;
import com.industrial.editor.model.elements.PlacedEnvObject;
import com.industrial.editor.model.node.FlatNode;
import com.industrial.editor.model.node.NodeWallsDefinitions;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * The world renderer.
 */
public class MapRendererImpl extends Editor implements MapRenderer {

	/**
	 * Camera's far.
	 */
	public static final float FAR = 200f;

	/**
	 * The rate of the cursor flicker animation.
	 */
	public static final Vector3 auxVector3_1 = new Vector3();
	public static final int TARGET_VERSION = 5;
	private static final float NEAR = 0.01f;
	private static final float CAMERA_HEIGHT = 14;
	private static final float[] CAMERA_START_POINT = {12F, CAMERA_HEIGHT, 12F};
	private static final float[] CAMERA_INITIAL_POSITION = {4F, 6F, 4F};

	@Getter
	public static EditorMode mode = EditModes.TILES;

	@Getter
	public static EditorTool tool = TilesTools.BRUSH;

	private final MapRendererData data;
	private final Vector2 lastMouseTouchPosition = new Vector2();
	private final HandlersManager handlers;
	private WallCreator wallCreator;
	private OrthographicCamera camera;

	public MapRendererImpl(final int width, final int height, final String assetsLocation) {
		data = new MapRendererData(new ViewportResolution(width / 50, height / 50));
		handlers = new HandlersManagerImpl(data);
		ResourcesHandler resourcesHandler = handlers.getResourcesHandler();
		resourcesHandler.init(assetsLocation);
		CursorHandler cursorHandler = handlers.getLogicHandlers().getCursorHandler();
		GameAssetsManager assetsManager = resourcesHandler.getAssetsManager();
		cursorHandler.getCursorHandlerModelData().setCursorSelectionModel(new CursorSelectionModel(assetsManager));
		PlacedElements placedElements = data.getPlacedElements();
		handlers.getMapFileHandler().init(assetsManager, cursorHandler, placedElements.getPlacedTiles());
		Arrays.stream(EditModes.values()).forEach(mode -> placedElements.getPlacedObjects().put(mode, new ArrayList<>()));
	}


	@Override
	public void create() {
		GameAssetsManager assetsManager = handlers.getResourcesHandler().getAssetsManager();
		wallCreator = new WallCreator(assetsManager);
		camera = createCamera();
		handlers.onCreate(camera, wallCreator, data.getMap().getDimension());
		initializeInput();
		handlers.getEventsNotifier().rendererIsReady();
		onToolSet(TilesTools.BRUSH);
	}


	private OrthographicCamera createCamera() {
		ViewportResolution viewportResolution = data.getViewportResolution();
		OrthographicCamera cam = new OrthographicCamera(
				viewportResolution.VIEWPORT_WIDTH,
				viewportResolution.VIEWPORT_HEIGHT);
		cam.near = NEAR;
		cam.far = FAR;
		cam.update();
		initializeCameraPosition(cam);
		return cam;
	}

	private void initializeCameraPosition(final OrthographicCamera cam) {
		cam.position.set(CAMERA_INITIAL_POSITION);
		cam.up.set(0, 1, 0);
		cam.zoom = 1;
		cam.lookAt(auxVector3_1.setZero());
		cam.position.set(CAMERA_START_POINT);
	}

	@Override
	public void render() {
		update();
		PlacedElements placedElements = data.getPlacedElements();
		handlers.getRenderHandler().render(
				mode,
				placedElements,
				handlers.getLogicHandlers().getSelectionHandler().getSelectedElement(),
				tool);
	}


	private void update() {
		InputProcessor inputProcessor = Gdx.input.getInputProcessor();
		if (inputProcessor != null && DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController cameraInputController = (CameraInputController) inputProcessor;
			cameraInputController.update();
		}
		camera.update();
		CursorHandler cursorHandler = handlers.getLogicHandlers().getCursorHandler();
		if (cursorHandler.getHighlighter() != null) {
			cursorHandler.updateCursorFlicker(mode);
		}
	}


	@Override
	public void dispose() {
		handlers.dispose();
		wallCreator.dispose();
	}

	@Override
	public void onTileSelected(final Assets.SurfaceTextures texture) {
		handlers.onTileSelected(texture);
	}

	@Override
	public void onEditModeSet(final EditModes mode) {
		handlers.onEditModeSet(mode);
	}


	@Override
	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		handlers.onTreeCharacterSelected(definition);
	}

	@Override
	public void onSelectedObjectRotate(final int direction) {
		handlers.onSelectedObjectRotate(direction, mode);
	}

	@Override
	public void onTreeEnvSelected(final EnvironmentDefinitions env) {
		handlers.onTreeEnvSelected(env);
	}

	@Override
	public void onTreePickupSelected(final ItemDefinition definition) {
		handlers.onTreePickupSelected(definition);
	}

	@Override
	public void onViewModeSet(final ViewModes mode) {
		handlers.onModeSet(mode);
	}

	@Override
	public void onSaveMapRequested(final String path) {
		handlers.getMapFileHandler().onSaveMapRequested(data, path);
	}

	@Override
	public void onNewMapRequested() {
		data.reset();
		initializeCameraPosition(camera);
		handlers.getRenderHandler().createModels(data.getMap().getDimension());
	}

	@Override
	public void onLoadMapRequested(final String path) throws IOException {
		handlers.getMapFileHandler().onLoadMapRequested(data, wallCreator, handlers.getRenderHandler(), path);
	}

	@Override
	public void onToolSet(final EditorTool tool) {
		handlers.onToolSet(tool);
	}

	@Override
	public void onNodeWallsDefined(final NodeWallsDefinitions definitions, final FlatNode src, final FlatNode dst) {
		handlers.getLogicHandlers().getActionsHandler().onNodeWallsDefined(definitions, src, dst);
	}

	@Override
	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		handlers.getLogicHandlers().getActionsHandler().onTilesLift(src, dst, value);
	}

	@Override
	public float getAmbientLightValue() {
		return data.getMap().getAmbientLight();
	}

	@Override
	public void onAmbientLightValueSet(final float value) {
		data.getMap().setAmbientLight(value);
	}

	@Override
	public void onEnvObjectDefined(final PlacedEnvObject element, final float height) {
		handlers.getLogicHandlers().getActionsHandler().onEnvObjectDefined(element, height);
	}

	@Override
	public void onMapSizeSet(final int width, final int depth) {
		if (this.data.getMap().getNodes().length == depth && this.data.getMap().getNodes()[0].length == width) return;
		Dimension dimension = new Dimension(width, depth);
		data.getMap().resetSize(dimension);
		handlers.getRenderHandler().createModels(dimension);
	}

	@Override
	public Dimension getMapSize() {
		MapNodeData[][] nodes = data.getMap().getNodes();
		return new Dimension(nodes.length, nodes[0].length);
	}

	@Override
	public List<MapNodeData> getRegion(FlatNode src, FlatNode dst) {
		List<MapNodeData> result = new ArrayList<>();
		int startRow = Math.min(src.getRow(), dst.getRow());
		int endRow = Math.max(src.getRow(), dst.getRow());
		int startCol = Math.min(src.getCol(), dst.getCol());
		int endCol = Math.max(src.getCol(), dst.getCol());
		for (int row = startRow; row <= endRow; row++) {
			for (int col = startCol; col <= endCol; col++) {
				result.add(data.getMap().getNodes()[row][col]);
			}
		}
		return result;
	}


	void initializeInput() {
		if (DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController processor = new CameraInputController(camera);
			Gdx.input.setInputProcessor(processor);
			processor.autoUpdate = true;
		} else {
			Gdx.input.setInputProcessor(this);
		}
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		if (button == Input.Buttons.LEFT) {
			lastMouseTouchPosition.set(screenX, screenY);
		}
		Set<MapNodeData> placedTiles = data.getPlacedElements().getPlacedTiles();
		GameAssetsManager assetsManager = handlers.getResourcesHandler().getAssetsManager();
		return handlers.getLogicHandlers().getActionsHandler().onTouchDown(assetsManager, placedTiles, button);
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		CursorHandler cursorHandler = handlers.getLogicHandlers().getCursorHandler();
		return handlers.onTouchUp(cursorHandler.getCursorHandlerModelData().getCursorTileModel());
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		boolean result = true;
		if (mode.getClass().equals(ViewModes.class)) {
			Vector3 rotationPoint = GeneralUtils.defineRotationPoint(auxVector3_1, camera);
			((ViewModes) mode).getManipulation().run(lastMouseTouchPosition, camera, screenX, screenY, rotationPoint);
		} else {
			result = handlers.getLogicHandlers().getCursorHandler().updateCursorByScreenCoords(screenX, screenY, camera, data.getMap());
		}
		lastMouseTouchPosition.set(screenX, screenY);
		return result;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		return handlers.getLogicHandlers().getCursorHandler().updateCursorByScreenCoords(screenX, screenY, camera, data.getMap());
	}

	public void subscribeForEvents(final MapManagerEventsSubscriber subscriber) {
		handlers.getEventsNotifier().subscribeForEvents(subscriber);
	}
}
