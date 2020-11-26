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


public class CopiaSeguridadFXMLController{
	

	@FXML private TextArea mensajeArea;
	@FXML private Button generarBoton;
	
	private final String INIT_URL = "http://localhost:8080/pro/Copias";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	


	
	
	@FXML protected void  onGenerarClicked(ActionEvent actionEvent) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		LocalDateTime fecha = LocalDateTime.now();
		String fechaString = "c:\\copias\\conta-" + fecha.format(formatter) + ".sql";
		System.out.println("La fecha a enviar es : " + fechaString);
		try {
			  restTemplate.put(INIT_URL+"/generar", fechaString);
		        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
		        alert.setTitle("Copia de Seguridad");
		        
		        alert.setContentText("Copia realizada. Archivo : " + fechaString);
		        alert.showAndWait();
			  
		}catch(HttpClientErrorException e) {
			e.printStackTrace();
	        Alert  alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error Copia de Seguridad");
	        
	        alert.setContentText("Error al generar la Copia de Seguridad");
	        alert.showAndWait();
		}
		
		
                
   	}



}
