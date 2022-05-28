package com.industrial.editor.actions.processes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SelectTilesFinishProcessParameters extends MappingProcess.FinishProcessParameters {
	private final int dstRow;
	private final int dstCol;
}
