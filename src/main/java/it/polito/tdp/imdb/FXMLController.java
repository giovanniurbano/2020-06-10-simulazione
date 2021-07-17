/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
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

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	if(this.model.getGrafo() == null) {
    		this.txtResult.setText("Creare grafo!");
    		return;
    	}
    	Actor a = this.boxAttore.getValue();
    	if(a == null) {
    		this.txtResult.setText("Scegliere un attore!");
    		return;
    	}
    	List<Actor> simili = this.model.getAttoriSimili(a);
    	this.txtResult.setText("Attori simili a: " + a);
    	for(Actor aa : simili)
    		this.txtResult.appendText("\n" + aa);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.boxAttore.getItems().clear();
    	String genere = this.boxGenere.getValue();
    	if(genere == null) {
    		this.txtResult.setText("Scegliere un genere!");
    		return;
    	}
    	String msg = this.model.creaGrafo(genere);
    	this.txtResult.appendText(msg);
    	this.boxAttore.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	if(this.model.getGrafo() == null) {
    		this.txtResult.setText("Creare grafo!");
    		return;
    	}
    	String ns = this.txtGiorni.getText();
    	try {
    		int n = Integer.parseInt(ns);
    		if(n < 0) {
    			this.txtResult.setText("Inserire un intero > 0!");
        		return;
    		}
    		this.model.simula(n);
    		this.txtResult.setText("Attori intervistati:");
    		for(Actor a : this.model.getIntervistati()) 
    			this.txtResult.appendText("\n" + a);
    		this.txtResult.appendText("\n\nNumero giorni di pausa: " + this.model.getGiorniPausa());
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.setText("Inserire un intero!");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(this.model.listAllGenres());
    }
}
