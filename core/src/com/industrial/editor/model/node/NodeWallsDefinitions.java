package com.industrial.editor.model.node;

import lombok.Getter;

@Getter
public class NodeWallsDefinitions {
	private final WallDefinition east;
	private final WallDefinition south;
	private final WallDefinition west;
	private final WallDefinition north;

	public NodeWallsDefinitions(final WallDefinition east,
								final WallDefinition south,
								final WallDefinition west,
								final WallDefinition north) {
		this.east = new WallDefinition(east.getTexture(), east.getVScale(),
				east.getHorizontalOffset(), east.getVerticalOffset());
		this.south = new WallDefinition(south.getTexture(), south.getVScale(),
				south.getHorizontalOffset(), south.getVerticalOffset());
		this.west = new WallDefinition(west.getTexture(), west.getVScale(),
				west.getHorizontalOffset(), west.getVerticalOffset());
		this.north = new WallDefinition(north.getTexture(), north.getVScale(),
				north.getHorizontalOffset(), north.getVerticalOffset());
	}
}
