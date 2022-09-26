package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.assets.definitions.ModelDefinition;
import com.gadarts.industrial.shared.model.GeneralUtils;
import com.gadarts.industrial.shared.model.env.EnvironmentObjectDefinition;
import lombok.Getter;

@Getter
public class PlacedEnvObject extends PlacedModelElement {
	private final ModelInstance appendixModelInstance;

	public PlacedEnvObject(final PlacedModelElementParameters parameters, final GameAssetsManager assetsManager) {
		super(parameters, assetsManager);
		EnvironmentObjectDefinition modelDefinition = (EnvironmentObjectDefinition) parameters.getModelDefinition();
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
