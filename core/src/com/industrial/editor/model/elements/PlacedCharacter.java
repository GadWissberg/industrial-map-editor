package com.industrial.editor.model.elements;

import com.gadarts.industrial.shared.assets.GameAssetManager;
import com.gadarts.industrial.shared.assets.declarations.characters.CharacterDeclaration;
import com.industrial.editor.utils.Utils;
import com.industrial.editor.model.node.FlatNode;
import lombok.Getter;

import static com.gadarts.industrial.shared.model.characters.CharacterTypes.BILLBOARD_Y;

@Getter
public class PlacedCharacter extends PlacedElement {

	private final CharacterDecal characterDecal;

	public PlacedCharacter(final PlacedElementParameters parameters, final GameAssetManager assetsManager) {
		super(parameters);
		this.characterDecal = Utils.createCharacterDecal(
				assetsManager,
				(CharacterDeclaration) parameters.getDeclaration(),
				new FlatNode(parameters.getNode()),
				parameters.getFacingDirection());
	}

	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		characterDecal.getDecal().getPosition().y = height + BILLBOARD_Y;
	}
}
