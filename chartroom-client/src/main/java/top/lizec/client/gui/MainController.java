package top.lizec.client.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable {
    private static final Logger logger = Logger.getLogger(MainController.class.getName());
    public TreeView main_treeview;
    private ChartroomGUIApplication app;
    public AnchorPane main_pane_under_scroll;

    /**
     * 设置TreeView
     */
    @SuppressWarnings("unchecked")
    public void setTreeView() {
        TreeItem<String> root = new TreeItem<String>(StaticResourcesConfig.MAIN_TREE_HEADER);
        root.setExpanded(true);
        root.getChildren().addAll(new TreeItem<String>(StaticResourcesConfig.MAIN_TREE_HEADER_ITEM1),
                new TreeItem<String>(StaticResourcesConfig.MAIN_TREE_HEADER_ITEM2),
                new TreeItem<String>(StaticResourcesConfig.MAIN_TREE_HEADER_ITEM3),
                new TreeItem<String>(StaticResourcesConfig.MAIN_TREE_HEADER_ITEM4),
                new TreeItem<String>(StaticResourcesConfig.MAIN_TREE_HEADER_ITEM5));
        main_treeview.setRoot(root);

    }

    /**
     * TreeView 点击事件
     *
     * @throws IOException
     */
    public void mainTreeViewClick() throws IOException {
        logger.log(Level.INFO, "点击TreeView");
        // 获取鼠标当前点击的Item
        TreeItem<String> selectedItem = (TreeItem<String>) main_treeview.getSelectionModel().getSelectedItem();
        logger.log(Level.INFO, selectedItem.getValue());

        String pagePath = "";
        switch (selectedItem.getValue()) {
            case StaticResourcesConfig.MAIN_TREE_HEADER:
                pagePath = StaticResourcesConfig.DEFAULT_VIEW_PATH;
                break;
            case StaticResourcesConfig.MAIN_TREE_HEADER_ITEM1:
                pagePath = StaticResourcesConfig.NOTE_VIEW_PATH;
                break;
            case StaticResourcesConfig.MAIN_TREE_HEADER_ITEM2:
                pagePath = StaticResourcesConfig.CLIP_VIEW_PATH;
                break;
            case StaticResourcesConfig.MAIN_TREE_HEADER_ITEM3:
                pagePath = StaticResourcesConfig.USER_VIEW_PATH;
                break;
            case StaticResourcesConfig.MAIN_TREE_HEADER_ITEM4:
                pagePath = StaticResourcesConfig.DATA_VIEW_PATH;
                break;
            case StaticResourcesConfig.MAIN_TREE_HEADER_ITEM5:
                pagePath = StaticResourcesConfig.LANGUAGE_VIEW_PATH;
                break;
        }

        skipView(pagePath);
    }

    /**
     * 改变右侧scroll的界面
     *
     * @param pagePath
     * @throws IOException
     */
    private void skipView(String pagePath) throws IOException {
        logger.info("显示剪切板界面");
        ObservableList<Node> scrolChildren = main_pane_under_scroll.getChildren();
        scrolChildren.clear();
        scrolChildren.add(FXMLLoader.load(getClass().getResource(pagePath)));
    }


    public void setApp(ChartroomGUIApplication app) {
        this.app = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTreeView();
    }
}
