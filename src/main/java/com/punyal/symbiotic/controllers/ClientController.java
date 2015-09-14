/*
 * The MIT License
 *
 * Copyright 2015 Your Organisation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.punyal.symbiotic.controllers;

import static com.punyal.symbiotic.constants.ConstantsNet.DEFAULT_COAP_PORT;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.net.lwm2m.LWM2Mengine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ClientController implements Initializable {
    private Core core;
    
    public void setCore(Core core) {
        this.core = core;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    /**
     * Correct initialization
     */
    public void init() {
        initClientWindow();
        
    }
    
    /*------------------------------------------------------------------------*/
    /*                        Client Window Controllers                         */
    /*------------------------------------------------------------------------*/
    @FXML
    private TabPane tabPaneClient;
    @FXML
    private Tab tabLWM2M;
    @FXML
    private Tab tabClient;
        
    @FXML
    private TextField textLWM2MIP;
    @FXML
    private TextField textLWM2MPort;
    @FXML
    private ProgressIndicator progressLWM2M;
    @FXML
    private Button buttonUpdateLWM2M;
    @FXML
    private Circle circleLWM2M;
    
    @FXML
    private TextField textClientIP;
    @FXML
    private TextField textClientPort;
    @FXML
    private Button buttonUpdateClient;
    
    @FXML
    private TreeView treeViewClients;
    
    @FXML
    private Pane paneBotton;
    @FXML
    private Label labelSelectedClient;
    
    private LWM2Mengine lwm2mEngine;
    
    private void initClientWindow() {
        tabPaneClient.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                    if (t1.getId().equals(tabClient.getId())) {
                        treeViewClients.setDisable(true);
                    }
                    else {
                        treeViewClients.setDisable(false);
                    }
                }
            }
        );
        
        // Loading values...
        textLWM2MIP.setText(core.getStatus().getLightWeightM2M().getAddress());
        textLWM2MPort.setText(""+core.getStatus().getLightWeightM2M().getPort());
        textClientIP.setText(core.getStatus().getSelectedThing().getAddress());
        textClientPort.setText(""+core.getStatus().getSelectedThing().getPort());
        LWM2Mdisconnected();
    }
    
    @FXML
    private void handleButtonUpdateLWM2M(ActionEvent e) throws IOException {
        if(textLWM2MIP.getText().isEmpty())
            textLWM2MIP.setText("localhost");
        core.getStatus().getLightWeightM2M().setAddress(textLWM2MIP.getText());
        if(textLWM2MPort.getText().isEmpty() || textLWM2MPort.getText().equals("0"))
            textLWM2MPort.setText(String.valueOf(DEFAULT_COAP_PORT));
        core.getStatus().getLightWeightM2M().setPort(Integer.parseInt(textLWM2MPort.getText()));
        core.getSettings().save();
        
        if (lwm2mEngine != null)
            lwm2mEngine.stopEngine();
        lwm2mEngine = new LWM2Mengine(core);
        lwm2mEngine.startEngine();
    }
    
    @FXML
    private void handleButtonUpdateClient(ActionEvent e) throws IOException {
        if(textClientIP.getText().isEmpty())
            textClientIP.setText("localhost");
        core.getStatus().getSelectedThing().setAddress(textClientIP.getText());
        if(textClientPort.getText().isEmpty() || textClientPort.getText().equals("0"))
            textClientPort.setText(String.valueOf(DEFAULT_COAP_PORT));
        core.getStatus().getSelectedThing().setPort(Integer.parseInt(textClientPort.getText()));
        
        core.getSettings().save();
    }
    
    public void LWM2Mconnecting() {
        progressLWM2M.setVisible(true);
        circleLWM2M.setVisible(false);
    }
    
    public void LWM2Mdisconnected() {
        progressLWM2M.setVisible(false);
        circleLWM2M.setVisible(true);
        circleLWM2M.setStyle("-fx-fill:red;");
    }
    
    public void LWM2Mconnected() {
        progressLWM2M.setVisible(false);
        circleLWM2M.setVisible(true);
        circleLWM2M.setStyle("-fx-fill:green;");
    }
}
