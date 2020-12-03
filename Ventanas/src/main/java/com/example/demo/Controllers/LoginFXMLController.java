package com.example.demo.Controllers;


import com.example.demo.model.dto.GetUserDTO;
import com.example.demo.model.dto.LoginDTO;
import com.example.demo.util.EncriptarPassword;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginFXMLController implements Initializable{

	@Getter
	@Setter
	public Button loginBoton;
	public Button logoutBoton;
	public TextField usernameText;
	public PasswordField passwordPWD;

	private final String INIT_URL = "http://localhost:8080/";

	private RestTemplate restTemplate = new RestTemplate();

	@Setter
	@Getter
	private GetUserDTO  userDTO;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {

			System.out.println("·APENAS VOY A ENTRAR");

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}


	public boolean onLoginBotonClicked() {

		LoginDTO login = new LoginDTO(usernameText.getText(), passwordPWD.getText().trim());

		boolean acertado = false;
		try {
			System.out.println(login);
			System.out.println(EncriptarPassword.encriptarPassword(passwordPWD.getText().trim()));
			ResponseEntity<GetUserDTO> respuesta = restTemplate.postForEntity(INIT_URL + "login",login, GetUserDTO.class );
			System.out.println("RESPUESTA : " + respuesta.getBody());
			userDTO = respuesta.getBody();
			acertado = true;
		}catch(HttpClientErrorException e){
			e.printStackTrace();

		}
		return  acertado;
	}

	public void onLogoutBotonClicked(ActionEvent actionEvent) {


	}
}
