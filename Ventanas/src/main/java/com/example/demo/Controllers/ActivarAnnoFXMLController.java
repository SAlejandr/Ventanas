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



public class ActivarAnnoFXMLController implements Initializable{
	

	@FXML private ComboBox<String> comboAnnos;
	@FXML private Button botonGuardar;
	
	
	private static ObservableList<String> losAnnos = FXCollections.observableArrayList();
	
	private Map<Integer, Anno> mapAnnos = Maps.newHashMap();
	
	private final String INIT_URL = "http://localhost:8080/pro/anno";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			  //LLena la list de Años
			  solicitarListaAnnos();
	
			comboAnnos.setItems(losAnnos);
		
		}catch (HttpClientErrorException e) {
			e.printStackTrace();
		}
	}
	
	@FXML protected void  onComboAnnosChanged() {
		 botonGuardar.setDisable(false);
	}
	
		
		

	
	@FXML protected void  onBotonGuardarClicked(ActionEvent actionEvent) {

		int annoSeleccionado = Integer.parseInt(comboAnnos.getValue());
		
/*		
		int intMes = Integer.parseInt(mesSeleccionado.getMes());
		int intAnno = mesSeleccionado.getAnno().getElAnno();
		
		//Necesito buscar el mes que se va a activar
		
		MesFiscalId id = new MesFiscalId();
		Map<String, Integer> params = Maps.newHashMap();
		
		params.put("idAnno", intAnno);
		
		ResponseEntity<Anno> getAnno = restTemplate.getForEntity("http://localhost:8080/pro/anno/get?id={idAnno}", Anno.class, params);
		
		Anno anno = getAnno.getBody();
		
		id.setAnno(anno);
		id.setMes(intMes+"");
		
		System.out.println("Voy a traer el objeto mes" + mesSeleccionado);
//OJO TODO LO ANTERIOR SOBRA. ERA UAN APRUEBA SI NO TENEMOS OBJETO MES ID
		
*/		
		System.out.println("Voy a traer el objeto Anno " + annoSeleccionado);
		//String respuesta = restTemplate.getForObject(INIT_URL+"/inactivateAll", String.class);
		Map<String, Integer> params = Maps.newHashMap();
		
		params.put("idAnno", annoSeleccionado);
		ResponseEntity<Anno> getAnno = restTemplate.getForEntity(INIT_URL+"/get?id={idAnno}", Anno.class, params);
		
		System.out.println("Ya ");

		
		Anno objetoAnno = getAnno.getBody();
		System.out.println("El objeto Año que tengo : " + objetoAnno);
		
		objetoAnno.setCerrado(false);

		
		//Actualizo el servidor
		System.out.println("Activando anno" + objetoAnno );
		
		String respuesta = restTemplate.getForObject(INIT_URL+"/inactivateAll", String.class);

		restTemplate.put(INIT_URL+"/update", objetoAnno);
		
        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Activar año");
        
        alert.setContentText("El año " + comboAnnos.getValue().toString() + " se ha activado  correctamente");
        alert.showAndWait();
        
        
   	}

	
	private void solicitarListaAnnos() {
		losAnnos.clear();
		
		ResponseEntity<Anno[]> respuesta = restTemplate.getForEntity(INIT_URL + "/get/all", Anno[].class);
		Stream<Anno> consummer = Stream.of(respuesta.getBody());
		consummer.forEach(anno -> {
			mapAnnos.put(anno.getElAnno(), anno);
			losAnnos.add(anno.getElAnno()+"");
		});
		
		losAnnos.stream().forEach(System.out::println);
	}

}
