package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.*;
import java.util.stream.Stream;

import com.example.demo.util.EncriptarPassword;
import javafx.scene.control.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojos.Estado;
import com.example.demo.model.pojos.EstadosDeUsuarios;
import com.example.demo.model.pojos.NaturalezaJuridica;
import com.example.demo.model.pojos.Usuario;
import com.example.demo.model.pojos.TipoDocumento;
import com.example.demo.model.pojos.Tipo_Identificacion;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsuarioFXMLController implements Initializable{

	@FXML TextField new_id;
	@FXML TextField new_username;
	@FXML TextField new_nombre;
	@FXML TextField new_cargo;
	@FXML ComboBox<EstadosDeUsuarios> new_estado;
	@FXML DatePicker new_fechaCreacion;
	@FXML PasswordField new_password;
	@FXML Button crearBoton;
	

	@FXML TextField upd_id;
	@FXML TextField upd_username;
	@FXML TextField upd_nombre;
	@FXML TextField upd_cargo;
	@FXML ComboBox<EstadosDeUsuarios> upd_estado;
	@FXML DatePicker upd_fechaCreacion;
	@FXML PasswordField upd_password;
	@FXML Button actualizarBoton;

	@FXML TableView<Usuario> tabla;
	
	private TreeMap<Integer, Usuario> mapUsuario = Maps.newTreeMap();
	private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

	private final String INIT_URL = "http://localhost:8080/pro";
	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
			refrescarLista();
			
			System.out.println("YA REFRESQUE LA LISTA  : *****************************************************" );


			TableColumn<Usuario, Integer> columna1 = new TableColumn<>("ID");
			TableColumn<Usuario, String> columna2 = new TableColumn<>("Username");
			TableColumn<Usuario, String> columna3 = new TableColumn<>("Nombre");
			TableColumn<Usuario, String> columna4 = new TableColumn<>("Cargo");
			TableColumn<Usuario, String> columna5 = new TableColumn<>("Estado");
			TableColumn<Usuario, LocalDate> columna6 = new TableColumn<>("Fecha Creación");


			columna1.setCellValueFactory(new PropertyValueFactory<>("id"));
			columna2.setCellValueFactory(new PropertyValueFactory<>("username"));
			columna3.setCellValueFactory(new PropertyValueFactory<>("nombre"));
			columna4.setCellValueFactory(new PropertyValueFactory<>("cargo"));
			columna5.setCellValueFactory(new PropertyValueFactory<>("estado"));
			columna6.setCellValueFactory(new PropertyValueFactory<>("fecha_creacion"));


			System.out.println("EN ESTE MOMENTO IMPRIMO TERCEROS DE ALEJANDRO : *****************************************************" );

			usuarios.stream().forEach(System.out::println);
			System.out.println("***********************************  YA IMPRIMI TERCEROS DE ALEJANDRO*******************************************");

			tabla.setItems(usuarios);
			
			System.out.println("***********************************  QUIERO VER ESTE MENSAJE*******************************************");

			tabla.getColumns().addAll(columna1,columna2,columna3,columna4,columna5, columna6);
			
			System.out.println("***********************************  QUIERO VER ESTE SEGUNDO MENSAJE*******************************************");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML public void crear() throws IOException {

		if(!new_id.getText().isBlank()) {
			Usuario usuario = Usuario.builder().
					id(Integer.parseInt(new_id.getText())).
					username(new_username.getText()).
					nombre(new_nombre.getText()).
					cargo(new_cargo.getText()).
					estado(new_estado.getValue()).
					fechaCreacion(new_fechaCreacion.getValue()).
					password(EncriptarPassword.encriptarPassword(new_password.getText())).
					build();
			try {
				restTemplate.postForEntity(INIT_URL+"/usuario/add", usuario, Usuario.class);
			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				AlertBox.display("Error", "Error al guardar: Comprube que NO exista ese usuario");
			}

		}else {
			AlertBox.display("Alerta", "El id está vacio");
		}

	}

	@FXML public void actualizar() throws IOException {

		if(!upd_id.getText().isBlank()) {
			Usuario usuario = Usuario.builder().
					id(Integer.parseInt(upd_id.getText())).
					username(upd_username.getText()).
					nombre(upd_nombre.getText()).
					cargo(upd_cargo.getText()).
					estado(upd_estado.getValue()).
					fechaCreacion(upd_fechaCreacion.getValue()).
					password(EncriptarPassword.encriptarPassword(upd_password.getText())).
					build();
			try {
				restTemplate.put(INIT_URL+"/usuario/update", usuario);
			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				AlertBox.display("Error", "Error al guardar: Comprube que SÍ exista este usuario");
			}

		}else {
			AlertBox.display("Alerta", "El id esta vacio");
		}
	}

	@FXML public void buscar() throws IOException {
		
		if(!upd_id.getText().isBlank()) {
			if(mapUsuario.containsKey(Integer.parseInt(upd_id.getText().trim()))) {
				Usuario usuario = mapUsuario.get(Integer.parseInt(upd_id.getText()));
				

			
				upd_username.setText(usuario.getUsername());
				upd_nombre.setText(usuario.getNombre());
				upd_cargo.setText(usuario.getCargo());
				EstadosDeUsuarios estado = usuario.getEstado();
				upd_estado.getSelectionModel().select(estado);
				upd_fechaCreacion.setValue(usuario.getFechaCreacion());
				upd_password.setText(usuario.getPassword());
				
				
			}else
				AlertBox.display("Error", "Error al buscar: Compruebe que SÍ exista este usuario");

		}else {
			AlertBox.display("Alerta", "Cuidado: El id esta vacio");
		}

	}

	@FXML public void refrescarLista() throws IOException {

		usuarios.clear();
		mapUsuario.clear();

		try {

			ResponseEntity<Usuario[]> responseUsuario = restTemplate.getForEntity(INIT_URL+"/usuario/getAll", Usuario[].class);
			

			Stream<Usuario> consumerUsuario = Stream.of(responseUsuario.getBody());

			consumerUsuario.forEach(t -> {
				usuarios.add(t);
				mapUsuario.put(t.getId(),t);
				System.out.println(t.toString()+"\t");
			});
				
			
			System.out.println("LOS PRIMEROS TERCEROS******************************** : " + usuarios.toString());

			
			
			

			//upd_naturaleza.setItems(FXCollections.observableArrayList(NaturalezaJuridica.values()));
			//new_naturaleza.setItems(FXCollections.observableArrayList(NaturalezaJuridica.values()));


			new_estado.setItems(FXCollections.observableArrayList(EstadosDeUsuarios.values()));

			upd_estado.setItems(FXCollections.observableArrayList(EstadosDeUsuarios.values()));


		}catch (HttpClientErrorException e) {
			// TODO: handle exception
			AlertBox.display("Listas", "No se pudo actualizar datos");
		}

	}
	@FXML public void borrar() throws IOException {
		
		boolean borrado = ConfirmBox.display("Confirmar borrado", "Seguro que desea borrar usuario", 
				getClass().getResource("/templates/Estilos/ConfirmBoxStyle.css").toExternalForm());
		
		if(borrado) {
			Usuario user = tabla.getSelectionModel().getSelectedItem();
			Map<String, Integer> params = new HashMap<>();
			params.put("id", user.getId());
			try {
				restTemplate.delete(INIT_URL+"/usuario/delete?id={id}", params);
				
				mapUsuario.remove(user.getId());
				usuarios.remove(user);
				
			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				AlertBox.display("Error de Borrado", "Ha sucedido un error de borrado, vuelva a intentarlo más tarde");
			}
		}
		
	}
}
