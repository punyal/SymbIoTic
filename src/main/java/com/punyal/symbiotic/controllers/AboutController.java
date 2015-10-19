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

import com.punyal.symbiotic.constants.ConstantsGUI;
import static com.punyal.symbiotic.constants.ConstantsGUI.*;
import com.punyal.symbiotic.core.Core;
import com.punyal.symbiotic.core.feature.wheelloader.Xform;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * FXML Controller class
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AboutController implements Initializable {
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
        initAboutWindow();
        
    }
    
    /*------------------------------------------------------------------------*/
    /*                       About Window Controllers                         */
    /*------------------------------------------------------------------------*/
    @FXML
    private Button buttonClose;
    @FXML
    private Pane paneAbout;
    @FXML
    private Label labelTitle, labelVersion, labelType, labelRelease;
    @FXML
    private TextArea textAbout;
    
    private SubScene subSceneAbout;
    
    
    final Group root = new Group();
    final Xform aboutGroup = new Xform();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 0.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 0.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    
    private static final double HYDROGEN_ANGLE = 104.5;
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    
    private void initAboutWindow() {
        labelTitle.setText(core.getConfiguration().getApp().getName());
        labelVersion.setText("Version: "+core.getConfiguration().getApp().getVersion());
        labelType.setText(core.getConfiguration().getApp().getType());
        labelRelease.setText("Release: "+core.getConfiguration().getApp().getBuildNumber());
        textAbout.setText(SYMBIOTIC_ABOUT);
        
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        root.setTranslateX(ABOUT_WIDTH/2);
        root.setTranslateY(ABOUT_HEIGHT/2);
        
        subSceneAbout = new SubScene(root, ABOUT_WIDTH, ABOUT_HEIGHT, true, SceneAntialiasing.BALANCED);
        paneAbout.getChildren().add(subSceneAbout);
        subSceneAbout.setRoot(root);
        subSceneAbout.setFill(Color.TRANSPARENT);
        buildCamera();
        buildAbout();
        
        animatorAbout.start();
        
    }
    
    private final AnimationTimer animatorAbout = new AnimationTimer() {
        long angle = 0;
        long positionX = 0, positionY = 0;
        boolean directionX = true, directionY = true;
        @Override
        public void handle(long now) {
            aboutGroup.setRotateY(angle++);
            aboutGroup.setRotateX(angle/2);
            aboutGroup.setRotateZ(angle/4);
            
            if (directionX) positionX++;
            else positionX--;
            if (positionX >= 450) directionX = false;
            if (positionX <= -450) directionX = true;
            aboutGroup.setTranslateX(positionX);
            
            if (directionY) positionY++;
            else positionY--;
            if (positionY >= 180) directionY = false;
            if (positionY <= -180) directionY = true;
            aboutGroup.setTranslateY(positionY);
            
        }
        
    };
    
    private void buildCamera() {
        System.out.println("buildCamera()");
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        //cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        //camera.setTranslateX(100);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);

    }



    
    
    private void buildAbout() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);
        
        final PhongMaterial punyalMaterial = new PhongMaterial();
        //punyalMaterial.setDiffuseColor(Color.WHITE);
        //punyalMaterial.setSpecularColor(Color.LIGHTGREEN);
        Image bumpMap = new Image("images/skin.jpg");
        punyalMaterial.setBumpMap(bumpMap);
        punyalMaterial.setDiffuseMap(bumpMap);
        punyalMaterial.setSpecularMap(bumpMap);
        
        Xform aboutXform = new Xform();
        
        Box aboutBox = new Box(200, 200, 200);
        aboutBox.setMaterial(punyalMaterial);
        aboutXform.getChildren().add(aboutBox);
        
        AmbientLight al = new AmbientLight(Color.WHITE);
        aboutXform.getChildren().add(al);
        
        //Sphere aboutSphere = new Sphere(200);
        //aboutSphere.setMaterial(punyalMaterial);
        //aboutXform.getChildren().add(aboutSphere);

        aboutGroup.getChildren().add(aboutXform);

        world.getChildren().addAll(aboutGroup);
    }
    
    
    @FXML
    private void handleButtonClose(ActionEvent e) throws IOException {
        core.getConfiguration().getAboutStageInfo().getStage().hide();
    }
   
    
}
