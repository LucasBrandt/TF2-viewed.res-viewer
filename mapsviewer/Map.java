/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapsviewer;

/**
 *
 * @author Lucas
 * A single map object read from viewed.res. Contains name, prefix, times loaded
 */
public class Map {
    private String name;
    private String prefix;
    private int loaded;
    
    public Map(String mapLine, String numLine) {
        //mapLine:
        //	"ctf_turbine"
        
        //numLine:
	//	"viewed"		"72"
        
        String trimmed = mapLine.trim();
        name = trimmed.replace("\"", "");
        prefix = name.substring(0, name.indexOf('_'));
        
        String splitArray[] = numLine.split("\"");
        this.loaded = Integer.parseInt(splitArray[3]);
    }
    
    @Override
    public String toString() {
        return name + " (" + loaded + ")";
    }
    
    public String getName() {
        return name;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public int getLoaded() {
        return loaded;
    }
}
