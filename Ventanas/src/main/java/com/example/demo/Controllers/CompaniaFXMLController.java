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
import com.example.demo.model.pojos.Compania;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



public class CompaniaFXMLController implements Initializable{
	

	@FXML private TextField nombreText;
	@FXML private TextField idText;
	@FXML private TextField tipoIdText;
	@FXML private TextField direccionText;
	@FXML private TextField ciudadText;
	@FXML private TextField movilText;
	@FXML private TextField fijoText;
	@FXML private TextField repLegalText;
	@FXML private TextField revisorFiscalText;
	@FXML private TextField contadorPublicoText;
	@FXML private CheckBox requiereCentrosCheck;
	
	@FXML private Button guardarBoton;
	
	
	private static ObservableList<Compania> lasCompanias = FXCollections.observableArrayList();
	
	private final String INIT_URL = "http://localhost:8080/pro/compania";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// TODO Auto-generated method stub
		try {
			System.out.println("APENAS VOY A ENRAR");

			  //LLena la list de Años
			  solicitarListaCompanias();
				System.out.println("YA VOY YA VOY " + lasCompanias.get(0));

			  Compania compania = lasCompanias.get(0);
			  tipoIdText.setText(compania.getTerceroPropio().getIdentificacion().toString());
			  idText.setText(compania.getTerceroPropio().getId());
			  nombreText.setText(compania.getTerceroPropio().getNombre());
			  direccionText.setText(compania.getTerceroPropio().getDireccion());
			  ciudadText.setText(compania.getTerceroPropio().getCiudad().getNombre());
			  movilText.setText(compania.getTerceroPropio().getTelefono1());
			  fijoText.setText(compania.getTerceroPropio().getTelefono2());
			  repLegalText.setText(compania.getRLegal());
			  revisorFiscalText.setText(compania.getRevFiscal());
			  contadorPublicoText.setText(compania.getContador());
			  requiereCentrosCheck.setSelected(true);
			  
				System.out.println("YA INICIALICÉ VARIABLES");

		}catch (HttpClientErrorException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML protected void  onBotonGuardarClicked(ActionEvent actionEvent) {

		  Compania compania = lasCompanias.get(0);
		  compania.setRLegal(repLegalText.getText());
		  compania.setRevFiscal(revisorFiscalText.getText());
		  compania.setContador(contadorPublicoText.getText());
		  compania.setCCostos(requiereCentrosCheck.selectedProperty().getValue());
		  
		


		
		//Actualizo el servidor
		System.out.println("Modificando Compania " + compania );
		
		restTemplate.put(INIT_URL+"/update", compania);
		
        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Datos de la Compañía");
        
        alert.setContentText("Dos datos se ha actualzado correctamente");
        alert.showAndWait();
        
        
   	}

	
	private void solicitarListaCompanias() {

		lasCompanias.clear();

		ResponseEntity<Compania[]> respuesta = restTemplate.getForEntity(INIT_URL + "/get/all", Compania[].class);
		System.out.println("ENTRE A SOLICITAR LA LISTA");

		Stream<Compania> consummer = Stream.of(respuesta.getBody());

		consummer.forEach(lasCompanias::add);
		
		lasCompanias.stream().forEach(System.out::println);
	}

}
