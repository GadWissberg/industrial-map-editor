package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.model.ElementDefinition;
import com.gadarts.industrial.shared.model.characters.Direction;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.gadarts.industrial.shared.model.characters.Direction.SOUTH;

@Getter
public class PlacedElement {

	private final ElementDefinition definition;
	private final Direction facingDirection;
	private final MapNodeData node;
	@Setter
	private float height;

	public PlacedElement(final PlacedElementParameters parameters) {
		this.definition = parameters.getDefinition();
		this.facingDirection = parameters.getFacingDirection();
		this.node = parameters.getNode();
		this.height = parameters.getHeight();
	}

	@Override
	public String toString() {
		return definition.getDisplayName();
	}

	@RequiredArgsConstructor
	@Getter
	public static class PlacedElementParameters {
		protected final ElementDefinition definition;
		private final Direction facingDirection;
		private final MapNodeData node;
		private final float height;

		public PlacedElementParameters(final ElementDefinition definition, final MapNodeData node, final float height) {
			this.definition = definition;
			this.node = node;
			this.height = height;
			this.facingDirection = SOUTH;
		}
	}
}
