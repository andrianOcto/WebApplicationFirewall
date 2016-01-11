/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Octo
 */

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Example {
    public static void premain(String args, Instrumentation inst) {
        System.out.println("Simple Agent");
        Configuration conf = new Configuration();
        try
        {
        Pattern p = Pattern.compile("'(\\p{Blank})*or(\\p{Blank})*(('.'(\\p{Blank})*=(\\p{Blank})'.))");
        Matcher m = p.matcher("./././test");
        boolean b = m.find();
        System.out.println(b);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
	RuleReader rd = new RuleReader();
        ArrayList<Rule> rule = rd.getRule();
        SimpleClassTransformer transformer = new SimpleClassTransformer(rule);
        inst.addTransformer(transformer,true);
    }
}
