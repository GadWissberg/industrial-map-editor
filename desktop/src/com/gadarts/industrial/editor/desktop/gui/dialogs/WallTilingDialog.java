package com.gadarts.industrial.editor.desktop.gui.dialogs;

import com.gadarts.industrial.editor.desktop.gui.GalleryButton;
import com.gadarts.industrial.editor.desktop.gui.GuiUtils;
import com.gadarts.industrial.shared.assets.Assets.SurfaceTextures;
import com.gadarts.industrial.shared.model.map.MapNodeData;
import com.gadarts.industrial.shared.model.map.Wall;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.model.node.FlatNode;
import com.industrial.editor.model.node.NodeWallsDefinitions;
import com.industrial.editor.model.node.WallDefinition;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.gadarts.industrial.editor.desktop.gui.GuiUtils.loadImage;
import static com.gadarts.industrial.shared.WallCreator.WORLD_UNIT_SIZE;
import static com.gadarts.industrial.shared.assets.Assets.SurfaceTextures.MISSING;
import static javax.swing.BorderFactory.*;

public class WallTilingDialog extends DialogPane {
	public static final int SECTION_PADDING = 10;
	private static final String LABEL_EAST = "East Wall:";
	private static final String LABEL_SOUTH = "South Wall:";
	private static final String LABEL_WEST = "West Wall:";
	private static final String LABEL_NORTH = "North Wall:";
	private final File assetsFolderLocation;
	private final MapRenderer mapRenderer;
	private final FlatNode src;
	private final FlatNode dst;
	private GalleryButton eastImageButton;
	private GalleryButton southImageButton;
	private GalleryButton westImageButton;
	private GalleryButton northImageButton;
	private WallSpinners eastWallSpinners;
	private WallSpinners northWallSpinners;
	private WallSpinners southWallSpinners;
	private WallSpinners westWallSpinners;

	public WallTilingDialog(File assetsFolderLocation, MapRenderer mapRenderer, FlatNode src, FlatNode dst) {
		this.assetsFolderLocation = assetsFolderLocation;
		this.mapRenderer = mapRenderer;
		this.src = src;
		this.dst = dst;
		init();
	}

	@Override
	void initializeView( ) {
		try {
			List<MapNodeData> nodes = mapRenderer.getRegion(src, dst);
			float maxHeight = nodes.stream().max(
							(o1, o2) -> Float.compare(o1.getHeight(), o2.getHeight()))
					.get()
					.getHeight();
			JPanel sectionsPanel = addSections(nodes, maxHeight);
			add(sectionsPanel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		addGeneralButtons(e -> {
			mapRenderer.onNodeWallsDefined(new NodeWallsDefinitions(
							createWallDef(eastImageButton, eastWallSpinners),
							createWallDef(southImageButton, southWallSpinners),
							createWallDef(westImageButton, westWallSpinners),
							createWallDef(northImageButton, northWallSpinners)),
					src,
					dst);
			closeDialog();
		});
	}

	private JPanel addSections(List<MapNodeData> nodes, float maxHeight) throws IOException {
		JPanel sectionsPanel = new JPanel();
		sectionsPanel.setLayout(new GridLayout(2, 2));
		addEastSection(sectionsPanel, decideEastInitialValues(nodes), maxHeight);
		addNorthSection(sectionsPanel, decideNorthInitialValues(nodes), maxHeight);
		addSouthSection(sectionsPanel, decideSouthInitialValues(nodes), maxHeight);
		addWestSection(sectionsPanel, decideWestInitialValues(nodes), maxHeight);
		return sectionsPanel;
	}

	private WallDefinition decideEastInitialValues(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream()
				.filter(n -> n.getWalls().getEastWall() != null)
				.map(mapNodeData -> mapNodeData.getWalls().getEastWall())
				.findFirst();
		WallDefinition initialValues = new WallDefinition(
				MISSING,
				WallSpinners.DEF_V_SCALE,
				WallSpinners.DEF_H_OFFSET, WallSpinners.DEF_V_OFFSET);

		if (def.isPresent()) {
			Wall wall = def.get();
			SurfaceTextures textureDefinition = wall.getDefinition();

			boolean allSameTexture = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> d.getWalls().getEastWall().getDefinition() == textureDefinition);
			initialValues.setTexture(allSameTexture ? textureDefinition : MISSING);

			boolean allSameVerScale = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getEastWall().getVScale(), wall.getVScale()));
			initialValues.setVScale(allSameVerScale ? wall.getVScale() : WallSpinners.DEF_V_SCALE);

			boolean allSameHorOffset = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getEastWall().getHOffset(), wall.getHOffset()));
			initialValues.setHorizontalOffset(allSameHorOffset ? wall.getHOffset() : WallSpinners.DEF_H_OFFSET);

			boolean allSameVerOffset = nodes.stream()
					.filter(n -> n.getWalls().getEastWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getEastWall().getVOffset(), wall.getVOffset()));
			initialValues.setVerticalOffset(allSameVerOffset ? wall.getVOffset() : WallSpinners.DEF_V_OFFSET);
		}

		return initialValues;
	}

	private WallDefinition decideNorthInitialValues(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream()
				.filter(n -> n.getWalls().getNorthWall() != null)
				.map(mapNodeData -> mapNodeData.getWalls().getNorthWall())
				.findFirst();
		WallDefinition initialValues = new WallDefinition(MISSING, WallSpinners.DEF_V_SCALE, WallSpinners.DEF_H_OFFSET, WallSpinners.DEF_V_OFFSET);

		if (def.isPresent()) {
			Wall wall = def.get();
			SurfaceTextures textureDefinition = wall.getDefinition();

			boolean allSameTexture = nodes.stream()
					.filter(n -> n.getWalls().getNorthWall() != null)
					.allMatch(d -> d.getWalls().getNorthWall().getDefinition() == textureDefinition);
			initialValues.setTexture(allSameTexture ? textureDefinition : MISSING);

			boolean allSameVerScale = nodes.stream()
					.filter(n -> n.getWalls().getNorthWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getNorthWall().getVScale(), wall.getVScale()));
			initialValues.setVScale(allSameVerScale ? wall.getVScale() : WallSpinners.DEF_V_SCALE);

			boolean allSameHorOffset = nodes.stream()
					.filter(n -> n.getWalls().getNorthWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getNorthWall().getHOffset(), wall.getHOffset()));
			initialValues.setHorizontalOffset(allSameHorOffset ? wall.getHOffset() : WallSpinners.DEF_H_OFFSET);

			boolean allSameVerOffset = nodes.stream()
					.filter(n -> n.getWalls().getNorthWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getNorthWall().getVOffset(), wall.getVOffset()));
			initialValues.setVerticalOffset(allSameVerOffset ? wall.getVOffset() : WallSpinners.DEF_V_OFFSET);
		}

		return initialValues;
	}

	private WallDefinition decideSouthInitialValues(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream()
				.filter(n -> n.getWalls().getSouthWall() != null)
				.map(mapNodeData -> mapNodeData.getWalls().getSouthWall())
				.findFirst();
		WallDefinition initialValues = new WallDefinition(MISSING, WallSpinners.DEF_V_SCALE, WallSpinners.DEF_H_OFFSET, WallSpinners.DEF_V_OFFSET);

		if (def.isPresent()) {
			Wall wall = def.get();
			SurfaceTextures textureDefinition = wall.getDefinition();

			boolean allSameTexture = nodes.stream()
					.filter(n -> n.getWalls().getSouthWall() != null)
					.allMatch(d -> d.getWalls().getSouthWall().getDefinition() == textureDefinition);
			initialValues.setTexture(allSameTexture ? textureDefinition : MISSING);

			boolean allSameVerScale = nodes.stream()
					.filter(n -> n.getWalls().getSouthWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getSouthWall().getVScale(), wall.getVScale()));
			initialValues.setVScale(allSameVerScale ? wall.getVScale() : WallSpinners.DEF_V_SCALE);

			boolean allSameHorOffset = nodes.stream()
					.filter(n -> n.getWalls().getSouthWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getSouthWall().getHOffset(), wall.getHOffset()));
			initialValues.setHorizontalOffset(allSameHorOffset ? wall.getHOffset() : WallSpinners.DEF_H_OFFSET);

			boolean allSameVerOffset = nodes.stream()
					.filter(n -> n.getWalls().getSouthWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getSouthWall().getVOffset(), wall.getVOffset()));
			initialValues.setVerticalOffset(allSameVerOffset ? wall.getVOffset() : WallSpinners.DEF_V_OFFSET);
		}

		return initialValues;
	}

	private WallDefinition decideWestInitialValues(List<MapNodeData> nodes) {
		Optional<Wall> def = nodes.stream()
				.filter(n -> n.getWalls().getWestWall() != null)
				.map(mapNodeData -> mapNodeData.getWalls().getWestWall())
				.findFirst();
		WallDefinition initialValues = new WallDefinition(MISSING, WallSpinners.DEF_V_SCALE, WallSpinners.DEF_H_OFFSET, WallSpinners.DEF_V_OFFSET);

		if (def.isPresent()) {
			Wall wall = def.get();
			SurfaceTextures textureDefinition = wall.getDefinition();

			boolean allSameTexture = nodes.stream()
					.filter(n -> n.getWalls().getWestWall() != null)
					.allMatch(d -> d.getWalls().getWestWall().getDefinition() == textureDefinition);
			initialValues.setTexture(allSameTexture ? textureDefinition : MISSING);

			boolean allSameVerScale = nodes.stream()
					.filter(n -> n.getWalls().getWestWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getWestWall().getVScale(), wall.getVScale()));
			initialValues.setVScale(allSameVerScale ? wall.getVScale() : WallSpinners.DEF_V_SCALE);

			boolean allSameHorOffset = nodes.stream()
					.filter(n -> n.getWalls().getWestWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getWestWall().getHOffset(), wall.getHOffset()));
			initialValues.setHorizontalOffset(allSameHorOffset ? wall.getHOffset() : WallSpinners.DEF_H_OFFSET);

			boolean allSameVerOffset = nodes.stream()
					.filter(n -> n.getWalls().getWestWall() != null)
					.allMatch(d -> Objects.equals(d.getWalls().getWestWall().getVOffset(), wall.getVOffset()));
			initialValues.setVerticalOffset(allSameVerOffset ? wall.getVOffset() : WallSpinners.DEF_V_OFFSET);
		}

		return initialValues;
	}

	private void addEastSection(JPanel sectionsPanel, WallDefinition initialValues, float maxHeight) throws IOException {
		ImageIcon imageIcon = loadImage(assetsFolderLocation, initialValues.getTexture());
		eastImageButton = GuiUtils.createTextureImageButton(initialValues.getTexture(), null, imageIcon);
		JPanel spinnersPanel = createSection(sectionsPanel, LABEL_EAST, eastImageButton);
		eastWallSpinners = new WallSpinners(spinnersPanel, initialValues);
		addWallImageButtonListener(maxHeight, imageIcon, eastImageButton, eastWallSpinners);
	}

	private void addWallImageButtonListener(float maxHeight,
											ImageIcon imageIcon,
											GalleryButton imageButton,
											WallSpinners wallSpinners) {
		imageButton.addItemListener(itemEvent -> {
			if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
				GuiUtils.openNewDialog(getParent(), new TexturesGalleryDialog(assetsFolderLocation, image -> {
					imageButton.applyTexture(image, imageIcon);
					double value = (maxHeight / (imageIcon.getIconHeight() / WORLD_UNIT_SIZE));
					wallSpinners.getWallVScale().getModel().setValue(value);
				}));
			}
		});
	}

	private void addNorthSection(JPanel sectionsPanel, WallDefinition initialValues, float maxHeight) throws IOException {
		ImageIcon imageIcon = loadImage(assetsFolderLocation, initialValues.getTexture());
		northImageButton = GuiUtils.createTextureImageButton(initialValues.getTexture(), null, imageIcon);
		JPanel spinnersPanel = createSection(sectionsPanel, LABEL_NORTH, northImageButton);
		northWallSpinners = new WallSpinners(spinnersPanel, initialValues);
		addWallImageButtonListener(maxHeight, imageIcon, northImageButton, northWallSpinners);
	}

	private void addSouthSection(JPanel sectionsPanel, WallDefinition initialValues, float maxHeight) throws IOException {
		ImageIcon imageIcon = loadImage(assetsFolderLocation, initialValues.getTexture());
		southImageButton = GuiUtils.createTextureImageButton(initialValues.getTexture(), null, imageIcon);
		JPanel spinnersPanel = createSection(sectionsPanel, LABEL_SOUTH, southImageButton);
		southWallSpinners = new WallSpinners(spinnersPanel, initialValues);
		addWallImageButtonListener(maxHeight, imageIcon, southImageButton, southWallSpinners);
	}

	private void addWestSection(JPanel sectionsPanel, WallDefinition initialValues, float maxHeight) throws IOException {
		ImageIcon imageIcon = loadImage(assetsFolderLocation, initialValues.getTexture());
		westImageButton = GuiUtils.createTextureImageButton(initialValues.getTexture(), null, imageIcon);
		JPanel spinnersPanel = createSection(sectionsPanel, LABEL_WEST, westImageButton);
		westWallSpinners = new WallSpinners(spinnersPanel, initialValues);
		addWallImageButtonListener(maxHeight, imageIcon, westImageButton, westWallSpinners);
	}

	private JPanel createSection(JPanel sectionsPanel, String header, GalleryButton imageButton) {
		JPanel section = new JPanel(new GridBagLayout());
		addSectionBorder(header, section);
		section.add(imageButton);
		JPanel spinnersPanel = addSpinnersPanel(section);
		sectionsPanel.add(section);
		return spinnersPanel;
	}

	private JPanel addSpinnersPanel(JPanel section) {
		JPanel spinnersPanel = new JPanel();
		spinnersPanel.setBorder(createEmptyBorder(SECTION_PADDING, SECTION_PADDING, SECTION_PADDING, SECTION_PADDING));
		spinnersPanel.setLayout(new GridLayout(3, 2));
		section.add(spinnersPanel);
		return spinnersPanel;
	}

	private void addSectionBorder(String header, JPanel section) {
		Border pad = createEmptyBorder(SECTION_PADDING, SECTION_PADDING, SECTION_PADDING, SECTION_PADDING);
		CompoundBorder compoundBorder = createCompoundBorder(pad, createCompoundBorder(createTitledBorder(header), pad));
		section.setBorder(compoundBorder);
	}


	private WallDefinition createWallDef(GalleryButton imageButton, WallSpinners wallSpinners) {
		SurfaceTextures def = imageButton.getTextureDefinition();
		boolean isDefined = def != MISSING;
		return new WallDefinition(isDefined ? def : null, isDefined ? ((Double) wallSpinners.getWallVScale().getModel().getValue()).floatValue() : null, isDefined ? ((Double) wallSpinners.getWallHorOffset().getModel().getValue()).floatValue() : null, isDefined ? ((Double) wallSpinners.getWallVerOffset().getModel().getValue()).floatValue() : null);
	}

	public String getDialogTitle( ) {
		return "Tile Walls";
	}

}
