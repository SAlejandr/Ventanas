package com.example.demo.Controllers;


import com.example.demo.dto.SaldoInicialCuentaDTO;
import com.example.demo.model.pojos.SaldoInicialCuenta;
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
public class SaldosInicialesCuentaFXMLController implements Initializable{

	@FXML private TableView<SaldoInicialCuentaDTO> saldosInicialesCuentaTabla;

	private static ObservableList<SaldoInicialCuentaDTO> losSaldosInicialesCuenta = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/saldos/saldoInicial";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losSaldosInicialesCuenta.forEach(System.out::println);
			

			
			TableColumn<SaldoInicialCuentaDTO, String> columna1 = new TableColumn<>("Año");
			TableColumn<SaldoInicialCuentaDTO, String> columna2 = new TableColumn<>("Cuenta");
			TableColumn<SaldoInicialCuentaDTO, BigDecimal> columna3 = new TableColumn<>("Debito");
			TableColumn<SaldoInicialCuentaDTO, BigDecimal> columna4 = new TableColumn<>("Credito");

			columna1.setCellValueFactory(new PropertyValueFactory<>("anno"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("cod_cuenta"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("debito"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("credito"));


			saldosInicialesCuentaTabla.setItems(losSaldosInicialesCuenta);
			saldosInicialesCuentaTabla.getColumns().addAll(columna1, columna2, columna3, columna4);
			
			System.out.println("LOS SALDOS INICIALES CUENTA : " + losSaldosInicialesCuenta.toString() );

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {

		ResponseEntity<SaldoInicialCuenta[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAll", SaldoInicialCuenta[].class);
		Stream <SaldoInicialCuenta> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> losSaldosInicialesCuenta.add(new SaldoInicialCuentaDTO(log)));
		
	}



	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losSaldosInicialesCuenta.clear();

		solicitarListaActualizada();
	}




}
