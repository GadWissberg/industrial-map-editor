package com.gadarts.industrial.editor.desktop.gui;

import com.gadarts.industrial.shared.model.ElementDefinition;

public record TreeSection(String header, ElementDefinition[] definitions, String entryIcon) {
}
