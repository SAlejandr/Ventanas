	package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.CiudadTablaDTO;
import com.example.demo.model.pojos.CentroDeCosto;
import com.example.demo.model.pojos.Ciudad;
import com.example.demo.model.pojos.Departamento;
import com.example.demo.model.pojos.IdCiudad;

import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

public class CiudadFXMLController implements Initializable{

	private final String INIT_URL = "http://localhost:8080/pro/ciudad";

	@FXML private TableView<CiudadTablaDTO> tabla;

	@FXML private TextField new_id;

	@FXML private TextField new_name;

	@FXML private TextField new_poblacion;

	@FXML private ComboBox<String> new_depart;

	@FXML private TextField upd_id;

	@FXML private TextField upd_name;

	@FXML private TextField upd_poblacion;

	@FXML private ComboBox<String> upd_depart;

	private static HashSet<Ciudad> lasCiudades = new HashSet<Ciudad>();

	private static ObservableList<CiudadTablaDTO> ciudades = FXCollections.observableArrayList();
	private static Vector<Departamento> departamentos = new Vector<Departamento>();
	private static ObservableList<String> nombreDepartamentos = FXCollections.observableArrayList();

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		departamentos.clear();
		refrescarLista();

		TableColumn<CiudadTablaDTO, Integer> columna1 = new TableColumn<CiudadTablaDTO, Integer>("ID");
		TableColumn<CiudadTablaDTO, String> columna2 = new TableColumn<CiudadTablaDTO, String>("Nombre");
		TableColumn<CiudadTablaDTO, Long> columna3 = new TableColumn<CiudadTablaDTO, Long>("Poblacion");
		TableColumn<CiudadTablaDTO, String> columna4 = new TableColumn<CiudadTablaDTO, String>("Departamento");


		columna1.setCellValueFactory(new PropertyValueFactory<>("id"));
		columna2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columna3.setCellValueFactory(new PropertyValueFactory<>("poblacion"));
		columna4.setCellValueFactory(new PropertyValueFactory<>("departamento"));

		tabla.setItems(ciudades);
		tabla.getColumns().addAll(columna1, columna2, columna3, columna4);
	}

	@FXML public void refrescarLista() {

		lasCiudades.clear();
		ciudades.clear();
		departamentos.clear();
		nombreDepartamentos.clear();

		ResponseEntity<Ciudad[]> respuesta = restTemplate.getForEntity(INIT_URL+"/get/all", Ciudad[].class);

		Stream<Ciudad> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(lasCiudades ::add);

		lasCiudades.stream().forEach( c  -> {

			CiudadTablaDTO ctd = new CiudadTablaDTO(c);
			ciudades.add(ctd);
		});


		ResponseEntity<Departamento[]> departResponse = restTemplate.getForEntity(INIT_URL+"/get/allDept", Departamento[].class);
		Stream<Departamento> departStream = Stream.of(departResponse.getBody());

		departStream.forEach(departamentos::add);



		departamentos.stream().forEach(d -> nombreDepartamentos.add(d.getNombre()));

		upd_depart.setItems(nombreDepartamentos);
		new_depart.setItems(nombreDepartamentos);
	}

	@FXML public void borrar() throws IOException {

		boolean confirmado = ConfirmBox.display("Borrar Ciudad", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		if (confirmado) {
			try {
				CiudadTablaDTO c =  tabla.getSelectionModel().getSelectedItem();


				//restTemplate.delete(INIT_URL+"/delete", );

				//ciudades.remove(tabla.getSelectionModel().getSelectedItem());

			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	@FXML public void generar(ActionEvent event) {

		IdCiudad id = IdCiudad.builder().codCiudad(new_id.getText()).
				departamento(departamentos.get(new_depart.getSelectionModel().getSelectedIndex())).
				build();
		Ciudad ciudad = Ciudad.builder().
				id(id).
				nombre(new_name.getText()).
				poblacion(Long.parseLong(new_poblacion.getText())).
				build();


		try {
			restTemplate.postForEntity(INIT_URL+"/add", ciudad, Ciudad.class);
			actualizarLista(ciudad);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();
		}

	}

	private void actualizarLista(Ciudad ciudad) {
		// TODO Auto-generated method stub
		ciudades.add(new CiudadTablaDTO(ciudad));
	}

	@FXML public void buscar(ActionEvent event) {

		boolean encontrado = false;

		Iterator<Ciudad> iterador = lasCiudades.iterator();

		Ciudad aux = Ciudad.builder().
				id(IdCiudad.builder().codCiudad(upd_id.getText()).
						departamento(departamentos.get(upd_depart.getSelectionModel().getSelectedIndex())).
						build()).
				build();

		Ciudad c = lasCiudades.stream().filter(city -> city.equals(aux)).findFirst().orElse(aux);

		upd_name.setText(c.getNombre());
		upd_poblacion.setText(""+c.getPoblacion());
		upd_depart.getSelectionModel().select(departamentos.lastIndexOf(c.getId().getDepartamento()));

		encontrado = true;


	}

	@FXML public void actualizar(ActionEvent event) throws IOException {

		Ciudad c = Ciudad.builder().
				id(IdCiudad.builder().codCiudad(upd_id.getText()).
						departamento(departamentos.get(upd_depart.getSelectionModel().getSelectedIndex())).
						build()).
				nombre(upd_name.getText()).
				poblacion(Long.parseLong(upd_poblacion.getText())).
				build();

		boolean confirmado = ConfirmBox.display("Update city", "Desea actualizar ahora?", 
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		try {

			refrescarLista();
			restTemplate.put(INIT_URL+"/update", c);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
