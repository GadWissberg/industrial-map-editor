package com.industrial.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.industrial.shared.model.characters.CharacterDeclaration;
import com.gadarts.industrial.shared.model.characters.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CharacterDecal {

	private final Decal decal;

	@Setter
	private CharacterDeclaration characterDefinition;

	@Setter
	private Direction spriteDirection;

}
