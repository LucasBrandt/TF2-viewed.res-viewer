/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapsviewer;

/**
 *
 * @author Lucas
 */
public class Prefix {
    private String str;
    private int num;
    
    public Prefix(String str, int num) {
        this.str = str;
        this.num = num;
    }
    
    @Override
    public String toString() {
        return str + " (" + num + ")";
    }
    
    public void addOne() {
        this.num += 1;
    }
    
    public String getStr() {
        return str;
    }
    
    public int getNum() {
        return num;
    }
    
}
