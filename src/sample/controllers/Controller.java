package sample.controllers;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Controller {
    ListProperty<String> listProperty = new SimpleListProperty<>();
    FileList l = new FileList();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private Stage primaryStage = new Stage();
    private File dirPath;
    String fileExt;

    @FXML
    private TextField fileDir;

    @FXML
    public ListView<String> listOfFiles;

    @FXML
    private TextField fileExtension;

    @FXML
    private TextArea openedFile;

    @FXML
    private Button fileDirButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchInf;


    private void getDir() {
        File dir = directoryChooser.showDialog(primaryStage);
        if (dir != null) {
            dirPath = new File(dir.getAbsolutePath());
            fileDir.setText(String.valueOf(dirPath));
        } else {
            fileDir.setText("");
        }
    }

    private void getExt(String ext){
        if(ext.equals(""))
            fileExt = ".log";
        else
            fileExt = "." + ext;
    }

    public void printFileList () {
        listProperty.set(FXCollections.observableArrayList(l.files));
        listOfFiles.itemsProperty().bind(listProperty);
    }

    private void clearAll () {
        l.files.clear();
        listOfFiles.getItems().clear();
        fileDir.setText("");
        fileExtension.setText("");
        searchInf.setText("");
    }
    @FXML
    public void handleMouseClick(MouseEvent arg0) throws IOException {
        BufferedInputStream bs = new BufferedInputStream(new FileInputStream(listOfFiles.getSelectionModel().getSelectedItem()), 8192 * 4);
        byte[] content = new byte[bs.available()];
        bs.read(content);
        bs.close();
        String text = new String(content, StandardCharsets.UTF_8);
        openedFile.setText(text);
    }

    @FXML
    public void initialize() {
        fileDirButton.setOnAction(event -> {
            clearAll();
            getDir();
        });
        searchButton.setOnAction(event -> {
            l.files.clear();
            getExt(fileExtension.getText());
            try {
                l.getFileList(dirPath, fileExt, searchInf.getText());
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            printFileList();
        });
    }
}