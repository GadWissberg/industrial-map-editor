package com.industrial.editor;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.assets.definitions.ModelDefinition;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.characters.Direction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;

import static com.badlogic.gdx.Files.*;
import static com.badlogic.gdx.Files.FileType.*;
import static com.badlogic.gdx.Gdx.files;
import static com.gadarts.industrial.shared.model.characters.Direction.EAST;

@RequiredArgsConstructor
@Getter
public class CursorSelectionModel {

	@Setter
	private Direction facingDirection = EAST;

	private static final Vector2 auxVector2_1 = new Vector2();
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector3 auxVector3_2 = new Vector3();
	private final GameAssetsManager assetsManager;
	private ModelInstance modelInstance;
	private ElementDefinition selectedElement;


	public void setSelection(final ElementDefinition selectedElement, final ModelDefinition model) {
		this.selectedElement = selectedElement;
		modelInstance = new ModelInstance(assetsManager.getModel(model));
		Optional.ofNullable(model.getTextureFileName()).ifPresent(t -> {
			for (Material material : modelInstance.materials) {
				if (material.has(TextureAttribute.Diffuse)) {
					TextureAttribute attribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
					attribute.textureDescription.texture = assetsManager.getModelExplicitTexture(model);
				}
			}
		});
		facingDirection = EAST;
	}
}
