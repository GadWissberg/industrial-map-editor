package com.gadarts.industrial.editor.desktop.gui.toolbar.sub;

import com.gadarts.industrial.editor.desktop.gui.commands.editing.RotateSelectionClockwiseCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.editing.RotateSelectionCounterClockwiseCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.tools.SetBrushToolCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.tools.SetEnvDefineToolCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.tools.SetLiftToolCommand;
import com.gadarts.industrial.editor.desktop.gui.commands.tools.SetWallTilingToolCommand;
import com.gadarts.industrial.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.industrial.editor.mode.EditModes;
import com.industrial.editor.mode.EditorMode;
import lombok.Getter;

import java.util.Arrays;



@Getter
public enum SubToolbarsDefinitions {
	EMPTY(null),

	TILES(EditModes.TILES,
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"brush",
							"Place Tiles",
							SetBrushToolCommand.class,
							EditModes.TILES.name())),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"staircase",
							"Set Tile Height",
							SetLiftToolCommand.class,
							EditModes.TILES.name())),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"wall",
							"Tile Walls",
							SetWallTilingToolCommand.class,
							EditModes.TILES.name()))),

	CHARACTERS(EditModes.CHARACTERS,
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_clockwise",
							"Rotate Character Clock-Wise",
							RotateSelectionClockwiseCommand.class)),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_counter_clockwise",
							"Rotate Character Counter Clock-Wise",
							RotateSelectionCounterClockwiseCommand.class))),

	ENVIRONMENT(EditModes.ENVIRONMENT,
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"brush",
							"Place environment objects",
							SetBrushToolCommand.class,
							EditModes.ENVIRONMENT.name())),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"define",
							"Define a placed environment object",
							SetEnvDefineToolCommand.class,
							EditModes.ENVIRONMENT.name())),
			new SubToolbarButtonDefinition(),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_clockwise",
							"Rotate Object Clock-Wise",
							RotateSelectionClockwiseCommand.class)),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_counter_clockwise",
							"Rotate Object Counter Clock-Wise",
							RotateSelectionCounterClockwiseCommand.class)));
	private final SubToolbarButtonDefinition[] buttons;
	private final EditModes mode;

	SubToolbarsDefinitions(final EditModes mode, final SubToolbarButtonDefinition... buttons) {
		this.buttons = buttons;
		this.mode = mode;
	}

	public static SubToolbarsDefinitions findByMode(final EditorMode mode) {
		return Arrays.stream(SubToolbarsDefinitions.values())
				.filter(def -> def.mode == mode)
				.findFirst()
				.orElse(null);
	}
}
