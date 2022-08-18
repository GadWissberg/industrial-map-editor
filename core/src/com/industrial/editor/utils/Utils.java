package com.industrial.editor.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.assets.definitions.ModelDefinition;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.characters.CharacterUtils;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.characters.SpriteType;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.ApplyActionOnTilesRegion;
import com.industrial.editor.model.elements.CharacterDecal;
import com.industrial.editor.model.node.FlatNode;

import java.util.HashMap;
import java.util.Optional;

import static com.gadarts.industrial.shared.model.characters.CharacterTypes.BILLBOARD_SCALE;
import static com.gadarts.industrial.shared.model.characters.CharacterTypes.BILLBOARD_Y;

public class Utils {
	public static final String FRAMES_KEY_CHARACTER = "frames/%s";
	public static final Vector3 auxVector3_1 = new Vector3();
	private final static Plane auxPlane = new Plane();
	private static final Vector3 auxVector3_2 = new Vector3();

	public static CharacterDecal createCharacterDecal(final GameAssetsManager assetsManager,
													  final CharacterDefinition definition,
													  final FlatNode node,
													  final Direction selectedCharacterDirection) {
		String idle = SpriteType.IDLE.name() + "_0_" + selectedCharacterDirection.name().toLowerCase();
		TextureAtlas atlas = assetsManager.getAtlas(definition.getAtlasDefinition());
		TextureAtlas.AtlasRegion region = atlas.findRegion(idle.toLowerCase());
		Decal decal = Decal.newDecal(region, true);
		decal.setPosition(node.getCol() + 0.5f, node.getHeight() + BILLBOARD_Y, node.getRow() + 0.5f);
		decal.setScale(BILLBOARD_SCALE);
		return new CharacterDecal(decal, definition, selectedCharacterDirection);
	}

	public static void applyFrameSeenFromCameraForCharacterDecal(final CharacterDecal characterDecal,
																 final Direction facingDirection,
																 final Camera camera,
																 final GameAssetsManager assetsManager) {
		Direction dirSeenFromCamera = CharacterUtils.calculateDirectionSeenFromCamera(camera, facingDirection);
		CharacterDefinition characterDefinition = characterDecal.getCharacterDefinition();
		String name = String.format(FRAMES_KEY_CHARACTER, characterDefinition.name());
		HashMap<Direction, TextureAtlas.AtlasRegion> hashMap = assetsManager.get(name);
		TextureAtlas.AtlasRegion textureRegion = hashMap.get(dirSeenFromCamera);
		Decal decal = characterDecal.getDecal();
		if (textureRegion != decal.getTextureRegion()) {
			decal.setTextureRegion(textureRegion);
		}
	}

	public static Decal createSimpleDecal(final Texture texture) {
		Decal decal = Decal.newDecal(new TextureRegion(texture), true);
		decal.setScale(BILLBOARD_SCALE);
		return decal;
	}

	public static void initializeTile(final MapNodeData tile,
									  final Assets.SurfaceTextures selectedTile,
									  final GameAssetsManager assetsManager) {
		tile.setTextureDefinition(selectedTile);
		Material material = tile.getModelInstance().materials.get(0);
		TextureAttribute textureAttribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
		textureAttribute.textureDescription.texture = assetsManager.getTexture(selectedTile);
	}

	public static Vector3 castRayTowardsPlane(final float screenX, final float screenY, final OrthographicCamera camera) {
		Ray ray = camera.getPickRay(screenX, screenY);
		auxPlane.set(Vector3.Zero, auxVector3_1.set(0, 1, 0));
		Intersector.intersectRayPlane(ray, auxPlane, auxVector3_2);
		return auxVector3_2;
	}


	public static void applyOnRegionOfTiles(final FlatNode srcNode,
											final FlatNode dstNode,
											final ApplyActionOnTilesRegion runnable) {
		int srcRow = srcNode.getRow();
		int srcCol = srcNode.getCol();
		int dstRow = dstNode.getRow();
		int dstCol = dstNode.getCol();
		for (int col = Math.min(dstCol, srcCol); col <= Math.max(dstCol, srcCol); col++) {
			for (int row = Math.min(dstRow, srcRow); row <= Math.max(dstRow, srcRow); row++) {
				runnable.run(row, col);
			}
		}
	}

}
