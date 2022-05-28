package com.industrial.editor.handlers.action;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.env.EnvironmentDefinitions;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.map.MapNodesTypes;
import com.gadarts.industrial.shared.model.map.Wall;
import com.gadarts.industrial.shared.model.pickups.ItemDefinition;
import com.industrial.editor.CursorSelectionModel;
import com.industrial.editor.MapRendererImpl;
import com.industrial.editor.actions.ActionAnswer;
import com.industrial.editor.actions.MappingAction;
import com.industrial.editor.actions.processes.*;
import com.industrial.editor.actions.types.*;
import com.industrial.editor.handlers.CursorHandler;
import com.industrial.editor.handlers.CursorHandlerModelData;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.mode.tools.ElementTools;
import com.industrial.editor.mode.tools.TilesTools;
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

/**
 * Responsible for handling the actions.
 */
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

	/**
	 * Executes the given action/process.
	 *
	 * @param mappingAction The action to execute.
	 */
	public void executeAction(final MappingAction mappingAction) {
		mappingAction.execute(services.eventsNotifier());
		if (mappingAction.isProcess()) {
			currentProcess = (MappingProcess<? extends MappingProcess.FinishProcessParameters>) mappingAction;
		}
	}

	@Override
	public void beginTilePlacingProcess(final GameAssetsManager assetsManager,
										final Set<MapNodeData> initializedTiles) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		PlaceTilesProcess placeTilesProcess = createPlaceTilesProcess(assetsManager, initializedTiles, position);
		currentProcess = placeTilesProcess;
		placeTilesProcess.execute(services.eventsNotifier());
	}

	private PlaceTilesProcess createPlaceTilesProcess(final GameAssetsManager assetsManager,
													  final Set<MapNodeData> initializedTiles,
													  final Vector3 position) {
		return new PlaceTilesProcess(
				new FlatNode((int) position.z, (int) position.x),
				assetsManager,
				initializedTiles,
				data.getMap());
	}

	/**
	 * Called when the mouse is pressed.
	 *
	 * @param assetsManager
	 * @param initializedTiles
	 * @param button
	 * @return Whether an action is taken in response to this event.
	 */
	@SuppressWarnings("JavaDoc")
	public boolean onTouchDown(final GameAssetsManager assetsManager,
							   final Set<MapNodeData> initializedTiles,
							   final int button) {
		if (button == Input.Buttons.LEFT) {
			onLeftClick(assetsManager, initializedTiles);
		} else if (button == Input.Buttons.RIGHT) {
			if (MapRendererImpl.getMode() instanceof EditModes) {
				if (MapRendererImpl.getTool() == TilesTools.BRUSH || MapRendererImpl.getTool() == ElementTools.BRUSH) {
					return removeElementByMode();
				}
			}
		}
		return false;
	}

	private void onLeftClick(GameAssetsManager assetsManager, Set<MapNodeData> initializedTiles) {
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


	@Override
	public void defineSelectedEnvObject( ) {
		MapNodeData mapNodeData = getMapNodeDataFromCursor();
		List<? extends PlacedElement> list = this.data.getPlacedElements().getPlacedObjects().get(MapRendererImpl.getMode());
		List<PlacedElement> elementsInTheNode = list.stream()
				.filter(placedElement -> placedElement.getNode().equals(mapNodeData))
				.collect(Collectors.toList());
		int size = elementsInTheNode.size();
		if (size == 1) {
			services.eventsNotifier().selectedEnvObjectToDefine((PlacedEnvObject) elementsInTheNode.get(0));
		} else if (size > 1) {
			defineSelectedEnvObjectsInNode(mapNodeData, elementsInTheNode);
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
		Vector3 cursorPosition = services.cursorHandler().getHighlighter().transform.getTranslation(auxVector);
		int row = (int) cursorPosition.z;
		int col = (int) cursorPosition.x;
		return data.getMap().getNodes()[row][col];
	}

	private boolean removeElementByMode( ) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		executeAction(new RemoveElementAction(
				data.getMap(),
				data.getPlacedElements(),
				new FlatNode((int) position.z, (int) position.x),
				(EditModes) MapRendererImpl.getMode()));
		return true;
	}

	@Override
	public void placeEnvObject(final GameAssetsManager am) {
		if (services.cursorHandler()
				.getCursorHandlerModelData()
				.getCursorSelectionModel()
				.getModelInstance() == null) return;

		GameMap map = data.getMap();
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		CursorSelectionModel cursorSelectionModel = cursorHandlerModelData.getCursorSelectionModel();
		Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		MapNodeData[][] nodes = map.getNodes();
		MapNodeData node = nodes[row][col];
		if (node == null) {
			node = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE);
			nodes[row][col] = node;
		}
		PlaceEnvObjectAction action = new PlaceEnvObjectAction(
				map,
				(List<PlacedEnvObject>) data.getPlacedElements().getPlacedObjects().get(EditModes.ENVIRONMENT),
				node,
				(EnvironmentDefinitions) services.selectionHandler().getSelectedElement(),
				am,
				cursorSelectionModel.getFacingDirection());
		executeAction(action);
	}

	@Override
	public void placePickup(final GameAssetsManager am) {
		if (services.selectionHandler().getSelectedElement() == null) return;

		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		CursorSelectionModel cursorSelectionModel = cursorHandlerModelData.getCursorSelectionModel();
		Vector3 position = cursorSelectionModel.getModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.getMap();
		PlacePickupAction action = new PlacePickupAction(
				map,
				(List<PlacedPickup>) data.getPlacedElements().getPlacedObjects().get(EditModes.PICKUPS),
				map.getNodes()[row][col],
				(ItemDefinition) services.selectionHandler().getSelectedElement(),
				am,
				cursorSelectionModel.getFacingDirection());
		executeAction(action);
	}

	@Override
	public void placeLight(final GameAssetsManager am) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.getMap();
		PlaceLightAction action = new PlaceLightAction(
				map,
				(List<PlacedLight>) data.getPlacedElements().getPlacedObjects().get(EditModes.LIGHTS),
				map.getNodes()[row][col],
				services.selectionHandler().getSelectedElement(),
				am);
		executeAction(action);
	}

	@Override
	public boolean beginSelectingTileForLiftProcess(final int direction,
													final Set<MapNodeData> initializedTiles) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 pos = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		SelectTilesForLiftProcess p = new SelectTilesForLiftProcess(data.getMap(), new FlatNode((int) pos.z, (int) pos.x));
		p.setDirection(direction);
		p.setWallCreator(services.wallCreator());
		p.setInitializedTiles(initializedTiles);
		currentProcess = p;
		return true;
	}

	@Override
	public void beginSelectingTilesForWallTiling( ) {
		CursorHandlerModelData cursorHandlerModelData = services.cursorHandler().getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		FlatNode src = new FlatNode((int) position.z, (int) position.x);
		currentProcess = new SelectTilesForWallTilingProcess(data.getMap(), src);
	}

	@Override
	public void placeCharacter(final GameAssetsManager am) {
		CursorHandler cursorHandler = services.cursorHandler();
		CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
		Vector3 position = cursorHandlerModelData.getCursorTileModelInstance().transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		GameMap map = data.getMap();
		PlaceCharacterAction action = new PlaceCharacterAction(
				map,
				(List<PlacedCharacter>) data.getPlacedElements().getPlacedObjects().get(EditModes.CHARACTERS),
				map.getNodes()[row][col],
				(CharacterDefinition) services.selectionHandler().getSelectedElement(),
				am,
				cursorHandler.getCursorCharacterDecal().getSpriteDirection());
		executeAction(action);
	}

	/**
	 * Called when the mouse button is released.
	 *
	 * @param selectedTile
	 * @param cursorTileModel
	 * @return Whether an action taken in response to this event.
	 */
	@SuppressWarnings("JavaDoc")
	public boolean onTouchUp(final Assets.SurfaceTextures selectedTile, final Model cursorTileModel) {
		boolean result = false;
		if (currentProcess != null) {
			finishProcess(selectedTile, cursorTileModel);
			result = true;
		}
		return result;
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

	/**
	 * Called when a node's walls were defined.
	 *
	 * @param defs
	 * @param src
	 * @param dst
	 */
	@SuppressWarnings("JavaDoc")
	public void onNodeWallsDefined(final NodeWallsDefinitions defs,
								   final FlatNode src,
								   final FlatNode dst) {

		Utils.applyOnRegionOfTiles(src, dst, (row, col) -> {
			MapNodeData[][] nodes = data.getMap().getNodes();
			defineNodeEastAndWestWalls(defs, row, col, nodes);
			defineNodeSouthAndNorthWalls(defs, row, col, nodes);
		});
	}

	private void defineNodeSouthAndNorthWalls(final NodeWallsDefinitions defs,
											  final int row,
											  final int col,
											  final MapNodeData[][] nodes) {
		if (nodes.length - 1 > row) {
			defineSouth(defs, nodes[row + 1][col], nodes[row][col]);
		}
		if (0 < row) {
			defineNorth(defs, nodes[row - 1][col], nodes[row][col]);
		}
	}

	private void defineNodeEastAndWestWalls(final NodeWallsDefinitions defs,
											final int row,
											final int col,
											final MapNodeData[][] nodes) {
		if (nodes[0].length - 1 > col) {
			defineEast(defs, nodes[row][col + 1], nodes[row][col]);
		}
		if (0 < col) {
			defineWest(defs, nodes[row][col - 1], nodes[row][col]);
		}
	}

	private void defineNorth(final NodeWallsDefinitions defs,
							 final MapNodeData mapNodeData,
							 final MapNodeData node) {
		Wall neighborWall = mapNodeData != null ? mapNodeData.getWalls().getSouthWall() : null;
		defineWall(node.getWalls().getNorthWall(), neighborWall, defs.getNorth());
	}

	private void defineWest(final NodeWallsDefinitions defs,
							final MapNodeData mapNodeData,
							final MapNodeData node) {
		Wall neighborWall = mapNodeData != null ? mapNodeData.getWalls().getEastWall() : null;
		defineWall(node.getWalls().getWestWall(), neighborWall, defs.getWest());
	}

	private void defineSouth(final NodeWallsDefinitions defs,
							 final MapNodeData mapNodeData,
							 final MapNodeData node) {
		Wall neighborWall = mapNodeData != null ? mapNodeData.getWalls().getNorthWall() : null;
		defineWall(node.getWalls().getSouthWall(), neighborWall, defs.getSouth());
	}

	private void defineEast(final NodeWallsDefinitions defs,
							final MapNodeData neighborNode,
							final MapNodeData node) {
		Wall neighborWall = neighborNode != null ? neighborNode.getWalls().getWestWall() : null;
		defineWall(node.getWalls().getEastWall(), neighborWall, defs.getEast());
	}

	private void defineWall(final Wall selectedWall,
							final Wall neighborWall,
							final WallDefinition wallDefinition) {
		Wall wall = Optional.ofNullable(selectedWall).orElse(neighborWall);
		Optional.ofNullable(wall).flatMap(w -> Optional.ofNullable(wallDefinition)).ifPresent(t -> {
			Material material = wall.getModelInstance().materials.get(0);
			TextureAttribute textureAtt = (TextureAttribute) material.get(TextureAttribute.Diffuse);
			defineWallTexture(wallDefinition, wall, textureAtt);
			Optional.ofNullable(wallDefinition.getVScale()).ifPresent(scale -> {
				if (scale != 0) {
					textureAtt.scaleV = textureAtt.scaleV < 0 ? -1 * scale : scale;
					wall.setVScale(scale);
				}
			});
			Optional.ofNullable(wallDefinition.getHorizontalOffset()).ifPresent(offset -> {
				textureAtt.offsetU = offset;
				wall.setHOffset(offset);
			});
			Optional.ofNullable(wallDefinition.getVerticalOffset()).ifPresent(offset -> {
				textureAtt.offsetV = offset;
				wall.setVOffset(offset);
			});
			material.set(textureAtt);
		});
	}

	private void defineWallTexture(final WallDefinition wallDefinition,
								   final Wall wall,
								   final TextureAttribute textureAtt) {
		Assets.SurfaceTextures texture = wallDefinition.getTexture();
		wall.setDefinition(texture != null ? texture : wall.getDefinition());
		GameAssetsManager assetsManager = services.assetsManager();
		Optional.ofNullable(texture)
				.ifPresent(tex -> textureAtt.textureDescription.texture = assetsManager.getTexture(texture));
	}

	/**
	 * Called when tiles height was changed.
	 *
	 * @param src
	 * @param dst
	 * @param value
	 */
	@SuppressWarnings("JavaDoc")
	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		LiftTilesAction.Parameters params = new LiftTilesAction.Parameters(src, dst, value, services.wallCreator());
		ActionFactory.liftTiles(data.getMap(), params).execute(services.eventsNotifier());
	}

	/**
	 * Called when an environment object was defined.
	 *
	 * @param element
	 * @param height
	 */
	@SuppressWarnings("JavaDoc")
	public void onEnvObjectDefined(final PlacedEnvObject element, final float height) {
		ActionFactory.defineEnvObject(data.getMap(), element, height).execute(services.eventsNotifier());
	}
}
