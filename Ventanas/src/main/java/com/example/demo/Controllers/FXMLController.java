package com.example.demo.Controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;

@Component
public class FXMLController {
	
	private ArrayList<Stage> arrayStages = Lists.newArrayList();
	
	public void cerrarTodas() {
		for (Stage s : arrayStages) {
			
			s.close();
			arrayStages.remove(s);
		}
	}

	@FXML protected void handleSubmitButtonAction(ActionEvent event) {

		boolean salir;
		try {
			salir = ConfirmBox.display("Exit", "¿Desea salir?", getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

			if(salir)
				System.exit(1);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	



	@FXML protected void annoVentana(ActionEvent event) throws IOException {


		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/AnnoVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Año");
		stage.setScene(scene);
		stage.show();
	}

	@FXML protected void centroDeCosVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/CentroDeCostosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);

		stage.setTitle("Centro de costos");
		stage.setScene(scene);
		stage.show();
	}
	@FXML protected void ciudadesVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/CiudadesVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Ciudades");
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML public void capturarMovimientos() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/DocCapturaMovimientos.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,1280,800);
		stage.setTitle("Captura de Movimientos");
		stage.setScene(scene);
		stage.show();
	}	
	
	@FXML protected void departamentoVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/DepartamentosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Departamentos");
		stage.setScene(scene);
		stage.show();
	}
	@FXML protected void documentoVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/DocumentosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Documentos");
		stage.setScene(scene);
		stage.show();
	}
	@FXML protected void mesVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/MesVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Mes");
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML protected void responsableVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/ResponsablesVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Responsables");
		stage.setScene(scene);
		stage.show();
	}

	@FXML protected void terceroVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/TercerosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Terceros");
		stage.setScene(scene);
		stage.show();
	}
	@FXML protected void tipoDocumentoVentana(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/TipoDocumentoVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Tipo de documento");
		stage.setScene(scene);
		stage.show();
	}

	@FXML public void cuentasVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasBasicas/CuentaVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Cuenta");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void activarMesVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Archivo/ActivarMesVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Activar Mes");
		stage.setScene(scene);
		stage.show();
		
	}

	@FXML public void activarAnnoVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Archivo/ActivarAnnoVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Activar Año");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void CompaniaVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Archivo/CompaniaVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Datos de la Compañía");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void CopiaSeguridadVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProcesosEspeciales/CopiaSeguridadVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Realizar Copia de Seguridad");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void RestaurarCopiaVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProcesosEspeciales/RestaurarCopiaVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Restaurar Copia de Seguridad");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void LogAccesoVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Administracion/LogAccesoVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Log de Accesos al Sistema");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void LogSucesosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Administracion/LogSucesosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Log de Sucesos del Sistema");
		stage.setScene(scene);
		stage.show();
		
	}

	
}
