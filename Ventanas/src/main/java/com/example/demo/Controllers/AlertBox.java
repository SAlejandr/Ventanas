package com.example.demo.Controllers;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	public static void display(String title, String text) throws IOException {

		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinHeight(200);
		Label label = new Label(text);

		Button botonAceptar = new Button("Aceptar");

		botonAceptar.setOnAction(e -> {
			window.close();
		});

		VBox principalLayout = new VBox();
		HBox layout = new HBox();
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(botonAceptar);
		principalLayout.getChildren().addAll(label, layout);

		Scene scene = new Scene(principalLayout);

		window.setScene(scene);
		window.showAndWait();

	}
}
