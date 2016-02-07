/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.MalformedURLException;  
import java.net.URL;  
import java.util.HashMap;  
  
import javassist.CannotCompileException;  
import javassist.ClassPath;  
import javassist.CtClass;  
import javassist.NotFoundException;  
/**
 *
 * @author Octo
 */
public class ClassPathForGeneratedClasses implements ClassPath{
    private HashMap<String, InputStream> classes;
    
    public ClassPathForGeneratedClasses() {  
       super();  
       classes = new HashMap<String, InputStream>();  
    }  
    
    public void addGeneratedClass(CtClass generated) throws CannotCompileException {  
    try {  
        generated.stopPruning(true);  
        ByteArrayInputStream source = new ByteArrayInputStream(generated.toBytecode());  
        classes.put(generated.getName(), source);  
        generated.stopPruning(false);  
    } catch (Exception e) {  
        // should not happen  
        System.out.println("unexpected : " + e);  
        System.exit(1);  
        }  
    }  
    public void close() {  
        this.classes.clear();  
    }  
    public URL find(String classname) {  
        try {  
            String urlString = "file:/ClassPathForGeneratedClasses/" + classname;  
            URL result = (classes.containsKey(classname)) ? new URL(urlString) : null;  
            return result;  
        } catch (MalformedURLException e) {  
            return null;  
        }  
    }  
    public InputStream openClassfile(String classname) throws NotFoundException {  
    if (!classes.containsKey(classname)) {  
        return null;  
    }  
        return classes.get(classname);  
    }  
}
