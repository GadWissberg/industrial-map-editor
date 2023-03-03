package com.industrial.editor.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.industrial.shared.assets.Assets.Declarations;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.declarations.enemies.EnemiesDeclarations;
import com.gadarts.industrial.shared.model.characters.CharacterDeclaration;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.characters.SpriteType;
import com.gadarts.industrial.shared.model.characters.player.PlayerDeclaration;
import com.industrial.editor.utils.Utils;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.gadarts.industrial.shared.assets.Assets.AssetsTypes.*;

@Getter
public class ResourcesHandler implements Disposable {
	private final MapFileHandler mapFileHandler = new MapFileHandler();

	private GameAssetManager assetsManager;

	void initializeGameFiles( ) {
		assetsManager.loadGameFiles(FONT, MELODY, SOUND, SHADER, PARTICLES);
		generateFramesMapForCharacter(PlayerDeclaration.getInstance());
		EnemiesDeclarations enemies = (EnemiesDeclarations) assetsManager.getDeclaration(Declarations.ENEMIES);
		enemies.enemiesDeclarations().forEach(this::generateFramesMapForCharacter);
		postAssetsLoading();
	}

	private void postAssetsLoading( ) {
		Array<Model> models = new Array<>();
		assetsManager.getAll(Model.class, models);
		models.forEach(model -> model.materials.get(0).set(new BlendingAttribute()));
		Array<Texture> textures = new Array<>();
		assetsManager.getAll(Texture.class, textures);
		textures.forEach(texture -> texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat));
	}

	private void generateFramesMapForCharacter(final CharacterDeclaration characterDeclaration) {
		if (characterDeclaration.getAtlasDefinition() == null) return;
		TextureAtlas atlas = assetsManager.getAtlas(characterDeclaration.getAtlasDefinition());
		HashMap<Direction, TextureAtlas.AtlasRegion> playerFrames = new HashMap<>();
		Arrays.stream(Direction.values()).forEach(direction -> {
			String name = SpriteType.IDLE.name() + "_0_" + direction.name();
			playerFrames.put(direction, atlas.findRegion(name.toLowerCase()));
		});
		String format = String.format(Utils.FRAMES_KEY_CHARACTER, characterDeclaration.name());
		assetsManager.addAsset(format, Map.class, playerFrames);
	}


	public void init(final String assetsLocation) {
		assetsManager = new GameAssetManager(assetsLocation.replace('\\', '/') + '/');
	}

	@Override
	public void dispose( ) {
		assetsManager.dispose();
	}
}
