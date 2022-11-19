package com.industrial.editor.actions.types;

import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.actions.MappingAction;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.node.FlatNode;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.Optional;
import java.util.stream.IntStream;

@Setter(AccessLevel.PACKAGE)
public class LiftNodesAction extends MappingAction {


	private final Parameters params;

	public LiftNodesAction(final GameMap map, final Parameters params) {
		super(map);
		this.params = params;
	}


	@Override
	public void execute(final MapEditorEventsNotifier eventsNotifier) {
		int minRow = Math.min(params.srcNode().getRow(), params.dstNode().getRow());
		int minCol = Math.min(params.srcNode().getCol(), params.dstNode().getCol());
		int maxRow = Math.max(params.srcNode().getRow(), params.dstNode().getRow());
		int maxCol = Math.max(params.srcNode().getCol(), params.dstNode().getCol());
		MapNodeData[][] t = map.getNodes();
		IntStream.rangeClosed(minRow, maxRow).forEach(row ->
				IntStream.rangeClosed(minCol, maxCol).forEach(col ->
						Optional.ofNullable(t[row][col]).ifPresent(n -> {
							if (n.getTextureDefinition() != null) {
								n.applyHeight(params.value());
							}
						})));
		IntStream.rangeClosed(minRow, maxRow).forEach(row ->
				IntStream.rangeClosed(minCol, maxCol).forEach(col ->
						Optional.ofNullable(t[row][col]).ifPresent(n -> adjustWalls(t, row, col, n))));
	}

	private void adjustWalls(final MapNodeData[][] t, final int row, final int col, final MapNodeData n) {
		if (row > 0) {
			Optional.ofNullable(t[row - 1][col]).ifPresent(north -> adjustWallBetweenNorthAndSouthNodes(north, n));
		}
		if (col < t[0].length - 1) {
			Optional.ofNullable(t[row][col + 1]).ifPresent(east -> adjustWallBetweenEastAndWestNodes(n, east));
		}
		if (row < t.length - 1) {
			Optional.ofNullable(t[row + 1][col]).ifPresent(south -> adjustWallBetweenNorthAndSouthNodes(n, south));
		}
		if (col > 0) {
			Optional.ofNullable(t[row][col - 1]).ifPresent(west -> adjustWallBetweenEastAndWestNodes(west, n));
		}
	}

	private void adjustWallBetweenEastAndWestNodes(MapNodeData westernNode,
												   MapNodeData easternNode) {
		if (westernNode.getHeight() > easternNode.getHeight()) {
			westernNode.getWalls().setEastWall(null);
			params.wallCreator().adjustWestWall(westernNode, easternNode);
		} else if (westernNode.getHeight() < easternNode.getHeight()) {
			easternNode.getWalls().setWestWall(null);
			params.wallCreator().adjustEastWall(westernNode, easternNode);
		} else {
			westernNode.getWalls().setEastWall(null);
			easternNode.getWalls().setWestWall(null);
		}
	}

	private void adjustWallBetweenNorthAndSouthNodes(MapNodeData northernNode,
													 MapNodeData southernNode) {
		if (northernNode.getHeight() > southernNode.getHeight()) {
			northernNode.getWalls().setSouthWall(null);
			params.wallCreator().adjustNorthWall(southernNode, northernNode);
		} else if (northernNode.getHeight() < southernNode.getHeight()) {
			southernNode.getWalls().setNorthWall(null);
			params.wallCreator().adjustSouthWall(southernNode, northernNode);
		} else {
			southernNode.getWalls().setNorthWall(null);
			northernNode.getWalls().setSouthWall(null);
		}
	}


	@Override
	public boolean isProcess( ) {
		return false;
	}

	public record Parameters(FlatNode srcNode, FlatNode dstNode, float value, WallCreator wallCreator) {
	}
}
