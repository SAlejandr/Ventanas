package com.example.demo.Controllers;

import com.example.demo.model.pojos.Anno;
import com.example.demo.model.pojos.Mes;
import com.example.demo.model.pojos.MesFiscalId;
import com.google.common.collect.Maps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;


public class BalanceGeneralFXMLController implements Initializable{

	public AnchorPane reporteAnchor;
	@FXML private Button closeButton;

	@FXML private ComboBox<String> annosCombo;
	@FXML private ComboBox<String> mesesCombo;


	@FXML private Button generarReporteBoton;
	
	private static ObservableList<String> losAnnos = FXCollections.observableArrayList();
	
	private static ObservableList<String> losMeses = FXCollections.observableArrayList();
	
	private Map<String, Anno> mapAnnos = Maps.newHashMap();
	
	private Map<String, Mes> mapMeses = Maps.newHashMap();

	
	private final String INIT_URL = "http://localhost:8080/pro";
	
	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			  //LLena la list de Meses
			  solicitarListaAnnos();

				annosCombo.setItems(losAnnos);

		}catch (HttpClientErrorException e) {
			e.printStackTrace();
		}
	}

	@FXML protected void  onMesesComboChanged() {
		 generarReporteBoton.setDisable(false);
	}
	
		
	@FXML protected void  onAnnosComboChanged(ActionEvent actionEvent) {
		 mesesCombo.setDisable(false);
			int anno = Integer.parseInt(annosCombo.getValue());
			solicitarMesesAnno(anno);
	}

	
	@FXML protected void  onGenerarReporteBotonClicked(ActionEvent actionEvent) {



		MesFiscalId mesSeleccionado = mapMeses.get(mesesCombo.getValue()).getIdFiscal();
		int intMes = mesSeleccionado.getMes();
		int intAnno = mesSeleccionado.getAnno().getElAnno();
		
		//Necesito buscar el mes que se va a activar
		
		System.out.println(mesSeleccionado);

		try{
			ResponseEntity<byte[]> respuesta = restTemplate.postForEntity(INIT_URL + "/reportes/balanceGeneral/imprimir", mesSeleccionado, byte[].class);

			byte[] elPDF = respuesta.getBody();

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Guardar reporte");
			FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF", "*.pdf");

			fileChooser.getExtensionFilters().add(extensionFilter);
			File file = fileChooser.showSaveDialog(reporteAnchor.getScene().getWindow());

			FileOutputStream fos =new FileOutputStream(file);
			fos.write(elPDF);
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpClientErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Generar reporte : Balance General");
        
        alert.setContentText("Reporte generado exitosamente");
        alert.showAndWait();

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
	
	private void solicitarMesesAnno(int anno) { 
		losMeses.clear();
		
		
		Map<String, Integer> params = Maps.newHashMap();
		params.put("anno", anno);
		ResponseEntity<Mes[]> respuesta = restTemplate.getForEntity(INIT_URL + "/mes/getAllDeAnno?anno={anno}", Mes[].class, params);
		Stream<Mes> consummer = Stream.of(respuesta.getBody());
		consummer.forEach(mes -> {
			mapMeses.put(mes.getNombre(), mes);
			losMeses.add(mes.getNombre());
		});
		mesesCombo.setItems(losMeses);
	}

}
