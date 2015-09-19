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

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Coordinates {
    private double x;
    private double y;
    
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Coordinates() {
        this.x = 0;
        this.y = 0;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getX() {
        return x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getY() {
        return y;
    }
    
    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public static short getInt(byte[] arr, int off) {
        return (short)(arr[2*off]&0xFF | arr[2*off+1]<<8);
    }
}
