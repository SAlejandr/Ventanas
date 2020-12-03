package com.example.demo.Controllers;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.dto.Remayorizacion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class RemayorizacionFXMLController{
	

	@FXML private TextArea mensajeArea;
	@FXML private Button remayorizarBoton;
	@FXML private TextField annoText;
	@FXML private TextField mesInicialText;
	@FXML private TextField mesFinalText;
	
	private final String INIT_URL = "http://localhost:8080/pro/procesos/remayorizacion";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	


	
	
	@FXML protected void  onGenerarClicked(ActionEvent actionEvent) {
		
		
		int anno = Integer.parseInt(annoText.getText().trim());
		int mesInicial = Integer.parseInt(mesInicialText.getText().trim());
		int mesFinal = Integer.parseInt(mesFinalText.getText().trim());
		
		try {
			Remayorizacion remayorizacion = new Remayorizacion(mesInicial, mesFinal, anno);
			  restTemplate.postForEntity(INIT_URL+"/generar", remayorizacion, Remayorizacion.class);
		        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
		        alert.setTitle("Proceso de Remayorización");
		        
		        alert.setContentText("Remayorizacion realizada con éxito");
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
