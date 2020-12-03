package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

@SpringBootApplication
public class VentanasApplication {
	
	public static void main(String[] args) {
		//SpringApplication.run(VentanasApplication.class, args);
		
		Application.launch(VentanaPrincipal.class, args);
	}

	

}
