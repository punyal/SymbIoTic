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

import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.tentacle.Cryptonizer;
import com.punyal.symbiotic.tentacle.Tentacle;
import com.punyal.symbiotic.tentacle.Ticket;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AuthenticationController implements Initializable {
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
        initAuthenticationWindow();
        
    }
    
    /*------------------------------------------------------------------------*/
    /*                 Authentication Window Controllers                      */
    /*------------------------------------------------------------------------*/
    @FXML
    private Button buttonClose, buttonRefresh;
    
    @FXML
    private PasswordField labelPass, labelSecret;
    
    @FXML
    private TextField labelName, labelServer, labelTicket, labelExpire;
    
    
    private void initAuthenticationWindow() {
        
        
    }
    
    @FXML
    private void handleButtonClose(ActionEvent e) throws IOException {
        core.getConfiguration().getAuthenticationStageInfo().getStage().hide();
    }
   
    @FXML
    private void handleButtonRefresh(ActionEvent e) throws IOException {
        System.out.println("Refresh");
        Tentacle tentacle = core.getTentacle();
        if (tentacle != null) tentacle.stopThread();
        tentacle = new Tentacle(core, labelServer.getText(), labelName.getText(), labelPass.getText(), labelSecret.getText());
        tentacle.startThread();
        core.setTentacle(tentacle);
    }
    
    public synchronized void setTicket(byte[] ticket) {
        labelTicket.setText(Cryptonizer.ByteArray2Hex(ticket));
    }
    
    public synchronized void setExpireTime(long expireTime) {
        labelExpire.setText((new Date(expireTime)).toString());
    }
    
}
