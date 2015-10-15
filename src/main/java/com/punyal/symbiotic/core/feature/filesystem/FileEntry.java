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
package com.punyal.symbiotic.core.feature.filesystem;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class FileEntry {
    public SimpleStringProperty fileName = new SimpleStringProperty("<FileName>");
    public SimpleIntegerProperty fileSize = new SimpleIntegerProperty(0);
    public SimpleIntegerProperty fileStatus = new SimpleIntegerProperty(0);
    
    public FileEntry() {
        this("<FileName>", 0, 0);
    }
    
    public FileEntry(String fileName, Integer fileSize, Integer fileStatus) {
        this.fileName.set(fileName);
        this.fileSize.set(fileSize);
        this.fileStatus.set(fileStatus);
    }
    
    public String getName() {
        return fileName.get();
    }
    
    public Integer getSize() {
        return fileSize.get();
    }
    
    public Integer getStatus() {
        return fileStatus.get();
    }
}
