package com.example.demo.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;


public class LoginController implements Initializable{

	
	@FXML TextField username;
	@FXML PasswordField passwd;
	
	private final String USER ="ADMIN", PASSWORD = "123";
	@Getter
	@FXML Button loginButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		username.setOnAction(e ->{
			e.consume();
			if(username.getText().isBlank())
				username.setStyle("-fx-background-color : red");
		});
		
	}

	@FXML public boolean logear() {
		
		if(username.getText().equals(USER) && passwd.getText().equals(PASSWORD)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("CORRECTO");
			alert.setContentText("Has acertado, crack");
			return true;
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("MAL");
			alert.setContentText("No has acertado, crack");
			return false;
		}
		
	}

}
