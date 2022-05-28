package com.industrial.editor;

import com.industrial.editor.actions.ActionAnswer;
import com.industrial.editor.model.elements.PlacedElement;
import com.industrial.editor.model.elements.PlacedEnvObject;
import com.industrial.editor.model.node.FlatNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapEditorEventsNotifier {
	private final Set<MapManagerEventsSubscriber> subscribers = new HashSet<>();

	public void subscribeForEvents(final MapManagerEventsSubscriber subscriber) {
		subscribers.add(subscriber);
	}

	public void tilesSelectedForLifting(final FlatNode src, final int dstRow, final int dstCol) {
		subscribers.forEach(subscriber -> subscriber.onTilesSelectedForLifting(src.getRow(), src.getCol(), dstRow, dstCol));
	}

	public void tilesSelectedForTiling(final FlatNode src, final int dstRow, final int dstCol) {
		subscribers.forEach(subscriber -> subscriber.onTileSelectedUsingWallTilingTool(new FlatNode(src.getRow(), src.getCol()), new FlatNode(dstRow, dstCol)));
	}

	public void nodeSelectedToSelectObjectsInIt(final List<? extends PlacedElement> elementsInTheNode,
												final ActionAnswer<PlacedElement> answer) {
		subscribers.forEach(subscriber -> subscriber.onNodeSelectedToSelectPlacedObjectsInIt(elementsInTheNode, answer));
	}

	public void selectedEnvObjectToDefine(final PlacedEnvObject data) {
		subscribers.forEach(subscriber -> subscriber.onSelectedEnvObjectToDefine(data));
	}

	public void rendererIsReady( ) {
		subscribers.forEach(MapManagerEventsSubscriber::onMapRendererIsReady);
	}
}
