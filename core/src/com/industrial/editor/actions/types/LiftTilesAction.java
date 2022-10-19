package com.industrial.editor.actions.types;

import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.actions.MappingAction;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.node.FlatNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.stream.IntStream;

@Setter(AccessLevel.PACKAGE)
public class LiftTilesAction extends MappingAction {


	private final Parameters params;

	public LiftTilesAction(final GameMap map, final Parameters params) {
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
			Optional.ofNullable(t[row - 1][col]).ifPresent(north -> params.wallCreator().adjustNorthWall(n, north));
		}
		if (col < t[0].length - 1) {
			Optional.ofNullable(t[row][col + 1]).ifPresent(east -> params.wallCreator().adjustEastWall(n, east));
		}
		if (row < t.length - 1) {
			Optional.ofNullable(t[row + 1][col]).ifPresent(south -> params.wallCreator().adjustSouthWall(n, south));
		}
		if (col > 0) {
			Optional.ofNullable(t[row][col - 1]).ifPresent(west -> params.wallCreator().adjustWestWall(n, west));
		}
	}


	@Override
	public boolean isProcess( ) {
		return false;
	}

	public record Parameters(FlatNode srcNode, FlatNode dstNode, float value, WallCreator wallCreator) {
	}
}
