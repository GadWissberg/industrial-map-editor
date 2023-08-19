package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.declarations.EnvironmentObjectDeclaration;
import com.gadarts.industrial.shared.assets.definitions.ModelDefinition;
import com.gadarts.industrial.shared.utils.GeneralUtils;
import lombok.Getter;

@Getter
public class PlacedEnvObject extends PlacedModelElement {
	private final ModelInstance appendixModelInstance;

	public PlacedEnvObject(final PlacedModelElementParameters parameters, final GameAssetManager assetsManager) {
		super(parameters, assetsManager);
		EnvironmentObjectDeclaration modelDefinition = (EnvironmentObjectDeclaration) parameters.getModelDefinition();
		ModelDefinition appendixModelDefinition = modelDefinition.getAppendixModelDefinition();
		if (appendixModelDefinition != null) {
			this.appendixModelInstance = new ModelInstance(assetsManager.getModel(appendixModelDefinition));
			GeneralUtils.applyExplicitModelTexture(appendixModelDefinition, appendixModelInstance, assetsManager);
			applyInitialTransformOnModelInstance(parameters, appendixModelInstance);
		} else {
			this.appendixModelInstance = null;
		}
	}

}
