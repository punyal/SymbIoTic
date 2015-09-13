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

import static com.punyal.symbiotic.constants.ConstantsGUI.*;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.feature.ipso.AccThread;
import com.punyal.symbiotic.core.feature.ipso.BatteryThread;
import com.punyal.symbiotic.core.feature.ipso.StrainThread;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.labs.scene.control.gauge.linear.elements.CompleteSegment;
import jfxtras.labs.scene.control.gauge.linear.elements.PercentSegment;

/**
 * FXML Controller class
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class SymbIoTicGUIController implements Initializable {

    private Core core;

    public void setCore(Core core) {
        this.core = core;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    /**
     * Correct initialization
     */
    public void init() {
        initMainWindow();
        initFeatureIPSO();
    }

    /*------------------------------------------------------------------------*/
    /*                        Main Window Controllers                         */
    /*------------------------------------------------------------------------*/
    @FXML
    private Label windowTitle, buildNumber;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuTitle;
    @FXML
    private MenuItem menuAbout, menuPreferences, menuClose;

    /* Client Part */
    @FXML
    private ToggleButton toggleButtonClient;

    @FXML
    private void handleMenuClose(ActionEvent e) throws IOException {
        Platform.exit();
    }

    private Stage clientStage;

    @FXML
    private void handleButtonClient(ActionEvent e) throws IOException {
        if (toggleButtonClient.selectedProperty().getValue()) {
            // Display Client Window
            core.getConfiguration().getClientStageInfo().getStage().show();

        } else {
            // Close Client Window
            core.getConfiguration().getClientStageInfo().getStage().hide();
        }
    }

    private void initMainWindow() {
        windowTitle.setText(core.getConfiguration().getApp().getName()
                + " v" + core.getConfiguration().getApp().getVersion());
        buildNumber.setText("r" + core.getConfiguration().getApp().getBuildNumber());
        menuTitle.setText(core.getConfiguration().getApp().getName());
        menuAbout.setText("About " + core.getConfiguration().getApp().getName());
        menuClose.setText("Close " + core.getConfiguration().getApp().getName());

        //Setting buttons
        toggleButtonClient.setContentDisplay(ContentDisplay.CENTER);
        toggleButtonClient.setText("");

        //Setting Tab
        tabPaneFeatures.setSide(Side.BOTTOM);
    }

    /*------------------------------------------------------------------------*/
    /*                        Features Tabs Controllers                       */
    /*------------------------------------------------------------------------*/
    @FXML
    private TabPane tabPaneFeatures;
    @FXML
    private AnchorPane anchorPaneFeature1, anchorPaneFeature2;

    /*------------------------------------------------------------------------*/
    /*                        Feature IPSO  Controllers                       */
    /*------------------------------------------------------------------------*/
    @FXML // Battery Gauge
    private SimpleMetroArcGauge metroGaugeBattery;
    private BatteryThread batteryThread;
    
    @FXML // Strain Gauge
    private SimpleMetroArcGauge metroGaugeStrain;
    private StrainThread strainThread;
    
    
    @FXML // ACC Chart
    private LineChart lineChartIPSO;
    @FXML
    private NumberAxis accXaxis, accYaxis;

    private XYChart.Series accSeries;
    private ConcurrentLinkedQueue<Number> accData = new ConcurrentLinkedQueue<>();
    private double lineChartIPSOindex;
    
    private AccThread accThread;
    
    private final AnimationTimer animatorIPSO = new AnimationTimer() {
            @Override
            public void handle(long now) {
                animateBattery();
                animateStrain();
                animateACC();
            }
        };

    private void initFeatureIPSO() {
        /* Battery Gauge */
        metroGaugeBattery.setMinValue(0);
        metroGaugeBattery.setMaxValue(100);
        metroGaugeBattery.setValue(0);
        // Battery Gauge Schema...
        metroGaugeBattery.getStyleClass().add("colorscheme-green-to-red-7");
        metroGaugeBattery.segments().add(new CompleteSegment(metroGaugeBattery));
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 65.0, 100.0, "normalSegment"));
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 30.0, 65.0, "test2"));
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 20.0, 30.0, "test"));
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 10.0, 20.0, "warningSegment"));
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 0.0, 10.0, "errorSegment"));
        
        /* Strain Gauge */
        metroGaugeStrain.setMinValue(0);
        metroGaugeStrain.setMaxValue(100);
        metroGaugeStrain.setValue(0);
        // Strain Gauge Schema...
        metroGaugeStrain.getStyleClass().add("colorscheme-green-to-red-7");
        metroGaugeStrain.segments().add(new CompleteSegment(metroGaugeStrain));
        metroGaugeStrain.segments().add(new PercentSegment(metroGaugeStrain, 0, 30, "strainSegment1"));
        metroGaugeStrain.segments().add(new PercentSegment(metroGaugeStrain, 30, 70, "strainSegment2"));
        metroGaugeStrain.segments().add(new PercentSegment(metroGaugeStrain, 70, 80, "strainSegment3"));
        metroGaugeStrain.segments().add(new PercentSegment(metroGaugeStrain, 80, 90, "strainSegment4"));
        metroGaugeStrain.segments().add(new PercentSegment(metroGaugeStrain, 90, 100, "strainSegment5"));
        
        /* ACC Chart */
        accXaxis.setLowerBound(0);
        accXaxis.setUpperBound(MAX_DATA_POINTS);
        accXaxis.setTickUnit(MAX_DATA_POINTS/10);
        accXaxis.setForceZeroInRange(false);
        accXaxis.setAutoRanging(false);
        accXaxis.setTickLabelsVisible(false);
        accXaxis.setTickMarkVisible(false);
        accXaxis.setMinorTickVisible(false);
        
        accYaxis.setAutoRanging(false);
        accYaxis.setLowerBound(CHART_MIN_VALUE);
        accYaxis.setUpperBound(CHART_MAX_VALUE);
        accYaxis.setTickUnit(1000);
        accYaxis.setTickLabelsVisible(true);
        
        lineChartIPSO.setAnimated(false);
        lineChartIPSO.setCreateSymbols(false);
        lineChartIPSO.setLegendVisible(false);
        accSeries = new XYChart.Series<>();
        lineChartIPSO.getData().addAll(accSeries);
        lineChartIPSOindex = 0;
               
        /* ------------- Init IPSO Threads -------------- */
        batteryThread = new BatteryThread(core);
        batteryThread.start();
        strainThread = new StrainThread((core));
        strainThread.start();
        accThread = new AccThread(core);
        accThread.start();
        
        /* ------------- Start animations -------------- */
        animatorIPSO.start();
    }
    
    
    private void animateBattery() {
        core.getController().getBatteryGauge().setValue(core.getStatus().getBatteryLevel());
    }
    
    public SimpleMetroArcGauge getBatteryGauge() {
        return metroGaugeBattery;
    }
    
    
    private void animateStrain() {
        core.getController().getStrainGauge().setValue(core.getStatus().getStrainLevel());
    }
    
    public SimpleMetroArcGauge getStrainGauge() {
        return metroGaugeStrain;
    }
    
    
    private void animateACC() {
        for (int i = 0; i<10; i++) { // Add 10 points per refresh
            if (accData.isEmpty()) break;
            accSeries.getData().add(new AreaChart.Data(lineChartIPSOindex++, accData.remove()));
        }
        // remove old points
        if (accSeries.getData().size() > MAX_DATA_POINTS)
            accSeries.getData().remove(0, accSeries.getData().size() - MAX_DATA_POINTS);
        
        // update range
        accXaxis.setLowerBound(lineChartIPSOindex-MAX_DATA_POINTS);
        accXaxis.setUpperBound(lineChartIPSOindex-1);
    }
    
    public ConcurrentLinkedQueue<Number> getAccData() {
        return accData;
    }
    
    public double getLineChartIPSOindex() {
        return lineChartIPSOindex;
    }
}
