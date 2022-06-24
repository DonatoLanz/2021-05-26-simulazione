/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Business partenza = cmbLocale.getValue();
    	Business arrivo = model.migliore();
    	double soglia = Double.parseDouble(txtX.getText());
    	List<Business> percorso = model.calcolarePercorso(partenza, arrivo, soglia);
    	
    	if(percorso.size() == 0) {
    		txtResult.appendText("Non è possibile trovare un percorso");
    	}else {
     	txtResult.appendText(percorso.toString());
    	   	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	String city = cmbCitta.getValue();
    	int anno = cmbAnno.getValue();
    	String msg = model.creaGrafo(city, anno);
    	txtResult.appendText(msg);
    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
       txtResult.clear();
       Business migliore = model.migliore();
       txtResult.appendText(migliore.getBusinessName());
       cmbLocale.getItems().addAll(model.vertici());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbAnno.getItems().add(2005);
    	cmbAnno.getItems().add(2006);
    	cmbAnno.getItems().add(2007);
    	cmbAnno.getItems().add(2008);
    	cmbAnno.getItems().add(2009);
    	cmbAnno.getItems().add(2010);
    	cmbAnno.getItems().add(2011);
    	cmbAnno.getItems().add(2012);
    	cmbAnno.getItems().add(2013);
    
    	cmbCitta.getItems().addAll(model.getCities());
    }
}
