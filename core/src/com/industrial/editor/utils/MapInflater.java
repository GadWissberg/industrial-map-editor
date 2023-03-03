package com.industrial.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.assets.Assets.Declarations;
import com.gadarts.industrial.shared.assets.Assets.SurfaceTextures;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.MapJsonKeys;
import com.gadarts.industrial.shared.assets.declarations.enemies.EnemiesDeclarations;
import com.gadarts.industrial.shared.assets.declarations.weapons.PlayerWeaponsDeclarations;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.ElementDeclaration;
import com.gadarts.industrial.shared.model.characters.CharacterDeclaration;
import com.gadarts.industrial.shared.model.characters.CharacterTypes;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.characters.player.PlayerDeclaration;
import com.gadarts.industrial.shared.model.env.EnvironmentObjectType;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.map.MapNodesTypes;
import com.gadarts.industrial.shared.model.map.Wall;
import com.google.gson.*;
import com.industrial.editor.actions.types.placing.PlaceLightActionParameters;
import com.industrial.editor.handlers.cursor.CursorHandler;
import com.industrial.editor.handlers.cursor.CursorHandlerModelData;
import com.industrial.editor.handlers.render.RenderHandler;
import com.industrial.editor.model.elements.PlacedElements;
import com.industrial.editor.MapRendererData;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElement;
import com.industrial.editor.model.elements.PlacedElement.PlacedElementParameters;
import com.industrial.editor.model.elements.PlacedLight;
import com.industrial.editor.model.node.FlatNode;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static com.gadarts.industrial.shared.assets.Assets.Declarations.*;
import static com.gadarts.industrial.shared.assets.Assets.SurfaceTextures.MISSING;
import static com.gadarts.industrial.shared.assets.MapJsonKeys.*;
import static com.gadarts.industrial.shared.model.characters.Direction.SOUTH;
import static com.gadarts.industrial.shared.model.env.light.LightConstants.DEFAULT_LIGHT_INTENSITY;
import static com.gadarts.industrial.shared.model.env.light.LightConstants.DEFAULT_LIGHT_RADIUS;

/**
 * Deserializes map json.
 */
@RequiredArgsConstructor
public class MapInflater {
	private final GameAssetManager assetsManager;
	private final CursorHandler cursorHandler;
	private final Set<MapNodeData> initializedNodes;
	private final Gson gson = new Gson();
	private GameMap map;

	private static SurfaceTextures extractTextureName(JsonObject wallJsonObj) {
		return SurfaceTextures.valueOf(wallJsonObj.get(TEXTURE).getAsString());
	}

	/**
	 * Deserializes map json into the given map object.
	 *
	 * @param data          The relevant map data.
	 * @param wallCreator   The tool used to create walls.
	 * @param renderHandler Stuff used for helping mapping.
	 * @param path          The file path.
	 */
	public void inflateMap(final MapRendererData data,
						   final WallCreator wallCreator,
						   final RenderHandler renderHandler,
						   final String path) throws IOException {
		this.map = data.getMap();
		try (Reader reader = new FileReader(path)) {
			JsonObject input = gson.fromJson(reader, JsonObject.class);
			inflateMapStructure(wallCreator, renderHandler, input);
			inflateAllElements(data, input);
			JsonElement ambient = input.get(AMBIENT);
			Optional.ofNullable(ambient).ifPresent(a -> map.setAmbientLight(a.getAsFloat()));
		} catch (final JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
	}

	private void inflateMapStructure(WallCreator wallCreator, RenderHandler renderHandler, JsonObject input) {
		JsonObject nodesJsonObject = input.getAsJsonObject(NODES);
		JsonArray nodesDataJson = nodesJsonObject.getAsJsonArray(NODES_DATA);
		map.setNodes(inflateNodes(nodesJsonObject, initializedNodes, renderHandler));
		nodesDataJson.forEach(nodeDataJson -> inflateNodeHeight(map, nodeDataJson.getAsJsonObject()));
		nodesDataJson.forEach(nodeDataJson -> inflateWalls(nodeDataJson.getAsJsonObject(), map, wallCreator));
		fillMissingTextures(map, wallCreator);
	}

	private void inflateAllElements(MapRendererData data, JsonObject input) {
		PlacedElements placedElements = data.getPlacedElements();
		inflateCharacters(input, placedElements);
		Arrays.stream(EditModes.values()).forEach(mode -> {
			if (!mode.isSkipGenericElementLoading()) {
				inflateElements(input, mode, placedElements, map);
			}
		});
	}

	private void fillMissingTextures(GameMap map, WallCreator wallCreator) {
		MapNodeData[][] nodes = map.getNodes();
		for (MapNodeData[] mapNodeData : nodes) {
			for (MapNodeData node : mapNodeData) {
				fillSouthWallMissingTexture(wallCreator, nodes, node);
				fillNorthWallMissingTexture(wallCreator, nodes, node);
				fillEastWallMissingTexture(wallCreator, nodes, node);
				fillWestWallMissingTexture(wallCreator, nodes, node);
			}
		}
	}

	private void fillSouthWallMissingTexture(WallCreator wallCreator, MapNodeData[][] nodes, MapNodeData node) {
		if (node.getCoords().row() == nodes.length - 1 || node.getWalls().getSouthWall() != null) return;
		MapNodeData southNode = nodes[node.getCoords().row() + 1][node.getCoords().col()];
		if (node.getHeight() < southNode.getHeight()) {
			inflateSouthWall(node, wallCreator.getSouthWallModel(), MISSING, southNode, wallCreator);
		}
	}

	private void fillNorthWallMissingTexture(WallCreator wallCreator, MapNodeData[][] nodes, MapNodeData node) {
		if (node.getCoords().row() == 0 || node.getWalls().getNorthWall() != null) return;
		MapNodeData northNode = nodes[node.getCoords().row() - 1][node.getCoords().col()];
		if (node.getHeight() < northNode.getHeight()) {
			inflateNorthWall(node, wallCreator.getNorthWallModel(), MISSING, northNode, wallCreator);
		}
	}

	private void fillWestWallMissingTexture(WallCreator wallCreator, MapNodeData[][] nodes, MapNodeData node) {
		if (node.getCoords().col() == 0 || node.getWalls().getWestWall() != null) return;
		MapNodeData westNode = nodes[node.getCoords().row()][node.getCoords().col() - 1];
		if (node.getHeight() < westNode.getHeight()) {
			inflateWestWall(node, wallCreator.getWestWallModel(), MISSING, westNode, wallCreator);
		}
	}

	private void fillEastWallMissingTexture(WallCreator wallCreator, MapNodeData[][] nodes, MapNodeData node) {
		if (node.getCoords().col() == nodes[0].length - 1 || node.getWalls().getEastWall() != null) return;
		MapNodeData eastNode = nodes[node.getCoords().row()][node.getCoords().col() + 1];
		if (node.getHeight() < eastNode.getHeight()) {
			inflateEastWall(node, wallCreator.getEastWallModel(), MISSING, eastNode, wallCreator);
		}
	}

	private void inflateNodeHeight(GameMap map,
								   JsonObject nodeJsonObject) {
		MapNodeData mapNodeData = map.getNodes()[nodeJsonObject.get(ROW).getAsInt()][nodeJsonObject.get(COL).getAsInt()];
		if (nodeJsonObject.has(HEIGHT)) {
			mapNodeData.lift(nodeJsonObject.get(HEIGHT).getAsFloat());
		}
	}

	private void inflateWalls(JsonObject nodeJsonObject,
							  GameMap map,
							  WallCreator wallCreator) {
		MapNodeData[][] nodes = map.getNodes();
		MapNodeData mapNodeData = map.getNodes()[nodeJsonObject.get(ROW).getAsInt()][nodeJsonObject.get(COL).getAsInt()];
		Coords coords = mapNodeData.getCoords();
		JsonObject wallsJsonObject = Optional.ofNullable(nodeJsonObject.get(WALLS))
				.orElseGet(JsonObject::new)
				.getAsJsonObject();

		Model eastWallModel = wallCreator.getEastWallModel();
		Model westWallModel = wallCreator.getWestWallModel();
		Model northWallModel = wallCreator.getNorthWallModel();
		Model southWallModel = wallCreator.getSouthWallModel();
		int row = coords.row();
		int col = coords.col();
		if (row > 0 && mapNodeData.getHeight() < nodes[row - 1][col].getHeight()) {
			Optional.ofNullable(wallsJsonObject.get(MapJsonKeys.NORTH)).ifPresent(north -> {
				SurfaceTextures texture = extractTextureName(north.getAsJsonObject());
				inflateNorthWall(mapNodeData, northWallModel, texture, nodes[row - 1][col], wallCreator);
			});
		}

		if (row < nodes.length - 1 && mapNodeData.getHeight() < nodes[row + 1][col].getHeight()) {
			Optional.ofNullable(wallsJsonObject.get(MapJsonKeys.SOUTH)).ifPresent(north -> {
				SurfaceTextures texture = extractTextureName(north.getAsJsonObject());
				inflateSouthWall(mapNodeData, southWallModel, texture, nodes[row + 1][col], wallCreator);
			});
		}

		if (col > 0 && mapNodeData.getHeight() < nodes[row][col - 1].getHeight()) {
			Optional.ofNullable(wallsJsonObject.get(WEST)).ifPresent(west -> {
				SurfaceTextures texture = extractTextureName(west.getAsJsonObject());
				inflateWestWall(mapNodeData, westWallModel, texture, nodes[row][col - 1], wallCreator);
			});
		}

		if (col < nodes[0].length - 1 && mapNodeData.getHeight() < nodes[row][col + 1].getHeight()) {
			Optional.ofNullable(wallsJsonObject.get(EAST)).ifPresent(east -> {
				SurfaceTextures texture = extractTextureName(east.getAsJsonObject());
				inflateEastWall(mapNodeData, eastWallModel, texture, nodes[row][col + 1], wallCreator);
			});
		}

	}

	private void inflateNorthWall(MapNodeData mapNodeData,
								  Model wallModel,
								  SurfaceTextures texture,
								  MapNodeData northNode,
								  WallCreator wallCreator) {
		Wall northWall = WallCreator.createWall(mapNodeData, wallModel, assetsManager, texture);
		mapNodeData.getWalls().setNorthWall(northWall);
		wallCreator.adjustNorthWall(mapNodeData, northNode);
	}

	private void inflateEastWall(MapNodeData mapNodeData,
								 Model wallModel,
								 SurfaceTextures texture,
								 MapNodeData eastNode,
								 WallCreator wallCreator) {
		Wall eastWall = WallCreator.createWall(mapNodeData, wallModel, assetsManager, texture);
		mapNodeData.getWalls().setEastWall(eastWall);
		wallCreator.adjustEastWall(mapNodeData, eastNode);
	}

	private void inflateWestWall(MapNodeData mapNodeData,
								 Model wallModel,
								 SurfaceTextures texture,
								 MapNodeData westNode,
								 WallCreator wallCreator) {
		Wall westWall = WallCreator.createWall(mapNodeData, wallModel, assetsManager, texture);
		mapNodeData.getWalls().setWestWall(westWall);
		wallCreator.adjustWestWall(westNode, mapNodeData);
	}

	private void inflateSouthWall(MapNodeData mapNodeData,
								  Model wallModel,
								  SurfaceTextures texture,
								  MapNodeData southNode,
								  WallCreator wallCreator) {
		Wall southWall = WallCreator.createWall(mapNodeData, wallModel, assetsManager, texture);
		mapNodeData.getWalls().setSouthWall(southWall);
		wallCreator.adjustSouthWall(southNode, mapNodeData);
	}

	private void inflateElements(final JsonObject input,
								 final EditModes mode,
								 final PlacedElements placedElements,
								 final GameMap map) {
		JsonElement jsonElement = input.get(mode.name().toLowerCase());
		if (jsonElement == null) return;

		Set<? extends PlacedElement> placedElementsList = placedElements.getPlacedObjects().get(mode);
		placedElementsList.clear();
		inflateElements(
				(Set<PlacedElement>) placedElementsList,
				mode,
				jsonElement.getAsJsonArray(),
				map);
	}

	private void inflateCharacters(final JsonObject input,
								   final PlacedElements placedElements) {
		JsonObject charactersJsonObject = input.get(CHARACTERS).getAsJsonObject();
		Set<? extends PlacedElement> placedCharacters = placedElements.getPlacedObjects().get(EditModes.CHARACTERS);
		placedCharacters.clear();
		Set<PlacedElement> placed = (Set<PlacedElement>) placedCharacters;
		inflateCharactersByType(charactersJsonObject, placed, List.of(PlayerDeclaration.getInstance()), CharacterTypes.PLAYER);
		EnemiesDeclarations enemies = (EnemiesDeclarations) assetsManager.getDeclaration(ENEMIES);
		inflateCharactersByType(charactersJsonObject, placed, enemies.enemiesDeclarations(), CharacterTypes.ENEMY);
	}

	private void inflateCharactersByType(JsonObject charactersJsonObject,
										 Set<PlacedElement> placedCharacters,
										 List<? extends CharacterDeclaration> declarations,
										 CharacterTypes type) {
		String typeName = type.name().toLowerCase();
		if (charactersJsonObject.has(typeName)) {
			JsonArray charactersArray = charactersJsonObject.get(typeName).getAsJsonArray();
			inflateElements(
					placedCharacters,
					EditModes.CHARACTERS,
					charactersArray,
					declarations);
		}
	}

	private void inflateElements(Set<PlacedElement> placedElements,
								 EditModes mode,
								 JsonArray elementsJsonArray,
								 GameMap map) {
		elementsJsonArray.forEach(jsonObject -> {
			JsonObject json = jsonObject.getAsJsonObject();
			try {
				PlacedElementParameters parameters = inflateElementParameters(getDeclarationsForMode(mode), json, map);
				Optional.ofNullable(mode.getCreationProcess()).ifPresentOrElse(
						c -> {
							PlacedElement placedElement = c.create(parameters, assetsManager);
							if (placedElement.getNode().getHeight() > placedElement.getHeight()) {
								placedElement.setHeight(placedElement.getNode().getHeight());
							}
							placedElements.add(placedElement);
						},
						( ) -> {
							float radius = json.has(RADIUS) ? json.get(RADIUS).getAsFloat() : DEFAULT_LIGHT_RADIUS;
							float i = json.has(INTENSITY) ? json.get(INTENSITY).getAsFloat() : DEFAULT_LIGHT_INTENSITY;
							PlacedLight placedLight = new PlacedLight(
									new PlacedElementParameters(null, parameters.getNode(), parameters.getHeight()),
									assetsManager).set(new PlaceLightActionParameters(parameters.getHeight(), radius, i));
							if (placedLight.getNode().getHeight() > placedLight.getHeight()) {
								placedLight.setHeight(placedLight.getNode().getHeight());
							}
							placedElements.add(placedLight);
						});
			} catch (NumberFormatException e) {
				logInflationError(json, e);
			}
		});
	}

	private List<? extends ElementDeclaration> getDeclarationsForMode(EditModes mode) {
		List<? extends ElementDeclaration> result = null;
		if (mode == EditModes.CHARACTERS) {
			EnemiesDeclarations declaration = (EnemiesDeclarations) assetsManager.getDeclaration(ENEMIES);
			result = declaration.enemiesDeclarations();
		} else if (mode == EditModes.ENVIRONMENT) {
			result = EnvironmentObjectType.collectAndGetAllDefinitions();
		} else if (mode == EditModes.PICKUPS) {
			PlayerWeaponsDeclarations dec = (PlayerWeaponsDeclarations) assetsManager.getDeclaration(PLAYER_WEAPONS);
			result = dec.playerWeaponsDeclarations();
		}
		return result;
	}

	private void logInflationError(JsonObject json, NumberFormatException e) {
		String simpleName = this.getClass().getSimpleName();
		String message = e.getMessage();
		Gdx.app.log(simpleName, String.format("Failed to inflate an element: %s | Message: %s", json.toString(), message));
	}

	private void inflateElements(Set<PlacedElement> placedElements,
								 EditModes mode,
								 JsonArray elementsJsonArray,
								 List<? extends ElementDeclaration> declarations) {
		elementsJsonArray.forEach(characterJsonObject -> {
			JsonObject json = characterJsonObject.getAsJsonObject();
			try {
				PlacedElementParameters parameters = inflateElementParameters(declarations, json, map);
				PlacedElement placedElement = mode.getCreationProcess().create(parameters, assetsManager);
				MapNodeData node = placedElement.getNode();
				float nodeHeight = node.getHeight();
				if (placedElement.getHeight() < nodeHeight) {
					placedElement.setHeight(nodeHeight);
				}
				placedElements.add(placedElement);
			} catch (NumberFormatException e) {
				logInflationError(json, e);
			}
		});
	}

	private PlacedElementParameters inflateElementParameters(List<? extends ElementDeclaration> declarations,
															 JsonObject json,
															 GameMap map) throws NumberFormatException {
		Direction dir = json.has(DIRECTION) ? Direction.values()[json.get(DIRECTION).getAsInt()] : SOUTH;
		float height = json.has(HEIGHT) ? json.get(HEIGHT).getAsFloat() : 0;
		MapNodeData node = map.getNodes()[json.get(ROW).getAsInt()][json.get(COL).getAsInt()];
		ElementDeclaration definition = null;
		if (declarations != null) {
			JsonElement definitionJsonElement = json.get(TYPE);
			String asString = definitionJsonElement.getAsString();
			definition = Optional.of(declarations.stream()
							.filter(def -> def.id().equalsIgnoreCase(asString))
							.findFirst())
					.orElseThrow()
					.get();
		}
		return new PlacedElementParameters(definition, dir, node, height);
	}

	private MapNodeData[][] inflateNodes(JsonObject nodesJsonObject,
										 Set<MapNodeData> initializedNodes,
										 RenderHandler renderHandler) {
		int width = nodesJsonObject.get(WIDTH).getAsInt();
		int depth = nodesJsonObject.get(DEPTH).getAsInt();
		renderHandler.createModels(new Dimension(width, depth));
		String matrix = nodesJsonObject.get(MATRIX).getAsString();
		MapNodeData[][] inputMap = new MapNodeData[depth][width];
		initializedNodes.clear();
		byte[] matrixByte = Base64.getDecoder().decode(matrix.getBytes());
		IntStream.range(0, depth)
				.forEach(row -> IntStream.range(0, width)
						.forEach(col -> inflateNode(
								width,
								matrixByte,
								inputMap,
								new FlatNode(row, col))));
		return inputMap;
	}

	private void inflateNode(int mapWidth,
							 byte[] matrix,
							 MapNodeData[][] inputMap,
							 FlatNode flatNode) {
		int row = flatNode.getRow();
		int col = flatNode.getCol();
		byte tileId = matrix[row * mapWidth + col % mapWidth];
		MapNodeData node;
		if (tileId != 0) {
			SurfaceTextures textureDefinition = SurfaceTextures.values()[tileId - 1];
			CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
			node = new MapNodeData(cursorHandlerModelData.getCursorTileModel(), row, col, MapNodesTypes.PASSABLE_NODE);
			Utils.initializeNode(node, textureDefinition, assetsManager);
			initializedNodes.add(node);
		} else {
			node = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE);
		}
		inputMap[row][col] = node;
	}

}
