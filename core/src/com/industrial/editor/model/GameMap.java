package com.industrial.editor.model;

import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.map.MapNodesTypes;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.stream.IntStream;

@Getter
@Setter
public class GameMap {
	private static final int DEFAULT_LEVEL_SIZE = 20;
	private Dimension dimension;
	private float ambientLight;
	private MapNodeData[][] nodes;

	public GameMap( ) {
		initialize();
	}

	private void initialize( ) {
		dimension = new Dimension(DEFAULT_LEVEL_SIZE, DEFAULT_LEVEL_SIZE);
		createNodesMatrix(dimension);
		ambientLight = 1;
	}

	private void createNodesMatrix(final Dimension dimension) {
		nodes = new MapNodeData[dimension.height][dimension.width];
		IntStream.range(0, dimension.height).forEach(row ->
				IntStream.range(0, dimension.width)
						.forEach(col -> nodes[row][col] = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE)));
	}

	public void resetSize(final Dimension dimension) {
		MapNodeData[][] newNodes = new MapNodeData[dimension.height][dimension.width];
		int minWidth = Math.min(newNodes[0].length, nodes[0].length);
		int minDepth = Math.min(newNodes.length, nodes.length);
		IntStream.range(0, minDepth).forEach(row ->
				IntStream.range(0, minWidth).forEach(col -> newNodes[row][col] = nodes[row][col]));
		nodes = newNodes;
	}

	public void reset( ) {
		initialize();
	}
}
