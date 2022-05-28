package com.industrial.editor.actions.processes;

import com.industrial.editor.MapEditorEventsNotifier;
import lombok.Getter;

@Getter
public class SelectTilesForWallTilingFinishProcessParameters extends SelectTilesFinishProcessParameters {
	private final MapEditorEventsNotifier notifier;

	public SelectTilesForWallTilingFinishProcessParameters(final int dstRow,
														   final int dstCol,
														   final MapEditorEventsNotifier notifier) {
		super(dstRow, dstCol);
		this.notifier = notifier;
	}
}

