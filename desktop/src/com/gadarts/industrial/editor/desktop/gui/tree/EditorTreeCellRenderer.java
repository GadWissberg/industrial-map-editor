package com.gadarts.industrial.editor.desktop.gui.tree;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public abstract class EditorTreeCellRenderer extends DefaultTreeCellRenderer {
	public EditorTreeCellRenderer() {
		setBackgroundSelectionColor(Color.CYAN);
	}
}
