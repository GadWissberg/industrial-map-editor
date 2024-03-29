package com.industrial.editor.handlers.action;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.declarations.EnvironmentObjectDeclaration;
import com.gadarts.industrial.shared.assets.declarations.characters.CharacterDeclaration;
import com.gadarts.industrial.shared.assets.declarations.pickups.ItemDeclaration;
import com.gadarts.industrial.shared.model.TriggersDefinitions;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.map.MapNodesTypes;
import com.gadarts.industrial.shared.model.map.Wall;
import com.industrial.editor.MapRendererImpl;
import com.industrial.editor.actions.ActionAnswer;
import com.industrial.editor.actions.MappingAction;
import com.industrial.editor.actions.processes.*;
import com.industrial.editor.actions.types.ActionFactory;
import com.industrial.editor.actions.types.LiftNodesAction;
import com.industrial.editor.actions.types.RemoveElementAction;
import com.industrial.editor.actions.types.placing.*;
import com.industrial.editor.handlers.cursor.CursorHandler;
import com.industrial.editor.handlers.cursor.CursorHandlerModelData;
import com.industrial.editor.handlers.cursor.CursorSelectionModel;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.*;
import com.industrial.editor.model.node.FlatNode;
import com.industrial.editor.model.node.NodeWallsDefinitions;
import com.industrial.editor.model.node.WallDefinition;
import com.industrial.editor.utils.Utils;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionsHandlerImpl implements ActionsHandler {
	private static final Vector3 auxVector = new Vector3();
	private final ActionHandlerRelatedData data;
	private final ActionHandlerRelatedServices services;

	@Getter
	private MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess;

	public ActionsHandlerImpl(final ActionHandlerRelatedData data,
							  final ActionHandlerRelatedServices services) {
		this.data = data;
		this.services = services;
	}

	public void executeAction(final MappingAction mappingAction) {
		mappingAction.execute(services.eventsNotifier());
		if (mappingAction.isProcess()) {
			currentProcess = (MappingProcess<? extends MappingProcess.FinishProcessParameters>) mappingAction;
		}
	}

	@Override
	public void beginTilePlacingProcess(final GameAssetManager assetsManager,
										final Set<MapNodeData> initializedTiles) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		PlaceTilesProcess placeTilesProcess = createPlaceTilesProcess(assetsManager, initializedTiles, position);
		currentProcess = placeTilesProcess;
		placeTilesProcess.execute(services.eventsNotifier());
	}

	public boolean onTouchDown(final GameAssetManager assetsManager,
							   final Set<MapNodeData> initializedTiles,
							   final int button) {
		if (button == Input.Buttons.LEFT) {
			onLeftClick(assetsManager, initializedTiles);
		} else if (button == Input.Buttons.RIGHT) {
			if (MapRendererImpl.getMode() instanceof EditModes) {
				removeElementByMode();
				return true;
			}
		}
		return false;
	}

	@Override
	public void placeEnvObject(final GameAssetManager am) {
		if (isCursorSelectionModelDisabled()) return;
		executeAction(new PlaceEnvObjectAction(
				data.map(),
				(Set<PlacedEnvObject>) data.placedElements().getPlacedObjects().get(EditModes.ENVIRONMENT),
				getOrCreateNode(),
				(EnvironmentObjectDeclaration) services.selectionHandler().getSelectedElement(),
				am,
				services.cursorHandler().getCursorHandlerModelData().getCursorSelectionModel().getFacingDirection()));
	}

	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		LiftNodesAction.Parameters params = new LiftNodesAction.Parameters(src, dst, value, services.wallCreator());
		ActionFactory.liftNodes(data.map(), params, data.placedElements()).execute(services.eventsNotifier());
	}

	public void onEnvObjectDefined(final PlacedEnvObject element, final float height) {
		ActionFactory.defineEnvObject(data.map(), element, height).execute(services.eventsNotifier());
	}

	@Override
	public void defineSelectedEnvObject( ) {
		MapNodeData mapNodeData = getMapNodeDataFromCursor();
		Set<? extends PlacedElement> list = this.data.placedElements().getPlacedObjects().get(MapRendererImpl.getMode());
		List<PlacedElement> elementsInTheNode = fetchElementsFromNode(mapNodeData, list);
		if (elementsInTheNode.size() == 1) {
			services.eventsNotifier().selectedEnvObjectToDefine((PlacedEnvObject) elementsInTheNode.get(0));
		} else if (elementsInTheNode.size() > 1) {
			defineSelectedEnvObjectsInNode(mapNodeData, elementsInTheNode);
		}
	}

	@Override
	public void selectedNodeToPlaceLight( ) {
		MapNodeData mapNodeData = getMapNodeDataFromCursor();
		Set<? extends PlacedElement> list = this.data.placedElements().getPlacedObjects().get(MapRendererImpl.getMode());
		Optional<? extends PlacedElement> lightInNode = list.stream()
				.filter(placedElement -> placedElement.getNode().equals(mapNodeData))
				.findFirst();
		services.eventsNotifier().selectedNodeToPlaceLight(
				mapNodeData,
				(PlacedLight) lightInNode.orElse(null));
	}

	@Override
	public void placePickup(final GameAssetManager am) {
		if (services.selectionHandler().getSelectedElement() == null) return;

		CursorSelectionModel cursorSelectionModel = services.cursorHandler().getCursorHandlerModelData().getCursorSelectionModel();
		Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
		Set<PlacedPickup> placedPickups = (Set<PlacedPickup>) data.placedElements().getPlacedObjects().get(EditModes.PICKUPS);
		MapNodeData node = data.map().getNodes()[(int) position.z][(int) position.x];
		ItemDeclaration selectedElement = (ItemDeclaration) services.selectionHandler().getSelectedElement();
		Direction facingDirection = cursorSelectionModel.getFacingDirection();
		executeAction(new PlacePickupAction(data.map(), placedPickups, node, selectedElement, am, facingDirection));
	}

	@Override
	public void placeLight(final GameAssetManager am, float height, float radius, float intensity) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.map();
		PlaceLightActionParameters parameters = new PlaceLightActionParameters(height, radius, intensity);
		PlaceLightAction action = new PlaceLightAction(
				map,
				(Set<PlacedLight>) data.placedElements().getPlacedObjects().get(EditModes.LIGHTS),
				map.getNodes()[row][col],
				am,
				parameters);
		executeAction(action);
	}

	@Override
	public void beginSelectingTileForLiftProcess(final int direction,
												 final Set<MapNodeData> initializedTiles) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 pos = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		SelectTilesForLiftProcess p = new SelectTilesForLiftProcess(data.map(), new FlatNode((int) pos.z, (int) pos.x));
		p.setDirection(direction);
		p.setWallCreator(services.wallCreator());
		p.setInitializedTiles(initializedTiles);
		currentProcess = p;
	}

	@Override
	public void beginSelectingTilesForWallTiling( ) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		FlatNode src = new FlatNode((int) position.z, (int) position.x);
		currentProcess = new SelectTilesForWallTilingProcess(data.map(), src);
	}

	@Override
	public void placeCharacter(final GameAssetManager am) {
		CursorHandler cursorHandler = services.cursorHandler();
		CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.map();
		PlaceCharacterAction action = new PlaceCharacterAction(
				map,
				(Set<PlacedCharacter>) data.placedElements().getPlacedObjects().get(EditModes.CHARACTERS),
				map.getNodes()[row][col],
				(CharacterDeclaration) services.selectionHandler().getSelectedElement(),
				am,
				cursorHandler.getCursorCharacterDecal().getSpriteDirection());
		executeAction(action);
	}

	public void onNodeWallsDefined(final NodeWallsDefinitions defs,
								   final FlatNode src,
								   final FlatNode dst) {

		Utils.applyOnRegionOfTiles(src, dst, (row, col) -> {
			MapNodeData[][] nodes = data.map().getNodes();
			defineNodeEastAndWestWalls(defs, row, col, nodes);
			defineNodeSouthAndNorthWalls(defs, row, col, nodes);
		});
	}

	public boolean onTouchUp(final Assets.SurfaceTextures selectedTile, final Model cursorTileModel) {
		boolean result = false;
		if (currentProcess != null) {
			finishProcess(selectedTile, cursorTileModel);
			result = true;
		}
		return result;
	}

	@Override
	public void placeTrigger(GameAssetManager assetsManager) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.map();
		PlaceTriggerAction action = new PlaceTriggerAction(
				map,
				(Set<PlacedTrigger>) data.placedElements().getPlacedObjects().get(EditModes.TRIGGERS),
				map.getNodes()[row][col],
				TriggersDefinitions.EXIT_MAP,
				assetsManager);
		executeAction(action);
	}

	private PlaceTilesProcess createPlaceTilesProcess(final GameAssetManager assetsManager,
													  final Set<MapNodeData> initializedTiles,
													  final Vector3 position) {
		return new PlaceTilesProcess(
				new FlatNode((int) position.z, (int) position.x),
				assetsManager,
				initializedTiles,
				data.map());
	}

	private boolean isCursorSelectionModelDisabled( ) {
		return services.cursorHandler()
				.getCursorHandlerModelData()
				.getCursorSelectionModel()
				.getModelInstance() == null;
	}

	private MapNodeData getOrCreateNode( ) {
		CursorHandlerModelData data = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = data.getCursorSelectionModel().getModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		MapNodeData node = this.data.map().getNodes()[row][col];
		if (node == null) {
			node = addNewNode(row, col);
		}
		return node;
	}

	private MapNodeData addNewNode(int row, int col) {
		MapNodeData[][] nodes = data.map().getNodes();
		MapNodeData node;
		node = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE);
		nodes[row][col] = node;
		return node;
	}

	private static List<PlacedElement> fetchElementsFromNode(MapNodeData mapNodeData, Set<? extends PlacedElement> list) {
		return list.stream()
				.filter(placedElement -> placedElement.getNode().equals(mapNodeData))
				.collect(Collectors.toList());
	}

	private void onLeftClick(GameAssetManager assetsManager, Set<MapNodeData> initializedTiles) {
		EditorMode mode = MapRendererImpl.getMode();
		if (mode.getClass().equals(EditModes.class)) {
			mode.onTouchDownLeft(
					currentProcess,
					this,
					assetsManager,
					initializedTiles,
					services.selectionHandler());
		}
	}


	private void defineSelectedEnvObjectsInNode(final MapNodeData mapNodeData,
												final List<PlacedElement> elementsInTheNode) {
		if (mapNodeData != null) {
			ActionAnswer<PlacedElement> answer = new ActionAnswer<>(data ->
					services.eventsNotifier().selectedEnvObjectToDefine((PlacedEnvObject) data));
			services.eventsNotifier().nodeSelectedToSelectObjectsInIt(elementsInTheNode, answer);
		}
	}

	private MapNodeData getMapNodeDataFromCursor( ) {
		Vector3 cursorPosition = services.cursorHandler().getCursorHandlerModelData().getHighlighter().transform.getTranslation(auxVector);
		int row = (int) cursorPosition.z;
		int col = (int) cursorPosition.x;
		return data.map().getNodes()[row][col];
	}

	private void removeElementByMode( ) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		executeAction(new RemoveElementAction(
				data.map(),
				data.placedElements(),
				new FlatNode((int) position.z, (int) position.x),
				(EditModes) MapRendererImpl.getMode()));
	}

	private void finishProcess(final Assets.SurfaceTextures selectedTile, final Model cursorTileModel) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int dstRow = (int) position.z;
		int dstCol = (int) position.x;
		if (currentProcess instanceof PlaceTilesProcess) {
			PlaceTilesProcess currentProcess = (PlaceTilesProcess) this.currentProcess;
			currentProcess.finish(new PlaceTilesFinishProcessParameters(dstRow, dstCol, selectedTile, cursorTileModel));
		} else if (currentProcess instanceof SelectTilesForLiftProcess) {
			SelectTilesForLiftProcess currentProcess = (SelectTilesForLiftProcess) this.currentProcess;
			currentProcess.finish(new SelectTilesForLiftFinishProcessParameters(dstRow, dstCol, services.eventsNotifier()));
		} else if (currentProcess instanceof SelectTilesForWallTilingProcess) {
			SelectTilesForWallTilingProcess currentProcess = (SelectTilesForWallTilingProcess) this.currentProcess;
			currentProcess.finish(new SelectTilesForWallTilingFinishProcessParameters(dstRow, dstCol, services.eventsNotifier()));
		}
		this.currentProcess = null;
	}

	private void defineNodeSouthAndNorthWalls(final NodeWallsDefinitions defs,
											  final int row,
											  final int col,
											  final MapNodeData[][] nodes) {
		MapNodeData selectedNode = nodes[row][col];
		if (nodes.length - 1 > row) {
			defineWall(selectedNode.getWalls().getSouthWall(), selectedNode, nodes[row + 1][col], defs.south());
		}
		if (0 < row) {
			defineWall(selectedNode.getWalls().getNorthWall(), selectedNode, nodes[row - 1][col], defs.north());
		}
	}

	private void defineNodeEastAndWestWalls(final NodeWallsDefinitions defs,
											final int row,
											final int col,
											final MapNodeData[][] nodes) {
		MapNodeData selectedNode = nodes[row][col];
		if (nodes[0].length - 1 > col) {
			defineWall(selectedNode.getWalls().getEastWall(), selectedNode, nodes[row][col + 1], defs.east());
		}
		if (0 < col) {
			defineWall(selectedNode.getWalls().getWestWall(), selectedNode, nodes[row][col - 1], defs.west());
		}
	}

	private void defineWall(Wall selectedWall,
							MapNodeData selectedNode,
							MapNodeData neighborNode,
							WallDefinition wallDefinition) {
		Optional.ofNullable(selectedWall).flatMap(w -> Optional.ofNullable(wallDefinition)).ifPresent(t -> {
			Material material = selectedWall.getModelInstance().materials.get(0);
			TextureAttribute textureAtt = (TextureAttribute) material.get(TextureAttribute.Diffuse);
			defineWallTexture(wallDefinition, selectedWall, textureAtt, selectedNode, neighborNode);
			material.set(textureAtt);
		});
	}

	private void defineWallTexture(WallDefinition wallDefinition,
								   Wall wall,
								   TextureAttribute textureAtt,
								   MapNodeData selectedNode,
								   MapNodeData neighborNode) {
		Assets.SurfaceTextures texture = wallDefinition.getTexture();
		wall.setDefinition(texture != null ? texture : wall.getDefinition());
		GameAssetManager assetsManager = services.assetsManager();
		Optional.ofNullable(texture)
				.ifPresent(tex -> {
					textureAtt.textureDescription.texture = assetsManager.getTexture(texture);
					float sizeHeight = Math.abs(selectedNode.getHeight() - neighborNode.getHeight());
					WallCreator.adjustWallTexture(wall.getModelInstance(), sizeHeight);
				});
	}
}
