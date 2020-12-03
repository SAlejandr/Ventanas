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

import com.example.demo.dto.LogAccesoDTO;
import com.example.demo.model.pojos.LogAcceso;
import com.example.demo.model.pojos.Usuario;
import com.google.common.collect.Sets;

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
public class LogAccesoFXMLController implements Initializable{

	@FXML private TableView<LogAccesoDTO> logAccesoTabla;

	@FXML private TextField id_borrado;

	private static ObservableList<LogAccesoDTO> losLogAccesos = FXCollections.observableArrayList();

	private HashSet<LogAcceso> logs = Sets.newHashSet();
	private final String INIT_URL = "http://localhost:8080/pro/logAcceso";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {

			System.out.println("·APENAS VOY A ENTRAR");
			solicitarListaActualizada();

			losLogAccesos.forEach(System.out::println);
			
		
			
			//AQUI TENDRIA QUE CREAR UNA OBSERVABLE LIST CON SOLO DATOS SIMPLES
			
			TableColumn<LogAccesoDTO, LocalDateTime> columna4 = new TableColumn<>("Ingreso");
			TableColumn<LogAccesoDTO, LocalDateTime> columna5 = new TableColumn<>("Salida");
			TableColumn<LogAccesoDTO, Long> columna1 = new TableColumn<>("Nº");
			TableColumn<LogAccesoDTO, String> columna3 = new TableColumn<>("Usuario");
			TableColumn<LogAccesoDTO, Integer> columna2 = new TableColumn<>("ID");

			columna4.setCellValueFactory(new PropertyValueFactory<>("instanteDeAcceso"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
			columna1.setCellValueFactory(new PropertyValueFactory<>("consecutivo"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("instanteDeSalida"));
			
			System.out.println("MIS LOGACCESOS : " + losLogAccesos.toString());

			logAccesoTabla.setItems(losLogAccesos);
			logAccesoTabla.getColumns().addAll(columna1, columna2, columna3, columna4, columna5);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {
		losLogAccesos.clear();

		ResponseEntity<LogAcceso[]> respuesta = restTemplate.getForEntity(INIT_URL + "/get/all", LogAcceso[].class);
		Stream <LogAcceso> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(log -> {logs.add(log);
		System.err.println(log.toString());
			});
		logs.stream().forEach(log -> losLogAccesos.add(new LogAccesoDTO(log)));
	}



	@FXML protected void refrescarLista(ActionEvent actionEvent) {


		solicitarListaActualizada();
	}




}
