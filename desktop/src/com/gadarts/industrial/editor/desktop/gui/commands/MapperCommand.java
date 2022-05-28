package com.gadarts.industrial.editor.desktop.gui.commands;

import com.gadarts.industrial.editor.desktop.gui.managers.Managers;
import com.industrial.editor.MapRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.ActionListener;

@RequiredArgsConstructor
@Getter
public abstract class MapperCommand implements ActionListener {
	private final MapRenderer mapRenderer;
	private final Managers managers;

}
