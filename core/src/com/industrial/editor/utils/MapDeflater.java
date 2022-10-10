package com.industrial.editor.utils;

import com.gadarts.industrial.shared.assets.MapJsonKeys;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.characters.CharacterTypes;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.map.NodeWalls;
import com.gadarts.industrial.shared.model.map.Wall;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.industrial.editor.MapRendererData;
import com.industrial.editor.mode.AdditionalDeflationProcess;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.PlacedElement;
import com.industrial.editor.model.elements.PlacedElements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.industrial.editor.MapRendererImpl.TARGET_VERSION;


public class MapDeflater {
	private final Gson gson = new Gson();

	public void deflate(final MapRendererData data, String path) {
		JsonObject output = new JsonObject();
		GameMap map = data.getMap();
		output.addProperty(MapJsonKeys.AMBIENT, map.getAmbientLight());
		output.addProperty(MapJsonKeys.TARGET, TARGET_VERSION);
		JsonObject tiles = createNodesData(map);
		output.add(MapJsonKeys.TILES, tiles);
		PlacedElements placedElements = data.getPlacedElements();
		addCharacters(output, placedElements);
		addElementsGroup(output, EditModes.ENVIRONMENT, true, placedElements);
		addElementsGroup(output, EditModes.PICKUPS, false, placedElements);
		addElementsGroup(output, EditModes.LIGHTS, false, placedElements);
		addElementsGroup(output, EditModes.TRIGGERS, false, placedElements);
		try (Writer writer = new FileWriter(path)) {
			gson.toJson(output, writer);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void addElementsGroup(final JsonObject output,
								  final EditModes mode,
								  final boolean addFacingDir,
								  final PlacedElements placedElements) {
		JsonArray jsonArray = new JsonArray();
		placedElements.getPlacedObjects()
				.get(mode)
				.forEach(element -> {
					AdditionalDeflationProcess additionalDeflationProcess = mode.getAdditionalDeflationProcess();
					jsonArray.add(createElementJsonObject(
							element,
							addFacingDir,
							additionalDeflationProcess,
							mode.isHeightDefinedByNode()));
				});
		output.add(mode.name().toLowerCase(), jsonArray);
	}

	private void addCharacters(final JsonObject output, final PlacedElements placedElements) {
		JsonObject charactersJsonObject = new JsonObject();
		Arrays.stream(CharacterTypes.values()).forEach(type -> {
			JsonArray charactersJsonArray = new JsonArray();
			charactersJsonObject.add(type.name().toLowerCase(), charactersJsonArray);
			placedElements.getPlacedObjects().get(EditModes.CHARACTERS).stream()
					.filter(character -> ((CharacterDefinition) character.getDefinition()).getCharacterType() == type)
					.forEach(character -> charactersJsonArray.add(createElementJsonObject(character, true)));
		});
		output.add(MapJsonKeys.CHARACTERS, charactersJsonObject);
	}

	private JsonObject createElementJsonObject(PlacedElement e, final boolean addFacingDirection) {
		return createElementJsonObject(e, addFacingDirection, null, false);
	}

	private JsonObject createElementJsonObject(PlacedElement e,
											   boolean addFacingDirection,
											   AdditionalDeflationProcess additionalDeflationProcess,
											   boolean heightDefinedByNode) {
		JsonObject jsonObject = new JsonObject();
		MapNodeData node = e.getNode();
		Coords coords = node.getCoords();
		jsonObject.addProperty(MapJsonKeys.ROW, coords.getRow());
		jsonObject.addProperty(MapJsonKeys.COL, coords.getCol());
		jsonObject.addProperty(MapJsonKeys.HEIGHT, heightDefinedByNode ? node.getHeight() : e.getHeight());
		if (addFacingDirection) {
			jsonObject.addProperty(MapJsonKeys.DIRECTION, e.getFacingDirection().ordinal());
		}
		Optional.ofNullable(e.getDefinition()).ifPresent(d -> jsonObject.addProperty(MapJsonKeys.TYPE, d.name()));
		Optional.ofNullable(additionalDeflationProcess).ifPresent(a -> a.run(jsonObject, e.getDefinition()));
		return jsonObject;
	}

	private JsonObject createNodesData(final GameMap map) {
		JsonObject tiles = new JsonObject();
		addMapSize(tiles, map);
		MapNodeData[][] nodes = map.getNodes();
		int numberOfRows = nodes.length;
		int numberOfCols = nodes[0].length;
		byte[] matrix = new byte[numberOfRows * numberOfCols];
		JsonArray heights = new JsonArray();
		IntStream.range(0, numberOfRows).forEach(row ->
				IntStream.range(0, numberOfCols).forEach(col -> {
					MapNodeData mapNodeData = nodes[row][col];
					int index = row * (numberOfCols) + col;
					if (mapNodeData != null && mapNodeData.getTextureDefinition() != null) {
						addHeight(mapNodeData, heights);
						matrix[index] = (byte) (mapNodeData.getTextureDefinition().ordinal() + 1);
					} else {
						matrix[index] = (byte) (0);
					}
				})
		);
		tiles.addProperty(MapJsonKeys.MATRIX, new String(Base64.getEncoder().encode(matrix)));
		if (heights.size() > 0) {
			tiles.add(MapJsonKeys.HEIGHTS, heights);
		}
		return tiles;
	}

	private void addMapSize(final JsonObject tiles, final GameMap map) {
		MapNodeData[][] nodes = map.getNodes();
		tiles.addProperty(MapJsonKeys.WIDTH, nodes[0].length);
		tiles.addProperty(MapJsonKeys.DEPTH, nodes.length);
	}

	private void addHeight(final MapNodeData node, final JsonArray heights) {
		if (node.getHeight() > 0) {
			JsonObject json = new JsonObject();
			Coords coords = node.getCoords();
			json.addProperty(MapJsonKeys.ROW, coords.getRow());
			json.addProperty(MapJsonKeys.COL, coords.getCol());
			json.addProperty(MapJsonKeys.HEIGHT, node.getHeight());
			deflateWalls(node, json);
			heights.add(json);
		}
	}

	private void deflateWalls(final MapNodeData node, final JsonObject output) {
		NodeWalls walls = node.getWalls();
		Optional.ofNullable(walls.getEastWall()).ifPresent(wall -> addWallDefinition(output, wall, MapJsonKeys.EAST));
		Optional.ofNullable(walls.getSouthWall()).ifPresent(wall -> addWallDefinition(output, wall, MapJsonKeys.SOUTH));
		Optional.ofNullable(walls.getWestWall()).ifPresent(wall -> addWallDefinition(output, wall, MapJsonKeys.WEST));
		Optional.ofNullable(walls.getNorthWall()).ifPresent(wall -> addWallDefinition(output, wall, MapJsonKeys.NORTH));
	}

	private void addWallDefinition(final JsonObject json, final Wall w, final String side) {
		if (w.getDefinition() == null) return;
		String textureName = w.getDefinition().getName();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(MapJsonKeys.TEXTURE, textureName);
		deflateWallProperty(jsonObject, w.getVScale(), MapJsonKeys.V_SCALE);
		deflateWallProperty(jsonObject, w.getHOffset(), MapJsonKeys.H_OFFSET);
		deflateWallProperty(jsonObject, w.getVOffset(), MapJsonKeys.V_OFFSET);
		json.add(side, jsonObject);
	}

	private void deflateWallProperty(final JsonObject jsonObject, final Float value, final String key) {
		Optional.ofNullable(value).ifPresent(v -> jsonObject.addProperty(key, value));
	}
}
