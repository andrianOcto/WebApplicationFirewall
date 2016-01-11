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
                                                      + "javax.servlet.http.HttpServletResponse resp = (javax.servlet.http.HttpServletResponse) $2;"
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
                                                      + "resp.sendError(403); return;}}"
                                                      + "if(matcher.equals(\"@contains\")){"
                                                      + "if(value.contains(\""+rule.getNilai()+"\")){"
                                                      + "resp.sendError(403); return;}}"
                                                      + "if(matcher.equals(\"@endWith\")){"
                                                      + "if(value.endsWith(\""+rule.getNilai()+"\")){"
                                                      + "resp.sendError(403); return;}}"
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "resp.sendError(403); return;}}}"
                                                      + "}"
                                                      + "else{"
                                                      + "String value = $1.getParameter(param);"
                                                      + "System.out.println(value);"
                                                      + "if(value != null){"
                                                      + "if(matcher.equals(\"@beginwith\")){"
                                                      + "if(value.startsWith(\""+rule.getNilai()+"\")){"
                                                      + "resp.sendError(403); return;}}"
                                                      + "if(matcher.equals(\"@rg\")){"
                                                      + "java.util.regex.Pattern p = java.util.regex.Pattern.compile(\""+rule.getNilai()+"\");"
                                                      + "java.util.regex.Matcher m = p.matcher(value);"
                                                      + "if(m.find()){"
                                                      + "resp.sendError(403); return;}}}"
                                                      + "}}");
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


