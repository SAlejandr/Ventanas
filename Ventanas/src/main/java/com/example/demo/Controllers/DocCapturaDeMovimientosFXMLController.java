package com.example.demo.Controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import com.example.demo.model.dto.GetUserDTO;
import com.example.demo.model.pojos.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import org.checkerframework.checker.units.qual.m;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.DocumentoDTO;
import com.example.demo.dto.MovimientoDTO;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

public class DocCapturaDeMovimientosFXMLController implements Initializable{

	@Setter
	@Getter
	private GetUserDTO usuario;

	@FXML ScrollPane capturadora;
	@FXML ScrollPane buscar;

	//Busqueda de movimientos
	@FXML TableView<DocumentoDTO> tablaDocumento;

	//Captura de movimientos
	//Documentos
	@FXML private GridPane seccionDocumento;
	@FXML private TextField numDoc;
	@FXML private TextArea docConcepto;
	@FXML private ComboBox tipoDoc;
	@FXML private ComboBox responsableCombo;
	@FXML private Button capturador;
	@FXML private DatePicker laFecha;

	//Movimientos
	@FXML private GridPane seccionMovimiento;
	@FXML private TextField elItem;
	@FXML private TextArea elConcepto;
	@FXML private TextField elCredito;
	@FXML private TextField elDebito;
	@FXML private TextField laBase;
	@FXML private ChoiceBox<Integer> elCCostos;
	@FXML private ComboBox<String> laCuenta;
	@FXML private ComboBox<String> elTercero;
	@FXML private ComboBox<String> elEstado;
	@FXML private TableView<MovimientoDTO> tabla;

	private HashMap<IdDocumento, Documento> mapDocumentos = Maps.newHashMap();
	private HashMap<String,TipoDocumento> mapTipDoc = Maps.newHashMap();
	private Documento documento;

	private HashMap<String, Tercero> mapTercero= Maps.newHashMap();
	private TreeMap<String, Cuenta> mapCuenta= Maps.newTreeMap();
	private HashMap<String, Estado> mapEstado= Maps.newHashMap();
	private HashMap<Integer, CentroDeCosto> mapCCostos= Maps.newHashMap();
	private HashMap<IdMovimiento, Movimiento> movimientosReservado = Maps.newHashMap();
	private static HashSet<Object> responsables = Sets.newHashSet();

	private ObservableList<DocumentoDTO> documentosDTO = FXCollections.observableArrayList();
	private ObservableList<String> tipoDeDocumentos = FXCollections.observableArrayList();
	private ObservableList<MovimientoDTO> movimientosDTO = FXCollections.observableArrayList();
	private ObservableList<String> cuentas = FXCollections.observableArrayList();
	private ObservableList<String> estados = FXCollections.observableArrayList();
	private ObservableList<String> terceros = FXCollections.observableArrayList();
	private ObservableList<Integer> centros = FXCollections.observableArrayList();

	private RestTemplate restTemplate = new RestTemplate();
	private Set<Movimiento> movimientos = Sets.newHashSet();

	private final String INIT_URL = "http://localhost:8080/pro";


	private boolean seleccionado;
	private boolean guardado;

	EstadoDeAplicacion estadoAction = EstadoDeAplicacion.CREANDO;
	EstadoDeAplicacion estadoAplicacion = EstadoDeAplicacion.CREANDO;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		seccionMovimiento.setDisable(true);



		//documento = new Documento();
		seleccionado = true;
		guardado =true;


		refrescarLista();
		

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



		TableColumn<MovimientoDTO, String> columna1 = new TableColumn<>("Item");
		TableColumn<MovimientoDTO, String> columna2 = new TableColumn<>("Concepto");
		TableColumn<MovimientoDTO, LocalDate> columna3 = new TableColumn<>("Fecha");
		TableColumn<MovimientoDTO, Integer> columna4 = new TableColumn<>("Cuenta");
		TableColumn<MovimientoDTO, String> columna5 = new TableColumn<>("Tercero");
		TableColumn<MovimientoDTO, Integer> columna6 = new TableColumn<>("C_Costos");
		TableColumn<MovimientoDTO, Float> columna7 = new TableColumn<>("Credito");
		TableColumn<MovimientoDTO, Float> columna8 = new TableColumn<>("Debito");
		TableColumn<MovimientoDTO, Float> columna9 = new TableColumn<>("Base");
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




		TableColumn<DocumentoDTO, Long> columnaDoc1 = new TableColumn<DocumentoDTO, Long>("Nº Documento");
		TableColumn<DocumentoDTO, String> columnaDoc2 = new TableColumn<DocumentoDTO, String>("Tipo Documento");
		TableColumn<DocumentoDTO, String> columnaDoc3 = new TableColumn<DocumentoDTO, String>("Concepto");
		TableColumn<DocumentoDTO, LocalDate> columnaDoc4 = new TableColumn<DocumentoDTO, LocalDate>("Fecha");
		TableColumn<DocumentoDTO, String> columnaDoc5 = new TableColumn<DocumentoDTO, String>("Responsable");
		TableColumn<DocumentoDTO, Float> columnaDoc6 = new TableColumn<DocumentoDTO, Float>("Total");

		columnaDoc1.setCellValueFactory(new PropertyValueFactory<>("numDocumento"));
		columnaDoc2.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
		columnaDoc3.setCellValueFactory(new PropertyValueFactory<>("concepto"));
		columnaDoc4.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columnaDoc5.setCellValueFactory(new PropertyValueFactory<>("responsable"));
		columnaDoc6.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

		tablaDocumento.setItems(documentosDTO);
		tablaDocumento.getColumns().addAll(columnaDoc1,columnaDoc2,columnaDoc3,columnaDoc4,columnaDoc5,columnaDoc6);



	}

	@FXML private void refrescarLista() {
		// TODO Auto-generated method 
		//Aunque obtenga varios meses, solo cojo el primero y unico activo, a futuro se piensa poder usar mas de un mes activo
		ResponseEntity<Mes[]> getMesesActivos = restTemplate.getForEntity(INIT_URL+"/mes/getAllDeAnnoActivo", Mes[].class);

		Stream<Mes> consumer = Stream.of(getMesesActivos.getBody());
		Mes mes = consumer.filter(m -> m.getEstado().equals(EstadosDeMes.ACTIVO)).findFirst().get();

		HashMap<String, String> params = Maps.newHashMap();
		params.put("nombre", mes.getNombre());

		ResponseEntity<Documento[]> respuestaDoc = restTemplate.getForEntity(INIT_URL + "/documento/getAllByMes?nombreMes={nombre}", Documento[].class, params);
		Stream<Documento> consumerDoc = Stream.of(respuestaDoc.getBody());
		mapDocumentos.clear();
		documentosDTO.clear();

		consumerDoc.forEach(doc -> {
			System.out.println(doc.toString());
			mapDocumentos.put(doc.getIdDocumento(), doc);
			documentosDTO.add(new DocumentoDTO(doc));
		});

		ResponseEntity<Responsable[]> respuesta1 = restTemplate.getForEntity(INIT_URL+"/responsable/getAll", Responsable[].class);
		Stream<Responsable> consumer1 = Stream.of(respuesta1.getBody());
		consumer1.forEach(responsables::add);

		responsableCombo.setItems(FXCollections.observableArrayList(responsables.toArray()));

		ResponseEntity<Cuenta[]> respuesta2 = restTemplate.getForEntity(INIT_URL+"/cuentas/getAll/1", Cuenta[].class);
		ResponseEntity<CentroDeCosto[]> respuesta3 = restTemplate.getForEntity(INIT_URL+"/centroDeCosto/get/all", CentroDeCosto[].class);
		ResponseEntity<Tercero[]> respuesta4 = restTemplate.getForEntity(INIT_URL+"/tercero/getAll", Tercero[].class);

		Stream<Cuenta> consumer2 = Stream.of(respuesta2.getBody());
		consumer2.forEach(cuenta -> mapCuenta.put(cuenta.getCodCuenta(), cuenta));
		Stream<CentroDeCosto> consumer3 = Stream.of(respuesta3.getBody());
		consumer3.forEach(cCostos -> mapCCostos.put(cCostos.getCodCentro(), cCostos));
		Stream<Tercero> consumer4 = Stream.of(respuesta4.getBody());
		consumer4.forEach(tercero -> mapTercero.put(tercero.getId(), tercero));


		ResponseEntity<TipoDocumento[]> respuesta5 = restTemplate.getForEntity(INIT_URL + "/tipoDocumento/getAll", TipoDocumento[].class);
		Stream<TipoDocumento> consumer5 = Stream.of(respuesta5.getBody());
		consumer5.forEach(td -> mapTipDoc.put(td.getTipoDoc(), td));

		mapTipDoc.keySet().stream().forEach(tipoDeDocumentos::add);

		tipoDoc.setItems(tipoDeDocumentos);

		ResponseEntity<Estado[]> respuesta6 = restTemplate.getForEntity(INIT_URL+"/estado/getAll", Estado[].class);
		Stream<Estado> consumer6 = Stream.of(respuesta6.getBody());
		consumer6.forEach(estado -> mapEstado.put(estado.getCodEstado(), estado));

		mapCuenta.keySet().stream().forEach(cuentas::add);
		mapCCostos.keySet().stream().forEach(centros::add);
		mapTercero.keySet().stream().forEach(terceros::add);
		mapEstado.keySet().stream().forEach(estados::add);

		elCCostos.setItems(centros);
		laCuenta.setItems(cuentas);
		elEstado.setItems(estados);
		elTercero.setItems(terceros);

		if(seleccionado && documento != null)
			iniciar();

	}

	public void iniciar() {

		System.err.println("El Documento seleccionado :"+documento);

		movimientosDTO.clear();
		movimientos.clear();


		movimientosReservado.clear();

		cuentas.clear();
		centros.clear();
		terceros.clear();
		estados.clear(); 

		try {
			ResponseEntity<Movimiento[]> respuesta = restTemplate.postForEntity(INIT_URL+ "/documento/getMovimientos", documento, Movimiento[].class);
			Stream<Movimiento> consumer = Stream.of(respuesta.getBody());
			consumer.forEach(mov -> {
				System.out.println(mov);
				movimientos.add(mov);
				movimientosReservado.put(mov.getId(), mov);
				movimientosDTO.add(new MovimientoDTO(mov));

			});

			movimientosReservado.keySet().stream().forEach(id -> {
				System.out.println(movimientosReservado.get(id).toString());
			});
		} catch (HttpClientErrorException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

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
		guardado = false;
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

			try {
				FuncionesVentanas.marcarErrorFloat(elCredito);
				FuncionesVentanas.marcarErrorFloat(elDebito);
				FuncionesVentanas.marcarErrorFloat(laBase);

				m.setCredito( new BigDecimal(elCredito.getText()));
				m.setDebito(new BigDecimal(elDebito.getText()));
				m.setBase(new BigDecimal(laBase.getText()));

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

			if (m.getCuenta().isTerceros()) {
				m.setTercero(mapTercero.get(elTercero.getValue()));
			}else {
				m.setTercero(mapTercero.get("."));
			}

			if (m.getCuenta().isCcostos()) {
				m.setCCostos(mapCCostos.get(elCCostos.getValue()));
			}else {
				m.setCCostos(mapCCostos.get(0));
			}
			if(correcto) {
				if(movimientos.contains(m)){
					restTemplate.put(INIT_URL+"/movimiento/update",m);
				}else{
					restTemplate.postForEntity(INIT_URL+"/movimiento/add", m, Movimiento.class);
				}
				movimientos.add(m);
				movimientosDTO.add(new MovimientoDTO(m));

				limpiarMov();
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

	public void guardarYSalir(){


		boolean sale = false;
		Alert alert = new Alert(AlertType.INFORMATION);
		//alert.getButtonTypes().
		if(advertir()) {
			guardarDocumento();

		}else {
			alert.setTitle("Cerrar documento");
			alert.setContentText("No se puede guardar documentos descuadrados");

		}

	}

	public void guardarDocumento() {
		BigDecimal valorTotal = new BigDecimal(0);
		
		for(Movimiento m : movimientos) {
			valorTotal = valorTotal.add(m.getDebito());
			System.err.println("Total::::::::::::::::::::::::::::::::"+valorTotal);
			System.err.println("Debito:+:+:+:+:+:+:+:+:+:+:+:+:+:+:+:"+m.getDebito());
			
		};

		documento.setValorTotal(valorTotal);
		
		System.err.println(documento.toString());

		if(estadoAction.equals(EstadoDeAplicacion.CREANDO)) {
			restTemplate.postForEntity(INIT_URL+"/documento/add", documento, Documento.class);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Creando");
			estadoAplicacion = EstadoDeAplicacion.AGREGANDO;
		}else {
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Modificar");
			restTemplate.put(INIT_URL+"/documento/update", documento);
		}

		estadoAplicacion = EstadoDeAplicacion.CREANDO;


	}


	public boolean advertir() {

		return (sacarTotal().compareTo(new BigDecimal(0)) != 0 ? true : false);
	}

	public BigDecimal sacarTotal() {

		BigDecimal total = new BigDecimal(0);;

		Iterator<Movimiento> iterator = movimientos.iterator();

		while (iterator.hasNext()) {
			Movimiento movimiento = iterator.next();

			total.add(movimiento.getDebito());
			total.subtract(movimiento.getCredito());

		}
		
	System.err.println("total ###################################>>>>>>>>>>>>>"+total);

		return total;
	}

	@FXML public void restaurarMovimientos() {
		System.out.println("restaurar");
	}

	@FXML public void modificar() {
		System.err.println(usuario.toString());
		if(seleccionado) {
			if(!capturadora.isVisible() && guardado) {
				DocumentoDTO dto = tablaDocumento.getSelectionModel().getSelectedItem();
				IdDocumento id = new IdDocumento(new TipoDocumento(dto.getTipoDocumento(), ""), dto.getNumDocumento());
				documento = mapDocumentos.get(id);
				buscar.setVisible(false);
				capturadora.setVisible(true);
				numDoc.setText(dto.getNumDocumento()+"");
				docConcepto.setText(dto.getConcepto());
				tipoDoc.setValue(documento.getIdDocumento().getTipoDocumento().getTipoDoc());
				laFecha.setValue(dto.getFecha());
				responsableCombo.setValue(documento.getResponsable());

				estadoAction = EstadoDeAplicacion.MODIFICANDO;
				estadoAplicacion = EstadoDeAplicacion.MODIFICANDO;
				iniciar();
			}else if(capturadora.isVisible() && guardado) {
				capturadora.setVisible(false);
				buscar.setVisible(true);
			}
		}

		System.out.println("cambio");
	}
	@FXML public void crear() {
		System.err.println(usuario.toString());
		if(seleccionado) {
			if(!capturadora.isVisible() && guardado) {

				buscar.setVisible(false);
				capturadora.setVisible(true);

				estadoAplicacion = EstadoDeAplicacion.MODIFICANDO;
				estadoAction = EstadoDeAplicacion.CREANDO;
			}else if(capturadora.isVisible() && guardado) {
				capturadora.setVisible(false);
				buscar.setVisible(true);
			}
		}

		System.out.println("cambio");
	}


	public void crearDocumento(ActionEvent actionEvent) {

		IdDocumento id = new IdDocumento();

		TipoDocumento td = mapTipDoc.get(tipoDoc.getValue());
		System.out.println(td);
		id.setNumDocumento(Long.parseLong(numDoc.getText()));
		id.setTipoDocumento(td);

		documento = Documento.builder().
				idDocumento(id).
				concepto(docConcepto.getText()).
				fecha(laFecha.getValue()).
				responsable((Responsable) responsableCombo.getSelectionModel().getSelectedItem()).
				userResponsable(Usuario.builder().id(usuario.getId()).build()).
				estado(Estado.builder().codEstado("AC").build()).
				build();

		try{
			if(estadoAction.equals(EstadoDeAplicacion.CREANDO)) {
				restTemplate.postForEntity(INIT_URL+"/documento/add", documento, Documento.class);
			}else {
				restTemplate.put(INIT_URL+"/documento/update", documento);

			}

			movimientos.stream().forEach(System.out::println);
			estadoAplicacion = EstadoDeAplicacion.AGREGANDO;

			seccionDocumento.setDisable(true);
			seccionMovimiento.setDisable(false);
		}catch (HttpClientErrorException e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Guardado");
			alert.setContentText("El usuario no existe o fallo la conexion");
			alert.showAndWait();
		}

	}

	@FXML public void cerrarDocumento(ActionEvent actionEvent) {

		if(!advertir()) {
			System.err.println("advertir -----------> "+ advertir());
			limpiarDoc();
		}
		guardarDocumento();


	}

	public void limpiarMov() {
		elItem.clear();
		elConcepto.clear();
		elCredito.clear();
		elDebito.clear();
		laBase.clear();
		laCuenta.setValue("");
		elEstado.setValue("AC");

	}

	public void limpiarDoc() {
		limpiarMov();

		numDoc.clear();;
		docConcepto.clear();;
		tipoDoc.setValue("");
		responsableCombo.setValue("");
	}
}
