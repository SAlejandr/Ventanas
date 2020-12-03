/**
 * 
 */
package com.example.demo.Controllers;

import com.example.demo.model.pojos.Suceso;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * @author alejandro
 *
 */
public class SucesoFXMLController implements Initializable{

	

	
	@FXML private TableView<Suceso> tabla;

	private static ObservableList<Suceso> sucesos = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/suceso";

	private RestTemplate restTemplate = new RestTemplate();
	@FXML TextField new_id;
	@FXML TextField new_name;
	@FXML TextField upd_id;
	@FXML TextField upd_name;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {

			sucesos.clear();
			solicitarListaActualizada();

			TableColumn<Suceso, Integer> columna1 = new TableColumn<Suceso, Integer>("Codigo");
			TableColumn<Suceso, String> columna2 = new TableColumn<Suceso, String>("Nombre");

			columna1.setCellValueFactory(new PropertyValueFactory<>("codSuceso"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("nomSuceso"));

			sucesos.stream().forEach(System.out::println);
			tabla.setItems(sucesos);
			
			tabla.getColumns().addAll(columna1, columna2);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void solicitarListaActualizada() {
		// TODO Auto-generated method stub

		ResponseEntity<Suceso[]> respuesta = restTemplate.getForEntity(INIT_URL+"/get/all", Suceso[].class);

		Stream<Suceso> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(sucesos:: add);
		
		sucesos.stream().forEach(System.out::println);

	}

	

	@FXML protected void generar (ActionEvent actionEvent) {

		Suceso c  = Suceso.builder().
				codSuceso(new_id.getText()).
					nomSuceso(new_name.getText()).
				build();

		try {

			restTemplate.postForEntity(INIT_URL+"/add", c, Suceso.class);
			refrescarLista(actionEvent);
			limpiarControles();

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@FXML protected void actualizar(ActionEvent actionEvent) {

		Suceso c  = Suceso.builder().
				codSuceso(upd_id.getText()).
				nomSuceso(upd_name.getText()).
				build();

		try {
			restTemplate.put(INIT_URL+"/update", c);

			refrescarLista(actionEvent);

			limpiarControles();


		} catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();

		}

	}

	@FXML protected void borrar(ActionEvent actionEvent) throws IOException {

		System.out.println("Borrar Suceso");

		boolean confirmado = ConfirmBox.display("Borrar Suceso", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		if (confirmado) {
			try {
				String id =  tabla.getSelectionModel().getSelectedItem().getCodSuceso();
				Map<String, String> params = new HashMap<String, String>();
				params.put("name", id);

				restTemplate.delete(INIT_URL+"/delete?id={name}", params);

				sucesos.remove(tabla.getSelectionModel().getSelectedItem());

			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	@FXML protected void buscar(ActionEvent actionEvent) {
		
		Suceso c = Suceso.builder().codSuceso(upd_id.getText()).build();
		
		System.out.println(c.toString());
		System.out.println(sucesos.contains(c));
		
		if(sucesos.contains(c)) {
			c = sucesos.get(sucesos.indexOf(c));
			
			System.out.println(c.toString());
			
			upd_name.setText(c.getNomSuceso());
		}
		
	}
	
	@FXML protected void refrescarLista(ActionEvent actionEvent) {
		
		sucesos.clear();
		solicitarListaActualizada();
	}

	protected void limpiarControles() {

		new_id.clear();
		new_name.clear();
		upd_id.clear();
		upd_name.clear();
	}
	
}
