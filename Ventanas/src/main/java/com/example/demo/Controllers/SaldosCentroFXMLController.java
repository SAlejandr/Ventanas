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

import com.example.demo.dto.SaldoCentroDTO;
import com.example.demo.model.pojos.SaldoCentroDeCostos;
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

	@FXML private TableView<SaldoCentroDTO> saldosCentroTabla;

	private static ObservableList<SaldoCentroDTO> losSaldosCentro = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/saldos/saldoCCosto";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losSaldosCentro.forEach(System.out::println);
			
			ObservableList<SaldoCentroDeCostos> losSaldosCentros1 = FXCollections.observableArrayList();
			
			/*for(int i =0; i< losSaldosCentros1.size(); i++) {
				SaldoCentroDeCostos aux1 = losSaldosCentros1.get(i);
				
				LocalDateTime ingreso = aux1.getInstanteDeAcceso();
				LocalDateTime salida = aux1.getInstanteDeSalida();
				Long consecutivo = aux1.getConsecutivo();
				Integer idUsuario = aux1.getUsuario().getId(); 
				
			}*/
			
			
			//AQUI TENDRIA QUE CREAR UNA OBSERVABLE LIST CON SOLO DATOS SIMPLES
			
			TableColumn<SaldoCentroDTO, String> columna1 = new TableColumn<>("Año");
			TableColumn<SaldoCentroDTO, Integer> columna2 = new TableColumn<>("Mes");
			TableColumn<SaldoCentroDTO, String> columna3 = new TableColumn<>("Cuenta");
			TableColumn<SaldoCentroDTO, Integer> columna4 = new TableColumn<>("Centro");
			TableColumn<SaldoCentroDTO, BigDecimal> columna5 = new TableColumn<>("Mvto_Debito");
			TableColumn<SaldoCentroDTO, BigDecimal> columna6 = new TableColumn<>("Mvto_Credito");
			TableColumn<SaldoCentroDTO, BigDecimal> columna7 = new TableColumn<>("Total_Debito");
			TableColumn<SaldoCentroDTO, BigDecimal> columna8 = new TableColumn<>("Todal_Credito");
			
			columna1.setCellValueFactory(new PropertyValueFactory<>("anno"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("mes"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("cod_cuenta"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("centro"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("suma_debito"));
			columna6.setCellValueFactory(new PropertyValueFactory<>("suma_credito"));
			columna7.setCellValueFactory(new PropertyValueFactory<>("total_debito"));
			columna8.setCellValueFactory(new PropertyValueFactory<>("total_credito"));
			

			saldosCentroTabla.setItems(losSaldosCentro);
			saldosCentroTabla.getColumns().addAll(columna1, columna2, columna3, columna4, columna5, columna6, columna7, columna8);
			
			System.out.println("LOS saLDOS cUENYA : " + losSaldosCentro.toString() );

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {				//QUITAR LOS COMENTARIOS

		ResponseEntity<SaldoCentroDeCostos[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAll", SaldoCentroDeCostos[].class);
		Stream <SaldoCentroDeCostos> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> losSaldosCentro.add(new SaldoCentroDTO(log)));
		
	}



	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losSaldosCentro.clear();

		solicitarListaActualizada();
	}




}
