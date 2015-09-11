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
package com.punyal.symbiotic.core.security;

import javafx.application.Platform;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AppCheck {
    
    /**
     * Checks if the jar name has been modified and return app values
     * @param path to the file
     * @return AppParameters
     */
    static public AppParameters checkApp(String path) {
        System.out.println(path);
        path = path.substring(path.lastIndexOf("/")+1);
        System.out.println(path);
        String[] s = path.split("-");
        if ((s.length != 4) || (s[0].isEmpty() || s[1].isEmpty() || s[2].isEmpty() || s[3].length() < 4)) {
            System.err.println("JAR MODIFIED!!");
            Platform.exit();
        }
        return new AppParameters(s[0], s[1], s[2], Integer.parseInt(s[3].substring(1, s[3].lastIndexOf("."))));
    }
    
}
