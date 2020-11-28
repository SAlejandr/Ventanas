package com.example.demo.Controllers;


import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.SaldoCuentaDTO;
import com.example.demo.model.pojos.SaldoCuenta;
import com.example.demo.model.pojos.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class SaldosCentroFXMLController implements Initializable{

	@FXML private TableView<SaldoCuentaDTO> saldosCuentaTabla;

	private static ObservableList<SaldoCuentaDTO> losSaldosCuenta = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/saldos/saldoCuenta";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losSaldosCuenta.forEach(System.out::println);
			
			/*ObservableList<SaldosCuenta> losSaldosCuentas1 = FXCollections.observableArrayList();
			
			for(int i =0; i< losSaldosCuentas.size(); i++) {
				SaldosCuenta aux1 = losSaldosCuentas.get(i);
				
				LocalDateTime ingreso = aux1.getInstanteDeAcceso();
				LocalDateTime salida = aux1.getInstanteDeSalida();
				Long consecutivo = aux1.getConsecutivo();
				Integer idUsuario = aux1.getUsuario().getId(); 
				
			}*/
			
			
			//AQUI TENDRIA QUE CREAR UNA OBSERVABLE LIST CON SOLO DATOS SIMPLES
			
			TableColumn<SaldoCuentaDTO, String> columna1 = new TableColumn<>("Cuenta");
			TableColumn<SaldoCuentaDTO, Integer> columna2 = new TableColumn<>("Año");
			TableColumn<SaldoCuentaDTO, String> columna3 = new TableColumn<>("Mes");
			TableColumn<SaldoCuentaDTO, BigDecimal> columna4 = new TableColumn<>("Mvto_Debito");
			TableColumn<SaldoCuentaDTO, BigDecimal> columna5 = new TableColumn<>("Mvto_Credito");
			TableColumn<SaldoCuentaDTO, BigDecimal> columna6 = new TableColumn<>("Total_Debito");
			TableColumn<SaldoCuentaDTO, BigDecimal> columna7 = new TableColumn<>("Todal_Credito");
			
			columna1.setCellValueFactory(new PropertyValueFactory<>("anno"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("mes"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("cod_cuenta"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("suma_debito"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("suma_credito"));
			columna6.setCellValueFactory(new PropertyValueFactory<>("saldo_debito"));
			columna7.setCellValueFactory(new PropertyValueFactory<>("saldo_credito"));
			

			saldosCuentaTabla.setItems(losSaldosCuenta);
			saldosCuentaTabla.getColumns().addAll(columna1, columna2, columna3, columna4, columna5);
			
			System.out.println("LOS saLDOS cUENYA : " + losSaldosCuenta.toString() );

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {

		ResponseEntity<SaldoCuenta[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAll", SaldoCuenta[].class);
		Stream <SaldoCuenta> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> losSaldosCuenta.add(new SaldoCuentaDTO(log)));
		
	}



	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losSaldosCuenta.clear();

		solicitarListaActualizada();
	}




}
