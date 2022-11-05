package com.industrial.editor.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;

@Getter
public class AxisModelHandler implements Disposable {
	private final static Vector3 auxVector3_1 = new Vector3();
	private final static Vector3 auxVector3_2 = new Vector3();

	private ModelInstance axisModelInstanceX;
	private ModelInstance axisModelInstanceY;
	private ModelInstance axisModelInstanceZ;
	private Model axisModelX;
	private Model axisModelY;
	private Model axisModelZ;

	public void createAxis( ) {
		ModelBuilder modelBuilder = new ModelBuilder();
		axisModelX = createAxisModel(modelBuilder, auxVector3_2.set(1, 0, 0), Color.RED);
		axisModelInstanceX = new ModelInstance(axisModelX);
		axisModelY = createAxisModel(modelBuilder, auxVector3_2.set(0, 1, 0), Color.GREEN);
		axisModelInstanceY = new ModelInstance(axisModelY);
		axisModelZ = createAxisModel(modelBuilder, auxVector3_2.set(0, 0, 1), Color.BLUE);
		axisModelInstanceZ = new ModelInstance(axisModelZ);
		scaleAxis();
	}

	private Model createAxisModel(final ModelBuilder modelBuilder, final Vector3 dir, final Color color) {
		return modelBuilder.createArrow(
				auxVector3_1.setZero(),
				dir,
				new Material(ColorAttribute.createDiffuse(color)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
		);
	}

	private void scaleAxis( ) {
		axisModelInstanceX.transform.scale(2, 2, 2);
		axisModelInstanceX.transform.translate(0, 0.2f, 0);
		axisModelInstanceY.transform.scale(2, 2, 2);
		axisModelInstanceY.transform.translate(0, 0.2f, 0);
		axisModelInstanceZ.transform.scale(2, 2, 2);
		axisModelInstanceZ.transform.translate(0, 0.2f, 0);
	}

	public void render(final ModelBatch modelBatch) {
		modelBatch.render(axisModelInstanceX);
		modelBatch.render(axisModelInstanceY);
		modelBatch.render(axisModelInstanceZ);
	}

	@Override
	public void dispose( ) {
		axisModelX.dispose();
		axisModelY.dispose();
		axisModelZ.dispose();
	}
}
