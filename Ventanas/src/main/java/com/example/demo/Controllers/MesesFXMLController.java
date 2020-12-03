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

import com.example.demo.dto.MesDTO;
import com.example.demo.model.pojos.Anno;
import com.example.demo.model.pojos.EstadosDeMes;
import com.example.demo.model.pojos.Mes;
import com.example.demo.model.pojos.MesFiscalId;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import javafx.scene.control.ComboBox;

@Component
public class MesesFXMLController implements Initializable{

	@FXML private TableView<MesDTO> tablaMeses;

	@FXML private TextField new_id;
	@FXML private DatePicker new_f_init;
	@FXML private DatePicker new_f_end;
	@FXML private CheckBox cerradoNew;
	@FXML private ComboBox<Integer> new_anno;
	@FXML private ComboBox<EstadosDeMes> new_estado;
	
	@FXML private TextField upd_id;
	@FXML private DatePicker upd_f_init;
	@FXML private DatePicker upd_f_end;
	@FXML private CheckBox cerradoUpd;
	@FXML private ComboBox<Integer> upd_anno;
	@FXML private ComboBox<EstadosDeMes> upd_estado;


	private HashMap<String, Mes> mapMesesDelete = Maps.newHashMap();
	private HashMap<MesFiscalId, Mes> mapMeses = Maps.newHashMap();
	private static ObservableList<MesDTO> mesesDTO = FXCollections.observableArrayList();
	
	private HashSet<Anno> annos = Sets.newHashSet();
	private static ObservableList<Integer> annosID = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro/mes";

	private RestTemplate restTemplate = new RestTemplate();


	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {

			solicitarListaActualizada();

			TableColumn<MesDTO, String> columna1 = new TableColumn<MesDTO, String>("El Mes");
			TableColumn<MesDTO, LocalDate> columna2 = new TableColumn<MesDTO, LocalDate>("Inicio");
			TableColumn<MesDTO, LocalDate> columna3 = new TableColumn<MesDTO, LocalDate>("Fin");
			TableColumn<MesDTO, Boolean> columna4 = new TableColumn<MesDTO, Boolean>("Estado");

			columna1.setCellValueFactory(new PropertyValueFactory<>("nombreMes"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("estado"));

			tablaMeses.setItems(mesesDTO);
			tablaMeses.getColumns().addAll(columna1, columna2, columna3, columna4);
			
			new_anno.setItems(annosID);
			upd_anno.setItems(annosID);
			
			new_estado.setItems(FXCollections.observableArrayList(EstadosDeMes.values()));
			upd_estado.setItems(FXCollections.observableArrayList(EstadosDeMes.values()));

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void solicitarListaActualizada() {

		mesesDTO.clear();
		mapMeses.clear();
		annos.clear();
		
		ResponseEntity<Mes[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAll", Mes[].class);
		Stream <Mes> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(mes  ->{  
			mapMeses.put(mes.getIdFiscal(), mes);
			mapMesesDelete.put(mes.getNombre(), mes);
			System.out.println(mes.toString());
			mesesDTO.add(new MesDTO(mes));
			
		});
		
		ResponseEntity<Anno[]> respuestaAnno = restTemplate.getForEntity("http://localhost:8080/pro/anno/get/all", Anno[].class);
		Stream<Anno> consumerAnno = Stream.of(respuestaAnno.getBody());
		consumerAnno.forEach(anno -> {
			annos.add(anno);
			annosID.add(anno.getElAnno());
		});

	}

	@FXML protected void generar() {

		System.out.println("Guardar anno");
		
		Mes mes = Mes.builder().
				idFiscal(MesFiscalId.builder().
						anno(Anno.builder().
								elAnno(new_anno.getValue()).
								build()
								).
						mes(Integer.parseInt(new_id.getText())).
						build()
						).
				inicio(new_f_init.getValue()).
				fin(new_f_end.getValue()).
				estado(new_estado.getValue()).
				build();
		mes.setNombre(mes.getIdFiscal().getMes()+"-"+mes.getIdFiscal().getAnno().getElAnno());
		try {

			restTemplate.postForObject(INIT_URL + "/add", mes, Mes.class);
			reconstruir();
			refrescarLista();
		}catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();
		}

	}

	@FXML public void actualizar() {

		System.out.println("Modificar anno");

		Mes mes = Mes.builder().
				idFiscal(MesFiscalId.builder().
						anno(Anno.builder().
								elAnno(upd_anno.getValue()).
								build()
								).
						mes(Integer.parseInt(upd_id.getText())).
						build()
						).
				inicio(upd_f_init.getValue()).
				fin(upd_f_end.getValue()).
				estado(upd_estado.getValue()).
				build();
		
			mes.setNombre(mes.getIdFiscal().getMes()+"-"+mes.getIdFiscal().getAnno().getElAnno());
		try {

			restTemplate.put(INIT_URL + "/update", mes);

			refrescarLista();

			reconstruir();
			
		}catch (HttpClientErrorException e) {
			// TODO: handle exception

			e.printStackTrace();
		}

	}

	@FXML public void borrar()  throws IOException {

		System.out.println("Borrar Mes");

		boolean confirmado = ConfirmBox.display("Borrar Mes", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		if (confirmado) {
			try {

				String id = tablaMeses.getSelectionModel().getSelectedItem().getNombreMes();
				
				Mes mes = mapMesesDelete.get(id);
				
				restTemplate.delete(INIT_URL+"/delete", mes.getIdFiscal());

				mapMeses.remove(id);
				mesesDTO.remove(tablaMeses.getSelectionModel().getSelectedItem());

			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}


	@FXML protected void refrescarLista() {

		solicitarListaActualizada();
	}

	@FXML public void cargar() {


		MesFiscalId id = MesFiscalId.builder().
				anno(Anno.builder().
						elAnno(upd_anno.getValue()).
						build()
				).
				mes(Integer.parseInt(upd_id.getText())).
				build();
		
		Mes mes = mapMeses.get(id);
		
		upd_f_init.setValue(mes.getInicio());
		upd_f_end.setValue(mes.getFin());
		upd_estado.setValue(mes.getEstado());
	}
	
	private void reconstruir() {

		new_id.clear();
		new_f_end.setValue(LocalDate.now());
		new_f_init.setValue(LocalDate.now());

		upd_id.clear();
		upd_f_end.setValue(LocalDate.now());
		upd_f_init.setValue(LocalDate.now());

	}

}
