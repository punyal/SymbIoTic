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
package com.punyal.symbiotic.Utils;

import static com.punyal.symbiotic.constants.ConstantsGUI.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.labs.scene.control.gauge.linear.AbstractLinearGauge;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class UtilsGUI {
        
    static public void configStage(Stage stage, Parent root, String name) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/"+name.toLowerCase()+"gui.css");
        scene.getStylesheets().add(AbstractLinearGauge.segmentColorschemeCSSPath());
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("SymbIoTic");
        stage.resizableProperty().setValue(false);
        stage.setScene(scene);
    }
    
}
