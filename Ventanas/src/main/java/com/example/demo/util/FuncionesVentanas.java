package com.example.demo.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.example.demo.Controllers.ConfirmBox;

import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;

public class FuncionesVentanas {

	@Value("classpath:/templates/Estilos/ConfirmBoxStyle.css")
	private static Resource ccsConfirm;

	public static void closeWindows() {

		boolean salir;
		try {
			salir = ConfirmBox.display("Exit", "¿Desea salir?",  ccsConfirm.getURL().toExternalForm());

			if(salir)
				System.exit(1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void marcarErrorInt(TextField text) {

		try {

			int i = Integer.parseUnsignedInt(text.getText());
			text.setStyle("");
		}catch (NumberFormatException e) {
			// TODO: handle exception
			text.setStyle("-fx-background-color: red");
		}

	}

	public static void marcarErrorFloat(TextField text) {

		try {

			float i = Float.parseFloat(text.getText());
			text.setStyle("");
		}catch (NumberFormatException e) {
			// TODO: handle exception
			text.setStyle("-fx-background-color: red");
		}

	}

	public static void marcarErrorLongitud(TextField text, short longitud) {

		String s = text.getText();

		if(s.length() != longitud) {
			text.setStyle("-fx-background-color: red");
		}else {

			text.setStyle("");
		}
	}

	public static void marcarErrorPocaLogitud(TextField text, short longitud) {
		String s = text.getText();

		if(s.length() <= longitud) {
			text.setStyle("-fx-background-color: red");
		}else {

			text.setStyle("");
		}
	}

}
