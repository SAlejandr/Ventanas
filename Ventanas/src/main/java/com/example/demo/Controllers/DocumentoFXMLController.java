package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.DocumentoDTO;
import com.example.demo.model.pojos.CentroDeCosto;
import com.example.demo.model.pojos.Cuenta;
import com.example.demo.model.pojos.Documento;
import com.example.demo.model.pojos.Estado;
import com.example.demo.model.pojos.IdDocumento;
import com.example.demo.model.pojos.Responsable;
import com.example.demo.model.pojos.Tercero;
import com.example.demo.model.pojos.TipoDocumento;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;

public class DocumentoFXMLController implements Initializable{

	private final String INIT_URL = "http://localhost:8080/pro/";

	@FXML TableView<DocumentoDTO> tabla;
	@FXML TextField upd_id;
	@FXML ComboBox upd_tipo;
	@FXML DatePicker upd_fecha;
	@FXML ComboBox upd_responsable;
	@FXML ComboBox new_responsable;
	@FXML DatePicker new_fecha;
	@FXML ComboBox new_tipo;
	@FXML TextField new_id;
	@FXML TextArea new_concepto;
	@FXML TextArea upd_concepto;

	private static ObservableList<DocumentoDTO> documentosDTO = FXCollections.observableArrayList();
	private static ObservableList<TipoDocumento> tipoDeDocumentos = FXCollections.observableArrayList();
	private static HashSet<Object> responsables = Sets.newHashSet();
	private static HashMap<IdDocumento, Documento> mapDocumentos = Maps.newHashMap();
	private static HashMap<String, Tercero> mapTercero = Maps.newHashMap();
	private static HashMap<String, Cuenta> mapCuenta = Maps.newHashMap();
	private static HashMap<String, Estado> mapEstado = Maps.newHashMap();
	private static HashMap<Integer, CentroDeCosto> mapCCostos = Maps.newHashMap();

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		refrescarLista();

		TableColumn<DocumentoDTO, Long> columna1 = new TableColumn<DocumentoDTO, Long>("Nº Documento");
		TableColumn<DocumentoDTO, String> columna2 = new TableColumn<DocumentoDTO, String>("Tipo Documento");
		TableColumn<DocumentoDTO, String> columna3 = new TableColumn<DocumentoDTO, String>("Concepto");
		TableColumn<DocumentoDTO, LocalDate> columna4 = new TableColumn<DocumentoDTO, LocalDate>("Fecha");
		TableColumn<DocumentoDTO, String> columna5 = new TableColumn<DocumentoDTO, String>("Responsable");
		TableColumn<DocumentoDTO, Float> columna6 = new TableColumn<DocumentoDTO, Float>("Total");

		columna1.setCellValueFactory(new PropertyValueFactory<>("numDocumento"));
		columna2.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
		columna3.setCellValueFactory(new PropertyValueFactory<>("concepto"));
		columna4.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columna5.setCellValueFactory(new PropertyValueFactory<>("responsable"));
		columna6.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

		tabla.setItems(documentosDTO);
		tabla.getColumns().addAll(columna1,columna2,columna3,columna4,columna5,columna6);

	}

	@FXML public void refrescarLista() {

		documentosDTO.clear();
		responsables.clear();
		tipoDeDocumentos.clear();
		mapDocumentos.clear();
		mapCCostos.clear();
		mapTercero.clear();
		mapCuenta.clear();
		mapEstado.clear();

		
		ResponseEntity<Documento[]> respuesta = restTemplate.getForEntity(INIT_URL + "documento/getAll", Documento[].class);
		ResponseEntity<Responsable[]> respuesta1 = restTemplate.getForEntity(INIT_URL+"responsable/getAll", Responsable[].class);
		ResponseEntity<Cuenta[]> respuesta2 = restTemplate.getForEntity(INIT_URL+"cuentas/getAll/1", Cuenta[].class);
		ResponseEntity<CentroDeCosto[]> respuesta3 = restTemplate.getForEntity(INIT_URL+"centroDeCosto/get/all", CentroDeCosto[].class);
		ResponseEntity<Tercero[]> respuesta4 = restTemplate.getForEntity(INIT_URL+"tercero/getAll", Tercero[].class);
		ResponseEntity<TipoDocumento[]> respuesta5 = restTemplate.getForEntity(INIT_URL + "tipoDocumento/getAll", TipoDocumento[].class);
		ResponseEntity<Estado[]> respuesta6 = restTemplate.getForEntity(INIT_URL+"/estado/getAll", Estado[].class);


		Stream<Documento> consumer =Stream.of(respuesta.getBody());
		HashSet<TipoDocumento> set = new HashSet<TipoDocumento>();

		consumer.forEach(documento -> mapDocumentos.put(documento.getIdDocumento(), documento));

		mapDocumentos.values().stream().forEach(d ->{

			DocumentoDTO dto = new DocumentoDTO(d);
			documentosDTO.add(dto);

		});

		Stream<Responsable> consumer1 = Stream.of(respuesta1.getBody());
		consumer1.forEach(responsables::add);

		new_responsable.setItems(FXCollections.observableArrayList(responsables.toArray()));
		upd_responsable.setItems(FXCollections.observableArrayList(responsables.toArray()));

		Stream<Cuenta> consumer2 = Stream.of(respuesta2.getBody());
		consumer2.forEach(cuenta -> mapCuenta.put(cuenta.getCodCuenta(), cuenta));
		Stream<CentroDeCosto> consumer3 = Stream.of(respuesta3.getBody());
		consumer3.forEach(cCostos -> mapCCostos.put(cCostos.getCodCentro(), cCostos));
		Stream<Tercero> consumer4 = Stream.of(respuesta4.getBody());
		consumer4.forEach(tercero -> mapTercero.put(tercero.getId(), tercero));

		Stream<TipoDocumento> consumer5 = Stream.of(respuesta5.getBody());
		consumer5.forEach(tipoDeDocumentos::add);
		
		Stream<Estado> consumer6 = Stream.of(respuesta6.getBody());
		consumer6.forEach(estado -> mapEstado.put(estado.getCodEstado(), estado));

		new_tipo.setItems(tipoDeDocumentos);
		upd_tipo.setItems(tipoDeDocumentos);


	}

	@FXML public void borrar() throws IOException {

		boolean confirmado = ConfirmBox.display("Borrar Ciudad", "¿Esta seguro que desea borrar el registro?",
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());

		if (confirmado) {
			try {
				DocumentoDTO dto =  tabla.getSelectionModel().getSelectedItem();
	
				TipoDocumento tipoDoc = new TipoDocumento();
				
				tipoDoc.setTipoDoc(dto.getTipoDocumento());
				IdDocumento id = new IdDocumento();

				id.setNumDocumento(dto.getNumDocumento());
				id.setTipoDocumento(tipoDoc);

				Documento doc = Documento.builder().idDocumento(id).build();


				restTemplate.delete(INIT_URL+"/delete",doc);


			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}


	@FXML public void generar() {

		IdDocumento id = new IdDocumento();

		id.setNumDocumento(Long.parseLong(new_id.getText()));
		id.setTipoDocumento((TipoDocumento) new_tipo.getSelectionModel().getSelectedItem());

		Documento documento = Documento.builder().
				idDocumento(id).
				concepto(new_concepto.getText()).
				fecha(new_fecha.getValue()).
				responsable((Responsable) new_responsable.getSelectionModel().getSelectedItem()).
				build();
		if(!mapDocumentos.containsKey(documento.getIdDocumento()))
			restTemplate.postForEntity(INIT_URL+"/documento/add", documento, Documento.class);


	}

	@FXML public void buscar() {
		IdDocumento id = new IdDocumento();

		id.setNumDocumento(Long.parseLong(upd_id.getText()));
		id.setTipoDocumento((TipoDocumento) upd_tipo.getSelectionModel().getSelectedItem());
		Documento documento;
		if(mapDocumentos.containsKey(id)) {
			documento = mapDocumentos.get(id);
			upd_concepto.setText(documento.getConcepto());
			upd_fecha.setValue(documento.getFecha());
			upd_responsable.getSelectionModel().select(documento.getResponsable());
		}

	}

	@FXML public void actualizar() throws IOException {

		try {
			IdDocumento id = new IdDocumento();


			if(!upd_id.getText().isBlank() && !upd_tipo.getSelectionModel().isEmpty()) {
				id.setNumDocumento(Long.parseLong(upd_id.getText()));
				id.setTipoDocumento((TipoDocumento) upd_tipo.getSelectionModel().getSelectedItem());

				if(mapDocumentos.containsKey(id)) {
					Documento d = Documento.builder().
							idDocumento(id).
							concepto(upd_concepto.getText()).
							fecha(upd_fecha.getValue()).
							responsable((Responsable) upd_responsable.getSelectionModel().getSelectedItem()).
							build();

					restTemplate.put(INIT_URL+"/update", d);
				}else {

					AlertBox.display("Error de actualizacion", "Id documento y/o tipo documento no es correcto o no exite");
				}
			}else
				AlertBox.display("Error de actualizacion", "Id documento y/o tipo documento no es correcto o no exite");

		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			AlertBox.display("Error de actualizacion", "No se pudo actualizar objeto");
		}
	}

}
