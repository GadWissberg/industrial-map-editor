package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.declarations.ModelElementDeclaration;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.utils.GeneralUtils;
import lombok.Getter;

import static com.gadarts.industrial.shared.model.characters.Direction.SOUTH;

@Getter
public abstract class PlacedModelElement extends PlacedElement {
	private static final Vector3 auxVector = new Vector3();
	protected final ModelInstance modelInstance;

	public PlacedModelElement(final PlacedModelElementParameters params, final GameAssetManager assetsManager) {
		super(params);
		Assets.Models modelDefinition = params.getModelDefinition().getModelDefinition();
		this.modelInstance = new ModelInstance(assetsManager.getModel(modelDefinition));
		GeneralUtils.applyExplicitModelTexture(modelDefinition, modelInstance, assetsManager);
		applyInitialTransformOnModelInstance(params, modelInstance);
	}

	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		Matrix4 transform = modelInstance.transform;
		Vector3 translation = transform.getTranslation(auxVector);
		translation.y = height;
		transform.setTranslation(translation);
	}

	protected void applyInitialTransformOnModelInstance(PlacedModelElementParameters params,
														ModelInstance modelInstance) {
		MapNodeData node = params.getNode();
		Coords coords = node.getCoords();
		float height = params.getHeight();
		modelInstance.transform.setTranslation(coords.col(), height, coords.row());
	}

	@Getter
	public static class PlacedModelElementParameters extends PlacedElementParameters {

		private final ModelElementDeclaration modelDefinition;

		public PlacedModelElementParameters(final ModelElementDeclaration definition,
											final Direction facingDirection,
											final MapNodeData node,
											final float height) {
			super(definition, facingDirection, node, height);
			this.modelDefinition = definition;
		}

		public PlacedModelElementParameters(final ModelElementDeclaration definition,
											final MapNodeData node,
											final float height) {
			this(definition, SOUTH, node, height);
		}

		public PlacedModelElementParameters(final PlacedElementParameters params) {
			this((ModelElementDeclaration) params.getDeclaration(), params.getFacingDirection(), params.getNode(), params.getHeight());
		}
	}
}
