
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Octo
 */
public final class Configuration {
    public static boolean logEnable;
    public static boolean enableWAF;

    public static ArrayList<String> whiteListParam;
    public static ArrayList<String> whiteList;
    public static ArrayList<String> confList;

    public Configuration() {
        whiteListParam  = new ArrayList<>();
        whiteList       = new ArrayList<>();
        confList        = new ArrayList<>();
        
        enableWAF = true;
    }
    
}
