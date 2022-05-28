package com.industrial.editor.mode.tools;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TilesTools implements EditorTool {
	BRUSH(false), LIFT, WALL_TILING;

	private final boolean forceCursorDisplay;


	TilesTools( ) {
		this(true);
	}

	@Override
	public boolean isForceCursorDisplay( ) {
		return forceCursorDisplay;
	}
}
