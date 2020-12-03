package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojos.Responsable;
import com.example.demo.model.pojos.TipoDocumento;
import com.example.demo.model.pojos.Responsable;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ResponsableFXMLController implements Initializable {



	@FXML TextField new_cod;
	@FXML TextField new_nombre;
	@FXML TextField upd_cod;
	@FXML TextField upd_nombre;
	@FXML TableView<Responsable> tabla;

	private final String INIT_URL = "http://localhost:8080/pro/responsable";

	private RestTemplate restTemplate = new RestTemplate();

	private ObservableList<Responsable> responsablesList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		refrescarLista();

		TableColumn<Responsable, Integer> columna1 = new TableColumn<Responsable, Integer>("ID_Responsable");
		TableColumn<Responsable, String> columna2 = new TableColumn<Responsable, String>("Nombre_Responsable");

		columna1.setCellValueFactory(new PropertyValueFactory<>("codRes"));
		columna2.setCellValueFactory(new PropertyValueFactory<>("nombre"));

		tabla.setItems(responsablesList);
		tabla.getColumns().addAll(columna1, columna2);
	}

	@FXML public void create() {

		try {

			Responsable responsable = new Responsable();
			responsable.setCodRes(Integer.parseInt(new_cod.getText()));

			if(!responsablesList.contains(responsable)) {
				responsable.setNombre(new_nombre.getText());

				restTemplate.postForObject(INIT_URL+"/add", responsable, Responsable.class);

				new_nombre.clear();
			}
			new_cod.clear();
		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@FXML public void search() throws IOException {

		Responsable responsable = Responsable.builder().codRes(Integer.parseInt(upd_cod.getText())).build();

		int index = responsablesList.lastIndexOf(responsable);

		if(index >= 0) {
			responsable = responsablesList.get(index);
			upd_nombre.setText(responsable.getNombre());
		}else {
			AlertBox.display("Sin responsable", "No existe el responsables\nRefresque y vuelva a intentarlo");
		}

	}

	@FXML public void update() throws IOException {

		try {
			Responsable responsabe = Responsable.builder().
					codRes(Integer.parseInt(upd_cod.getText())).
					nombre(upd_nombre.getText()).
					build();

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			AlertBox.display("Error", "No se pudo actualizar el objeto");
		}
	}

	@FXML public void borrar() throws IOException {

		try {

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			AlertBox.display("Error", "No se pudo borrar el objeto");
		}
	}

	@FXML public void refrescarLista() {

		try {
			responsablesList.clear();

			ResponseEntity<Responsable[]> respuesta = restTemplate.getForEntity(INIT_URL+"/getAll", Responsable[].class);

			Stream<Responsable> stream = Stream.of(respuesta.getBody());

			stream.forEach(responsablesList::add);
		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			try {
				AlertBox.display("Sin responsable", "No existen responsables en la lista");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
