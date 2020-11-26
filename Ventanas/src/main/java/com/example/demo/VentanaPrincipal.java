package com.example.demo;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VentanaPrincipal extends Application {

	private ConfigurableApplicationContext context;
	
	
	@Override
	public void start(Stage primaryStage) {
		
		context.publishEvent(new StageReadyEvent(primaryStage));
	}
	
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
		context = new SpringApplicationBuilder(VentanasApplication.class).run();

	}


	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		context.close();
		Platform.exit();
	}

	static class StageReadyEvent extends ApplicationEvent{

		public StageReadyEvent(Stage stage) {
			super(stage);
			// TODO Auto-generated constructor stub
		}

		public Stage getStage() {
			// TODO Auto-generated method stub
			return ((Stage) getSource()); 
		}
	}
	
}
