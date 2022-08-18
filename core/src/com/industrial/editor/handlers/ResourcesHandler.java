package com.industrial.editor.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.characters.CharacterTypes;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.characters.SpriteType;
import com.industrial.editor.utils.Utils;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.gadarts.industrial.shared.assets.Assets.AssetsTypes.FONT;
import static com.gadarts.industrial.shared.assets.Assets.AssetsTypes.MELODY;
import static com.gadarts.industrial.shared.assets.Assets.AssetsTypes.PARTICLES;
import static com.gadarts.industrial.shared.assets.Assets.AssetsTypes.SHADER;
import static com.gadarts.industrial.shared.assets.Assets.AssetsTypes.SOUND;

@Getter
public class ResourcesHandler implements Disposable {

	private GameAssetsManager assetsManager;

	void initializeGameFiles() {
		assetsManager.loadGameFiles(FONT, MELODY, SOUND, SHADER, PARTICLES);
		Arrays.stream(CharacterTypes.values()).forEach(type ->
				Arrays.stream(type.getDefinitions()).forEach(this::generateFramesMapForCharacter));
		postAssetsLoading();
	}

	private void postAssetsLoading() {
		Array<Model> models = new Array<>();
		assetsManager.getAll(Model.class, models);
		models.forEach(model -> model.materials.get(0).set(new BlendingAttribute()));
		Array<Texture> textures = new Array<>();
		assetsManager.getAll(Texture.class, textures);
		textures.forEach(texture -> texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat));
	}

	private void generateFramesMapForCharacter(final CharacterDefinition characterDefinition) {
		if (characterDefinition.getAtlasDefinition() == null) return;
		TextureAtlas atlas = assetsManager.getAtlas(characterDefinition.getAtlasDefinition());
		HashMap<Direction, TextureAtlas.AtlasRegion> playerFrames = new HashMap<>();
		Arrays.stream(Direction.values()).forEach(direction -> {
			String name = SpriteType.IDLE.name() + "_0_" + direction.name();
			playerFrames.put(direction, atlas.findRegion(name.toLowerCase()));
		});
		String format = String.format(Utils.FRAMES_KEY_CHARACTER, characterDefinition.name());
		assetsManager.addAsset(format, Map.class, playerFrames);
	}


	public void init(final String assetsLocation) {
		assetsManager = new GameAssetsManager(assetsLocation.replace('\\', '/') + '/');
	}

	@Override
	public void dispose() {
		assetsManager.dispose();
	}
}
