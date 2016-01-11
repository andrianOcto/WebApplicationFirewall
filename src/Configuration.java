
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
    public static String logLocation;
    public static String logLocation2;
    public static ArrayList<String> blackList;
    public static ArrayList<String> whiteList;
    public static ArrayList<String> confList;

    public Configuration() {
        blackList = new ArrayList<>();
        whiteList = new ArrayList<>();
        confList  = new ArrayList<>();
        
        //inisialisasi daftar konfigurasi
        confList.add("SecAuditEngine");
        confList.add("SecAuditLog");
        confList.add("SecAuditLog2");
        confList.add("SecBlackList");
        confList.add("SecWhiteList");
    }
    
}
