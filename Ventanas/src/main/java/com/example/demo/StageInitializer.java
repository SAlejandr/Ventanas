package com.example.demo;

import java.io.IOException;

import com.example.demo.Controllers.LoginFXMLController;
import javafx.scene.control.Alert;
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

	@Value("classpath:/fxml/Login.fxml")
	private Resource loginResource;

	@Value("classpath:/fxml/Ventana.fxml")
	private Resource ventanaResource;
	
	@Value("classpath:/templates/Estilos/ConfirmBoxStyle.css")
	private Resource ccsConfirm;
	
	private Stage stage;
	private Scene sceneLogin, sceneVentana;
	private boolean auntenticado;

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		// TODO Auto-generated method stub
		Parent root;

		auntenticado =false;
		FXMLLoader loader;
		FXMLLoader loaderVentana;
		try {
			loaderVentana = new FXMLLoader(ventanaResource.getURL());
			loader = new FXMLLoader(loginResource.getURL());

			root = loader.load();
			stage = event.getStage();

			sceneLogin = new Scene(root,1280,720);

			sceneVentana = new Scene(loaderVentana.load(), 1280,720);

			LoginFXMLController loginController = loader.getController();

			loginController.getLoginBoton().setOnMouseClicked(e ->{
				e.consume();
				auntenticado = loginController.onLoginBotonClicked();
				if(auntenticado) {
					stage.setScene(sceneVentana);
					FXMLController f = loaderVentana.getController();
					f.setUserDTO(loginController.getUserDTO());
				}else{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("No existe usuario o contraseña");
					alert.showAndWait();
				}
			});
			
			stage.setOnCloseRequest(e ->{
				e.consume();
				FXMLController f = loaderVentana.getController();
				try {
					if(auntenticado)
						f.cerrarTodas();
					closeProgram();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			stage.setTitle("Pantalla principal");

			stage.setScene(sceneLogin);
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
