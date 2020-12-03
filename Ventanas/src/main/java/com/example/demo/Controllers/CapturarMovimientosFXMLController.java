package com.example.demo.Controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.m;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.MovimientoDTO;
import com.example.demo.model.pojos.CentroDeCosto;
import com.example.demo.model.pojos.Cuenta;
import com.example.demo.model.pojos.Documento;
import com.example.demo.model.pojos.Estado;
import com.example.demo.model.pojos.IdDocumento;
import com.example.demo.model.pojos.IdMovimiento;
import com.example.demo.model.pojos.Movimiento;
import com.example.demo.model.pojos.Tercero;
import com.example.demo.util.FuncionesVentanas;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Tables;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class CapturarMovimientosFXMLController implements Initializable{

	@FXML private DatePicker laFecha;
	@FXML private TextField elItem;
	@FXML private TextField elConcepto;
	@FXML private TextField elCredito;
	@FXML private TextField elDebito;
	@FXML private TextField laBase;
	@FXML private ChoiceBox<Integer> elCCostos;
	@FXML private ComboBox<String> laCuenta;
	@FXML private ComboBox<String> elTercero;
	@FXML private ComboBox<String> elEstado;
	@FXML private TableView<MovimientoDTO> tabla;

	@Getter
	@Setter
	@Autowired 
	private Documento documento ;
	@Getter
	@Setter
	private HashMap<String, Tercero> mapTercero;
	@Getter
	@Setter
	private HashMap<String, Cuenta> mapCuenta;
	@Getter
	@Setter
	private HashMap<String, Estado> mapEstado;
	@Getter
	@Setter
	private HashMap<Integer, CentroDeCosto> mapCCostos;

	private ObservableList<MovimientoDTO> movimientosDTO = FXCollections.observableArrayList();
	private ObservableList<String> cuentas = FXCollections.observableArrayList();
	private ObservableList<String> estados = FXCollections.observableArrayList();
	private ObservableList<String> terceros = FXCollections.observableArrayList();
	private ObservableList<Integer> centros = FXCollections.observableArrayList();

	private RestTemplate restTemplate = new RestTemplate();
	private Set<Movimiento> movimientos = Sets.newHashSet();

	private final String INIT_URL = "http://localhost:8080/pro/movimiento";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		laCuenta.setOnAction(e->{
			e.consume();
			Cuenta c = mapCuenta.get(laCuenta.getValue());
			if(c.isCcostos())
				elCCostos.setDisable(false);
			else 
				elCCostos.setDisable(true);

			if(c.isTerceros())
				elTercero.setDisable(false);
			else 
				elTercero.setDisable(true);

		});

		Stage stage = (Stage) laCuenta.getScene().getWindow();

		stage.setWidth(700);
		stage.setHeight(700);
		
		TableColumn<MovimientoDTO, String> columna1 = new TableColumn<>("Item");
		TableColumn<MovimientoDTO, String> columna2 = new TableColumn<>("Concepto");
		TableColumn<MovimientoDTO, LocalDate> columna3 = new TableColumn<>("Fecha");
		TableColumn<MovimientoDTO, String> columna4 = new TableColumn<>("Cuenta");
		TableColumn<MovimientoDTO, String> columna5 = new TableColumn<>("Tercero");
		TableColumn<MovimientoDTO, Integer> columna6 = new TableColumn<>("C_Costos");
		TableColumn<MovimientoDTO, BigDecimal> columna7 = new TableColumn<>("Credito");
		TableColumn<MovimientoDTO, BigDecimal> columna8 = new TableColumn<>("Debito");
		TableColumn<MovimientoDTO, BigDecimal> columna9 = new TableColumn<>("Base");
		TableColumn<MovimientoDTO, String> columna10 = new TableColumn<>("Estado");

		columna1.setCellValueFactory(new PropertyValueFactory<>("item"));
		columna2.setCellValueFactory(new PropertyValueFactory<>("concepto"));
		columna3.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columna4.setCellValueFactory(new PropertyValueFactory<>("cuenta"));
		columna5.setCellValueFactory(new PropertyValueFactory<>("tercero"));
		columna6.setCellValueFactory(new PropertyValueFactory<>("ccostos"));
		columna7.setCellValueFactory(new PropertyValueFactory<>("credito"));
		columna8.setCellValueFactory(new PropertyValueFactory<>("debito"));
		columna9.setCellValueFactory(new PropertyValueFactory<>("base"));
		columna10.setCellValueFactory(new PropertyValueFactory<>("estado"));

		tabla.setItems(movimientosDTO);
		tabla.getColumns().addAll(columna1,columna2,columna3,columna4,columna5,columna6,columna7,columna8, columna9, columna10);



	}

	public void iniciar() {

		System.out.println(documento);
		movimientosDTO.clear();
		movimientos.clear();

		cuentas.clear();
		centros.clear();
		terceros.clear();
		estados.clear(); 
		
		Map<String, String> params = Maps.newHashMap();
		params.put("n", ""+documento.getIdDocumento().getNumDocumento());
		params.put("tDoc", documento.getIdDocumento().getTipoDocumento().getTipoDoc());
		ResponseEntity<Movimiento[]> respuesta = restTemplate.getForEntity(INIT_URL+ "/getByDocumento?n={n}&tDoc={tDoc}", Movimiento[].class, params);
		Stream<Movimiento> consumer = Stream.of(respuesta.getBody());
		consumer.forEach(movimientos::add);
		
		movimientos.stream().forEach(m -> movimientosDTO.add(new MovimientoDTO(m)));

		mapCuenta.keySet().stream().forEach(cuentas::add);
		mapCCostos.keySet().stream().forEach(centros::add);
		mapTercero.keySet().stream().forEach(terceros::add);
		mapEstado.keySet().stream().forEach(estados::add);

		elCCostos.setItems(centros);
		laCuenta.setItems(cuentas);
		elEstado.setItems(estados);
		elTercero.setItems(terceros);

	}

	@FXML public void guardar() {
		int item = 0;

		try {
			FuncionesVentanas.marcarErrorInt(elItem);
			item = Integer.parseUnsignedInt(elItem.getText());

		}catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cuidado");
			alert.setContentText( "El campo item debe ser un numero entero");
			alert.showAndWait();
		}

		if(item != 0 ) {
			boolean correcto = false;

			IdMovimiento id = new IdMovimiento(item, documento);

			Movimiento m = Movimiento.builder().
					id(id).
					fecha(laFecha.getValue()).
					concepto(elConcepto.getText()).
					cuenta(mapCuenta.get(laCuenta.getValue())).
					estado(mapEstado.get(elEstado.getValue())).
					build();
/*																											//CONVERTIR ESTA VALIDACION A BIGDECIMAL
			try {
				FuncionesVentanas.marcarErrorBigDecimal(elCredito);
				FuncionesVentanas.marcarErrorBigDecimal(elDebito);
				FuncionesVentanas.marcarErrorBigDecimal(laBase);

				m.setCredito(BigDecimal.parseBigDecimal(elCredito.getText()));
				m.setDebito(BigDecimal.parseBigDecimal(elDebito.getText()));
				m.setBase(BigDecimal.parseBigDecimal(laBase.getText()));

				correcto = true;
			}catch (NumberFormatException e) {
				// TODO: handle exception
				try {
					AlertBox.display("Cuidado", "Credito, debito y base deben ser numericos");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
*/
			if (m.getCuenta().isTerceros()) {
				m.setTercero(mapTercero.get(elTercero.getValue()));
			}

			if (m.getCuenta().isCcostos()) {
				m.setCCostos(mapCCostos.get(elCCostos.getValue()));
			}
			if(correcto) {
				movimientos.add(m);
				movimientosDTO.add(new MovimientoDTO(m));
			}
		}

	}

	@FXML public void cargar() {

		MovimientoDTO dto = tabla.getSelectionModel().getSelectedItem();

		elItem.setText(dto.getItem()+"");
		elConcepto.setText(dto.getConcepto());
		elEstado.setValue(dto.getEstado());
		elCCostos.setValue(dto.getCcostos());
		elTercero.setValue(dto.getTercero());
		elCredito.setText(dto.getCredito()+"");
		elDebito.setText(dto.getDebito()+"");
		laBase.setText(dto.getBase()+"");
		laCuenta.setValue(dto.getCuenta());
		laFecha.setValue(dto.getFecha());
	}

	@FXML public void guardarYSalir() throws IOException {


		boolean sale = false;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		//alert.getButtonTypes().
		if(advertir()) {
			boolean confirmar;

			alert.setTitle("Guardar y salir");
			alert.setContentText("Desea guardar antes de salir");

			Optional<ButtonType> resultado = alert.showAndWait();

			alert.getButtonTypes().stream().forEach(System.out::println);

			confirmar = (resultado.get() == ButtonType.OK);

			if(confirmar) {
				guardarDocumento(confirmar);
				sale = true;
			}
		}else {
			boolean confirmar;

			alert.setTitle("Guardar y salir");
			alert.setContentText("No se puede guardar documentos descuadrados\n¿Desea salir igualmente?");

			Optional<ButtonType> resultado = alert.showAndWait();

			confirmar = (resultado.get() == ButtonType.OK);

			if(confirmar) 
				sale = true;
		}
		if (sale) {
			salir();
		}

	}

	public void guardarDocumento(boolean confirmado) {
		if(confirmado) {
			movimientos.stream().forEach(m -> {
				try {
					restTemplate.postForEntity(INIT_URL+"/add", m, Movimiento.class);
				}catch (HttpClientErrorException e) {
					// TODO: handle exception
					restTemplate.put(INIT_URL + "/update", m, Movimiento.class);
				}
			});
			
		}
	}

	public void salir() throws IOException {
		Stage stage = (Stage) elDebito.getScene().getWindow();

		stage.close();

	}

	public boolean advertir() {

		return (sacarTotal().equals(BigDecimal.ZERO))?true:false;
	}

	public BigDecimal sacarTotal() {

		BigDecimal total = BigDecimal.ZERO;

		Iterator<Movimiento> iterator = movimientos.iterator();

		while (iterator.hasNext()) {
			Movimiento movimiento = iterator.next();

			total = total.add(movimiento.getDebito());
			total = total.subtract(movimiento.getCredito());

		}

		return total;
	}

}