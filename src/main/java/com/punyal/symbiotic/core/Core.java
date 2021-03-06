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
package com.punyal.symbiotic.core;

import com.punyal.symbiotic.controllers.AuthenticationController;
import com.punyal.symbiotic.controllers.ClientController;
import com.punyal.symbiotic.controllers.SymbIoTicGUIController;
import com.punyal.symbiotic.tentacle.Tentacle;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Core {
    private final Configuration configuration;
    private final Status status;
    private final SymbIoTicGUIController controller;
    private final ClientController clientController;
    private final AuthenticationController authenticationController;
    private final Settings settings;
    private Tentacle tentacle;
    
    public Core(SymbIoTicGUIController controller, ClientController clientController, AuthenticationController authenticationController) {
        settings = new Settings(this);
        configuration = new Configuration();
        status = new Status(this);
        this.controller = controller;
        this.clientController = clientController;
        this.authenticationController = authenticationController;
        settings.load(); // Load all previous saved data
    }
    
    public Tentacle getTentacle() {
        return tentacle;
    }
    
    public void setTentacle(Tentacle tentacle) {
        this.tentacle = tentacle;
    }
    
    
    public Settings getSettings() {
        return settings;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public SymbIoTicGUIController getController() {
        return controller;
    }
    
    public ClientController getClientController() {
        return clientController;
    }
    
    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }
}
