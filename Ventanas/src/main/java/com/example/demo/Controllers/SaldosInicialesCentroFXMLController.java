package com.example.demo.Controllers;


import com.example.demo.dto.SaldoInicialCentroDTO;
import com.example.demo.model.pojos.SaldoInicialCentroDeCostos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@Component
public class SaldosInicialesCentroFXMLController implements Initializable{

	@FXML private TableView<SaldoInicialCentroDTO> saldosInicialesCentroTabla;

	private static ObservableList<SaldoInicialCentroDTO> losSaldosInicialesCentro = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/saldos/saldoInicialCCostos";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losSaldosInicialesCentro.forEach(System.out::println);
			

			
			TableColumn<SaldoInicialCentroDTO, Integer> columna1 = new TableColumn<>("Año");
			TableColumn<SaldoInicialCentroDTO, String> columna2 = new TableColumn<>("Cuenta");
			TableColumn<SaldoInicialCentroDTO, Integer> columna3 = new TableColumn<>("Centro");
			TableColumn<SaldoInicialCentroDTO, BigDecimal> columna4 = new TableColumn<>("Debito");
			TableColumn<SaldoInicialCentroDTO, BigDecimal> columna5 = new TableColumn<>("Credito");

			columna1.setCellValueFactory(new PropertyValueFactory<>("anno"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("cod_cuenta"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("centro"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("debito"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("credito"));


			saldosInicialesCentroTabla.setItems(losSaldosInicialesCentro);
			saldosInicialesCentroTabla.getColumns().addAll(columna1, columna2, columna3, columna4, columna5);
			
			System.out.println("LOS SALDOS INICIALES CENTRO : " + losSaldosInicialesCentro.toString() );

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {

		ResponseEntity<SaldoInicialCentroDeCostos[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAll", SaldoInicialCentroDeCostos[].class);
		Stream <SaldoInicialCentroDeCostos> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> losSaldosInicialesCentro.add(new SaldoInicialCentroDTO(log)));
		
	}



	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losSaldosInicialesCentro.clear();

		solicitarListaActualizada();
	}




}
