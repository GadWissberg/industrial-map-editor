package com.industrial.editor.handlers.action;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.model.elements.PlacedEnvObject;
import com.industrial.editor.model.node.FlatNode;
import com.industrial.editor.model.node.NodeWallsDefinitions;

import java.util.Set;

public interface ActionsHandler {
	void beginTilePlacingProcess(final GameAssetsManager assetsManager, final Set<MapNodeData> initializedTiles);

	boolean beginSelectingTileForLiftProcess(int direction, Set<MapNodeData> initializedTiles);

	void beginSelectingTilesForWallTiling();

	void placeEnvObject(GameAssetsManager assetsManager);

	void defineSelectedEnvObject();

	void placeLight(GameAssetsManager assetsManager);

	void placeCharacter(GameAssetsManager assetsManager);

	void placePickup(GameAssetsManager assetsManager);


	MappingProcess<? extends MappingProcess.FinishProcessParameters> getCurrentProcess();

	void onNodeWallsDefined(NodeWallsDefinitions definitions,
							FlatNode src,
							FlatNode dst);

	void onTilesLift(FlatNode src, FlatNode dst, float value);

	void onEnvObjectDefined(PlacedEnvObject element, float height);

	boolean onTouchDown(GameAssetsManager assetsManager, Set<MapNodeData> placedTiles, int button);

	boolean onTouchUp(Assets.SurfaceTextures selectedTile, Model cursorTileModel);

	void placeTrigger(GameAssetsManager assetsManager);
}
