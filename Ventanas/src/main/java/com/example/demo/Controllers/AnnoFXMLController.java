package com.example.demo.Controllers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojos.Anno;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AmbientLight;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class AnnoFXMLController implements Initializable{

	@FXML private TableView<Anno> tablaAnno;

	@FXML private TextField new_id_anno;
	@FXML private DatePicker new_f_init;
	@FXML private DatePicker new_f_end;
	@FXML private CheckBox cerradoNew;

	@FXML private TextField upd_id_anno;
	@FXML private DatePicker upd_f_init;
	@FXML private DatePicker upd_f_end;
	@FXML private CheckBox cerradoUpd;

	@FXML private TextField id_borrado;

	private static ObservableList<Anno> losAnnos = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/anno";

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {

			solicitarListaActualizada();

			TableColumn<Anno, String> columna1 = new TableColumn<Anno, String>("el Anno");
			TableColumn<Anno, LocalDate> columna2 = new TableColumn<Anno, LocalDate>("Inicio");
			TableColumn<Anno, LocalDate> columna3 = new TableColumn<Anno, LocalDate>("Fin");
			TableColumn<Anno, Boolean> columna4 = new TableColumn<Anno, Boolean>("Cerrado");

			columna1.setCellValueFactory(new PropertyValueFactory<>("elAnno"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("inicioAnno"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("finAnno"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("cerrado"));

			tablaAnno.setItems(losAnnos);
			tablaAnno.getColumns().addAll(columna1, columna2, columna3, columna4);

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {

		losAnnos.clear();
		
		ResponseEntity<Anno[]> respuesta = restTemplate.getForEntity(INIT_URL + "/get/all", Anno[].class);
		Stream <Anno> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(losAnnos::add);

	}

	@FXML protected void generarAnno(ActionEvent actionEvent) {

		System.out.println("Guardar anno");

		Anno anno = new  Anno();
		anno.setElAnno(Integer.parseInt(new_id_anno.getText()));
		anno.setInicioAnno(new_f_init.getValue());
		anno.setFinAnno(new_f_end.getValue());
		anno.setCerrado(cerradoNew.isSelected());


		try {

			restTemplate.postForObject(INIT_URL + "/add", anno, Anno.class);
			reconstruir();
			refrescarLista(actionEvent);
		}catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();
		}

	}

	@FXML protected void modificarAnno(ActionEvent actionEvent) {

		System.out.println("Modificar anno");


		Anno anno1 = new Anno();
		anno1.setElAnno(Integer.parseInt(upd_id_anno.getText()));
		anno1.setInicioAnno(upd_f_init.getValue().plusDays(1));
		anno1.setFinAnno(upd_f_end.getValue().plusDays(1));
		anno1.setCerrado(cerradoUpd.isSelected());

		try {

			restTemplate.put(INIT_URL + "/update", anno1);

			refrescarLista(actionEvent);

			reconstruir();
			
		}catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();
		}

	}

	@FXML protected void borrarAnno(ActionEvent actionEvent) throws IOException {

		System.out.println("Borrar Anno");

		boolean confirmado = ConfirmBox.display("Borrar anno", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		if (confirmado) {
			try {
				int id =  tablaAnno.getSelectionModel().getSelectedItem().getElAnno();
				Map<String, Integer> params = new HashMap<String, Integer>();
				params.put("name", id);

				restTemplate.delete(INIT_URL+"/delete?id={name}", params);

				losAnnos.remove(tablaAnno.getSelectionModel().getSelectedItem());

			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}


	@FXML protected void refrescarLista(ActionEvent actionEvent) {

		losAnnos.clear();

		solicitarListaActualizada();
	}


	private void reconstruir() {

		new_id_anno.clear();
		new_f_end.setValue(LocalDate.now());
		new_f_init.setValue(LocalDate.now());
		cerradoNew.setSelected(false);

		upd_id_anno.clear();
		upd_f_end.setValue(LocalDate.now());
		upd_f_init.setValue(LocalDate.now());
		cerradoUpd.setSelected(false);

	}

}
