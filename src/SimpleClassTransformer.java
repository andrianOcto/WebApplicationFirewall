/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Octo
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.SignatureAttribute.ClassSignature;
import javassist.bytecode.SignatureAttribute.TypeParameter;
import javassist.bytecode.annotation.Annotation;

public class SimpleClassTransformer implements ClassFileTransformer{
private ArrayList<Rule> listRule;
private static Logger myLogger = Logger.getLogger("Class");

    public SimpleClassTransformer(ArrayList<Rule> listRule) {
        this.listRule = listRule;
    }

    @Override
    public byte[] transform(ClassLoader    loader,
            String              className,
            Class<?>            classBeingRedefined,
            ProtectionDomain    protectionDomain,
            byte[]              classfileBuffer)
			throws IllegalClassFormatException {
            byte[] byteCode = classfileBuffer;
             ClassPool cp = ClassPool.getDefault();
             ClassPathForGeneratedClasses gcp = new ClassPathForGeneratedClasses();  
             cp.insertClassPath(gcp); 
             ClassLoader tomcatLoader = Thread.currentThread().getContextClassLoader(); 
             LoaderClassPath tomcat = new LoaderClassPath(tomcatLoader);
            cp.insertClassPath(tomcat);
            
             
             String ClassTemp = className.replace("/", ".");
                    try{
                    CtClass cl = cp.makeClass(new ByteArrayInputStream(
						classfileBuffer));
                    if(ClassTemp.equals("org.apache.tomcat.websocket.server.WsFilter"))
                    {
                    
                    try {
                    
//                    CtClass cc = cp.makeClass("CharResponseWrapper");
//                    CtClass wrapper = cp.get("javax.servlet.http.HttpServletResponseWrapper");
//                    cc.setSuperclass(wrapper);
//                    CtField f = CtField.make("public java.io.CharArrayWriter output = new java.io.CharArrayWriter();", cc);
//                    cc.addField(f);
//                    CtConstructor constr = CtNewConstructor.make("public CharResponseWrapper(javax.servlet.http.HttpServletResponse response){super(response);output = new java.io.CharArrayWriter();}", cc);
//                    cc.addConstructor(constr);
//                    CtMethod m1 = CtNewMethod.make(
//                    "public String toString() { return output.toString(); }",
//                    cc);
//                    CtMethod m2 = CtNewMethod.make(
//                    "public java.io.PrintWriter getWriter() { return new java.io.PrintWriter(output); }",
//                    cc);
//                    cc.addMethod(m2);
//                    cc.addMethod(m1);
//                    gcp.addGeneratedClass(cc);  
//                    cp.insertClassPath(new ClassClassPath(cc.getClass()));
//                    
//                    cc.writeFile();
//                    cc.detach();
                    
                    System.out.println("masuk");
                    System.out.println(ClassTemp);
                    int methodCount = 0;
                    
                  
                    
                    for (Rule rule : listRule) {
                    CtMethod m2 = CtNewMethod.make(
                    "public boolean WAFMethod"+methodCount+"(javax.servlet.ServletRequest req1, javax.servlet.ServletResponse resp1) {boolean retVal = false;"
                            + "javax.servlet.http.HttpServletResponse resp = (javax.servlet.http.HttpServletResponse) resp1;"
                            + "javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest) req1;"
                            + "String method = \""+rule.getMethod()+"\";"
                            + "String variable = \""+rule.getVariable()+"\";"

                            // Setting Log
                            + "boolean isLog = "+rule.isLog()+";"
                            + "boolean isBlock = "+rule.isBlock()+";"
                            + "boolean confEngine = "+Configuration.logEnable+";" 
                            + "java.util.logging.Logger myLogger = java.util.logging.Logger.getLogger(\"WAF Tugas Akhir\");"
                            //ARGS cek all parameter terlebih dahulu
                            + "if(method.equals(\"all\")){"
                            + "String param = \""+rule.getVariable()+"\"; "
                            + "boolean cek = true;"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "if(param.equals(\"all\")){"
                            + "java.util.Enumeration enumeration = req1.getParameterNames();"
                            + "while (enumeration.hasMoreElements()) {"
                            + "String parameterName = (String) enumeration.nextElement();"
                            + "String value = req1.getParameter(parameterName);"

                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",value);"

                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(value.contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(value.endsWith(messageString)){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"                                            
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(value);"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}}"
                            + "}"

                            //cek untuk parameter tertentu
                            + "else{"
                            + "String value = req1.getParameter(param);"

                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",value);"
                            + "if(value != null){"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(value.contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(value);"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"

                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"
                            + "}}"
                            
                            //ARGS_GET cek all parameter terlebih dahulu
                            + "if(method.equals(\"get\") && variable.equals(\"all\")){"
                            + "String param = \""+rule.getVariable()+"\"; "
                            + "boolean cek = true;"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "if(param.equals(\"all\")){"
                            + "java.util.Enumeration enumeration = $1.getParameterNames();"
                            + "while (enumeration.hasMoreElements()) {"
                            + "String parameterName = (String) enumeration.nextElement();"
                            + "String value = $1.getParameter(parameterName);"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",value);"

                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(value.contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"                                            
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(value);"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}}"
                            + "}"

                            //ARGS_GET_NAME untuk parameter tertentu
                            + "else if(method.equals(\"get\") && !variable.equals(\"all\")){"
                            + "String value = req1.getParameter(param);"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",value);"

                            + "if(value != null){"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(value.contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(value);"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"

                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"
                            + "}}"
                            
                            //ARGS_POST cek all parameter terlebih dahulu
                            + "if(method.equals(\"post\") && variable.equals(\"all\")){"
                            + "String param = \""+rule.getVariable()+"\"; "
                            + "boolean cek = true;"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "if(param.equals(\"all\")){"
                            + "java.util.Enumeration enumeration = req1.getParameterNames();"
                            + "while (enumeration.hasMoreElements()) {"
                            + "String parameterName = (String) enumeration.nextElement();"
                            + "String value = req1.getParameter(parameterName);"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",value);"

                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(value.contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"                                            
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(value);"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}}"
                            + "}"

                            //cek untuk parameter tertentu
                            + "else if(method.equals(\"post\") && !variable.equals(\"all\")){"
                            + "String value = req1.getParameter(param);"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",value);"

                            + "if(value != null){"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(value.contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(value);"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"

                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"
                            + "}}"

                            //REQUEST COOKIES
                            + "javax.servlet.http.Cookie[] cookies = req.getCookies();"
                            + "if(method.equals(\"cookies\") && variable.equals(\"all\")){"
                            + "String param = \""+rule.getVariable()+"\"; "
                            + "boolean cek = true;"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "for(int i=0;i<cookies.length;i++){"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",cookies[i].getValue());"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(cookies[i].getValue().startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(cookies[i].getValue().contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(cookies[i].getValue().endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(cookies[i].getValue());"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"

                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}}"

                            //REQUEST_COOKIES_NAME
                            + "javax.servlet.http.Cookie[] cookies = req.getCookies();"
                            + "if(method.equals(\"cookies\") && variable.equals(\"all\")){"
                            + "String param = \""+rule.getVariable()+"\"; "
                            + "boolean cek = true;"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "for(int i=0;i<cookies.length;i++){"
                            + "if(cookies[i].getName().equals(param)){"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",cookies[i].getValue());"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(cookies[i].getValue().startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(cookies[i].getValue().contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(cookies[i].getValue().endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(cookies[i].getValue());"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"

                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}}}"

                            // REQUEST_METHOD
                            + "if(method.equals(\"method\")){"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",req.getMethod());"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(req.getMethod().startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(req.getMethod().contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(req.getMethod().endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(req.getMethod());"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"

                            // REQUEST_PROTOCOL
                            + "if(method.equals(\"protocol\")){"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",req.getProtocol());"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(req.getProtocol().startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(req.getProtocol().contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(req.getProtocol().endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(req.getProtocol());"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"

                            // Query String
                            + "if(method.equals(\"query\")){"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",req.getQueryString());"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(req.getQueryString().startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(req.getQueryString().contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(req.getQueryString().endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(req.getQueryString());"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"

                            // Query String
                            + "if(method.equals(\"uri\")){"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",req.getRequestURI());"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(req.getRequestURI().startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(req.getRequestURI().contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(req.getRequestURI().endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(req.getRequestURI());"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"

                            // Query String
                            + "if(method.equals(\"path\")){"
                            + "String matcher = \""+rule.getOperator()+"\";"
                            + "String messageString=\""+rule.getMessage()+"\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            + "fullURI=fullURI+\"?\"+req.getQueryString();}"
                            + "String messageString = messageString.replace(\"%request_uri\",fullURI);"
                            + "messageString = messageString.replace(\"%treat\",req.getPathInfo());"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + "if(req.getPathInfo().startsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@contains\")){"
                            + "if(req.getPathInfo().contains(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "if(matcher.equals(\"@endWith\")){"
                            + "if(req.getPathInfo().endsWith(\""+rule.getNilai()+"\")){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"                    
                            + "if(matcher.equals(\"@rg\")){"
                            + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                            + "java.util.regex.Matcher m = p.matcher(req.getPathInfo());"
                            + "if(m.find()){"
                            + "if(isLog && confEngine){"
                            + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                            + "myLogger.info(messageString);"
                            + "}"
                            + "if(isBlock){retVal=true; return retVal;}"
                            + "}}"
                            + "}"
                            
                            + "return retVal;"
                            + "}",cl);
                    
                    cl.addMethod(m2);
                    methodCount++;
                    }
                    
                    String append = "";
                    int whiteListCount=0;
                    for(String value : Configuration.whiteList)
                    {
                        if(whiteListCount==0)
                        {
                        append+= "if(value.startsWith(\""+value+"\")){"
                            + "cek=false;System.out.println(\"ini adalah test\");}";
                        }
                        else 
                        {
                            append+= "else if(value.startsWith(\""+value+"\")){"
                            + "cek=false;}";
                        }
                        
                        append += "else{cek=true;}";
                    }
                    
                    CtMethod whiteList = CtNewMethod.make(
                    "public boolean WAFWhiteList(javax.servlet.ServletRequest req1, javax.servlet.ServletResponse resp1, String whiteListParam) {boolean retVal = false;"
                            + "javax.servlet.http.HttpServletResponse resp = (javax.servlet.http.HttpServletResponse) resp1;"
                            + "javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest) req1;"
                            + "String method = \"all\";"
                            + "String variable = \"-----\";"

                            // Setting Log
                            + "boolean isLog = false;"
                            + "boolean isBlock = false;"
                            + "boolean confEngine = "+Configuration.logEnable+";" 
                            + "java.util.logging.Logger myLogger = java.util.logging.Logger.getLogger(\"WAF Tugas Akhir\");"
                            //ARGS cek all parameter terlebih dahulu
                            + "if(method.equals(\"all\")){"
                            + "String param = whiteListParam; "
                            + "boolean cek = false;"
                            + "String matcher = \"@beginwith\";"
                            
                            //cek untuk parameter tertentu
                            + ""
                            + "String value = req1.getParameter(whiteListParam);System.out.println(param);"

                            + "String messageString=\"Unvalidated Redirect and Forward\";"
                            + "String fullURI=req.getRequestURL().toString();"
                            + "if(req.getQueryString()!=null){"
                            
                            + "if(value != null){"
                            + "if(matcher.equals(\"@beginwith\")){"
                            + append
                            + "}"
                          
                            + "}"
                            + "retVal=cek;"
                            + "}}"
                            
                            + "return retVal;"
                            + "}",cl);
                    cl.addMethod(whiteList);
                    CtMethod[] methods =cl.getMethods();
                    
                        for (CtMethod method : methods) {
                            if(method.getName().equals("doFilter"))
                            {
                            try{
                                method.addLocalVariable("elapsedTime", CtClass.longType);
                                method.insertBefore("elapsedTime = System.currentTimeMillis();");
                                method.insertAfter("elapsedTime = System.currentTimeMillis() - elapsedTime;System.out.println(\"Method Executed in ms: \" + elapsedTime);");
                                int i =0;
                                for(String param : Configuration.whiteListParam)
                                {
                                        System.out.println("test masuk2");
                                        
                                        method.insertBefore("javax.servlet.http.HttpServletResponse resp = (javax.servlet.http.HttpServletResponse) $2;"
                                                            +"if(WAFWhiteList($1,$2,\""+param+"\")){resp.sendError(403); return;};" );
                                    
                                }
                                for (Rule rule : listRule) {
                                   
                                   
                                   
                                   //method.insertBefore("javax.servlet.http.HttpServletResponse resp = (javax.servlet.http.HttpServletResponse) $2;"
                                   //                   +"if(WAFMethod"+i+"($1,$2)){resp.sendError("+rule.getErrorCode()+"); return;};" );
                                   
                                   // method.insertAt(51, "CharResponseWrapper pp = new CharResponseWrapper((javax.servlet.http.HttpServletResponse) $2);");
                                   i++;
                                }
                                
                                
                            }
                            catch(Exception p)
                            {
                                p.printStackTrace();
                            }
                            }
                        }
                        byteCode = cl.toBytecode();
                        cl.detach();
                        System.out.println("Instrumentation complete.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                     }
                    }
                    

                
                    }
                catch (Exception ex) {
                   //ex.printStackTrace();
                }    
                    
                
		return byteCode;
	}
}


