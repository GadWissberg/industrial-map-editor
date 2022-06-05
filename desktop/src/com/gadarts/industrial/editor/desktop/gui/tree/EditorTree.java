package com.gadarts.industrial.editor.desktop.gui.tree;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class EditorTree extends JTree {
	public EditorTree(final DefaultMutableTreeNode top) {
		this(top, false);

	}

	@Override
	public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	public EditorTree(final DefaultMutableTreeNode top, final boolean alwaysOpen) {
		super(top);
		setExpandsSelectedPaths(true);
		expandPath(new TreePath(top.getPath()));
		addTreeExpansionListener(new TreeExpansionListener() {
			@Override
			public void treeExpanded(final TreeExpansionEvent event) {
			}

			@Override
			public void treeCollapsed(final TreeExpansionEvent event) {
				EditorTree editorTree = EditorTree.this;
				for (int i = 0; i < (alwaysOpen ? editorTree.getRowCount() : 1); i++) {
					editorTree.expandRow(i);
				}
			}
		});
	}
}
