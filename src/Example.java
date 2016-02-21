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
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?:\\b(?:(?:n(?:et(?:\\b\\W+?\\blocalgroup|\\.exe)|(?:map|c)\\.exe)|t(?:racer(?:oute|t)|elnet\\.exe|clsh8?|ftp)|(?:w(?:guest|sh)|rcmd|ftp)\\.exe|echo\\b\\W*?\\by+)\\b|c(?:md(?:(?:\\.exe|32)\\b|\\b\\W*?\\/c)|d(?:\\b\\W*?[\\\\/]|\\W*?\\.\\.)|hmod.{0,40}?\\+.{0,3}x))|[\\;\\|\\`]\\W*?\\b(?:(?:c(?:h(?:grp|mod|own|sh)|md|pp)|p(?:asswd|ython|erl|ing|s)|n(?:asm|map|c)|f(?:inger|tp)|(?:kil|mai)l|(?:xte)?rm|ls(?:of)?|telnet|uname|echo|id)\\b|g(?:\\+\\+|cc\\b)))");
	java.util.regex.Matcher m = p.matcher("asdasda");
        if(m.find())
        {
            System.out.println("founcd");
        }
        RuleReader rd = new RuleReader();
        ArrayList<Rule> rule = rd.getRule();
        SimpleClassTransformer transformer = new SimpleClassTransformer(rule);
        inst.addTransformer(transformer,true);
    }
    
}
