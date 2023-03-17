package com.industrial.editor.handlers.cursor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.ModelElementDeclaration;
import com.gadarts.industrial.shared.model.characters.CharacterDeclaration;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.utils.CameraUtils;
import com.industrial.editor.MapRendererImpl;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.elements.CharacterDecal;
import com.industrial.editor.model.node.FlatNode;
import com.industrial.editor.utils.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import squidpony.squidmath.Coord3D;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

import static com.gadarts.industrial.shared.model.characters.CharacterTypes.BILLBOARD_Y;
import static com.gadarts.industrial.shared.model.characters.Direction.NORTH;
import static com.gadarts.industrial.shared.model.characters.Direction.SOUTH;


@Getter
@Setter
public class CursorHandler implements Disposable {

	private static final float FLICKER_RATE = 0.05f;
	private static final float CURSOR_Y = 0.01f;
	private static final float CURSOR_OPACITY = 0.5f;
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector3 auxVector3_2 = new Vector3();
	private static final Vector3 auxVector3_3 = new Vector3();
	@Getter(AccessLevel.NONE)
	private final Vector2 lastMouseTouchPosition = new Vector2();

	private CursorHandlerModelData cursorHandlerModelData = new CursorHandlerModelData();
	private CharacterDecal cursorCharacterDecal;
	private ModelInstance highlighter;
	private Decal cursorSimpleDecal;
	private float flicker;
	private Map<EditModes, Assets.UiTextures> modesToDecal = Map.of(
			EditModes.LIGHTS, Assets.UiTextures.BULB,
			EditModes.TRIGGERS, Assets.UiTextures.TRIGGER);

	public void applyOpacity( ) {
		ModelInstance modelInstance = cursorHandlerModelData.getCursorSelectionModel().getModelInstance();
		BlendingAttribute blend = (BlendingAttribute) modelInstance.materials.get(0).get(BlendingAttribute.Type);
		if (blend != null) {
			blend.opacity = CursorHandler.CURSOR_OPACITY;
		}
	}

	public void updateCursorFlicker(final EditorMode mode) {
		Material material;
		if ((mode == EditModes.TILES || mode == EditModes.CHARACTERS)) {
			material = getHighlighter().materials.get(0);
		} else {
			material = cursorHandlerModelData.getCursorTileModelInstance().materials.get(0);
		}
		BlendingAttribute blend = (BlendingAttribute) material.get(BlendingAttribute.Type);
		blend.opacity = Math.abs(MathUtils.sin(flicker += FLICKER_RATE));
		material.set(blend);
	}

	public boolean updateCursorByScreenCoords(final int screenX,
											  final int screenY,
											  final OrthographicCamera camera,
											  final GameMap map) {
		if (highlighter != null) {
			ArrayDeque<squidpony.squidmath.Coord3D> coords = CameraUtils.findAllCoordsOnRay(screenX, screenY, camera);
			MapNodeData result = findNearestNodeOnCameraLineOfSight(map.getNodes(), coords);
			updateCursorModelAndAdditionalsByCollisionPoint(map, result);
			return true;
		}
		return false;
	}

	public void renderRectangleMarking(final int srcRow, final int srcCol, final ModelBatch modelBatch) {
		Vector3 initialTilePos = highlighter.transform.getTranslation(auxVector3_1);
		for (int i = Math.min((int) initialTilePos.x, srcCol); i <= Math.max((int) initialTilePos.x, srcCol); i++) {
			for (int j = Math.min((int) initialTilePos.z, srcRow); j <= Math.max((int) initialTilePos.z, srcRow); j++) {
				highlighter.transform.setTranslation(i, initialTilePos.y, j);
				modelBatch.render(highlighter);
			}
		}
		highlighter.transform.setTranslation(initialTilePos);
	}

	public void renderModelCursorFloorGrid(final ModelElementDeclaration selectedElement, final ModelBatch modelBatch) {
		ModelInstance cursorTileModelInstance = cursorHandlerModelData.getCursorTileModelInstance();
		Vector3 originalPosition = cursorTileModelInstance.transform.getTranslation(auxVector3_1);
		Vector3 cursorPosition = highlighter.transform.getTranslation(auxVector3_3);
		cursorPosition.y = CURSOR_Y;
		Direction facingDirection = cursorHandlerModelData.getCursorSelectionModel().getFacingDirection();
		renderModelCursorFloorGridCells(cursorPosition, selectedElement, facingDirection, modelBatch);
		cursorTileModelInstance.transform.setTranslation(originalPosition);
	}

	public void createCursors(final GameAssetManager assetsManager, final Model tileModel) {
		cursorHandlerModelData.createCursors(tileModel);
		createCursorSimpleDecal(assetsManager);
	}

	@Override
	public void dispose( ) {
	}

	public void initializeCursorCharacterDecal(GameAssetManager assetsManager, CharacterDeclaration definition) {
		createCursorCharacterDecal(assetsManager, definition);
	}

	public void onEditModeSet(EditModes mode, GameAssetManager assetsManager) {
		if (mode.isDecalCursor()) {
			Optional.ofNullable(modesToDecal.get(mode)).ifPresent(decal -> {
				Texture decalTexture = assetsManager.getTexture(decal);
				cursorSimpleDecal = Utils.createSimpleDecal(decalTexture);
			});
		}
		setHighlighter(getCursorHandlerModelData().getCursorTileModelInstance());
	}

	public void setLastMouseTouchPosition(int screenX, int screenY) {
		lastMouseTouchPosition.set(screenX, screenY);
	}

	public Vector2 getLastMouseTouchPosition(Vector2 output) {
		return output.set(lastMouseTouchPosition);
	}

	private MapNodeData findNearestNodeOnCameraLineOfSight(MapNodeData[][] map,
														   ArrayDeque<Coord3D> coords) {
		MapNodeData result = null;
		for (Coord3D coord : coords) {
			int row = MathUtils.clamp(coord.z, 0, map.length - 1);
			int col = MathUtils.clamp(coord.x, 0, map[0].length - 1);
			MapNodeData node = map[row][col];
			if (node != null) {
				result = node;
			}
		}
		return result;
	}

	private void updateCursorModelAndAdditionalsByCollisionPoint(GameMap map, MapNodeData node) {
		Coords coords = node.getCoords();
		int x = MathUtils.clamp(coords.col(), 0, map.getNodes()[0].length - 1);
		int z = MathUtils.clamp(coords.row(), 0, map.getNodes().length - 1);
		float y = (map.getNodes()[z][x] != null ? map.getNodes()[z][x].getHeight() : 0) + 0.01f;
		highlighter.transform.setTranslation(x, y, z);
		updateCursorAdditionals(x, y, z, MapRendererImpl.getMode());
	}

	private void updateCursorOfDecalMode(final int x, final float y, final int z, final EditorMode mode) {
		float xFinal = x + 0.5f;
		float yFinal = y + BILLBOARD_Y;
		float zFinal = z + 0.5f;
		if (mode == EditModes.CHARACTERS) {
			Optional.ofNullable(cursorCharacterDecal).ifPresent(c -> c.getDecal().setPosition(xFinal, yFinal, zFinal));
		} else {
			cursorSimpleDecal.setPosition(xFinal, yFinal, zFinal);
		}
	}

	private void updateCursorAdditionals(final int x, final float y, final int z, final EditorMode mode) {
		CursorSelectionModel cursorSelectionModel = cursorHandlerModelData.getCursorSelectionModel();
		ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
		if (modelInstance != null) {
			modelInstance.transform.setTranslation(x, y, z);
			ModelInstance appendixModelInstance = cursorSelectionModel.getAppendixModelInstance();
			if (appendixModelInstance != null) {
				appendixModelInstance.transform.setTranslation(x, y, z);
			}
		}
		updateCursorOfDecalMode(x, y, z, mode);
	}


	private void renderModelCursorFloorGridCells(final Vector3 cursorPosition,
												 final ModelElementDeclaration def,
												 final Direction facingDirection,
												 final ModelBatch modelBatch) {
		int halfWidth = def.getWidth() / 2;
		int halfDepth = def.getDepth() / 2;
		if (facingDirection == NORTH || facingDirection == SOUTH) {
			int swap = halfWidth;
			halfWidth = halfDepth;
			halfDepth = swap;
		}
		for (int row = -halfDepth; row < Math.max(halfDepth, 1); row++) {
			renderModelCursorFloorGridRow(cursorPosition, halfWidth, row, modelBatch);
		}
	}

	private void renderModelCursorFloorGridRow(final Vector3 cursorPosition,
											   final int halfWidth,
											   final int row,
											   final ModelBatch modelBatch) {
		for (int col = -halfWidth; col < Math.max(halfWidth, 1); col++) {
			ModelInstance cursorTileModelInstance = cursorHandlerModelData.getCursorTileModelInstance();
			cursorTileModelInstance.transform.setTranslation(cursorPosition).translate(col, 0, row);
			modelBatch.render(cursorTileModelInstance);
		}
	}

	private void createCursorCharacterDecal(final GameAssetManager assetsManager, CharacterDeclaration definition) {
		cursorCharacterDecal = Utils.createCharacterDecal(
				assetsManager,
				definition,
				new FlatNode(0, 0),
				SOUTH);
		Color color = cursorCharacterDecal.getDecal().getColor();
		cursorCharacterDecal.getDecal().setColor(color.r, color.g, color.b, CURSOR_OPACITY);
	}

	private void createCursorSimpleDecal(final GameAssetManager assetsManager) {
		Texture bulb = assetsManager.getTexture(Assets.UiTextures.BULB);
		cursorSimpleDecal = Utils.createSimpleDecal(bulb);
		Color color = cursorSimpleDecal.getColor();
		cursorSimpleDecal.setColor(color.r, color.g, color.b, CURSOR_OPACITY);
	}
}
