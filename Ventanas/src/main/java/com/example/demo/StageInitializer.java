package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demo.VentanaPrincipal.StageReadyEvent;
import com.example.demo.Controllers.ConfirmBox;
import com.example.demo.Controllers.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

	@Value("classpath:/fxml/Ventana.fxml")
	private Resource ventanaResource;
	
	@Value("classpath:/templates/Estilos/ConfirmBoxStyle.css")
	private Resource ccsConfirm;
	
	private Stage stage; 

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		// TODO Auto-generated method stub
		Parent root;

		FXMLLoader loader;
		try {
			loader = new FXMLLoader(ventanaResource.getURL());
			root = loader.load();
			stage = event.getStage();
			
			stage.setOnCloseRequest(e ->{
				e.consume();
				FXMLController f = loader.getController();
				try {
					f.cerrarTodas();
					closeProgram();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			stage.setTitle("Pantalla principal");
			
			Scene scene = new Scene(root,500,500);
			stage.setScene(scene);
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
