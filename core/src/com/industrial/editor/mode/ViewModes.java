package com.industrial.editor.mode;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.utils.Utils;
import com.industrial.editor.CameraManipulation;
import com.industrial.editor.handlers.action.ActionsHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum ViewModes implements EditorMode {
	PAN("Pan Camera",
			(lastMouseTouchPosition, camera, screenX, screenY, vector3) -> {
				Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.03f);
				Vector3 left = Utils.auxVector3_1.set(camera.direction).crs(camera.up).nor().scl(0.3f);
				float x = camera.direction.x * -velocity.y + left.x * velocity.x;
				float z = camera.direction.z * -velocity.y + left.z * velocity.x;
				camera.translate(x, 0, z);
			}),

	ROTATE("Rotate Camera",
			(lastMouseTouchPosition, camera, screenX, screenY, rotationPoint) -> {
				Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.12f);
				camera.rotateAround(rotationPoint, Vector3.Y, velocity.x);
			}),

	ZOOM("Zoom Camera",
			(lastMouseTouchPosition, camera, screenX, screenY, vector3) -> {
				Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.005f);
				camera.zoom = Math.min(Math.max(0.2f, camera.zoom + velocity.y), 2f);
			});

	private final String displayName;
	private final CameraManipulation manipulation;

	@Override
	public void onTouchDownLeft(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
								final ActionsHandler tool,
								final GameAssetsManager actionsHandler,
								final Set<MapNodeData> initializedTiles, SelectionHandler selectionHandler) {

	}

	@Override
	public String getDisplayName( ) {
		return displayName;
	}

	@Override
	public EditorTool[] getTools( ) {
		return null;
	}

	@Override
	public ModeType getType( ) {
		return ModeType.VIEW;
	}
}
