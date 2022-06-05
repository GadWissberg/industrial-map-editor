package com.industrial.editor.mode;

import com.gadarts.industrial.shared.assets.GameAssetsManager;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.env.ThingsDefinitions;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.pickups.WeaponsDefinitions;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.mode.events.*;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.ElementTools;
import com.industrial.editor.mode.tools.TilesTools;
import com.industrial.editor.model.elements.*;
import com.industrial.editor.handlers.action.ActionsHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum EditModes implements EditorMode {
	TILES("Tiles Mode", true, TilesTools.values(), new TilesOnTouchDownLeftEvent()),

	CHARACTERS("Characters Mode",
			true,
			PlacedCharacter::new,
			null,
			true,
			ElementTools.values(),
			new CharactersOnTouchDownLeftEvent()),

	ENVIRONMENT("Environment Objects Mode",
			ThingsDefinitions.values(),
			(params, assetsManager) -> new PlacedEnvObject(
					new PlacedModelElement.PlacedModelElementParameters(params),
					assetsManager),
			ElementTools.values(),
			new EnvOnTouchDownEventLeft()),

	PICKUPS("Pickups Mode", WeaponsDefinitions.values(),
			(params, am) -> new PlacedPickup(new PlacedModelElement.PlacedModelElementParameters(params), am),
			ElementTools.values(),
			new PickupsOnTouchDownEventLeft()),

	LIGHTS("Lights Mode", true,
			PlacedLight::new,
			new LightsOnTouchDownEventLeft());
	private final String displayName;
	private final boolean decalCursor;
	private final PlacedElementCreation creationProcess;
	private final ElementDefinition[] definitions;
	private final boolean skipGenericElementLoading;
	private final EditorTool[] tools;
	private final OnTouchDownLeftEvent onTouchDownLeft;

	EditModes(String displayName,
			  boolean decalCursor,
			  PlacedElementCreation creation,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				decalCursor,
				creation,
				null,
				false,
				null,
				onTouchDownLeftEvent);
	}

	EditModes(String displayName,
			  boolean skipGenericElementLoading,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				false,
				null,
				null,
				skipGenericElementLoading,
				tools,
				onTouchDownLeftEvent);
	}

	EditModes(String displayName,
			  ElementDefinition[] definitions,
			  PlacedElementCreation creation,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				false,
				creation,
				definitions,
				false,
				tools,
				onTouchDownLeftEvent);
	}


	@Override
	public void onTouchDownLeft(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
								ActionsHandler actionsHandler,
								GameAssetsManager assetsManager,
								Set<MapNodeData> initializedTiles,
								SelectionHandler selectionHandler) {
		onTouchDownLeft.run(currentProcess, actionsHandler, assetsManager, initializedTiles, selectionHandler);
	}

	@Override
	public String getDisplayName( ) {
		return displayName;
	}

	@Override
	public ModeType getType( ) {
		return ModeType.EDIT;
	}

}
