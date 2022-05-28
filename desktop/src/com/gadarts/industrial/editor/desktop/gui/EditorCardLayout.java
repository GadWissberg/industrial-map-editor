package com.gadarts.industrial.editor.desktop.gui;

import lombok.Getter;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Getter
public class EditorCardLayout extends CardLayout {
	private final Set<String> cards = new HashSet<>();

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		super.addLayoutComponent(comp, constraints);
		cards.add((String) constraints);
	}
}
