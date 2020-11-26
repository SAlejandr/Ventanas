package com.example.demo.Controllers;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.LogSucesoDTO;
import com.example.demo.model.pojos.LogSuceso;
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
public class LogSucesoFXMLController implements Initializable{

	@FXML private TableView<LogSucesoDTO> logSucesoTabla;

	@FXML private TextField id_borrado;

	private static ObservableList<LogSucesoDTO> losLogSucesos = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/logSuceso";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losLogSucesos.forEach(System.out::println);

			/*ObservableList<LogSuceso> losLogSucesos1 = FXCollections.observableArrayList();

			for(int i =0; i< losLogSucesos.size(); i++) {
				LogSuceso aux1 = losLogSucesos.get(i);

				LocalDateTime ingreso = aux1.getInstanteDeAcceso();
				LocalDateTime salida = aux1.getInstanteDeSalida();
				Long consecutivo = aux1.getConsecutivo();
				Integer idUsuario = aux1.getUsuario().getId(); 

				LogSuceso aux = 
			}*/


			//AQUI TENDRIA QUE CREAR UNA OBSERVABLE LIST CON SOLO DATOS SIMPLES
			TableColumn<LogSucesoDTO, Long> columna1 = new TableColumn<>("Consecutivo");
			TableColumn<LogSucesoDTO, Integer> columna2 = new TableColumn<>("ID Usuario");
			TableColumn<LogSucesoDTO, String> columna3 = new TableColumn<>("Usuario");
			TableColumn<LogSucesoDTO, Integer> columna4 = new TableColumn<>("ID Tabla");
			TableColumn<LogSucesoDTO, String> columna5 = new TableColumn<>("Tabla");
			TableColumn<LogSucesoDTO, LocalDateTime> columna6 = new TableColumn<>("Fecha-Hora");

			columna1.setCellValueFactory(new PropertyValueFactory<>("consecutivo"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("idTabla"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("nombre"));			
			columna5.setCellValueFactory(new PropertyValueFactory<>("instante"));

			System.out.println("MIS LOGSUCESOS : " + losLogSucesos.toString());

			logSucesoTabla.setItems(losLogSucesos);
			logSucesoTabla.getColumns().addAll(columna1, columna2, columna3, columna4, columna5, columna6);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {

		ResponseEntity<LogSuceso[]> respuesta = restTemplate.getForEntity(INIT_URL + "/get/all", LogSuceso[].class);
		Stream <LogSuceso> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> losLogSucesos.add(new LogSucesoDTO(log)));

	}



	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losLogSucesos.clear();

		solicitarListaActualizada();
	}




}
