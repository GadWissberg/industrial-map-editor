package com.gadarts.industrial.editor.desktop.dialogs;

import com.industrial.editor.actions.ActionAnswer;
import com.industrial.editor.model.elements.PlacedElement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectObjectInNodeDialog extends DialogPane {
	private final ActionAnswer<PlacedElement> answer;
	private final List<? extends PlacedElement> elementsInTheNode;

	public SelectObjectInNodeDialog(final List<? extends PlacedElement> elementsInTheNode,
									final ActionAnswer<PlacedElement> answer) {
		this.elementsInTheNode = elementsInTheNode;
		this.answer = answer;
		init();
	}

	@Override
	void initializeView( ) {
		DefaultListModel<PlacedElement> jLabelDefaultListModel = new DefaultListModel<>();
		JList<PlacedElement> list = new JList<>(jLabelDefaultListModel);
		ListSelectionModel listSelectionModel = list.getSelectionModel();
		elementsInTheNode.forEach(jLabelDefaultListModel::addElement);
		add(list);
		Button ok = addGeneralButtons(e -> {
			answer.apply(elementsInTheNode.get(listSelectionModel.getSelectedIndices()[0]));
			closeDialog();
		});
		listSelectionModel.addListSelectionListener(e -> ok.setEnabled(true));
		ok.setEnabled(false);
	}

	@Override
	public String getDialogTitle() {
		return null;
	}
}
