package com.industrial.editor.actions.processes;

import com.gadarts.industrial.shared.WallCreator;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.industrial.editor.MapEditorEventsNotifier;
import com.industrial.editor.model.GameMap;
import com.industrial.editor.model.node.FlatNode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class SelectTilesForLiftProcess extends MappingProcess<SelectTilesForLiftFinishProcessParameters> {
    private int direction;
    private WallCreator wallCreator;
    private Set<MapNodeData> initializedTiles;

    public SelectTilesForLiftProcess(final GameMap map, final FlatNode src) {
        super(map, src, true);
    }

    @Override
    public boolean isProcess() {
        return false;
    }

	@Override
	public void execute(final MapEditorEventsNotifier eventsNotifier) {

	}

    @Override
    public void finish(final SelectTilesForLiftFinishProcessParameters params) {
        params.getNotifier().tilesSelectedForLifting(srcNode, params.getDstRow(), params.getDstCol());
    }
}
