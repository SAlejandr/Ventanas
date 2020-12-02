package com.example.demo.Controllers;


import java.net.URL;
import java.util.ResourceBundle;

import com.example.demo.model.dto.GetUserDTO;
import com.example.demo.model.dto.UpdateUserPassDTO;
import com.example.demo.util.EncriptarPassword;
import javafx.fxml.Initializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class CambioPasswordFXMLController implements Initializable {
	

	@FXML private Button guardarBoton;
	@FXML private TextField password1Text;
	@FXML private TextField password2Text;
	
	private final String INIT_URL = "http://localhost:8080/pro/administracion/cambiarPassword";
	
	private RestTemplate restTemplate = new RestTemplate();

	@Getter
	@Setter
	private GetUserDTO user;
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		password2Text.setOnKeyReleased(e ->{
			e.consume();
			if(!password2Text.getText().isBlank() && !password2Text.getText().equals(password1Text.getText())){
				password2Text.setStyle("-fx-background-color: red");
			}else{
				password2Text.setStyle("-fx-background-color: green");
			}
		});
	}
	
	@FXML protected void  onGuardarClicked(ActionEvent actionEvent) {
		
		
		String password1 = password1Text.getText().trim();
		String password2 = password2Text.getText().trim();

		if(password1.equals(password2)){

			UpdateUserPassDTO updateUserPassDTO = UpdateUserPassDTO.builder().
					username(user.getUsername()).
					passwordNew(password1).
					passwordNewR(password2).
					build();
			try {
				restTemplate.put(INIT_URL+"/cambio", updateUserPassDTO);


				Alert  alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Proceso de Cambio de Password");

				alert.setContentText("*********Cambio de Password en proceso ****************");
				alert.showAndWait();

			}catch(HttpClientErrorException e) {
				e.printStackTrace();


				Alert  alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Confirmacion password");

				alert.setContentText("Fue errada");
				alert.showAndWait();
			}

		}

   	}

}
