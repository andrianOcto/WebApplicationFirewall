
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Octo
 */
public class RuleReader {

    private String file;
    private BufferedReader br;
    private ArrayList<String> listRule;
    private int logIndex;
    public RuleReader() {
        file        = "rule.txt";
        listRule    = new ArrayList<>();
        listRule.add("ARGS");
        listRule.add("ARGS_GET");
        listRule.add("ARGS_GET_NAME");
        listRule.add("ARGS_POST");
        listRule.add("ARGS_POST_NAME");
        listRule.add("REQUEST_COOKIES");
        listRule.add("REQUEST_COOKIES_NAME");
        listRule.add("REQUEST_METHOD");
        listRule.add("REQUEST_PROTOCOL");
        listRule.add("QUERY_STRING");
        listRule.add("REQUEST_URI");
        listRule.add("REQUEST_HEADER");
        listRule.add("PATH_INFO");
        listRule.add("RESPONSE_STATUS");
        listRule.add("RESPONSE_HEADER");
        listRule.add("RESPONSE_PROTOCOL");
        listRule.add("RESPONSE_CONTENT_TYPE");
        logIndex=0;
    }
    
    public ArrayList<Rule> getRule(){
        ArrayList<Rule> retVal = new ArrayList<>();
        
        String path = Example.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replace("testAgent.jar", file);
        System.out.println(path);
        
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                //System.out.println(line);
                String[] ruleString = line.split("#");
                
                //cek variable rule
                if(ruleString[0] != null)
                {
                    ruleString[0] = ruleString[0].trim();
                    
                    //cek configuration
                    if(ruleString[0].equals("SecAuditEngine"))
                    {
                        if(ruleString[1] != null)
                        {
                            String bool = ruleString[1].toLowerCase();
                            if(bool.equals("on"))
                                Configuration.logEnable = true;
                            else if(bool.equals("off"))
                                Configuration.logEnable = false;
                        }
                    }
                    if(ruleString[0].equals("SecWhiteList"))
                    {
                        System.out.println("SecThi");
                        if(ruleString[1]!=null)
                            Configuration.whiteList.add(ruleString[1].trim());
                    }
                    if(ruleString[0].equals("SecWhiteListParam"))
                    {
                        
                        if(ruleString[1]!=null)
                            Configuration.whiteListParam.add(ruleString[1].trim());
                    }
                    //cek Rule List
                    if(ruleString[0].equals("SecRule"))
                    {
                        ArrayList<Rule> ruleList=new ArrayList<>();
                        
                        if(ruleString[1] != null)
                        {
                            System.out.println(ruleString[1].trim());
                            String variabel = ruleString[1].trim();
                            String[] variabelString = variabel.split("&");
                            
                            for (String variabelString1 : variabelString) {
                                System.out.println(variabelString1);
                                Rule rule = new Rule();
                                rule.generateVariable(variabelString1);
                                ruleList.add(rule);
                            }
                            
                            
                        }
                        if(ruleString[2] != null)
                        {
                            String operator = ruleString[2].trim();
                            
                            
                            for (Rule ruleList1 : ruleList)
                                ruleList1.generateOperator(operator);
                        }
                        if(ruleString[3] != null)
                        {
                            logIndex = 4;
                            String nilai = ruleString[3];

                            for(int i = 4;i<(ruleString.length-1);i++){
                                nilai = nilai +"#"+ruleString[i];
                                logIndex++;
                            }
                            nilai = nilai.trim();
                            
                            for (Rule ruleList1 : ruleList)
                                ruleList1.generateNilai(nilai);
                        }
                        if(ruleString[logIndex] != null)
                        {
                            String log = ruleString[logIndex].trim();
                            System.out.println("masuk hahaha "+log);
                            
                            for (Rule ruleList1 : ruleList)
                                ruleList1.generateLog(log);
                            
                        }
                        for (Rule ruleList1 : ruleList) {
                            retVal.add(ruleList1);
                        }

                    }
                    
                }

                line = br.readLine();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return retVal;
    }

    public BufferedReader getBr() {
        return br;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }

    public ArrayList<String> getListRule() {
        return listRule;
    }

    public void setListRule(ArrayList<String> listRule) {
        this.listRule = listRule;
    }
    
    public RuleReader(String file) {
        this.file = file;
    }
    
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
    
}
