package com.example.demo.Controllers;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.w3c.dom.css.CSSCharsetRule;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class ConfirmBox {
	
	static boolean respuesta;
	
	public static boolean display(String title, String text, String css) throws IOException {
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		Label label = new Label(text);
		
		Button botonSi = new Button("Si");
		Button botonNo = new Button("No");
	
		
		botonSi.setOnAction(e -> {
			respuesta = true;
			window.close();
		});
		
		botonNo.setOnAction(e -> {
			respuesta = false;
			window.close();
		});
		
	
		
		VBox principalLayout = new VBox();
		HBox layout = new HBox();
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(botonSi, botonNo);
		principalLayout.getChildren().addAll(label, layout);
	
		Scene scene = new Scene(principalLayout);
		
		scene.getStylesheets().add(css);
		window.setScene(scene);
		window.showAndWait();
		
		return respuesta;
		
	}

}

