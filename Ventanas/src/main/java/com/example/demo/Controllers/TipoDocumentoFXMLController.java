package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojos.TipoDocumento;
import com.google.common.collect.Maps;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;import javafx.scene.layout.Border;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TipoDocumentoFXMLController implements Initializable{

	@FXML TextField upd_name;
	@FXML TextField upd_id;
	@FXML TextField new_name;
	@FXML TextField new_id;
	@FXML TableView<TipoDocumento> tabla;

	private final String INIT_URL = "http://localhost:8080/pro/tipoDocumento";


	private RestTemplate restTemplate = new RestTemplate();

	private ObservableList<TipoDocumento> losTipoDocumento = FXCollections.observableArrayList();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		refrescarLista();

		TableColumn<TipoDocumento, String> columna1 = new TableColumn<>("ID_Tipo Documento");
		TableColumn<TipoDocumento, String> columna2 = new TableColumn<>("Nombre_Tipo Documento");

		columna1.setCellValueFactory(new PropertyValueFactory<>("tipoDoc"));
		columna2.setCellValueFactory(new PropertyValueFactory<>("nombreDocumento"));

		tabla.setItems(losTipoDocumento);
		tabla.getColumns().addAll(columna1, columna2);

	}

	@FXML public void borrar() throws IOException {

		System.out.println("Borrar Tipo Documento");

		boolean confirmado = ConfirmBox.display("Borrar anno", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		try {
			String id = tabla.getSelectionModel().getSelectedItem().getTipoDoc();

			HashMap<String, String> params = Maps.newHashMap();

			params.put("id", id);

			restTemplate.delete(INIT_URL+"/delete?id={id}", params);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
		}
	}

	@FXML public void refrescarLista() {

		try {
			losTipoDocumento.clear();

			ResponseEntity<TipoDocumento[]> response = restTemplate.getForEntity(INIT_URL + "/getAll", TipoDocumento[].class);

			Stream<TipoDocumento> stream = Stream.of(response.getBody());

			stream.forEach(losTipoDocumento::add);

		} catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@FXML public void create() {

		try {

			TipoDocumento td  = new TipoDocumento();
			td.setTipoDoc(new_id.getText());

			if(!losTipoDocumento.contains(td)) {
				td.setNombreDocumento(new_name.getText());

				restTemplate.postForObject(INIT_URL+"/add", td, TipoDocumento.class);

				new_name.clear();
			}
			new_id.clear();
		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@FXML public void update() throws IOException {
		try {

			TipoDocumento td  = new TipoDocumento();
			td.setTipoDoc(upd_id.getText());

			if(!losTipoDocumento.contains(td)) {
				td.setNombreDocumento(new_name.getText());

				restTemplate.put(INIT_URL+"/update", td);

				upd_name.clear();
			}
			
			upd_id.clear();
		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			AlertBox.display("Error", "Error de actualizado:\n"
					+ "El tipo Documento no existe o no fue enviado de manera correcta	");
		}

	}

	@FXML public void search() {
		
		TipoDocumento td = new TipoDocumento();
		
		td.setTipoDoc(upd_id.getText());
		
		int index = losTipoDocumento.lastIndexOf(td);
		
		if (index >= 0 ) {
			
			losTipoDocumento.get(index);
		}else {
			try {
				AlertBox.display("Error", "No se encontro el objeto buscado");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}


}
