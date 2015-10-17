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
package com.punyal.symbiotic.core.net;

import static com.punyal.symbiotic.constants.ConstantsJSON.*;
import com.punyal.symbiotic.core.Core;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ThingsList {
    private final Core core;
    private final List<Thing> list;
    
    public ThingsList(Core core) {
        this.core = core;
        list = new ArrayList<>();
    }
    
    
    private Thing findThingByID(String registrationId) {
        for (Thing thing : list) {
            if (thing.getID().equals(registrationId))
                return thing;
        }
        return null;
    }
    
    public int size() {
        return list.size();
    }
    
    private void add(Thing thing) {
        list.add(thing);
        TreeItem<String> node, level1, level2;
        node = new TreeItem<>(thing.getEndPoint());
        node.setExpanded(false);
        
        //level1 = new TreeItem<>("ID: "+thing.getID());
        //node.getChildren().add(level1);
        
        
        level1 = new TreeItem<>("IP: "+thing.getAddress());
        node.getChildren().add(level1);
        
        level1 = new TreeItem<>("Port: "+thing.getPort());
        node.getChildren().add(level1);
        
        level1 = new TreeItem<>("Resources");
        level1.setExpanded(false);
        
        for (String link : thing.getObjectLinks()) {
            if(!link.equals("</>;rt=\"oma.lwm2m\"")) {
                level2 = new TreeItem<>(link);
                level1.getChildren().add(level2);
            }
        }
        
        node.getChildren().add(level1);
        core.getClientController().add2Tree(node);
        
        // Request directly the information to the Thing.
        //ThingThread thingThread = new ThingThread(core, thing);
        //thingThread.startThread();
        
    }
    
    private void remove(Thing thing) {
        System.out.println("removing...");
        // remove from tree
        core.getClientController().removeFromTree(thing.getID());
        list.remove(thing);
    }
    
    public void checkNewListJSON(ArrayList<JSONObject> listJSON) {
        for (JSONObject json: listJSON) {
            try {
                Thing newThing = new Thing(json);
                Thing temp = findThingByID(newThing.getID());
                if (temp == null) {// New thing detected
                    System.out.println("New thing detected: "+newThing.getID());
                    add(newThing);
                }
                else {
                    if (!temp.getObjectLinks().toString().equals(newThing.getObjectLinks().toString())) {
                        // New resources
                        System.out.println("Updating resources for "+temp.getID());
                        temp.setObjectLins(newThing.getObjectLinks());
                    } else {
                        System.out.println(newThing.getID()+" is already on the database");
                    }
                }
            } catch (UnknownHostException ex) {
                System.out.println("Error parsing JSON: "+ex);
            }   
        }
        
        // Check if something has been removed from the server
        
        boolean remove;
        ArrayList<String> toRemove = new ArrayList<>();
        for (Thing thing: list) {
            remove = true;
            for (JSONObject json: listJSON) {
                if (json.get(JSON_REGISTRATIONID).equals(thing.getID()))
                    remove = false;
            }
            if (remove)
                toRemove.add(thing.getID());
        }
        for (String id2remove: toRemove) {
            remove(findThingByID(id2remove));
        }
    }
    
    
    public void clearList() {
        list.clear();
    }
}
