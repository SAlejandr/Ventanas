package com.example.demo.Controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.example.demo.model.dto.GetUserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class FXMLController {
	
	private ArrayList<Stage> arrayStages = Lists.newArrayList();
	@Setter
	@Getter
	private static GetUserDTO userDTO;

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
		System.err.println("Esninvdpiosnipn ------>"+userDTO);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TablasBasicas/DocCapturaMovimientosVentana.fxml"));
		Parent root = loader.load();
		DocCapturaDeMovimientosFXMLController controller = loader.getController();
		controller.setUsuario(userDTO);


		Stage stage = new Stage();
		stage.setOnCloseRequest(e -> {
			if(controller.advertir()) {
				controller.guardarYSalir();
				stage.close();
			}
		});
		Scene scene = new Scene(root,1280,720);
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
		Scene scene = new Scene(root,600,500);
		stage.setTitle("Log de Accesos al Sistema");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void LogSucesosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Administracion/LogSucesosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,600,500);
		stage.setTitle("Log de Sucesos del Sistema");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void CapturarMovimientoVentana() throws IOException {					//******************************************************************
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Archivo/CapturarMovimientoVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Log de Sucesos del Sistema");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void SaldosCuentaVentana() throws IOException {					//******************************************************************
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasEspeciales/SaldosCuentaVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,750,500);
		stage.setTitle("Saldos de Cuenta del Mes");
		stage.setScene(scene);
		stage.show();
		
	}

	@FXML public void SaldosInicialesCuentaVentana() throws IOException {					//******************************************************************

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasEspeciales/SaldosInicialesCuentaVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,750,500);
		stage.setTitle("Saldos Iniciales en Cuentas para el Mes");
		stage.setScene(scene);
		stage.show();

	}

	@FXML public void SaldosInicialesCentroVentana() throws IOException {					//******************************************************************

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasEspeciales/SaldosInicialesCentroVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,750,500);
		stage.setTitle("Saldos Iniciales para Cuenta y Centro del Mes");
		stage.setScene(scene);
		stage.show();

	}

	@FXML public void SaldosInicialesTerceroVentana() throws IOException {					//******************************************************************

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasEspeciales/SaldosInicialesTerceroVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,750,500);
		stage.setTitle("Saldos Iniciales para Cuenta y Tercero del Mes");
		stage.setScene(scene);
		stage.show();

	}

	@FXML public void SucesosVentana() throws IOException {					//******************************************************************

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasEspeciales/SucesosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Tabla de Sucesos");
		stage.setScene(scene);
		stage.show();

	}

	@FXML public void SaldosCentroVentana() throws IOException {					//******************************************************************
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasEspeciales/SaldosCentroVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,800,500);
		stage.setTitle("Saldos por Cuenta y Centro del Mes");
		stage.setScene(scene);
		stage.show();
		
	}

	@FXML public void SaldosTerceroVentana() throws IOException {					//******************************************************************

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/TablasEspeciales/SaldosTerceroVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,800,500);
		stage.setTitle("Saldos por Cuenta y Tercero del Mes");
		stage.setScene(scene);
		stage.show();

	}
	
	@FXML public void RemayorizacionVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProcesosEspeciales/RemayorizacionVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Proceso de Remayorización");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void BalancePruebaVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/reportes/BalancePruebaVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Reporte de Balance de Prueba");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void CuentasNoAfectablesVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/CuentasNoAfectablesVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría de Cuentas no Afectables");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void MovimientosSinValorVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/MovimientosSinValorVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría a  Movimientos sin valor");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void DocumentosSinMovimientosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/DocumentosSinMovimientosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría a  Documentos sin Movimientos");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void DebitosDiferenteCreditosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/DebitosDiferenteCreditosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría a  Documentos con Debitos diferente a Creditos");
		stage.setScene(scene);
		stage.show();
		
	}	
	
	@FXML public void MovimientosSinTercerosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/MovimientosSinTercerosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría a Movimientos sin Terceros");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void MovimientosConTercerosInnecesariosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/MovimientosConTercerosInnecesariosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría a Movimientos con Terceros innecesarios");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void MovimientosSinCCostosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/MovimientosSinCCostosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría a Movimientos sin Centros de Costos");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void MovimientosConCCostosInnecesariosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/MovimientosConCCostosInnecesariosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría a Movimientos con Centros de Costos Innecesarios");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void AuditoriaTotalVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Auditoria/AuditoriaTotalVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Auditoría Total");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void UsuariosVentana() throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Administracion/UsuariosVentana.fxml"));

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Administracion de Usuarios");
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML public void CambioPasswordVentana() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Administracion/CambioPasswordVentana.fxml"));
		Parent root = loader.load();

		CambioPasswordFXMLController controller = loader.getController();

		controller.setUser(userDTO);

		Stage stage = new Stage();
		Scene scene = new Scene(root,500,500);
		stage.setTitle("Cambio de password");
		stage.setScene(scene);
		stage.show();
		
	}
	
	
}
