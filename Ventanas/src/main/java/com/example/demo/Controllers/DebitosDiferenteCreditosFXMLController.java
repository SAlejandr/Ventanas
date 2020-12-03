package com.example.demo.Controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.Auditoria;
import com.example.demo.dto.MovimientoDTO;
import com.example.demo.model.pojos.Anno;
import com.example.demo.model.pojos.Ciudad;
import com.example.demo.model.pojos.Estado;
import com.example.demo.model.pojos.EstadosDeMes;
import com.example.demo.model.pojos.IdMovimiento;
import com.example.demo.model.pojos.Mes;
import com.example.demo.model.pojos.MesFiscalId;
import com.example.demo.model.pojos.Movimiento;
import com.google.common.collect.Maps;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.Window;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import javafx.scene.control.TableView;



public class DebitosDiferenteCreditosFXMLController implements Initializable{

	@FXML private javafx.scene.control.Button closeButton;
	@FXML private ComboBox<String> annosCombo;
	@FXML private ComboBox<String> mesInicialCombo;
	@FXML private ComboBox<String> mesFinalCombo;
	@FXML private TextField annoText;
	@FXML TableView<MovimientoDTO> resultadoTabla;

	@FXML private Button iniciarAuditoriaBoton;

	private static ObservableList<String> losAnnos = FXCollections.observableArrayList();

	private static ObservableList<String> losMesesIniciales = FXCollections.observableArrayList();

	private static ObservableList<String> losMesesFinales = FXCollections.observableArrayList();
	
	private static ObservableList<MovimientoDTO> movimentosDTO  = FXCollections.observableArrayList();
	
	private Map<String, Anno> mapAnnos = Maps.newHashMap();


	private Map<String, Mes> mapMesesIniciales = Maps.newHashMap();
	private Map<String, Mes> mapMesesFinales = Maps.newHashMap();

	private Anno anno;
	private final String INIT_URL = "http://localhost:8080/pro/auditoria/debitosDiferenteCreditos";

	private RestTemplate restTemplate = new RestTemplate();
	



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			//solicitarListaAnnos();

			anno = restTemplate.getForObject("http://localhost:8080/pro/anno/get/activo", Anno.class);

			annoText.setText(anno.getElAnno()+"");
			solicitarMesesInicialesAnno(anno.getElAnno()); 


		}catch (HttpClientErrorException e) {
			e.printStackTrace();
		}

	}


	@FXML protected void  onMesInicialComboChanged() {
		mesFinalCombo.setDisable(false);

		int index = mesInicialCombo.getSelectionModel().getSelectedIndex();

		losMesesFinales.stream().forEach(System.out::println);

		System.out.println(losMesesFinales.indexOf("FEBRERO DE 2020"));

		for(int i = 0; i< index; i++) {
			System.out.println(i);

			losMesesFinales.remove(0);
		}
	}

	@FXML protected void  onMesFinalComboChanged() {
		iniciarAuditoriaBoton.setDisable(false);
	}


	@FXML protected void  onAnnosComboChanged(ActionEvent actionEvent) {
		mesInicialCombo.setDisable(false);
		int anno = Integer.parseInt(annosCombo.getValue());
		solicitarMesesInicialesAnno(anno);
	}


	@FXML protected void  onIniciarAuditoriaBotonClicked(ActionEvent actionEvent) {
		movimentosDTO.clear();

		//00000000000000000000000000000000000000000000000000000    DESDE AQUI COMIENZA EL PROCESO DE AUDITORIA      00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
		Auditoria audi = Auditoria.builder().
				mesInicial(mapMesesIniciales.get(mesInicialCombo.getValue())).
				mesFinal(mapMesesFinales.get(mesFinalCombo.getValue())).
				build();
		
		ResponseEntity<Movimiento[]> respuesta = restTemplate.postForEntity(INIT_URL, audi , Movimiento[].class);
		
		Stream<Movimiento> consumer = Stream.of(respuesta.getBody());
		
		consumer.forEach(m -> movimentosDTO.add(new MovimientoDTO(m)));
		
		
		resultadoTabla.setItems(movimentosDTO);

		
		TableColumn<MovimientoDTO, Long> columna1 = new TableColumn<>("Num_Documento");
		TableColumn<MovimientoDTO, String> columna2 = new TableColumn<>("TipoDoc");
		TableColumn<MovimientoDTO, Integer> columna3 = new TableColumn<>("Item");
		TableColumn<MovimientoDTO,LocalDate > columna4 = new TableColumn<>("Fecha");
		TableColumn<MovimientoDTO, String> columna5 = new TableColumn<>("Cuenta");
		
		columna1.setCellValueFactory(new PropertyValueFactory<>("numDoc"));
		columna2.setCellValueFactory(new PropertyValueFactory<>("tipoDoc"));
		columna3.setCellValueFactory(new PropertyValueFactory<>("item"));
		columna4.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		columna5.setCellValueFactory(new PropertyValueFactory<>("cuenta"));
		
		resultadoTabla.getColumns().addAll(columna1,columna2,columna3,columna4,columna5);

	}

	private void solicitarListaAnnos() {

		losAnnos.clear();

		ResponseEntity<Anno[]> respuesta = restTemplate.getForEntity(INIT_URL + "/anno/get/all", Anno[].class);
		Stream <Anno> consumer = Stream.of(respuesta.getBody());

		consumer.forEach(anno -> {
			mapAnnos.put(anno.getElAnno()+"", anno);
			losAnnos.add(anno.getElAnno()+"");
		});


	}

	private void solicitarMesesInicialesAnno(int anno) { 			//ORGANIZAR QUE SEA PARA EL AÑO SELECCIONADO
		losMesesIniciales.clear();
		losMesesFinales.clear();


		Map<String, Integer> params = Maps.newHashMap();
		params.put("anno", anno);
		ResponseEntity<Mes[]> respuesta = restTemplate.getForEntity("http://localhost:8080/pro/mes/getAllDeAnno?anno={anno}", Mes[].class, params);
		Stream<Mes> consummer = Stream.of(respuesta.getBody());
		consummer.forEach(mes -> {
			mapMesesIniciales.put(mes.getNombre(), mes);
			mapMesesFinales.put(mes.getNombre(), mes);
			losMesesIniciales.add(mes.getNombre());
			losMesesFinales.add(mes.getNombre());
		});
		mesInicialCombo.setItems(losMesesIniciales);

		mesFinalCombo.setItems(losMesesFinales);			// OJO HACER QUE MESES FINALES POSTERIORES AL INICIAL

	}

}
