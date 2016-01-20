/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Octo
 */
public class Rule {
    private String variable;
    private String method;
    private String nilai;
    private String operator;
    private String action;
    private String errorCode;
    private String message;
    private String pattern;
    private String listRule;
    
    private boolean negasi;
    private boolean block;
    private boolean log;
    
    

    public Rule() {
        message = "";
        negasi  = false; 
        block   = false;
        errorCode = "403";
    }
    
    public void generateVariable(String text)
    {
        if(text.startsWith("ARGS:"))
        {
            
            String[] parseString = text.split(":");
            if(parseString.length > 1)
            {
                variable = parseString[1];
                method   = "all";
            }
        }
        else if(text.equals("ARGS_GET"))
        {
            variable = "all";
            method   = "get";
        }
        else if(text.startsWith("ARGS_GET_NAMES:"))
        {
            String[] parseString = text.split(":");
            if(parseString.length > 1)
            {
                variable = parseString[1];
                method   = "get";
            }
        }
        else if(text.equals("ARGS_POST"))
        {
            variable = "all";
            method   = "post";
        }
        else if(text.equals("REQUEST_COOKIES"))
        {
            variable = "all";
            method   = "cookies";
        }
        else if(text.startsWith("REQUEST_COOKIES_NAMES:"))
        {
            String[] parseString = text.split(":");
            if(parseString.length > 1)
            {
                variable = parseString[1];
                method   = "cookies";
            }
        }
        
        System.out.println("generateVariable");
        System.out.println(variable + "-" + method+"-"+text);
        System.out.println("");
    }
    
    public void generateOperator(String text)
    {
        if(text.startsWith("!"))
        {
            negasi   = true;
            operator = text.substring(1);
            operator = operator.toLowerCase();
        }
        else{
            operator = text.toLowerCase();
        }
        System.out.println("generateOperator");
        System.out.println(operator+"-"+negasi);
        System.out.println("");
    }
    
    public void generateNilai(String text)
    {
        text  = text.trim();
        text  = text.substring(1, text.length()-1);
        nilai = text;
        System.out.println("generateNilai");
        System.out.println(nilai);
    }
    
    public void generateLog(String text)
    {
        text = text.trim();
        String[] logText = text.split(",");
 
        if(logText[0] != null)
        {
            if(logText[0].equals("log"))
                log = true;
            else 
                log = false;
        }
        if(logText[1] != null)
        {
            if(logText[1].equals("block"))
                block = true;
            else if(logText[1].startsWith("block:"))
            {
                block = true;
                String[] blockString = logText[1].split(":");
                if(blockString[1] != null)
                {
                    errorCode = blockString[1];
                }
            }
            else
                block = false;
        }
        if(logText[2] != null)
        {
            message = logText[2];
        }
        System.out.println("generateLog");
        System.out.println(isLog()+"-"+block+errorCode+"-"+message);
    }
    
    public void blockParsing(String rule){
        String[] ruleString = rule.split(":");
        errorCode = ruleString[1];
    }
    
    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNegasi() {
        return negasi;
    }

    public void setNegasi(boolean negasi) {
        this.negasi = negasi;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getListRule() {
        return listRule;
    }

    public void setListRule(String listRule) {
        this.listRule = listRule;
    }
    
    
    
}
