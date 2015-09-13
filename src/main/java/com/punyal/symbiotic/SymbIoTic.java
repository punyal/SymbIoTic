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
package com.punyal.symbiotic;

import com.punyal.symbiotic.Utils.UtilsGUI;
import static com.punyal.symbiotic.constants.ConstantsGUI.*;
import com.punyal.symbiotic.controllers.ClientController;
import com.punyal.symbiotic.controllers.SymbIoTicGUIController;
import com.punyal.symbiotic.core.Core;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class SymbIoTic extends Application {
    // App Core
    private Core core;
    
    @Override
    public void start(final Stage stage) throws Exception {
        
        /* ----------------------- Loading main window ---------------------- */
        //Stage mainStage = new Stage();
        FXMLLoader mainloader = new FXMLLoader(getClass().getResource("/fxml/SymbIoTicGUI.fxml"));
        Parent root = mainloader.load();
        UtilsGUI.configStage(stage, root, "symbiotic");
        stage.show();
        
        SymbIoTicGUIController mainController = mainloader.<SymbIoTicGUIController>getController();
        core = new Core(mainController);
        mainController.setCore(core);
        mainController.init();
        
        core.getConfiguration().setMainStage(stage);
        /*--------------------------------------------------------------------*/
        /* ----------------------- Loading client window -------------------- */
        Stage clientStage = new Stage();
        FXMLLoader clientloader = new FXMLLoader(getClass().getResource("/fxml/ClientGUI.fxml"));
 
        UtilsGUI.configStage(clientStage, (Parent) clientloader.load(), "client");
        clientStage.initOwner(stage);
        clientStage.setX(stage.getX()+CLIENT_X_OFFSET);
        clientStage.setY(stage.getY()+CLIENT_Y_OFFSET);
        
        ClientController clientController = clientloader.<ClientController>getController();
        clientController.setCore(core);
        clientController.init();
        
        core.getConfiguration().setClientStage(clientStage);
        /*--------------------------------------------------------------------*/
        /* Functionality */
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Save actual window position
                core.getConfiguration().getMainStageInfo().coordinates.setXY(event.getSceneX(), event.getSceneY());
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Move Main window
                core.getConfiguration().getMainStageInfo().setStageXY(
                        event.getScreenX() - core.getConfiguration().getMainStageInfo().coordinates.getX(),
                        event.getScreenY() - core.getConfiguration().getMainStageInfo().coordinates.getY());
                // Move Client window
                core.getConfiguration().getClientStageInfo().setStageXY(
                        core.getConfiguration().getMainStageInfo().getStage().getX() + CLIENT_X_OFFSET, 
                        core.getConfiguration().getMainStageInfo().getStage().getY() + CLIENT_Y_OFFSET);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
