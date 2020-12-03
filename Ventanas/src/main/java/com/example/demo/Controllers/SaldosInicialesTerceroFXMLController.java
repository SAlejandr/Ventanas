package com.example.demo.Controllers;


import com.example.demo.dto.SaldoInicialTerceroDTO;
import com.example.demo.model.pojos.SaldoInicialTercero;
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
public class SaldosInicialesTerceroFXMLController implements Initializable{

	@FXML private TableView<SaldoInicialTerceroDTO> saldosInicialesTerceroTabla;

	private static ObservableList<SaldoInicialTerceroDTO> losSaldosInicialesTercero = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/saldos/saldoInicialTercero";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losSaldosInicialesTercero.forEach(System.out::println);
			

			
			TableColumn<SaldoInicialTerceroDTO, Integer> columna1 = new TableColumn<>("Año");
			TableColumn<SaldoInicialTerceroDTO, String> columna2 = new TableColumn<>("Cuenta");
			TableColumn<SaldoInicialTerceroDTO, String> columna3 = new TableColumn<>("Tercero");
			TableColumn<SaldoInicialTerceroDTO, BigDecimal> columna4 = new TableColumn<>("Debito");
			TableColumn<SaldoInicialTerceroDTO, BigDecimal> columna5 = new TableColumn<>("Credito");

			columna1.setCellValueFactory(new PropertyValueFactory<>("anno"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("cod_cuenta"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("tercero"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("debito"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("credito"));


			saldosInicialesTerceroTabla.setItems(losSaldosInicialesTercero);
			saldosInicialesTerceroTabla.getColumns().addAll(columna1, columna2, columna3, columna4, columna5);
			
			System.out.println("LOS SALDOS INICIALES TERCERO : " + losSaldosInicialesTercero.toString() );

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {

		ResponseEntity<SaldoInicialTercero[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAll", SaldoInicialTercero[].class);
		Stream <SaldoInicialTercero> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> losSaldosInicialesTercero.add(new SaldoInicialTerceroDTO(log)));
		
	}



	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losSaldosInicialesTercero.clear();

		solicitarListaActualizada();
	}




}
