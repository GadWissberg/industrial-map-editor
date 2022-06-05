package com.gadarts.industrial.editor.desktop.gui;

import com.gadarts.industrial.editor.desktop.gui.dialogs.DialogPane;
import com.gadarts.industrial.shared.assets.Assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

public final class GuiUtils {

	private static final int GALLERY_VIEW_WIDTH = 420;

	public static GalleryButton createTextureImageButton(final File assetsFolderLocation,
														 final Assets.SurfaceTextures texture) throws IOException {
		return createTextureImageButton(assetsFolderLocation, texture, null);
	}

	static GalleryButton createTextureImageButton(final File assetsFolderLocation,
												  final Assets.SurfaceTextures texture,
												  final ItemListener onClick) throws IOException {
		ImageIcon imageIcon = loadImage(assetsFolderLocation, texture);
		GalleryButton button = new GalleryButton(texture, imageIcon);
		button.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		button.setMaximumSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight() + 40));
		Optional.ofNullable(onClick).ifPresent(click -> button.addItemListener(onClick));
		return button;
	}

	public static ImageIcon loadImage(final File assetsFolderLocation, final Assets.SurfaceTextures texture) throws IOException {
		String path = assetsFolderLocation.getAbsolutePath() + File.separator + texture.getFilePath();
		FileInputStream inputStream = new FileInputStream(path);
		ImageIcon imageIcon = new ImageIcon(ImageIO.read(inputStream));
		inputStream.close();
		return imageIcon;
	}

	public static JScrollPane createEntitiesGallery(final File assetsFolderLoc, final ItemListener onClick) {
		GridLayout layout = new GridLayout(0, 3);
		JPanel gallery = new JPanel(layout);
		JScrollPane jScrollPane = new JScrollPane(gallery);

		ButtonGroup buttonGroup = new ButtonGroup();
		Assets.SurfaceTextures[] surfaceTextures = Assets.SurfaceTextures.values();
		IntStream.range(0, surfaceTextures.length).forEach(i -> {
			try {
				GalleryButton button = GuiUtils.createTextureImageButton(assetsFolderLoc, surfaceTextures[i], onClick);
				buttonGroup.add(button);
				gallery.add(button);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});

		gallery.setPreferredSize(new Dimension(GALLERY_VIEW_WIDTH, gallery.getPreferredSize().height));
		jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setPreferredSize(new Dimension(jScrollPane.getPreferredSize().width, 480));
		return jScrollPane;
	}

	public static void openNewDialog(final Component parent, final DialogPane content) {
		JDialog d = new JDialog(SwingUtilities.getWindowAncestor(parent));
		d.setTitle(content.getDialogTitle());
		d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		d.setContentPane(content);
		d.setResizable(false);
		d.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		d.setLocation((screenSize.width) / 2 - d.getWidth() / 2, (screenSize.height) / 2 - d.getHeight() / 2);
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		d.setVisible(true);
	}

	public static Component findByNameInPanel(final JPanel container, final String name) {
		return Arrays.stream(container.getComponents())
				.filter(component -> component.getName() != null && component.getName().equals(name))
				.findFirst().orElse(null);
	}

	public static JSpinner createSpinner(double value,
										 int minimum,
										 int maximum,
										 float step,
										 boolean allowNegative,
										 int spinnerWidth) {
		SpinnerModel model = new SpinnerNumberModel(value, minimum, maximum, step);
		model.setValue(value);
		JSpinner jSpinner = new JSpinner(model);
		Dimension preferredSize = jSpinner.getPreferredSize();
		jSpinner.setPreferredSize(new Dimension(spinnerWidth, preferredSize.height));
		jSpinner.addChangeListener(e -> {
			if (!allowNegative && ((Double) jSpinner.getValue()) < 0) {
				jSpinner.setValue(0.0);
			}
		});
		return jSpinner;
	}
}
