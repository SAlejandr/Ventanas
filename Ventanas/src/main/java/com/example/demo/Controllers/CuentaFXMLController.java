package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.Vector;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.CuentaDTO;
import com.example.demo.model.pojos.ConfiguracionCuentas;
import com.example.demo.model.pojos.ConfiguracionDePlanDeCuentas;
import com.example.demo.model.pojos.Cuenta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.CheckBox;

public class CuentaFXMLController implements Initializable{

	@FXML TableView<CuentaDTO> tabla;

	@FXML TextField new_codCuenta;
	@FXML ComboBox new_cuentaS;
	@FXML CheckBox new_hasTercero;
	@FXML CheckBox new_hasCCostos;
	@FXML CheckBox new_hasMovimientos;
	@FXML TextField new_nombre;
	@FXML ComboBox<ConfiguracionCuentas> new_config;

	@FXML ComboBox upd_cuentaS;
	@FXML TextField upd_codCuenta;
	@FXML CheckBox upd_hasTercero;
	@FXML CheckBox upd_hasCCostos;
	@FXML CheckBox upd_hasMovimientos;
	@FXML TextField upd_nombre;
	@FXML ComboBox<ConfiguracionCuentas> upd_config;

	private RestTemplate restTemplate = new RestTemplate();
	private final String INIT_URL = "http://localhost:8080/pro"; 

	private ObservableList<CuentaDTO> cuentasDTO = FXCollections.observableArrayList();
	private ObservableList<ConfiguracionCuentas> configuraciones = FXCollections.observableArrayList();
	private ObservableList<String> codCuentas = FXCollections.observableArrayList();
	private Vector<ConfiguracionDePlanDeCuentas> niveles = new Vector<ConfiguracionDePlanDeCuentas>();
	private TreeMap<String, Cuenta> cuentas = new TreeMap<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		refrescarLista();
		ResponseEntity<ConfiguracionDePlanDeCuentas[]> response = restTemplate.getForEntity(INIT_URL+"/confPlanDeCuentas/getAll", ConfiguracionDePlanDeCuentas[].class);

		Stream<ConfiguracionDePlanDeCuentas> stream = Stream.of(response.getBody());
		stream.forEach(niveles::add);



		TableColumn<CuentaDTO, String> columna1 = new TableColumn<>("Cod.Cuenta");
		TableColumn<CuentaDTO, String> columna2 = new TableColumn<>("Nombre");
		TableColumn<CuentaDTO, String> columna3 = new TableColumn<>("Cuenta superior");
		TableColumn<CuentaDTO, String> columna4 = new TableColumn<>("Configuracion cuenta");
		TableColumn<CuentaDTO, Boolean> columna5 = new TableColumn<>("Tercero");
		TableColumn<CuentaDTO, Boolean> columna6 = new TableColumn<>("Centro de costos");
		TableColumn<CuentaDTO, Boolean> columna7 = new TableColumn<>("Movimientos");

		columna1.setCellValueFactory(new PropertyValueFactory<>("codCuenta"));
		columna2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columna3.setCellValueFactory(new PropertyValueFactory<>("codCuentaS"));
		columna4.setCellValueFactory(new PropertyValueFactory<>("codConfig"));
		columna5.setCellValueFactory(new PropertyValueFactory<>("tercero"));
		columna6.setCellValueFactory(new PropertyValueFactory<>("codCuenta"));
		columna7.setCellValueFactory(new PropertyValueFactory<>("codCuenta"));

		tabla.getColumns().addAll(columna1, columna2, columna3,columna4,columna5,columna6, columna7);
		tabla.setItems(cuentasDTO);

		new_config.setItems(configuraciones);
		upd_config.setItems(configuraciones);
		new_cuentaS.setItems(codCuentas); 
		upd_cuentaS.setItems(codCuentas);

	}

	@FXML public void autocompletar() {

		Cuenta cuentaSuperior = cuentas.get(new_cuentaS.getSelectionModel().getSelectedItem());

		new_codCuenta.setText(""+cuentaSuperior.getCodCuenta());

	}

	@FXML public void remplazar() {

		String codCuenta = upd_codCuenta.getText().trim();

		ConfiguracionDePlanDeCuentas nivel = null;

		for (int i = 0; i < niveles.size()-1 && codCuenta.length()>= niveles.get(i).getLongitud(); i++) {

			nivel = niveles.get(i);
		}

		String cuentaSuper = codCuenta.substring(0, nivel.getLongitud());
		int codCuentaPadre = Integer.parseInt(cuentaSuper);

		upd_cuentaS.getSelectionModel().select(cuentaSuper);
	}

	@FXML public void crear() throws IOException {

		String in = new_codCuenta.getText();

		if(!cuentas.containsKey(in)) {

			Cuenta c = Cuenta.builder().
					codCuenta(in).
					cuentaSuperior(cuentas.get(new_cuentaS.getSelectionModel().getSelectedItem()).getCodCuenta()).			//PROBAR SI QUEDA BIEN
					nombre(new_nombre.getText()).
					claseCuenta(new_config.getSelectionModel().getSelectedItem()).
					ccostos(new_hasCCostos.isSelected()).
					movimientos(new_hasMovimientos.isSelected()).
					terceros(new_hasTercero.isSelected()).
					build();

			try {
				restTemplate.postForEntity(INIT_URL+"/cuentas/add", c, Cuenta.class);
				
				codCuentas.add(in);
				cuentas.put(in, c);
			}catch (HttpClientErrorException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}else {
			AlertBox.display("Error", "La cuenta ya existe");
		}

	}

	@FXML public void actualizar() throws IOException {

		String in = upd_codCuenta.getText();

		if(cuentas.containsKey(in)) {

			Cuenta c = Cuenta.builder().
					codCuenta(in).
					cuentaSuperior(cuentas.get(upd_cuentaS.getSelectionModel().getSelectedItem()).getCodCuenta()).
					nombre(upd_nombre.getText()).
					claseCuenta(upd_config.getSelectionModel().getSelectedItem()).
					ccostos(upd_hasCCostos.isSelected()).
					movimientos(upd_hasMovimientos.isSelected()).
					terceros(upd_hasTercero.isSelected()).
					build();

			try {
				restTemplate.put(INIT_URL+"/cuentas/update", c);

				refrescarLista();
			}catch(HttpClientErrorException e) {
				e.printStackTrace();
			}

		}else {
			AlertBox.display("Error", "La cuenta debe existir con anterioridad");
		}
	}

	@FXML public void buscar() throws IOException {

		String codCuenta = upd_codCuenta.getText().trim();

		Integer cod = Integer.parseInt(codCuenta);


		if(cuentas.containsKey(cod) && codCuenta.length() > upd_cuentaS.getSelectionModel().getSelectedItem().toString().length()) {

			Cuenta c = cuentas.get(cod);
			upd_nombre.setText(c.getNombre());
			upd_config.getSelectionModel().select(c.getClaseCuenta());
			upd_hasMovimientos.setSelected(c.isMovimientos());
			upd_hasTercero.setSelected(c.isTerceros());
			upd_hasCCostos.setSelected(c.isCcostos());

		}else {
			AlertBox.display("ERROR!", "La cuenta no existe o su cuenta padre no es correcta");
		}
	}

	@FXML public void borrar() {

		CuentaDTO dto = tabla.getSelectionModel().getSelectedItem();

		Map<String, String> params = new TreeMap<String, String>();
		params.put("id", dto.getCodCuenta());

		restTemplate.delete(INIT_URL+"/cuenta/delete?id={id}", params);

	}

	@FXML public void refrescarLista() {

		cuentasDTO.clear();
		cuentas.clear();
		niveles.clear();

		try {
			ResponseEntity<Cuenta[]> response = restTemplate.getForEntity(INIT_URL+"/cuentas/getAll/0", Cuenta[].class);
			ResponseEntity<ConfiguracionCuentas[]> responseConfiguracion = restTemplate.getForEntity(INIT_URL+"/confCuenta/get/all", ConfiguracionCuentas[].class);


			Stream<Cuenta> streamCuentas = Stream.of(response.getBody());
			Stream<ConfiguracionCuentas> streamConf = Stream.of(responseConfiguracion.getBody());

			streamCuentas.forEach(c -> {
				cuentas.put(c.getCodCuenta(), c);
				cuentasDTO.add(new CuentaDTO(c));
			});
			streamConf.forEach(configuraciones::add);

			codCuentas = FXCollections.observableArrayList(cuentas.keySet());
		}catch(HttpClientErrorException e) {



			e.printStackTrace();
		}


	}
}
