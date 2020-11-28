package com.example.demo.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.dto.GetUserDTO;
import com.example.demo.model.dto.LoginUserDTO;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;


public class LoginFXMLController implements Initializable{

	
	@FXML TextField username;
	@FXML PasswordField passwd;
	
	private final String USER ="ADMIN", PASSWORD = "123";
	@Getter
	@FXML Button loginButton;
	
	@Getter
	private static GetUserDTO user;
	
	private RestTemplate restTemplate = new RestTemplate();

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
		
		LoginUserDTO loginDTO = LoginUserDTO.builder().
				username(username.getText()).
				pass(passwd.getText()).
				build();
		
		try {
			ResponseEntity<GetUserDTO> respuesta = restTemplate.postForEntity("http://localhost:8080/pro/login", loginDTO, GetUserDTO.class);
			
			user = respuesta.getBody();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("CORRECTO");
			alert.setContentText("Has acertado, crack");
			return true;
		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("MAL");
			alert.setContentText("No has acertado, crack");
			return false;
		
		}
			
	}

}
