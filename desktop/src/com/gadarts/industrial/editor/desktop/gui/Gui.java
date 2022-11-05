package com.gadarts.industrial.editor.desktop.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.gadarts.industrial.editor.desktop.gui.dialogs.DefineEnvObjectDialog;
import com.gadarts.industrial.editor.desktop.gui.dialogs.SelectObjectInNodeDialog;
import com.gadarts.industrial.editor.desktop.gui.dialogs.TilesLiftDialog;
import com.gadarts.industrial.editor.desktop.gui.dialogs.WallTilingDialog;
import com.gadarts.industrial.editor.desktop.gui.managers.ManagersImpl;
import com.gadarts.industrial.editor.desktop.gui.managers.PersistenceManager;
import com.gadarts.industrial.editor.desktop.gui.menu.MenuItemProperties;
import com.gadarts.industrial.editor.desktop.gui.menu.definitions.MenuItemDefinition;
import com.gadarts.industrial.editor.desktop.gui.menu.definitions.Menus;
import com.gadarts.industrial.editor.desktop.DesktopLauncher;
import com.industrial.editor.MapManagerEventsSubscriber;
import com.industrial.editor.MapRenderer;
import com.industrial.editor.actions.ActionAnswer;
import com.industrial.editor.model.elements.PlacedElement;
import com.industrial.editor.model.elements.PlacedEnvObject;
import com.industrial.editor.model.node.FlatNode;
import org.lwjgl.openal.AL;
import org.pushingpixels.radiance.theming.api.skin.RadianceTwilightLookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static com.gadarts.industrial.editor.desktop.gui.managers.PersistenceManager.SETTINGS_KEY_LAST_OPENED_FOLDER;


public class Gui extends JFrame implements MapManagerEventsSubscriber {
	public static final String FOLDER_TOOLBAR_BUTTONS = "toolbar_buttons";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int MENU_ITEM_IMAGE_SIZE = 16;
	public static final int MENU_LABEL_PAD = 4;
	public static final String DEFAULT_MAP_NAME = "Unnamed map";
	public static final String WINDOW_HEADER = "%s - %s";
	public static final String PROGRAM_TILE = "Necronemes Map Editor";
	public static final String SETTINGS_KEY_LAST_OPENED_FILE = "last_opened_file";
	public static final String SETTINGS_FILE = "settings.json";
	public static final int MENU_SEPARATOR_HEIGHT = 10;
	private static final String FOLDER_ASSETS = "core" + File.separator + "assets";
	private static final String ICON_FORMAT = ".png";
	public static final String UI_ASSETS_FOLDER_PATH = FOLDER_ASSETS
			+ File.separator
			+ "%s"
			+ File.separator
			+ "%s"
			+ ICON_FORMAT;
	private final LwjglAWTCanvas lwjgl;
	private final MapRenderer mapRenderer;
	private final File assetsFolderLocation;
	private final ManagersImpl managers;
	private final Map<String, ButtonGroup> menuItemGroups = new HashMap<>();

	public Gui(final LwjglAWTCanvas lwjgl, final MapRenderer mapRenderer, final Properties properties) {
		super(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		this.managers = new ManagersImpl(mapRenderer, this);
		this.lwjgl = lwjgl;
		this.mapRenderer = mapRenderer;
		this.assetsFolderLocation = new File(properties.getProperty(DesktopLauncher.PROPERTIES_KEY_ASSETS_PATH));
		try {
			UIManager.setLookAndFeel(new RadianceTwilightLookAndFeel());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		defineWindow();
	}

	private void addMenuBar( ) {
		JMenuBar menuBar = new JMenuBar();
		Arrays.stream(Menus.values()).forEach(menu -> {
			JMenu jMenu = new JMenu(menu.getLabel());
			jMenu.setBorder(new EmptyBorder(MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD));
			Arrays.stream(menu.getDefinitions()).forEach(item -> {
				JMenuItem menuItem = createMenuItem(item);
				Optional.ofNullable(menuItem).ifPresentOrElse(jMenu::add, ( ) -> {
					JPopupMenu.Separator sep = new JPopupMenu.Separator();
					sep.setPreferredSize(new Dimension(1, MENU_SEPARATOR_HEIGHT));
					jMenu.add(sep);
				});
			});
			menuBar.add(jMenu);
		});
		setJMenuBar(menuBar);
	}

	private JMenuItem createMenuItem(MenuItemDefinition item) {
		MenuItemProperties prop = item.getMenuItemProperties();
		if (prop != null) {
			JMenuItem menuItem;
			menuItem = new JMenuItem(prop.getLabel());
			defineMenuItem(item, prop, menuItem);
			return menuItem;
		}
		return null;
	}

	private void defineMenuItem(MenuItemDefinition item, MenuItemProperties menuItemProperties, JMenuItem menuItem) {
		applyIconToMenuItem(item, menuItem);
		try {
			Constructor<?> constructor = menuItemProperties.getActionClass().getConstructors()[0];
			menuItem.addActionListener((ActionListener) constructor.newInstance(
					mapRenderer,
					managers));
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		menuItem.setBorder(new EmptyBorder(MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD, MENU_LABEL_PAD));
	}

	private ButtonGroup addMenuItemsGroup(String buttonGroupName) {
		ButtonGroup buttonGroup = new ButtonGroup();
		menuItemGroups.put(buttonGroupName, buttonGroup);
		return buttonGroup;
	}

	private void applyIconToMenuItem(MenuItemDefinition item, JMenuItem menuItem) {
		if (item.getMenuItemProperties().getIcon() != null) {
			try {
				ImageIcon icon = createMenuItemIcon(item.getMenuItemProperties());
				menuItem.setIcon(icon);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private ImageIcon createMenuItemIcon(MenuItemProperties prop) throws IOException {
		String path = String.format(UI_ASSETS_FOLDER_PATH, FOLDER_TOOLBAR_BUTTONS, prop.getIcon());
		ImageIcon icon = new ImageIcon(ImageIO.read(new File(path)));
		icon = new ImageIcon(icon.getImage().getScaledInstance(MENU_ITEM_IMAGE_SIZE, MENU_ITEM_IMAGE_SIZE, 0));
		return icon;
	}

	private JPanel createEntitiesPanel( ) {
		EditorCardLayout entitiesLayout = new EditorCardLayout();
		return new JPanel(entitiesLayout);
	}

	private void defineWindow( ) {
		addWindowComponents();
		defineWindowClose();
		setSize(WIDTH, HEIGHT);
		setLocationByPlatform(true);
		setVisible(true);
		setResizable(false);
	}


	private void addWindowComponents( ) {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel entitiesPanel = createEntitiesPanel();
		JSplitPane splitPane = createSplitPane(lwjgl.getCanvas(), entitiesPanel);
		mainPanel.add(splitPane);
		addMenuBar();
		managers.onApplicationStart(mainPanel, this, entitiesPanel, assetsFolderLocation);
		getContentPane().add(mainPanel);
	}


	private JSplitPane createSplitPane(final Canvas canvas, final JPanel entitiesPanel) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entitiesPanel, canvas);
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(0.4);
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		BasicSplitPaneUI flatDividerSplitPaneUI = new BasicSplitPaneUI() {
			@Override
			public BasicSplitPaneDivider createDefaultDivider( ) {
				return new BasicSplitPaneDivider(this) {
					@Override
					public void setBorder(Border b) {
					}
				};
			}
		};
		splitPane.setUI(flatDividerSplitPaneUI);
		return splitPane;
	}

	private void defineWindowClose( ) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				lwjgl.stop();
				AL.destroy();
				e.getWindow().dispose();
			}
		});
	}

	@Override
	public void onTileSelectedUsingWallTilingTool(final FlatNode src, final FlatNode dst) {
		managers.getDialogsManager().openDialog(new WallTilingDialog(assetsFolderLocation, mapRenderer, src, dst));
	}

	@Override
	public void onTilesSelectedForLifting(final int srcRow, final int srcCol, final int dstRow, final int dstCol) {
		managers.getDialogsManager()
				.openDialog(new TilesLiftDialog(new FlatNode(srcRow, srcCol), new FlatNode(dstRow, dstCol), mapRenderer));
	}

	@Override
	public void onNodeSelectedToSelectPlacedObjectsInIt(List<? extends PlacedElement> elementsInTheNode,
														ActionAnswer<PlacedElement> answer) {
		managers.getDialogsManager().openDialog(new SelectObjectInNodeDialog(elementsInTheNode, answer));
	}

	@Override
	public void onSelectedEnvObjectToDefine(final PlacedEnvObject data) {
		managers.getDialogsManager().openDialog(new DefineEnvObjectDialog(data, mapRenderer));
	}


	@Override
	public void onMapRendererIsReady( ) {
		SwingUtilities.invokeLater(( ) -> managers.onMapRendererIsReady(mapRenderer, this));
	}
}
