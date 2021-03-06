package com.industrial.editor;


import com.gadarts.industrial.shared.assets.Assets;
import com.gadarts.industrial.shared.model.ModelElementDefinition;
import com.gadarts.industrial.shared.model.characters.CharacterDefinition;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.pickups.ItemDefinition;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.ViewModes;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.model.elements.PlacedEnvObject;
import com.industrial.editor.model.node.FlatNode;
import com.industrial.editor.model.node.NodeWallsDefinitions;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public interface MapRenderer {
	int CLOCKWISE = -1;
	int COUNTER_CLOCKWISE = 1;

	void onTileSelected(Assets.SurfaceTextures texture);

	void onEditModeSet(EditModes mode);

	void onTreeCharacterSelected(CharacterDefinition definition);

	void onSelectedObjectRotate(int direction);

	void onTreeEnvSelected(ModelElementDefinition env);

	void onTreePickupSelected(ItemDefinition definition);

	void onViewModeSet(ViewModes mode);

	void onSaveMapRequested(String path);

	void onNewMapRequested();

	void onLoadMapRequested(String path) throws IOException;

	void onToolSet(EditorTool tool);

	void onNodeWallsDefined(NodeWallsDefinitions definitions, FlatNode row, FlatNode col);

	void onTilesLift(FlatNode src, FlatNode dst, float value);

	float getAmbientLightValue();

	void onAmbientLightValueSet(float value);

	void onEnvObjectDefined(PlacedEnvObject element, float height);

	void onMapSizeSet(int width, int depth);

	Dimension getMapSize();

	List<MapNodeData> getRegion(FlatNode src, FlatNode dst);
}
