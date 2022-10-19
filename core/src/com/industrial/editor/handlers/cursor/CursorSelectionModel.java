package com.industrial.editor.handlers.cursor;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.assets.definitions.ModelDefinition;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.GeneralUtils;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.env.DoorsDefinitions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
	private ModelInstance appendixModelInstance;
	private ElementDefinition selectedElement;


	public void setSelection(final ElementDefinition selectedElement, final ModelDefinition model) {
		this.selectedElement = selectedElement;
		modelInstance = new ModelInstance(assetsManager.getModel(model));
		initializeAppendix(selectedElement);
		GeneralUtils.applyExplicitModelTexture(model, modelInstance, assetsManager);
		facingDirection = EAST;
	}

	private void initializeAppendix(ElementDefinition selectedElement) {
		if (selectedElement instanceof DoorsDefinitions) {
			DoorsDefinitions selectedDoor = (DoorsDefinitions) selectedElement;
			appendixModelInstance = new ModelInstance(assetsManager.getModel(selectedDoor.getFrameModelDefinition()));
		} else {
			appendixModelInstance = null;
		}
	}
}
