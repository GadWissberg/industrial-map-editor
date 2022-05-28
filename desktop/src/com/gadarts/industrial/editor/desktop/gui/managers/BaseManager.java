package com.gadarts.industrial.editor.desktop.gui.managers;

import com.industrial.editor.MapRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class BaseManager {
	private final MapRenderer mapRenderer;

}
