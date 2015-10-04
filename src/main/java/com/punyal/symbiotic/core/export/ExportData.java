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
package com.punyal.symbiotic.core.export;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.DataItem;
import com.punyal.symbiotic.core.Core;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ExportData {
    private Core core;
    private boolean active;
    private File file;
    
    public ExportData(Core core) {
        this.core = core;
    }
    
    public void startNew() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as CBOR");
        fileChooser.setInitialFileName("IPSO_DATA.cbor");
        file = fileChooser.showSaveDialog(core.getConfiguration().getMainStageInfo().getStage());
        active = true;
    }
    
    public void stop() {
        active = false;
    }
    
    public int save2File(byte[] bytes) {
        if (!active) return 0;
        
        
        try {
            try (FileOutputStream output = new FileOutputStream(file, true)) {
               output.write(bytes);
            }
        } catch (IOException ex) {
            Logger.getLogger(ExportData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        // Debugging....
        try {
            System.out.println("Debugging...");
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            List<DataItem> dataItems = new CborDecoder(bais).decode();
            for (DataItem dataItem : dataItems)
                System.out.println(dataItem.toString());
            
            System.out.println("............");
        } catch (Exception ex) {
            System.out.println("Error "+ex);
        }
        // .... remove till here
        
        return bytes.length;
    }
    
    
    
}
