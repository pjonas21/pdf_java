package org.example;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Main extends Application {
	
	private TextField folderPathField;
	private TextField fileName;
	
	private PDFMergerUtility merger;
	private CombinePdf combinador;

	private ProgressIndicator spinner = new ProgressIndicator();


    public static void main(String[] args) {
    	
    	launch(args);

    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		setAppIcon(primaryStage);
		
		folderPathField = new TextField();
		folderPathField.setPromptText("Selecione a pasta dos arquivos");
		folderPathField.setPrefWidth(250);
		spinner.setVisible(false);
		
		Alert alertSuccess = new Alert(AlertType.INFORMATION);

		Alert alertError = new Alert(AlertType.ERROR);

		
		Button selectFolderButton = new Button("Selecionar pasta");
		selectFolderButton.setOnAction(event -> selectFolderPath(primaryStage));
		
		Button actionButton = new Button("Mesclar PDFs");

		actionButton.setOnAction(e -> {
			try {
				spinner.setVisible(true);

				Task<Void> backgroundTask = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						mergeActionButton();
                        return null;
                    }
				};

				backgroundTask.setOnSucceeded(ev -> {
					spinner.setVisible(false); // Oculta o spinner
					alertSuccess.setTitle("Sucesso");
					alertSuccess.setHeaderText("Sucesso");
					alertSuccess.setContentText("PDFs mesclados com sucesso.");
					alertSuccess.show();
					folderPathField.clear();
					fileName.clear();
				});

				backgroundTask.setOnFailed(ev -> {
					spinner.setVisible(false);
					alertError.setTitle("Falha");
					alertError.setContentText("Erro ao processar arquivos. Arquivos corrompidos/inexistentes.");
					alertError.show();
				});

				new Thread(backgroundTask).start();

			} catch (Exception e1) {
				alertError.setTitle("Falha");
				alertError.setContentText("Erro ao processar arquivos. Arquivos corrompidos/inexistentes.");
				alertError.show();
			}
		});

		fileName = new TextField();
		fileName.setPromptText("Nome do novo arquivo.");
		fileName.setMaxWidth(250);
		
		HBox hLayout = new HBox(10, folderPathField, selectFolderButton);
		HBox footerLayout = new HBox(10, actionButton, spinner);
		footerLayout.setAlignment(Pos.CENTER_LEFT);
		VBox vLayout = new VBox(10, hLayout, fileName, footerLayout);
		vLayout.setPadding(new Insets(15));
		
		Scene scene = new Scene(vLayout, 450, 150);
		
		primaryStage.setTitle("PDF Merger");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}

	private void mergeActionButton() throws Exception {
		String folderPath = folderPathField.getText();
		if(!folderPath.isEmpty()) {
			mergeAction(folderPath, this.fileName.getText());
		} else {
			throw new FileNotFoundException("Nenhum arquivo encontrado.");
		}
	}
	
	private void mergeAction(String path, String newFileName) throws FileNotFoundException {
		merger = new PDFMergerUtility();
		combinador = new CombinePdf(path, merger, newFileName);
		combinador.combinePdf();
	}

	private void selectFolderPath(Stage stage) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Selecione uma pasta");
		File selectedDirectory = chooser.showDialog(stage);
		if(selectedDirectory != null) {
			folderPathField.setText(selectedDirectory.getAbsolutePath());
		}
	}
	
	private void setAppIcon(Stage stage) {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/pdf.png")));
        stage.getIcons().add(icon);
    }
	
	

}