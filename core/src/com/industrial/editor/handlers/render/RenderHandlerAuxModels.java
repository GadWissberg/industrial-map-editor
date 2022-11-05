package com.industrial.editor.handlers.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;
import com.industrial.editor.handlers.AxisModelHandler;
import lombok.Getter;

import java.awt.*;

public class RenderHandlerAuxModels implements Disposable {
	private static final com.badlogic.gdx.graphics.Color GRID_COLOR = Color.GRAY;
	@Getter
	private Model tileModel;
	private Model gridModel;

	@Getter
	private ModelInstance gridModelInstance;

	public void createModels(final Dimension levelSize, AxisModelHandler axisModelHandler) {
		Model axisModelX = axisModelHandler.getAxisModelX();
		Model axisModelY = axisModelHandler.getAxisModelY();
		Model axisModelZ = axisModelHandler.getAxisModelZ();
		if (axisModelX == null && axisModelY == null && axisModelZ == null) {
			axisModelHandler.createAxis();
		}
		createGrid(levelSize);
	}

	void init(Dimension levelSize, AxisModelHandler axisModelHandler) {
		createModels(levelSize, axisModelHandler);
		tileModel = createRectModel();
	}

	private void createGrid(final Dimension levelSize) {
		Gdx.app.postRunnable(( ) -> {
			if (gridModel != null) {
				gridModel.dispose();
			}
			ModelBuilder builder = new ModelBuilder();
			Material material = new Material(ColorAttribute.createDiffuse(GRID_COLOR));
			int attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;
			gridModel = builder.createLineGrid(levelSize.width, levelSize.height, 1, 1, material, attributes);
			gridModelInstance = new ModelInstance(gridModel);
			gridModelInstance.transform.translate(levelSize.width / 2f, 0.01f, levelSize.height / 2f);
		});
	}

	private Model createRectModel( ) {
		ModelBuilder builder = new ModelBuilder();
		BlendingAttribute highlightBlend = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Material material = new Material(highlightBlend);
		return builder.createRect(
				0, 0, 1,
				1, 0, 1,
				1, 0, 0,
				0, 0, 0,
				0, 1, 0,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
		);
	}

	@Override
	public void dispose( ) {
		tileModel.dispose();
		gridModel.dispose();
	}
}
