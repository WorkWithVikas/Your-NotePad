package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.*;

import javax.swing.JFileChooser;

public class Main extends Application {

    private TextArea textArea;
    private TextField titleField;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        // Initialize database connection
        initDB();
        
        /*
         code by Vikas Rathore 
        */
        

        // UI Components
        textArea = new TextArea();
        titleField = new TextField();
        titleField.setPromptText("Enter note title");
        titleField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #b0b0b0; -fx-border-radius: 5px;");

        // MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #c0c0c0");

        // File Menu
        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-text-fill: white;");
        MenuItem openFile = new MenuItem("Open File");
        openFile.setOnAction(e -> openFile());
        MenuItem saveToFile = new MenuItem("Save to File");
        saveToFile.setOnAction(e -> saveToFile());
        fileMenu.getItems().addAll(openFile, saveToFile);

        // Edit Menu
        Menu editMenu = new Menu("Edit");
        editMenu.setStyle("-fx-text-fill: white;");
        MenuItem fontSize = new MenuItem("Change Font Size");
        fontSize.setOnAction(e -> changeFontSize());
        MenuItem fontStyle = new MenuItem("Change Font Style");
        fontStyle.setOnAction(e -> changeFontStyle());
        MenuItem fontType = new MenuItem("Change Font Type");
        fontType.setOnAction(e -> changeFontType());
        MenuItem textColor = new MenuItem("Change Text Color");
        textColor.setOnAction(e -> changeTextColor());
        editMenu.getItems().addAll(fontSize, fontStyle, fontType, textColor);

        // About Menu
        Menu aboutMenu = new Menu("About");
        MenuItem aboutItem = new MenuItem("About Developers");
        aboutItem.setOnAction(e -> showAboutDialog());
        aboutMenu.getItems().add(aboutItem);

        // Add Menus to MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, aboutMenu);

        // Layout
        VBox layout = new VBox(menuBar, titleField, textArea);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Buttons
        Button saveToDbButton = new Button("Save to DB");
        saveToDbButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
        saveToDbButton.setOnAction(e -> saveNote());

        Button clearButton = new Button("Clear Text");
        clearButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10px;");
        clearButton.setOnAction(e -> textArea.clear());

        HBox buttonBox = new HBox(10, saveToDbButton, clearButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        // Make the TextArea fill the available space
        VBox.setVgrow(textArea, Priority.ALWAYS);
        //add logo 
        
        Image i = new Image("logo.png");
        // Scene and Stage
        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());  // Link to external CSS file
        primaryStage.setTitle("Your Notes");
        primaryStage.getIcons().add(i);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

      // connect with your database  
    private void initDB() {
        String url = "jdbc:mysql://localhost:3307/NotepadApp"; 
        String user = "root";
        String password = "";
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                titleField.setText(selectedFile.getName());
                showAlert("Success", "File loaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("File Error", "Failed to read the file.");
            }
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save File");
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                writer.write(textArea.getText());
                showAlert("Success", "File saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("File Error", "Failed to save the file.");
            }
        }
    }

    private void saveNote() {
        String title = titleField.getText();
        String content = textArea.getText();

        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Validation Error", "Title and content cannot be empty.");
            return;
        }

        String query = "INSERT INTO notes (title, content) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.executeUpdate();
            showAlert("Success", "Note saved to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save the note to the database.");
        }
    }

    private void changeFontSize() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Font Size");
        dialog.setHeaderText("Set a new font size:");
        dialog.setContentText("Enter size:");
        dialog.showAndWait().ifPresent(size -> {
            try {
                int fontSize = Integer.parseInt(size);
                textArea.setFont(Font.font(textArea.getFont().getFamily(), fontSize));
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please enter a valid number.");
            }
        });
    }

    private void changeFontStyle() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Normal", "Normal", "Bold", "Italic");
        dialog.setTitle("Change Font Style");
        dialog.setHeaderText("Select a font style:");
        dialog.setContentText("Style:");
        dialog.showAndWait().ifPresent(style -> {
            switch (style) {
                case "Bold":
                    textArea.setStyle("-fx-font-weight: bold;");
                    break;
                case "Italic":
                    textArea.setStyle("-fx-font-style: italic;");
                    break;
                default:
                    textArea.setStyle("-fx-font-weight: normal; -fx-font-style: normal;");
                    break;
            }
        });
    }

    private void changeFontType() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Arial", "Arial", "Courier New", "Verdana", "Tahoma");
        dialog.setTitle("Change Font Type");
        dialog.setHeaderText("Select a font type:");
        dialog.setContentText("Font:");
        dialog.showAndWait().ifPresent(font -> {
            textArea.setFont(Font.font(font, textArea.getFont().getSize()));
        });
    }

    private void changeTextColor() {
        ColorPicker colorPicker = new ColorPicker();
        Alert alert = new Alert(AlertType.NONE);
        alert.setTitle("Change Text Color");
        alert.getDialogPane().setContent(colorPicker);
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(okButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                Color color = colorPicker.getValue();
                textArea.setStyle("-fx-text-fill: #" + color.toString().substring(2, 8) + ";");
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAboutDialog() {
        // Create an About Dialog
        Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setTitle("About Developers");
        aboutDialog.setHeaderText("Notepad App - Developer Information");

        // Set the content for the About dialog
        aboutDialog.setContentText(
            "Developed by:\n\n" +
            "Vikas Rathore (Lead Developer) - , Java , Swing , JavaFX, MySQL, JDBC\n\n" +
            "Version: 1.0\n" +
            "Date: December 2024\n\n" +
            "Work Port :\n" +
            "Email: jarvis.90v@gmail.com\n" +
            "GitHub: https://github.com/WorkWithVikas"
        );

        // Display the About dialog
        aboutDialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
