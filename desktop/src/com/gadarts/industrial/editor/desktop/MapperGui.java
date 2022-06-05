package com.gadarts.industrial.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.gadarts.industrial.editor.desktop.gui.GuiUtils;
import com.gadarts.industrial.editor.desktop.gui.dialogs.DialogPane;
import com.gadarts.industrial.editor.desktop.gui.Gui;
import com.gadarts.industrial.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.gadarts.industrial.shared.model.ElementDefinition;
import com.google.gson.Gson;
import com.industrial.editor.MapRenderer;
import org.lwjgl.openal.AL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapperGui extends JFrame implements PropertyChangeListener {
	public static final String FOLDER_TOOLBAR_BUTTONS = "toolbar_buttons";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final String MSG_FAILED_TO_OPEN_MAP_FILE = "Failed to open map file!";
	private static final String MSG_FAILED_TO_SAVE_MAP_FILE = "Failed to save map file!";
	private static final String MSG_FAILED_TO_READ_SETTINGS = "Failed to read the editor's settings!";
	private static final String MSG_SUCCESS_TO_SAVE_MAP_FILE = "Map was saved successfully to: %s";
	private static final String PROGRAM_TILE = "Necronemes Map Editor";
	private static final String DEFAULT_MAP_NAME = "Unnamed map";
	private static final String WINDOW_HEADER = "%s - %s";
	private static final String SETTINGS_FILE = "settings.json";
	private static final String SETTINGS_KEY_LAST_OPENED_FILE = "last_opened_file";
//	private final LwjglAWTCanvas lwjgl;
private final Map<String, ButtonGroup> buttonGroups = new HashMap<>();
	//	private final File assetsFolderLocation;
//	private final GuiEventsSubscriber guiEventsSubscriber;
//	private final ModesHandler modesHandler;
	private final Gson gson = new Gson();
	private JPanel entitiesPanel;
	private JPanel subToolbarPanel;

	private Map<String, String> settings;


	public MapperGui(final LwjglAWTCanvas lwjgl,
					 final MapRenderer mapRenderer,
					 final Properties properties) {
//		super(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
//		this.assetsFolderLocation = new File(properties.getProperty(DesktopLauncher.PROPERTIES_KEY_ASSETS_PATH));
//		this.lwjgl = lwjgl;
//		this.guiEventsSubscriber = guiEventsSubscriber;
//		modesHandler = new ModesHandler();
//		modesHandler.setVisible(false);
//		add(modesHandler);
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			SwingUtilities.updateComponentTreeUI(this);
//		} catch (final Exception e1) {
//			e1.printStackTrace();
//		}
////		addToolBar(ToolbarDefinition.values(), this).add(Box.createHorizontalGlue());
////		addMenuBar();
////		defineMapperWindow(lwjgl.getCanvas());
	}

//	private void readSettingsFile() {
//		File settingsFile = new File(SETTINGS_FILE);
//		if (settingsFile.exists()) {
//			try {
//				Reader json = new FileReader(SETTINGS_FILE);
//				//noinspection unchecked
//				settings = gson.fromJson(json, HashMap.class);
//			} catch (final FileNotFoundException e) {
//				e.printStackTrace();
//				JOptionPane.showMessageDialog(this, MSG_FAILED_TO_READ_SETTINGS, PROGRAM_TILE, JOptionPane.ERROR_MESSAGE);
//			}
//			if (settings.containsKey(SETTINGS_KEY_LAST_OPENED_FILE)) {
//				File file = new File(settings.get(SETTINGS_KEY_LAST_OPENED_FILE));
//				tryOpeningFile(file);
//			}
//		} else {
//			settings = new HashMap<>();
//		}
//	}

//	private void addMenuBar() {
//		JMenuBar menuBar = new JMenuBar();
//		Arrays.stream(Menus.values()).forEach(menu -> {
//			JMenu jMenu = new JMenu(menu.getLabel());
//			Arrays.stream(menu.getDefinitions()).forEach(item -> {
//				MenuItemProperties menuItemProperties = item.getMenuItemProperties();
//				JMenuItem menuItem = new JMenuItem(menuItemProperties.getLabel());
//				menuItem.addActionListener(menuItemProperties.getActionClass());
//				jMenu.add(menuItem);
//			});
//			menuBar.add(jMenu);
//		});
//		setJMenuBar(menuBar);
//	}

//	protected JToolBar addToolBar(final ToolbarButtonDefinition[] toolbarOptions, final Container container) {
//		return addToolBar(toolbarOptions, container, BorderLayout.PAGE_START);
//	}

//	protected JToolBar addToolBar(final ToolbarButtonDefinition[] toolbarOptions,
//								  final Container container,
//								  final String name) {
//		JToolBar toolBar = new JToolBar();
//		toolBar.setFloatable(false);
//		Arrays.stream(toolbarOptions).forEach(option -> {
//			if (option.getButtonProperties() != null) {
//				AbstractButton jButton;
//				try {
//					jButton = createToolbarButtonOfMenuItem(option);
//					toolBar.add(jButton);
//					toolBar.setName(name);
//				} catch (final IOException e) {
//					e.printStackTrace();
//				}
//			} else {
//				toolBar.addSeparator();
//			}
//		});
//		container.add(toolBar, name);
//		return toolBar;
//	}

//	private AbstractButton createToolbarButtonOfMenuItem(final ToolbarButtonDefinition button) throws IOException {
//		ToolbarButtonProperties buttonProperties = button.getButtonProperties();
//		MenuItemDefinition menuItemDef = buttonProperties.getMenuItemDefinition();
//		boolean noMenuItem = menuItemDef == null;
//		if ((noMenuItem && buttonProperties.getButtonGroup() == null)
//				|| (!noMenuItem && menuItemDef.getMenuItemProperties().getButtonGroup() == null)) {
//			return new ToolBarButton(getButtonIcon(buttonProperties), buttonProperties, fileManager, guiEventsSubscriber, settings);
//		} else {
//			return createToolbarRadioButtonOfMenuItem(button, buttonProperties, getButtonIcon(buttonProperties));
//		}
//	}

//	private AbstractButton createToolbarRadioButtonOfMenuItem(final ToolbarButtonDefinition button,
//															  final ToolbarButtonProperties buttonProperties,
//															  final ImageIcon imageIcon) {
//		AbstractButton toolBarButton;
//		MenuItemDefinition menuItemDefinition = button.getButtonProperties().getMenuItemDefinition();
//		String groupName;
//		if (menuItemDefinition != null) {
//			MenuItemProperties menuItemProperties = menuItemDefinition.getMenuItemProperties();
//			groupName = menuItemProperties.getButtonGroup();
//		} else {
//			groupName = buttonProperties.getButtonGroup();
//		}
//		boolean isNew = !buttonGroups.containsKey(groupName);
//		if (isNew) {
//			ButtonGroup buttonGroup = new ButtonGroup();
//			buttonGroups.put(groupName, buttonGroup);
//		}
//		toolBarButton = new RadioToolBarButton(imageIcon, buttonProperties, fileManager, guiEventsSubscriber, settings);
//		ButtonGroup buttonGroup = buttonGroups.get(groupName);
//		buttonGroup.add(toolBarButton);
//		if (isNew) {
//			toolBarButton.setSelected(true);
//		}
//		return toolBarButton;
//	}

	private ImageIcon getButtonIcon(final ToolbarButtonProperties buttonProperties) throws IOException {
		String path = String.format(Gui.UI_ASSETS_FOLDER_PATH, FOLDER_TOOLBAR_BUTTONS, buttonProperties.getIcon());
		return new ImageIcon(ImageIO.read(new File(path)), buttonProperties.getToolTip());
	}

//	private void defineMapperWindow(final Canvas canvas) {
//		defineMapperWindowAttributes();
//		JPanel mainPanel = new JPanel(new BorderLayout());
//		addSubToolbars(mainPanel);
//		entitiesPanel = createEntitiesPanel();
//		JSplitPane splitPane = createSplitPane(canvas, entitiesPanel);
//		mainPanel.add(splitPane);
//		getContentPane().add(mainPanel);
//	}

//	private void addSubToolbars(final JPanel mainPanel) {
//		CardLayout subToolbarsCardLayout = new CardLayout();
//		subToolbarPanel = new JPanel(subToolbarsCardLayout);
//		mainPanel.add(subToolbarPanel, BorderLayout.PAGE_START);
//		Arrays.stream(SubToolbarsDefinitions.values()).forEach(sub ->
//				addToolBar(sub.getButtons(), subToolbarPanel, sub.name()).add(Box.createHorizontalGlue()));
//		subToolbarsCardLayout.show(subToolbarPanel, SubToolbarsDefinitions.TILES.name());
//	}

	private JSplitPane createSplitPane(final Canvas canvas, final JPanel entitiesPanel) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entitiesPanel, canvas);
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(0.4);
		return splitPane;
	}

	private JPanel createEntitiesPanel() {
		CardLayout entitiesLayout = new CardLayout();
		return new JPanel(entitiesLayout);
	}

	private DefaultMutableTreeNode createSectionNodeForTree(final String header, final ElementDefinition[] definitions) {
		DefaultMutableTreeNode sectionNode = new DefaultMutableTreeNode(header);
		Arrays.stream(definitions).forEach(def -> sectionNode.add(new DefaultMutableTreeNode(def, false)));
		return sectionNode;
	}

//	private EditorTree createResourcesTree(final EditModes mode) {
//		DefaultMutableTreeNode top = new DefaultMutableTreeNode(mode.getDisplayName());
//		EditorTree tree = new EditorTree(top);
//		Arrays.stream(mode.getTreeSections()).forEach(modeSection -> {
//			top.add(createSectionNodeForTree(modeSection.getHeader(), modeSection.getDefinitions()));
//			tree.setCellRenderer(new ResourcesTreeCellRenderer(modeSection.getEntryIcon()));
//			tree.addMouseListener(new MouseAdapter() {
//
//				@Override
//				public void mouseClicked(final MouseEvent e) {
//					super.mouseClicked(e);
//					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
//					tree.setSelectionPath(path);
//					if (path != null && e.getClickCount() > 1) {
//						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//						if (node.isLeaf()) {
//							ElementDefinition definition = (ElementDefinition) node.getUserObject();
//							tree.firePropertyChange(Events.TREE_ENTRY_SELECTED.name(), null, definition);
//						}
//					}
//				}
//			});
//		});
//		tree.expandPath(new TreePath(top.getPath()));
//		return tree;
//	}

//	private void addEntitiesDataSelectors(final JPanel entitiesPanel) {
//		CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
//		Arrays.stream(EditModes.values()).forEach(mode -> {
//			EntriesDisplayTypes entriesDisplayType = mode.getEntriesDisplayTypes();
//			if (entriesDisplayType == EntriesDisplayTypes.GALLERY) {
//				JScrollPane entitiesGallery = GuiUtils.createEntitiesGallery(assetsFolderLocation, itemEvent -> {
//					if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
//						Optional.ofNullable(guiEventsSubscriber).ifPresent(sub -> {
//							Assets.SurfaceTextures texture = ((GalleryButton) itemEvent.getItem()).getTextureDefinition();
//							sub.onTileSelected(texture);
//						});
//					}
//				});
//				entitiesPanel.add(entitiesGallery, EditModes.TILES.name());
//			} else if (entriesDisplayType == EntriesDisplayTypes.TREE) {
//				EditorTree resourcesTree = createResourcesTree(mode);
//				resourcesTree.addPropertyChangeListener(this);
//				entitiesPanel.add(resourcesTree, mode.name());
//			}
//		});
//		entitiesPanel.add(new JPanel(), NONE.name());
//		EditModes mode = (EditModes) ModesHandler.getMode();
//		entitiesLayout.show(entitiesPanel, mode.name());
//	}


	private void defineMapperWindowAttributes() {
		defineWindowClose();
		setSize(WIDTH, HEIGHT);
		setLocationByPlatform(true);
		setVisible(true);
		setResizable(false);
	}

	private void defineWindowClose() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
//				lwjgl.stop();
				AL.destroy();
				e.getWindow().dispose();
			}
		});
	}

//	@Override
//	public void propertyChange(final PropertyChangeEvent evt) {
//		String propertyName = evt.getPropertyName();
//		EditorMode mode = ModesHandler.getMode();
//		if (propertyName.equals(Events.MODE_SET_EDIT.name())) {
//			int newModeIndex = (int) evt.getNewValue();
//			mode = EditModes.values()[newModeIndex];
//			EditModes editMode = (EditModes) mode;
//			CardLayout cardLayout = (CardLayout) entitiesPanel.getLayout();
//			guiEventsSubscriber.onEditModeSet(editMode);
//			updateSubToolbar(editMode);
//			if (editMode.getEntriesDisplayTypes() != NONE) {
//				cardLayout.show(entitiesPanel, editMode.name());
//			} else {
//				cardLayout.show(entitiesPanel, NONE.name());
//			}
//			EditorMode finalMode = mode;
//			Optional.ofNullable(editMode.getTools()).ifPresent(tools -> SubToolbarsDefinitions.findByMode(finalMode).getButtons()[0].getButtonProperties());
//		} else if (propertyName.equals(Events.MODE_SET_CAMERA.name())) {
//			int newModeIndex = (int) evt.getNewValue();
//			mode = CameraModes.values()[newModeIndex];
//			guiEventsSubscriber.onCameraModeSet((CameraModes) mode);
//			CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
//			entitiesLayout.show(entitiesPanel, NONE.name());
//			updateSubToolbar(mode);
//		} else if (propertyName.equals(Events.TILE_TOOL_SET.name())) {
//			TilesTools selectedTool = TilesTools.values()[(int) evt.getNewValue()];
//			CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
//			if (selectedTool == TilesTools.BRUSH) {
//				entitiesLayout.show(entitiesPanel, EditModes.TILES.name());
//			} else {
//				entitiesLayout.show(entitiesPanel, NONE.name());
//			}
//			guiEventsSubscriber.onToolSet(selectedTool);
//		} else if (propertyName.equals(Events.ENV_TOOL_SET.name())) {
//			EnvTools selectedTool = EnvTools.values()[(int) evt.getNewValue()];
//			CardLayout entitiesLayout = (CardLayout) entitiesPanel.getLayout();
//			if (selectedTool == EnvTools.BRUSH) {
//				entitiesLayout.show(entitiesPanel, EditModes.ENVIRONMENT.name());
//			} else {
//				entitiesLayout.show(entitiesPanel, NONE.name());
//			}
//			guiEventsSubscriber.onToolSet(selectedTool);
//		} else if (propertyName.equals(Events.TREE_ENTRY_SELECTED.name())) {
//			guiEventsSubscriber.onToolSet(EnvTools.BRUSH);
//			if (mode == EditModes.CHARACTERS) {
//				guiEventsSubscriber.onTreeCharacterSelected((CharacterDefinition) evt.getNewValue());
//			} else if (mode == EditModes.ENVIRONMENT) {
//				guiEventsSubscriber.onTreeEnvSelected((EnvironmentDefinitions) evt.getNewValue());
//			} else if (mode == EditModes.PICKUPS) {
//				guiEventsSubscriber.onTreePickupSelected((ItemDefinition) evt.getNewValue());
//			}
//		} else if (propertyName.equals(Events.REQUEST_TO_ROTATE_SELECTED_OBJECT.name())) {
//			guiEventsSubscriber.onSelectedObjectRotate((Integer) evt.getNewValue());
//		} else if (propertyName.equals(Events.REQUEST_TO_NEW.name())) {
//			resetCurrentlyOpenedFile();
//			guiEventsSubscriber.onNewMapRequested();
//		} else if (propertyName.equals(Events.REQUEST_TO_SAVE.name())) {
//			File file = currentlyOpenedMap;
//			if (file == null) {
//				JFileChooser fileChooser = new JFileChooser();
//				if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//					file = fileChooser.getSelectedFile();
//				}
//			}
//			try {
//				//noinspection ConstantConditions
//				guiEventsSubscriber.onSaveMapRequested(file.getPath());
//				updateCurrentlyOpenedFile(file);
//				JOptionPane.showMessageDialog(
//						this,
//						String.format(MSG_SUCCESS_TO_SAVE_MAP_FILE, file.getPath()), PROGRAM_TILE, JOptionPane.INFORMATION_MESSAGE);
//			} catch (final NullPointerException e) {
//				JOptionPane.showMessageDialog(this, MSG_FAILED_TO_SAVE_MAP_FILE, PROGRAM_TILE, JOptionPane.ERROR_MESSAGE);
//			}
//		} else if (propertyName.equals(Events.REQUEST_TO_LOAD.name())) {
//			JFileChooser fileChooser = new JFileChooser();
//			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//				File file = fileChooser.getSelectedFile();
//				tryOpeningFile(file);
//			}
//		} else if (propertyName.equals(Events.REQUEST_TO_OPEN_AMBIENT_LIGHT_DIALOG.name())) {
//			openDialog(new SetAmbientLightDialog(guiEventsSubscriber.getAmbientLightValue(), guiEventsSubscriber));
//		} else if (propertyName.equals(Events.REQUEST_TO_OPEN_MAP_SIZE_DIALOG.name())) {
//			openDialog(new SetMapSizeDialog(guiEventsSubscriber.getMapSize(), guiEventsSubscriber));
//		}
//	}


	private void saveSettings() {
		String serialized = gson.toJson(settings);
		try (PrintWriter out = new PrintWriter(SETTINGS_FILE)) {
			out.println(serialized);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

//	private void tryOpeningFile(final File file) {
//		try {
//			guiEventsSubscriber.onLoadMapRequested(file.getPath());
//			updateCurrentlyOpenedFile(file);
//		} catch (final IOException e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(this, MSG_FAILED_TO_OPEN_MAP_FILE);
//		}
//	}

//	private void updateCurrentlyOpenedFile(final File file) {
//		currentlyOpenedMap = file;
//		setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, file.getName()));
//		settings.put(SETTINGS_KEY_LAST_OPENED_FILE, file.getPath());
//		saveSettings();
//	}

//	private void updateSubToolbar(final EditorMode mode) {
//		CardLayout subToolbarLayout = (CardLayout) subToolbarPanel.getLayout();
//		Optional.ofNullable(SubToolbarsDefinitions.findByMode(mode))
//				.ifPresentOrElse(
//						sub -> {
//							subToolbarLayout.show(subToolbarPanel, sub.name());
//						},
//						() -> subToolbarLayout.show(subToolbarPanel, SubToolbarsDefinitions.EMPTY.name()));
//	}


	private void openDialog(final DialogPane pane) {
		GuiUtils.openNewDialog(this, pane);
	}

//	@Override
//	public void onTileSelectedUsingWallTilingTool(final FlatNode src, final FlatNode dst) {
//		openDialog(new WallTilingDialog(assetsFolderLocation, guiEventsSubscriber, src, dst));
//	}
//
//	@Override
//	public void onTilesSelectedForLifting(final int srcRow, final int srcCol, final int dstRow, final int dstCol) {
//		openDialog(new TilesLiftDialog(new FlatNode(srcRow, srcCol), new FlatNode(dstRow, dstCol), guiEventsSubscriber));
//	}
//
//	@Override
//	public void onNodeSelectedToSelectPlacedObjectsInIt(final List<? extends PlacedElement> elementsInTheNode,
//														final ActionAnswer<PlacedElement> answer) {
//		openDialog(new SelectObjectInNodeDialog(elementsInTheNode, answer));
//	}
//
//	@Override
//	public void onSelectedEnvObjectToDefine(final PlacedEnvObject data) {
//		openDialog(new DefineEnvObjectDialog(data, guiEventsSubscriber));
//	}

//	@Override
//	public void onEditorIsReady( ) {
//		readSettingsFile();
//	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}
}
