package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.Coords;
import com.gadarts.industrial.shared.model.ModelElementDefinition;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.utils.Utils;
import lombok.Getter;

import static com.gadarts.industrial.shared.model.characters.Direction.SOUTH;

@Getter
public abstract class PlacedModelElement extends PlacedElement {
	protected final ModelInstance modelInstance;

	public PlacedModelElement(final PlacedModelElementParameters params, final GameAssetsManager assetsManager) {
		super(params);
		Assets.Models modelDefinition = params.getModelDefinition().getModelDefinition();
		this.modelInstance = new ModelInstance(assetsManager.getModel(modelDefinition));
		Utils.applyExplicitModelTexture(modelDefinition, modelInstance, assetsManager);
		applyInitialTransformOnModelInstance(params, modelInstance);
	}

	protected void applyInitialTransformOnModelInstance(PlacedModelElementParameters params,
														ModelInstance modelInstance) {
		MapNodeData node = params.getNode();
		Coords coords = node.getCoords();
		float height = params.getHeight();
		modelInstance.transform.setTranslation(coords.getCol(), height > 0 ? height : node.getHeight(), coords.getRow());
	}

	@Getter
	public static class PlacedModelElementParameters extends PlacedElementParameters {

		private final ModelElementDefinition modelDefinition;

		public PlacedModelElementParameters(final ModelElementDefinition definition,
											final Direction facingDirection,
											final MapNodeData node,
											final float height) {
			super(definition, facingDirection, node, height);
			this.modelDefinition = definition;
		}

		public PlacedModelElementParameters(final ModelElementDefinition definition,
											final MapNodeData node,
											final float height) {
			this(definition, SOUTH, node, height);
		}

		public PlacedModelElementParameters(final PlacedElementParameters params) {
			this((ModelElementDefinition) params.getDefinition(), params.getFacingDirection(), params.getNode(), params.getHeight());
		}
	}
}
