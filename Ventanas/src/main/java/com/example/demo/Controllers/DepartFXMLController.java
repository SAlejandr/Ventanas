/**
 * 
 */
package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojos.Departamento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author alejandro
 *
 */
public class DepartFXMLController implements Initializable{

	

	
	@FXML private TableView<Departamento> tabla;

	private static ObservableList<Departamento> departamentos = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/departamento";

	private RestTemplate restTemplate = new RestTemplate();
	@FXML TextField new_id;
	@FXML TextField new_name;
	@FXML TextField upd_id;
	@FXML TextField upd_name;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {

			departamentos.clear();
			solicitarListaActualizada();

			TableColumn<Departamento, Integer> columna1 = new TableColumn<Departamento, Integer>("Codigo");
			TableColumn<Departamento, String> columna2 = new TableColumn<Departamento, String>("Nombre");

			columna1.setCellValueFactory(new PropertyValueFactory<>("codDepartamento"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("nombre"));

			departamentos.stream().forEach(System.out::println);
			tabla.setItems(departamentos);
			
			tabla.getColumns().addAll(columna1, columna2);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void solicitarListaActualizada() {
		// TODO Auto-generated method stub

		ResponseEntity<Departamento[]> respuesta = restTemplate.getForEntity(INIT_URL+"/get/all", Departamento[].class);

		Stream<Departamento> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(departamentos:: add);
		
		departamentos.stream().forEach(System.out::println);

	}

	

	@FXML protected void generar (ActionEvent actionEvent) {

		Departamento c  = Departamento.builder().
				codDepartamento(Integer.parseInt(new_id.getText())).
				nombre(new_name.getText()).
				build();

		try {

			restTemplate.postForEntity(INIT_URL+"/add", c, Departamento.class);
			refrescarLista(actionEvent);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@FXML protected void actualizar(ActionEvent actionEvent) {

		Departamento c  = Departamento.builder().
				codDepartamento(Integer.parseInt(upd_id.getText())).
				nombre(upd_name.getText()).
				build();

		try {

			restTemplate.put(INIT_URL+"/update", c);

			departamentos.clear();

			solicitarListaActualizada();

		} catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();

		}

	}

	@FXML protected void borrar(ActionEvent actionEvent) throws IOException {

		System.out.println("Borrar Anno");

		boolean confirmado = ConfirmBox.display("Borrar C. Costos", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		if (confirmado) {
			try {
				int id =  tabla.getSelectionModel().getSelectedItem().getCodDepartamento();
				Map<String, Integer> params = new HashMap<String, Integer>();
				params.put("name", id);

				restTemplate.delete(INIT_URL+"/delete?id={name}", params);

				departamentos.remove(tabla.getSelectionModel().getSelectedItem());

			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	@FXML protected void buscar(ActionEvent actionEvent) {
		
		Departamento c = Departamento.builder().codDepartamento(Integer.parseInt(upd_id.getText())).build();
		
		System.out.println(c.toString());
		System.out.println(departamentos.contains(c));
		
		if(departamentos.contains(c)) {
			c = departamentos.get(departamentos.indexOf(c));
			
			System.out.println(c.toString());
			
			upd_name.setText(c.getNombre());
		}
		
	}
	
	@FXML protected void refrescarLista(ActionEvent actionEvent) {
		
		departamentos.clear();

		solicitarListaActualizada();
	}

	
}
