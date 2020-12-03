package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojos.CentroDeCosto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CCostosFXMLController implements Initializable{


	@FXML private TextField new_cod;
	@FXML private TextField new_nombre;

	@FXML private TextField upd_cod;
	@FXML private TextField upd_nombre;

	@FXML private TableView<CentroDeCosto> tabla;

	private static ObservableList<CentroDeCosto> centros = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/centroDeCosto";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {

			solicitarListaActualizada();

			TableColumn<CentroDeCosto, Integer> columna1 = new TableColumn<CentroDeCosto, Integer>("Codigo");
			TableColumn<CentroDeCosto, String> columna2 = new TableColumn<CentroDeCosto, String>("Nombre");

			columna1.setCellValueFactory(new PropertyValueFactory<>("codCentro"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("nombre"));

			tabla.setItems(centros);
			tabla.getColumns().addAll(columna1, columna2);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void solicitarListaActualizada() {
		// TODO Auto-generated method stub

		ResponseEntity<CentroDeCosto[]> respuesta = restTemplate.getForEntity(INIT_URL+"/get/all", CentroDeCosto[].class);

		Stream<CentroDeCosto> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(centros:: add);
		
		centros.stream().forEach(System.out::println);

	}

	private void actualizarLista(CentroDeCosto c) {

		centros.add(c);
	}

	@FXML protected void generar (ActionEvent actionEvent) {

		CentroDeCosto c  = CentroDeCosto.builder().
				codCentro(Integer.parseInt(new_cod.getText())).
				nombre(new_nombre.getText()).
				build();

		try {

			restTemplate.postForEntity(INIT_URL+"/add", c, CentroDeCosto.class);
			actualizarLista(c);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@FXML protected void actualizar(ActionEvent actionEvent) {

		CentroDeCosto c  = CentroDeCosto.builder().
				codCentro(Integer.parseInt(upd_cod.getText())).
				nombre(upd_nombre.getText()).
				build();

		try {

			restTemplate.put(INIT_URL+"/update", c);

			refrescarLista(actionEvent);

		} catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();

		}

	}

	@FXML protected void borrar(ActionEvent actionEvent) throws IOException {


		boolean confirmado = ConfirmBox.display("Borrar C. Costos", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		if (confirmado) {
			try {
				int id =  tabla.getSelectionModel().getSelectedItem().getCodCentro();
				Map<String, Integer> params = new HashMap<String, Integer>();
				params.put("name", id);

				restTemplate.delete(INIT_URL+"/delete?id={name}", params);

				centros.remove(tabla.getSelectionModel().getSelectedItem());

			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
	
	@FXML protected void buscar(ActionEvent actionEvent) {
		
		CentroDeCosto c = CentroDeCosto.builder().codCentro(Integer.parseInt(upd_cod.getText())).build();
		
		System.out.println(c.toString());
		System.out.println(centros.contains(c));
		
		if(centros.contains(c)) {
			c = centros.get(centros.indexOf(c));
			
			System.out.println(c.toString());
			
			upd_nombre.setText(c.getNombre());
		}
		
	}

	@FXML protected void refrescarLista(ActionEvent actionEvent) {
		
		centros.clear();
		solicitarListaActualizada();
	}
	
}
