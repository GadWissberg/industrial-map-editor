package com.industrial.editor.handlers.render;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;

@Getter
public class RenderHandlerBatches implements Disposable {
	private static final int DECALS_POOL_SIZE = 200;
	private ModelBatch modelBatch;
	private DecalBatch decalBatch;

	@Override
	public void dispose( ) {
		modelBatch.dispose();
		decalBatch.dispose();
	}

	void createBatches(CameraGroupStrategy groupStrategy) {
		this.decalBatch = new DecalBatch(DECALS_POOL_SIZE, groupStrategy);
		this.modelBatch = new ModelBatch();
	}
}
