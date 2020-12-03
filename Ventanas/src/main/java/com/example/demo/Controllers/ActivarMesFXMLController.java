package com.example.demo.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.pojos.Anno;
import com.example.demo.model.pojos.Ciudad;
import com.example.demo.model.pojos.Estado;
import com.example.demo.model.pojos.EstadosDeMes;
import com.example.demo.model.pojos.Mes;
import com.example.demo.model.pojos.MesFiscalId;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



public class ActivarMesFXMLController implements Initializable{
	
	@FXML private javafx.scene.control.Button closeButton;

	@FXML private ComboBox<String> comboMeses;
	@FXML private Button botonGuardar;
	
	
	private static ObservableList<String> losMeses = FXCollections.observableArrayList();
	
	private Map<String, Mes> mapMeses = Maps.newHashMap();
	
	private final String INIT_URL = "http://localhost:8080/pro/mes";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			  //LLena la list de Meses
			  solicitarMesesAnnoActivo();
			  /*
				TableColumn<Mes, String> columna1 = new TableColumn<Mes, String>("elMes");
				TableColumn<Mes, LocalDate> columna2 = new TableColumn<Mes, LocalDate>("Inicio");
				TableColumn<Mes, LocalDate> columna3 = new TableColumn<Mes, LocalDate>("Fin");
				TableColumn<Mes, Boolean> columna4 = new TableColumn<Mes, Boolean>("Cerrado");

				columna1.setCellValueFactory(new PropertyValueFactory<>("elMes"));
				columna2.setCellValueFactory(new PropertyValueFactory<>("inicioMes"));
				columna3.setCellValueFactory(new PropertyValueFactory<>("finMes"));
				columna4.setCellValueFactory(new PropertyValueFactory<>("cerrado"));
			*/	
				comboMeses.setItems(losMeses);
				//tablaMes.getColumns().addAll(columna1, columna2, columna3, columna4);
				
				
			
		}catch (HttpClientErrorException e) {
			e.printStackTrace();
		}
	}
	
	@FXML protected void  onComboMesesChanged() {
		 botonGuardar.setDisable(false);
	}
	
		
		

	
	@FXML protected void  onBotonGuardarClicked(ActionEvent actionEvent) {

		MesFiscalId mesSeleccionado = mapMeses.get(comboMeses.getValue()).getIdFiscal();
		int intMes = mesSeleccionado.getMes();
		int intAnno = mesSeleccionado.getAnno().getElAnno();
		
		//Necesito buscar el mes que se va a activar
		
		MesFiscalId id = new MesFiscalId();
		Map<String, Integer> params = Maps.newHashMap();
		
		params.put("idAnno", intAnno);
		
		ResponseEntity<Anno> getAnno = restTemplate.getForEntity("http://localhost:8080/pro/anno/get?id={idAnno}", Anno.class, params);
		
		Anno anno = getAnno.getBody();
		
		id.setAnno(anno);
		id.setMes(intMes);
		
		System.out.println("Voy a traer el objeto mes" + mesSeleccionado);
//OJO TODO LO ANTERIOR SOBRA. ERA UAN APRUEBA SI NO TENEMOS OBJETO MES ID
		
		
		String respuesta = restTemplate.getForObject(INIT_URL+"/inactivateAll", String.class);
		ResponseEntity<Mes> getMes = restTemplate.postForEntity(INIT_URL+"/get", mesSeleccionado, Mes.class);
		
		System.out.println("Ya ");

		
		Mes objetoMes = getMes.getBody();
		
		//Debo cambiar el estado al nuevo mes
		EstadosDeMes newEstado = mapMeses.get(comboMeses.getValue()).getEstado();
		
		//Pongo el nuevo estado dependiendo del valor estado del mes obtenido
		newEstado = (objetoMes.getEstado().equals(EstadosDeMes.ACTIVO)?EstadosDeMes.CERRADO:EstadosDeMes.ACTIVO);
		objetoMes.setEstado(newEstado);
		
		//Actualizo el servidor
		System.out.println("Activando mes" + mesSeleccionado + " " + newEstado);
		
		restTemplate.put(INIT_URL+"/update", objetoMes);
		
        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Activar mes");
        
        alert.setContentText("El mes " + comboMeses.getValue().toString() + " se ha activado  correctamente");
        alert.showAndWait();
   	}

	
	private void solicitarMesesAnnoActivo() {
		losMeses.clear();
		
		ResponseEntity<Mes[]> respuesta = restTemplate.getForEntity(INIT_URL + "/getAllDeAnnoActivo", Mes[].class);
		Stream<Mes> consummer = Stream.of(respuesta.getBody());
		consummer.forEach(mes -> {
			mapMeses.put(mes.getNombre(), mes);
			losMeses.add(mes.getNombre());
		});
	}

}
