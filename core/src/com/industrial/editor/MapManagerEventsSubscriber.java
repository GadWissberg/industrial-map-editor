package com.industrial.editor;

import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.ActionAnswer;
import com.industrial.editor.model.elements.PlacedElement;
import com.industrial.editor.model.elements.PlacedEnvObject;
import com.industrial.editor.model.elements.PlacedLight;
import com.industrial.editor.model.node.FlatNode;

import java.util.List;

public interface MapManagerEventsSubscriber {
	void onTileSelectedUsingWallTilingTool(FlatNode src, FlatNode dst);

	void onTilesSelectedForLifting(int srcRow, int srcCol, int dstRow, int dstCol);

	void onNodeSelectedToSelectPlacedObjectsInIt(List<? extends PlacedElement> elementsInTheNode,
												 ActionAnswer<PlacedElement> answer);

	void onSelectedEnvObjectToDefine(PlacedEnvObject data);

	void onMapRendererIsReady( );

	void onSelectedNodeToPlaceLight(MapNodeData nodeData, PlacedLight lightInNode);
}
