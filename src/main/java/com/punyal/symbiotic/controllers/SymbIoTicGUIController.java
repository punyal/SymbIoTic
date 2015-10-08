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
import com.punyal.symbiotic.core.feature.ipso.Strain;
import com.punyal.symbiotic.core.feature.wheelloader.Xform;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SnapshotParameters;
import javafx.scene.SubScene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
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
        initFeatureWheelLoader();
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
    private MenuItem menuAbout, menuClose, menuExportPNG;

    /* Client Part */
    @FXML
    private ToggleButton toggleButtonClient;

    @FXML
    private void handleMenuClose(ActionEvent e) throws IOException {
        Platform.exit();
        System.exit(0);
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
        menuBar.useSystemMenuBarProperty().set(true);
        menuBar.setVisible(false);
        
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
    @FXML
    Label labelStrain;
    private Strain strain;
    
    
    @FXML // ACC Chart
    private LineChart lineChartIPSO;
    @FXML
    private NumberAxis accXaxis, accYaxis;

    private XYChart.Series accSeries;
    private ConcurrentLinkedQueue<Number> accData = new ConcurrentLinkedQueue<>();
    private double lineChartIPSOindex;
    
    private AccThread accThread;
    
    
    @FXML // Controls
    private ToggleButton toggleButtonIPSO, toggleButtonRecordData;
    @FXML
    private Button buttonSave2PNG;
    
    
    private final AnimationTimer animatorIPSO = new AnimationTimer() {
        private double angle = 0;
            @Override
            public void handle(long now) {
                animateBattery();
                animateStrain();
                if (animationActive) animateACC();
                
                // wheel-loader
                
                moleculeGroup.setRotateY(angle);
                angle += 1;
                //System.out.println(angle);
            }
        };
    
    private boolean animationActive;
    
     
    public boolean isAnimated () {
        return animationActive;
    }
    
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
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 10.0, 30.0, "test"));
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 5.0, 10.0, "warningSegment"));
        metroGaugeBattery.segments().add(new PercentSegment(metroGaugeBattery, 0.0, 5.0, "errorSegment"));
        
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
        accYaxis.setTickUnit(0.5);
        accYaxis.setTickLabelsVisible(true);
        
        lineChartIPSO.setAnimated(false);
        lineChartIPSO.setCreateSymbols(false);
        lineChartIPSO.setLegendVisible(false);
        accSeries = new XYChart.Series<>();
        lineChartIPSO.getData().addAll(accSeries);
        lineChartIPSOindex = 0;
        
        toggleButtonIPSO.setText("START");
        
        /* Controls */
               
        /* ------------- Init IPSO Clients -------------- */
        strain = new Strain(core);
        //batteryThread = new BatteryThread(core);
        //batteryThread.start();
        //strainThread = new StrainThread((core));
        //strainThread.start();
        
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
        for (int i = 0; i<30; i++) { // Add 30 points per refresh
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

    
    
    @FXML
    private void handleButtonIPSO(ActionEvent e) throws IOException {
        if (toggleButtonIPSO.selectedProperty().getValue()) {
            toggleButtonIPSO.setText("STOP");
            accThread = new AccThread(core);
            accThread.startThread();
            batteryThread = new BatteryThread(core);
            batteryThread.startThread();
            strain.startObserve();
            //animatorIPSO.start();
            animationActive = true;
            
        } else {
            toggleButtonIPSO.setText("START");
            accThread.stopThread();
            batteryThread.stopThread();
            strain.stopObserver();
            //animatorIPSO.stop();
            animationActive = false;
        }
        // reset gauges
        core.getStatus().setBatteryLevel(0);
        core.getStatus().setStrainLevel(0, false);
    }
    
    @FXML
    private void handleButtonExportPNG(ActionEvent e) throws IOException {
        WritableImage image = lineChartIPSO.snapshot(new SnapshotParameters(), null);
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PNG");
        File file = fileChooser.showSaveDialog(core.getConfiguration().getMainStageInfo().getStage());
        System.out.println(file);
        if (file != null) 
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }
    
    
    
    
    @FXML
    private void handleButtonRecordData(ActionEvent e) throws IOException {
        if (toggleButtonRecordData.selectedProperty().getValue()) {
            core.getStatus().getExportData().startNew();
            toggleButtonRecordData.setText(IPSO_RECORDING);
        } else {
            toggleButtonRecordData.setText(IPSO_RECORD_DATA);
            core.getStatus().getExportData().stop();
        }
    }
    
    @FXML
    private void handleButtonSave2PNG(ActionEvent e) throws IOException {
        
    }
    

    /*------------------------------------------------------------------------*/
    /*                   Feature Wheel Loader  Controllers                    */
    /*------------------------------------------------------------------------*/
    
    @FXML
    private AnchorPane anchoPaneTest;
    //private SubScene subSceneWheelLoader;
    
    
    private SubScene subSceneWheelLoader;
    
    final Group root = new Group();
    final Xform world = new Xform();
    // Camera
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    // Axes
    final Xform axisGroup = new Xform();
    // Molecule
    final Xform moleculeGroup = new Xform();
    
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 250.0;
    private static final double HYDROGEN_ANGLE = 104.5;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;
    
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    
    private void initFeatureWheelLoader() {
        System.out.println("Loading wheel...");
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        //buildScene();
        subSceneWheelLoader = new SubScene(root, 600, 600, true, SceneAntialiasing.BALANCED);
        anchoPaneTest.getChildren().add(subSceneWheelLoader);
        //subSceneWheelLoader = new SubScene
        subSceneWheelLoader.setRoot(root);
        subSceneWheelLoader.setFill(Color.GREY);
        buildCamera();
        subSceneWheelLoader.setCamera(camera);
        buildAxes();
        buildMolecule();
        handleKeyboard(subSceneWheelLoader, root);
        handleMouse(subSceneWheelLoader, root);
    }
    
    
    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }
    
    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
 
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
 
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
 
        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);
        
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
 
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        world.getChildren().addAll(axisGroup);
    }
    
    

    private void handleMouse(SubScene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX); 
                mouseDeltaY = (mousePosY - mouseOldY); 
                
                double modifier = 1.0;
                
                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                } 
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }     
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                }
                else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  
                }
            }
        });
    }
    
    private void handleKeyboard(SubScene scene, final Node root) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                    case V:
                        moleculeGroup.setVisible(!moleculeGroup.isVisible());
                        break;
                }
            }
        });
    }
    
    private void buildMolecule() {
 
       final PhongMaterial redMaterial = new PhongMaterial();
       redMaterial.setDiffuseColor(Color.DARKRED);
       redMaterial.setSpecularColor(Color.RED);
 
       final PhongMaterial whiteMaterial = new PhongMaterial();
       whiteMaterial.setDiffuseColor(Color.WHITE);
       whiteMaterial.setSpecularColor(Color.LIGHTBLUE);
 
       final PhongMaterial greyMaterial = new PhongMaterial();
       greyMaterial.setDiffuseColor(Color.DARKGREY);
       greyMaterial.setSpecularColor(Color.GREY);
 
       // Molecule Hierarchy
       // [*] moleculeXform
       //     [*] oxygenXform
       //         [*] oxygenSphere
       //     [*] hydrogen1SideXform
       //         [*] hydrogen1Xform
       //             [*] hydrogen1Sphere
       //         [*] bond1Cylinder
       //     [*] hydrogen2SideXform
       //         [*] hydrogen2Xform
       //             [*] hydrogen2Sphere
       //         [*] bond2Cylinder
 
       Xform moleculeXform = new Xform();
       Xform oxygenXform = new Xform();
       Xform hydrogen1SideXform = new Xform();
       Xform hydrogen1Xform = new Xform();
       Xform hydrogen2SideXform = new Xform();
       Xform hydrogen2Xform = new Xform();

      Sphere oxygenSphere = new Sphere(40.0);
      oxygenSphere.setMaterial(redMaterial);

      Sphere hydrogen1Sphere = new Sphere(30.0);
      hydrogen1Sphere.setMaterial(whiteMaterial);
      hydrogen1Sphere.setTranslateX(0.0);

      Sphere hydrogen2Sphere = new Sphere(30.0);
      hydrogen2Sphere.setMaterial(whiteMaterial);
      hydrogen2Sphere.setTranslateZ(0.0);

      Cylinder bond1Cylinder = new Cylinder(5, 100);
      bond1Cylinder.setMaterial(greyMaterial);
      bond1Cylinder.setTranslateX(50.0);
      bond1Cylinder.setRotationAxis(Rotate.Z_AXIS);
      bond1Cylinder.setRotate(90.0);

      Cylinder bond2Cylinder = new Cylinder(5, 100);
      bond2Cylinder.setMaterial(greyMaterial);
      bond2Cylinder.setTranslateX(50.0);
      bond2Cylinder.setRotationAxis(Rotate.Z_AXIS);
      bond2Cylinder.setRotate(90.0);

      moleculeXform.getChildren().add(oxygenXform);
      moleculeXform.getChildren().add(hydrogen1SideXform);
      moleculeXform.getChildren().add(hydrogen2SideXform);
      oxygenXform.getChildren().add(oxygenSphere);
      hydrogen1SideXform.getChildren().add(hydrogen1Xform);
      hydrogen2SideXform.getChildren().add(hydrogen2Xform);
      hydrogen1Xform.getChildren().add(hydrogen1Sphere);
      hydrogen2Xform.getChildren().add(hydrogen2Sphere);
      hydrogen1SideXform.getChildren().add(bond1Cylinder);
      hydrogen2SideXform.getChildren().add(bond2Cylinder);
 
      hydrogen1Xform.setTx(100.0);
      hydrogen2Xform.setTx(100.0);
      hydrogen2SideXform.setRotateY(HYDROGEN_ANGLE);

      moleculeGroup.getChildren().add(moleculeXform);

      world.getChildren().addAll(moleculeGroup);
}
}
