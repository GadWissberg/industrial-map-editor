package com.industrial.editor.mode;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.MapJsonKeys;
import com.gadarts.industrial.shared.assets.declarations.EnvironmentObjectDeclaration;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.actions.processes.MappingProcess;
import com.industrial.editor.handlers.SelectionHandler;
import com.industrial.editor.handlers.action.ActionsHandler;
import com.industrial.editor.mode.events.*;
import com.industrial.editor.mode.tools.EditorTool;
import com.industrial.editor.mode.tools.ElementTools;
import com.industrial.editor.mode.tools.TilesTools;
import com.industrial.editor.model.elements.*;
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
			true,
			ElementTools.values(),
			new CharactersOnTouchDownLeftEvent()),

	ENVIRONMENT("Environment Objects Mode",
			(params, assetsManager) -> new PlacedEnvObject(
					new PlacedModelElement.PlacedModelElementParameters(params),
					assetsManager),
			ElementTools.values(),
			new EnvOnTouchDownEventLeft(),
			(jsonObject, def) -> {
				EnvironmentObjectDeclaration definition = (EnvironmentObjectDeclaration) def.getDefinition();
				String envType = definition.getEnvironmentObjectType().name().toLowerCase();
				jsonObject.addProperty(MapJsonKeys.ENV_TYPE, envType);
			}),

	PICKUPS("Pickups Mode",
			(params, am) -> new PlacedPickup(new PlacedModelElement.PlacedModelElementParameters(params), am),
			ElementTools.values(),
			new PickupsOnTouchDownEventLeft()),

	LIGHTS("Lights Mode",
			true,
			new LightsOnTouchDownEventLeft(),
			false,
			(jsonObject, placedElement) -> {
				PlacedLight placedLight = (PlacedLight) placedElement;
				jsonObject.addProperty(MapJsonKeys.RADIUS, placedLight.getRadius());
				jsonObject.addProperty(MapJsonKeys.INTENSITY, placedLight.getIntensity());
			}),

	TRIGGERS("Triggers Mode",
			true,
			PlacedTrigger::new,
			new TriggersOnTouchDownEventLeft(),
			true);
	private final String displayName;
	private final boolean decalCursor;
	private final PlacedElementCreation creationProcess;
	private final boolean skipGenericElementLoading;
	private final EditorTool[] tools;
	private final OnTouchDownLeftEvent onTouchDownLeft;
	private final AdditionalDeflationProcess additionalDeflationProcess;
	private final boolean heightDefinedByNode;

	EditModes(String displayName,
			  boolean decalCursor,
			  OnTouchDownLeftEvent onTouchDownLeftEvent,
			  boolean heightDefinedByNode,
			  AdditionalDeflationProcess additionalDeflationProcess) {
		this(displayName,
				decalCursor,
				null,
				false,
				null,
				onTouchDownLeftEvent,
				additionalDeflationProcess,
				heightDefinedByNode);
	}

	EditModes(String displayName,
			  boolean decalCursor,
			  PlacedElementCreation creation,
			  OnTouchDownLeftEvent onTouchDownLeftEvent,
			  boolean heightDefinedByNode) {
		this(displayName,
				decalCursor,
				creation,
				false,
				null,
				onTouchDownLeftEvent,
				null,
				heightDefinedByNode);
	}

	EditModes(String displayName,
			  PlacedElementCreation creationProcess,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent,
			  AdditionalDeflationProcess additionalDeflationProcess) {
		this(displayName,
				false,
				creationProcess,
				false,
				tools,
				onTouchDownLeftEvent,
				additionalDeflationProcess,
				false);
	}

	EditModes(String displayName,
			  boolean skipGenericElementLoading,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				false,
				null,
				skipGenericElementLoading,
				tools,
				onTouchDownLeftEvent,
				null,
				false);
	}

	EditModes(String displayName,
			  PlacedElementCreation creation,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				false,
				creation,
				false,
				tools,
				onTouchDownLeftEvent,
				null,
				false);
	}

	EditModes(String displayName,
			  boolean decalCursor,
			  PlacedElementCreation creation,
			  boolean skipGenericElementLoading,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				decalCursor,
				creation,
				skipGenericElementLoading,
				tools,
				onTouchDownLeftEvent,
				null,
				false);
	}


	@Override
	public void onTouchDownLeft(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
								ActionsHandler actionsHandler,
								GameAssetManager assetsManager,
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
