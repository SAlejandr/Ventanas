package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demo.VentanaPrincipal.StageReadyEvent;
import com.example.demo.Controllers.ConfirmBox;
import com.example.demo.Controllers.FXMLController;
import com.example.demo.Controllers.LoginFXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

	@Value("classpath:/fxml/Ventana.fxml")
	private Resource ventanaResource;
	@Value("classpath:/fxml/Login.fxml")
	private Resource loginResource;
	
	@Value("classpath:/templates/Estilos/ConfirmBoxStyle.css")
	private Resource ccsConfirm;
	
	private Stage stage; 
	private Scene scene1, scene2; 
	private boolean acertar;

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		// TODO Auto-generated method stub
		Parent root;

		FXMLLoader loader;
		FXMLLoader loaderW;
		try {
			loader = new FXMLLoader(loginResource.getURL());
			loaderW = new FXMLLoader(ventanaResource.getURL());
			
			stage = event.getStage();
			
			root = loader.load();
			
			stage.setTitle("Pantalla principal");
			
			scene1 = new Scene(root);
			stage.setScene(scene1);
			
			
			
			acertar = false;
			
			LoginFXMLController login = loader.getController();
			
			login.getLoginButton().setOnMouseClicked(e -> {
				e.consume();
				acertar = login.logear();
				if(acertar) {
					try {
						scene2 = new Scene(loaderW.load(), 1280, 800);
						stage.setScene(scene2);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
					
			});
			
			stage.setOnCloseRequest(e ->{
				e.consume();
				FXMLController f = loaderW.getController();
				try {
					if(acertar)
						f.cerrarTodas();
					closeProgram();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	

	private void closeProgram() throws IOException {
		// TODO Auto-generated method stub
		
		boolean salir = ConfirmBox.display("Salir", "¿Desea salir?", ccsConfirm.getURL().toExternalForm());
		if(salir)
			stage.close();
	}
	
	

}
