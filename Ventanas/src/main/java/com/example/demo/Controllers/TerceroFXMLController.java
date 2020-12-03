package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.*;
import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.TerceroDTO;
import com.example.demo.model.pojos.Ciudad;
import com.example.demo.model.pojos.IdCiudad;
import com.example.demo.model.pojos.NaturalezaJuridica;
import com.example.demo.model.pojos.Tercero;
import com.example.demo.model.pojos.TipoDocumento;
import com.example.demo.model.pojos.Tipo_Identificacion;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TerceroFXMLController implements Initializable{

	@FXML TextField new_id;
	@FXML TextField new_name;
	@FXML ComboBox<Tipo_Identificacion> new_tipo_id;
	@FXML ComboBox<String> new_city;
	@FXML TextField new_direccion;
	@FXML TextField new_telefono_1;
	@FXML TextField new_telefono_2;
	@FXML ComboBox<NaturalezaJuridica> new_naturaleza;
	@FXML TextField new_email;

	@FXML TextField upd_id;
	@FXML TextField upd_nombre;
	@FXML ComboBox<Tipo_Identificacion> upd_tipo;
	@FXML ComboBox<String> upd_ciudad;
	@FXML TextField upd_direccion;
	@FXML TextField upd_telef1;
	@FXML TextField upd_telef2;
	@FXML ComboBox<NaturalezaJuridica> upd_naturaleza;
	@FXML TextField upd_email;

	@FXML TableView<TerceroDTO> tabla;


	private TreeMap<String, Tercero> terceros = Maps.newTreeMap();
	private HashMap<IdCiudad, Ciudad> ciudades = Maps.newHashMap();
	private Hashtable<String, IdCiudad> idCiudades = new Hashtable<String, IdCiudad>();
	
	private ObservableList<TerceroDTO> dtoTerceros = FXCollections.observableArrayList();
	private ObservableList<String> ciudadesDTO = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro";
	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			refrescarLista();
			
			System.out.println("YA REFRESQUE LA LISTA  : *****************************************************" );


			TableColumn<TerceroDTO, String> columna1 = new TableColumn<>("ID");
			TableColumn<TerceroDTO, String> columna2 = new TableColumn<>("Naturaleza juridica");
			TableColumn<TerceroDTO, String> columna3 = new TableColumn<>("Tipo_Identificacion");
			TableColumn<TerceroDTO, String> columna4 = new TableColumn<>("Nombre");
			TableColumn<TerceroDTO, String> columna5 = new TableColumn<>("Direccion");
			TableColumn<TerceroDTO, String> columna6 = new TableColumn<>("Email");
			TableColumn<TerceroDTO, String> columna7 = new TableColumn<>("Telefono1");
			TableColumn<TerceroDTO, String> columna8 = new TableColumn<>("Telefono2");
			TableColumn<TerceroDTO, String> columna9 = new TableColumn<>("Ciudad");


			columna1.setCellValueFactory(new PropertyValueFactory<>("id"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("naturaleza"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("tipoIdentificacion"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("nombre"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("direccion"));
			columna6.setCellValueFactory(new PropertyValueFactory<>("email"));
			columna7.setCellValueFactory(new PropertyValueFactory<>("telefono1"));
			columna8.setCellValueFactory(new PropertyValueFactory<>("telefono2"));
			columna9.setCellValueFactory(new PropertyValueFactory<>("ciudad"));

			

			System.out.println("EN ESTE MOMENTO IMPRIMO TERCEROS DE ALEJANDRO : *****************************************************" );

			dtoTerceros.stream().forEach(System.out::println);
			System.out.println("***********************************  YA IMPRIMI TERCEROS DE ALEJANDRO*******************************************");

			tabla.setItems(dtoTerceros);
			
			System.out.println("***********************************  QUIERO VER ESTE MENSAJE*******************************************");

			tabla.getColumns().addAll(columna1,columna2,columna3,columna4,columna5, columna6, columna7, columna8, columna9);
			
			System.out.println("***********************************  QUIERO VER ESTE SEGUNDO MENSAJE*******************************************");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML public void crear() throws IOException {

		if(!new_id.getText().isBlank()) {
			Tercero tercero = Tercero.builder().
					id(new_id.getText()).
					nombre(new_name.getText()).
					naturaleza(new_naturaleza.getValue()).
					identificacion(new_tipo_id.getValue()).
					email(new_email.getText()).
					ciudad(ciudades.get(new_city.getValue())).
					direccion(new_direccion.getText()).
					telefono1(new_telefono_1.getText()).
					telefono2(new_telefono_2.getText()).
					build();
			try {
				restTemplate.postForEntity(INIT_URL+"/tercero/add", tercero, Tercero.class);
			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				AlertBox.display("Error", "Error al guarda: Comprube que NO exista ese tercero");
			}

		}else {
			AlertBox.display("Cuidado", "Cuidado: El id esta vacio");
		}

	}

	@FXML public void actualizar() throws IOException {

		if(!upd_id.getText().isBlank()) {
			Tercero tercero = Tercero.builder().
					id(upd_id.getText()).
					nombre(upd_nombre.getText()).
					naturaleza(upd_naturaleza.getValue()).
					identificacion(upd_tipo.getValue()).
					email(upd_email.getText()).
					ciudad(ciudades.get(upd_ciudad.getValue())).
					direccion(upd_direccion.getText()).
					telefono1(upd_telef1.getText()).
					telefono2(upd_telef2.getText()).
					build();
			try {
				restTemplate.put(INIT_URL+"/tercero/update", tercero);
			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				AlertBox.display("Error", "Error al guarda: Comprube que SÍ exista ese tercero");
			}

		}else {
			AlertBox.display("Cuidado", "Cuidado: El id esta vacio");
		}
	}

	@FXML public void buscar() throws IOException {
		
		if(!upd_id.getText().isBlank()) {
			if(terceros.containsKey(upd_id.getText())) {
				Tercero tercero = terceros.get(upd_id.getText());
				
				String ciudad = tercero.getCiudad().getId().getDepartamento().getNombre()+"/"+tercero.getCiudad().getNombre();
				upd_ciudad.getSelectionModel().select(ciudad);
				
				upd_naturaleza.getSelectionModel().select(tercero.getNaturaleza());
				upd_tipo.getSelectionModel().select(tercero.getIdentificacion());
				
				upd_nombre.setText(tercero.getNombre());
				upd_direccion.setText(tercero.getDireccion());
				upd_email.setText(tercero.getEmail());
				upd_telef1.setText(tercero.getTelefono1());
				upd_telef2.setText(tercero.getTelefono2());
				
				
			}else
				AlertBox.display("Error", "Error al guarda: Comprube que SÍ exista ese tercero");

		}else {
			AlertBox.display("Cuidado", "Cuidado: El id esta vacio");
		}

	}

	@FXML public void refrescarLista() throws IOException {

		terceros.clear();
		dtoTerceros.clear();
		ciudades.clear();

		try {

			ResponseEntity<Tercero[]> responseTercero = restTemplate.getForEntity(INIT_URL+"/tercero/getAll", Tercero[].class);
			ResponseEntity<Ciudad[]> responseCiudad = restTemplate.getForEntity(INIT_URL+"/ciudad/get/all", Ciudad[].class);
			
			System.out.println("LAS CIUDADES OBTENIDAS : " + responseCiudad.toString());

			Stream<Tercero> consumerTercero = Stream.of(responseTercero.getBody());
			Stream<Ciudad> consumerCiudad = Stream.of(responseCiudad.getBody());

			consumerTercero.forEach(t -> {terceros.put(t.getId(), t);
				System.out.println(t.toString()+"\t");
			});
				
			terceros.keySet().stream().forEach(id-> {
				Tercero tercero = terceros.get(id);
				TerceroDTO dto = new TerceroDTO(tercero);
				dtoTerceros.add(dto);
			});
			
			System.out.println("LOS PRIMEROS TERCEROS******************************** : " + dtoTerceros.toString());

			consumerCiudad.forEach(c ->{
				String nombre = c.getId().getDepartamento().getNombre()+"/"+c.getNombre();
				
				ciudades.put(c.getId(), c);
				idCiudades.put(nombre, c.getId());
			});
			
			idCiudades.keySet().stream().forEach(id -> ciudadesDTO.add(id));
			
			System.out.println("LAS CIUDADES MOSTRADAS 1 : " + ciudades.toString());

			upd_naturaleza.setItems(FXCollections.observableArrayList(NaturalezaJuridica.values()));
			new_naturaleza.setItems(FXCollections.observableArrayList(NaturalezaJuridica.values()));

			System.out.println("LAS CIUDADES MOSTRADAS 2 : " + ciudadesDTO.toString());

			new_city.setItems(ciudadesDTO);
			new_tipo_id.setItems(FXCollections.observableArrayList(Tipo_Identificacion.values()));

			upd_ciudad.setItems(ciudadesDTO);
			upd_tipo.setItems(FXCollections.observableArrayList(Tipo_Identificacion.values()));


		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			AlertBox.display("Listas", "No se pudo actualizar datos");
		}

	}
	@FXML public void borrar() throws IOException {
		
		boolean borrado = ConfirmBox.display("Confirmar borrado", "Seguro que desea borrar tercero", 
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());
		
		if(borrado) {
			TerceroDTO dto = tabla.getSelectionModel().getSelectedItem();
			Map<String, String> params = new HashMap<>();
			params.put("id", dto.getId());
			try {
				restTemplate.delete(INIT_URL+"/tercero/delete?id={id}", params);
				
				terceros.remove(dto.getId());
				dtoTerceros.remove(dto);
				
			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				AlertBox.display("Error de Borrado", "Ha sucedido un error de borrado, vuelva a intentarlo más tarde");
			}
		}
		
	}
}
