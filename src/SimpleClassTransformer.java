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
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
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

   
             String ClassTemp = className.replace("/", ".");
                    try{
                    CtClass cl = cp.makeClass(new ByteArrayInputStream(
						classfileBuffer));
                    if(ClassTemp.equals("org.apache.tomcat.websocket.server.WsFilter"))
                    {
                    try {
                    System.out.println("masuk");
                    System.out.println(ClassTemp);
                    CtMethod[] methods =cl.getMethods();
                        for (CtMethod method : methods) {
                            if(method.getName().equals("doFilter"))
                            {
                            try{
                                for (Rule rule : listRule) {

                                    method.insertBefore("String method = \""+rule.getMethod()+"\";"
                                                      + "String variable = \""+rule.getVariable()+"\";"
                                                      + "javax.servlet.http.HttpServletResponse resp = (javax.servlet.http.HttpServletResponse) $2;"
                                                      + "javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest) $1;"
                                                      // Setting Log
                                                      + "boolean isLog = "+rule.isLog()+";"
                                                      + "boolean isBlock = "+rule.isBlock()+";"
                                                      + "java.util.logging.Logger myLogger = java.util.logging.Logger.getLogger(\"WAF Tugas Akhir\");"
                                                     
                                                      
                                                      //ARGS cek all parameter terlebih dahulu
                                                      + "if(method.equals(\"all\")){"
                                                      + "String param = \""+rule.getVariable()+"\"; "
                                                      + "boolean cek = true;"
                                                      + "String matcher = \""+rule.getOperator()+"\";"
                                                      + "if(param.equals(\"all\")){"
                                                      + "java.util.Enumeration enumeration = $1.getParameterNames();"
                                                      + "while (enumeration.hasMoreElements()) {"
                                                      + "String parameterName = (String) enumeration.nextElement();"
                                                      + "String value = $1.getParameter(parameterName);"
                                                      + "System.out.println(value);"
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(value.contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"                                            
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}}"
                                                      + "}"
                                                      
                                                      //cek untuk parameter tertentu
                                                      + "else{"
                                                      + "String value = $1.getParameter(param);"
                                                      + "System.out.println(value);"
                                                      + "if(value != null){"
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(value.contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"                    
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "System.out.println(\"seharusnya masuk log\");"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
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
                                                      + "System.out.println(value);"
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(value.contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"                                            
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}}"
                                                      + "}"
                                            
                                                      //ARGS_GET_NAME untuk parameter tertentu
                                                      + "else if(method.equals(\"get\") && !variable.equals(\"all\")){"
                                                      + "String value = $1.getParameter(param);"
                                                      + "System.out.println(value);"
                                                      + "if(value != null){"
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(value.contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"                    
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "System.out.println(\"seharusnya masuk log\");"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "}"
                                                      + "}}"
                                            
                                                      //ARGS_POST cek all parameter terlebih dahulu
                                                      + "if(method.equals(\"post\") && variable.equals(\"all\")){"
                                                      + "String param = \""+rule.getVariable()+"\"; "
                                                      + "boolean cek = true;"
                                                      + "String matcher = \""+rule.getOperator()+"\";"
                                                      + "if(param.equals(\"all\")){"
                                                      + "java.util.Enumeration enumeration = $1.getParameterNames();"
                                                      + "while (enumeration.hasMoreElements()) {"
                                                      + "String parameterName = (String) enumeration.nextElement();"
                                                      + "String value = $1.getParameter(parameterName);"
                                                      + "System.out.println(value);"
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(value.contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"                                            
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}}"
                                                      + "}"
                                            
                                                      //cek untuk parameter tertentu
                                                      + "else if(method.equals(\"post\") && !variable.equals(\"all\")){"
                                                      + "String value = $1.getParameter(param);"
                                                      + "System.out.println(value);"
                                                      + "if(value != null){"
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(value.contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"                    
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "System.out.println(\"seharusnya masuk log\");"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
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
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(cookies[i].getValue().startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(cookies[i].getValue().contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(cookies[i].getValue().endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"                    
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(cookies[i].getValue());"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "System.out.println(\"seharusnya masuk log\");"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
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
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(cookies[i].getValue().startsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(cookies[i].getValue().contains(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(cookies[i].getValue().endsWith(\""+rule.getNilai()+"\")){"
                                                      + "if(isLog){"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"                    
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(cookies[i].getValue());"
                                                      + "if(m.find()){"
                                                      + "if(isLog){"
                                                      + "System.out.println(\"seharusnya masuk log\");"
                                                      + "myLogger.info(\"==================WAF Instrumentation Tugas Akhir=================\");"
                                                      + "myLogger.info(\""+rule.getMessage()+"\");"
                                                      + "}"
                                                      + "if(isBlock){resp.sendError("+rule.getErrorCode()+"); return;}"
                                                      + "}}"
                                                      + "}}}"
                                    );
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


