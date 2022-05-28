package com.gadarts.industrial.editor.desktop;

import com.gadarts.industrial.shared.assets.Assets;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;

import static java.awt.Image.SCALE_SMOOTH;

@Getter
public class GalleryButton extends JToggleButton {
	private static final Color COLOR_SELECTED = Color.decode("0x14a9ff");
	private static final Color BACKGROUND_COLOR = Color.decode("0x45433B");
	private Assets.SurfaceTextures textureDefinition;

	public GalleryButton(final Assets.SurfaceTextures textureDefinition, final ImageIcon imageIcon) {
		super(imageIcon);
		this.textureDefinition = textureDefinition;
		defineButton(textureDefinition);
	}

	private void defineButton(final Assets.SurfaceTextures textureDefinition) {
		String name = textureDefinition.getName();
		setBorderPainted(false);
		setToolTipText(name);
		setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor( ) {
				return COLOR_SELECTED;
			}
		});
		setBackground(BACKGROUND_COLOR);
	}

	public void applyTexture(Assets.SurfaceTextures texture, ImageIcon imageIcon) {
		Dimension preferredSize = getPreferredSize();
		int iconWidth = imageIcon.getIconWidth();
		int iconHeight = imageIcon.getIconHeight();
		if (iconWidth > preferredSize.width || iconHeight > preferredSize.height) {
			imageIcon = scaleDownProportionally(imageIcon, preferredSize, iconWidth, iconHeight);
		}
		setIcon(imageIcon);
		textureDefinition = texture;
	}

	private ImageIcon scaleDownProportionally(ImageIcon imageIcon,
											  Dimension preferredSize,
											  int iconWidth,
											  int iconHeight) {
		int bigger = Math.max(iconWidth, iconHeight);
		int ratio = bigger / preferredSize.width;
		Image image = imageIcon.getImage();
		Image scaled = image.getScaledInstance(iconWidth / ratio, iconHeight / ratio, SCALE_SMOOTH);
		imageIcon = new ImageIcon(scaled);
		return imageIcon;
	}
}
