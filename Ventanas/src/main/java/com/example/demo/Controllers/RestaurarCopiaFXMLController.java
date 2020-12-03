package com.example.demo.Controllers;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class RestaurarCopiaFXMLController{
	

	@FXML private TextArea mensajeArea;
	@FXML private Button restaurarBoton;
	@FXML private TextField nombreArchivoText;
	
	
	private final String INIT_URL = "http://localhost:8080/pro/Copias";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	


	
	
	@FXML protected void  onRestaurarClicked(ActionEvent actionEvent) {
		

		try {
			  restTemplate.put(INIT_URL+"/restaurar", nombreArchivoText.getText());
		        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
		        alert.setTitle("Restaurar Copia de Seguridad");
		        
		        alert.setContentText("!Restauración realizada!. Archivo : " + nombreArchivoText.getText());
		        alert.showAndWait();
			  
		}catch(HttpClientErrorException e) {
			e.printStackTrace();
	        Alert  alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("restaurar Copia de Seguridad");
	        
	        alert.setContentText("Error al intentar restaurar la Copia de Seguridad");
	        alert.showAndWait();
		}
		
		
                
   	}



}
