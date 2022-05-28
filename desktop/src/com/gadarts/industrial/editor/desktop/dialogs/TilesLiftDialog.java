package com.gadarts.industrial.editor.desktop.dialogs;

import com.gadarts.industrial.editor.desktop.GuiUtils;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.model.node.FlatNode;

import javax.swing.*;
import java.util.List;

import static com.industrial.editor.model.node.FlatNode.MAX_HEIGHT;

public class TilesLiftDialog extends DialogPane {
	static final float STEP = 0.1f;
	private static final String LABEL_HEIGHT = "Height: ";
	private final FlatNode src;
	private final FlatNode dst;
	private final MapRenderer mapRenderer;

	public TilesLiftDialog(final FlatNode src, final FlatNode dst, final MapRenderer mapRenderer) {
		this.src = src;
		this.dst = dst;
		this.mapRenderer = mapRenderer;
		init();
	}

	@Override
	void initializeView( ) {
		JSpinner spinner = addSpinnerWithLabel(LABEL_HEIGHT, createHeightSpinner());
		addGeneralButtons(e -> {
			float value = ((Double) spinner.getValue()).floatValue();
			if (value >= 0) {
				mapRenderer.onTilesLift(src, dst, value);
			}
			closeDialog();
		});
	}


	private JSpinner createHeightSpinner( ) {
		List<MapNodeData> nodes = mapRenderer.getRegion(src, dst);
		float value = -1;
		if (nodes.stream().allMatch(n -> n.getHeight() == nodes.get(0).getHeight())) {
			value = nodes.get(0).getHeight();
		}
		return GuiUtils.createSpinner(value, -1, MAX_HEIGHT, STEP, false, SPINNER_WIDTH);
	}

	@Override
	public String getDialogTitle( ) {
		return "Lift Tiles";
	}
}
