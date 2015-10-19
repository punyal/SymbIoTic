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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class FileSystem {
    private final TableView table;
    private ObservableList<FileEntry> data;
    
    public FileSystem(TableView table) {
        this.table = table;
        TableColumn colName, colSize, colStatus;
        colName = new TableColumn<>("Name");
        colSize = new TableColumn<>("Size");
        colStatus = new TableColumn<>("Status");
        
        colName.prefWidthProperty().bind(table.widthProperty().divide(2));
        colSize.prefWidthProperty().bind(table.widthProperty().divide(4));
        colStatus.prefWidthProperty().bind(table.widthProperty().divide(4));
        
        colName.setCellValueFactory(new PropertyValueFactory<FileEntry,String>("name"));
        colSize.setCellValueFactory(new PropertyValueFactory<FileEntry,Integer>("size"));
        colStatus.setCellValueFactory(new PropertyValueFactory<FileEntry,String>("status"));
        
        table.getColumns().addAll(colName, colSize, colStatus);
        
        this.init();
    }
    
    public final void init() {
        data = FXCollections.observableArrayList();
        table.setItems(data);
    }
    
    public void add(String name, Integer Size, String Status) {
        data.add(new FileEntry(name, Size, Status));
    }
    
    
}
