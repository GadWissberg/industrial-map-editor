package com.gadarts.industrial.editor.desktop.gui.dialogs;

import com.gadarts.industrial.editor.desktop.gui.GuiUtils;
import com.gadarts.industrial.editor.desktop.gui.GalleryButton;
import com.gadarts.industrial.shared.assets.Assets;

import java.io.File;

public class TexturesGalleryDialog extends DialogPane {
	private final File assetsFolderLocation;
	private final OnTextureSelected onTextureSelected;

	public TexturesGalleryDialog(final File assetsFolderLocation, final OnTextureSelected onTextureSelected) {
		this.assetsFolderLocation = assetsFolderLocation;
		this.onTextureSelected = onTextureSelected;
		init();
	}

	@Override
	void initializeView( ) {
		add(GuiUtils.createEntitiesGallery(assetsFolderLocation, itemEvent -> {
			Assets.SurfaceTextures texture = ((GalleryButton) itemEvent.getItem()).getTextureDefinition();
			onTextureSelected.run(texture);
			closeDialog();
		}));
	}

	@Override
	public String getDialogTitle( ) {
		return "Select Texture";
	}

	public interface OnTextureSelected {
		void run(Assets.SurfaceTextures texture);
	}
}
