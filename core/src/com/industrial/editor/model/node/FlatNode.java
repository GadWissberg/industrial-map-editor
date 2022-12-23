package com.industrial.editor.model.node;

import com.gadarts.industrial.shared.model.map.MapNodeData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FlatNode {
	public static final int MAX_HEIGHT = 10;

	private final int row;
	private final int col;
	private final float height;

	public FlatNode(final int row, final int col) {
		this.row = row;
		this.col = col;
		this.height = 0;
	}

	public FlatNode(final MapNodeData node) {
		this.row = node.getCoords().getRow();
		this.col = node.getCoords().getCol();
		this.height = node.getHeight();
	}

	public boolean equals(final int row, final int col) {
		return this.row == row && this.col == col;
	}


	public boolean equals(final MapNodeData node) {
		return equals(node.getCoords().getRow(), node.getCoords().getCol());
	}
}
