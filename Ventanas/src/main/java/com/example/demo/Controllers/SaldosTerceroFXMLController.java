package com.example.demo.Controllers;


import com.example.demo.dto.SaldoTerceroDTO;
import com.example.demo.model.pojos.SaldoTercero;
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
public class SaldosTerceroFXMLController implements Initializable{

	@FXML private TableView<SaldoTerceroDTO> saldosTerceroTabla;

	private static ObservableList<SaldoTerceroDTO> losSaldosTercero = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/saldos/saldoTercero";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losSaldosTercero.forEach(System.out::println);
			

			//AQUI TENDRIA QUE CREAR UNA OBSERVABLE LIST CON SOLO DATOS SIMPLES
			
			TableColumn<SaldoTerceroDTO, String> columna1 = new TableColumn<>("Año");
			TableColumn<SaldoTerceroDTO, Integer> columna2 = new TableColumn<>("Mes");
			TableColumn<SaldoTerceroDTO, String> columna3 = new TableColumn<>("Cuenta");
			TableColumn<SaldoTerceroDTO, Integer> columna4 = new TableColumn<>("tercero");
			TableColumn<SaldoTerceroDTO, BigDecimal> columna5 = new TableColumn<>("Mvto_Debito");
			TableColumn<SaldoTerceroDTO, BigDecimal> columna6 = new TableColumn<>("Mvto_Credito");
			TableColumn<SaldoTerceroDTO, BigDecimal> columna7 = new TableColumn<>("Total_Debito");
			TableColumn<SaldoTerceroDTO, BigDecimal> columna8 = new TableColumn<>("Todal_Credito");
			
			columna1.setCellValueFactory(new PropertyValueFactory<>("anno"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("mes"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("cod_cuenta"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("tercero"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("suma_debito"));
			columna6.setCellValueFactory(new PropertyValueFactory<>("suma_credito"));
			columna7.setCellValueFactory(new PropertyValueFactory<>("total_debito"));
			columna8.setCellValueFactory(new PropertyValueFactory<>("total_credito"));
			

			saldosTerceroTabla.setItems(losSaldosTercero);
			saldosTerceroTabla.getColumns().addAll(columna1, columna2, columna3, columna4, columna5, columna6, columna7, columna8);
			
			System.out.println("LOS saLDOS cUENYA : " + losSaldosTercero.toString() );

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {				//QUITAR LOS COMENTARIOS

		ResponseEntity<SaldoTercero[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAll", SaldoTercero[].class);
		Stream <SaldoTercero> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> losSaldosTercero.add(new SaldoTerceroDTO(log)));
		
	}

	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losSaldosTercero.clear();

		solicitarListaActualizada();
	}

}
