package org.snapscript.compile;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.snapscript.core.MapModel;
import org.snapscript.core.Model;
import org.snapscript.core.ResultType;
import org.snapscript.parse.SourceCode;
import org.snapscript.parse.SourceProcessor;
import org.snapscript.parse.SyntaxCompiler;
import org.snapscript.parse.SyntaxParser;

import com.sun.management.ThreadMXBean;

//Assembly time  took 376
//Binary assemble time was 2004 normal was 376
//Time taken to compile  was 2989 size was 57071
public class CompilePerformanceTest extends TestCase {   

   private static final int ITERATIONS = 10;
   public static void main(String[] l)throws Exception{
      new CompilePerformanceTest().testCompilerPerformance();
   }
   public void testCompilerPerformance() throws Exception {
      //compileScript("perf4.js");  
     // compileScript("/script/script13.snap"); 
      compileScript("/perf/perf4.snap"); 
 /*     executeScript("perf2.js");    
      executeScript("perf3.js"); */   
   }

   public static void compileScript(String source) throws Exception {
      executeScript(source, false);
      
   }
   public static Object executeScript(String source) throws Exception {
      return executeScript(source, true);
   }
   public static Object executeScript(String source, boolean execute) throws Exception {
      String script = ClassPathReader.load(source);
      SourceProcessor processor = new SourceProcessor(100);
      SourceCode code = processor.process(script);
      int compressed = code.getSource().length;
      int maxLine = code.getLines()[code.getLines().length -1];
      
      for(int j=0;j<ITERATIONS;j++){
         parseScript(source, script, compressed, maxLine);
      }
      for(int j=0;j<ITERATIONS;j++){
         compileScript(source, script, compressed, maxLine);
      }
      for(int j=0;j<ITERATIONS;j++){
         checkMemory(source, script, compressed, maxLine);
      }
      return ResultType.getNormal();
   } 
   
   private static void compileScript(String source, String script, int compressed, int maxLine) throws Exception {
      long start=System.currentTimeMillis();
      Map<String, Object> map = new HashMap<String, Object>();
      Model model = new MapModel(map);
      Compiler compiler = ClassPathCompilerBuilder.createCompiler();

      map.put("out", System.out);
      map.put("err", System.err);
      map.put("count", 100);
      
      
      Executable e=compiler.compile(script);
      long finish=System.currentTimeMillis();
      long duration=finish-start;
      System.err.println("Time taken to compile "+source+" was " + duration+" size was "+script.length() + " compressed to " + compressed + " and " + maxLine + " lines");
      start=System.currentTimeMillis();
      finish=System.currentTimeMillis();
      duration=finish-start;
   }
   
   private static void parseScript(String source, String script, int compressed, int maxLine) throws Exception {
      long start=System.currentTimeMillis();
      SyntaxParser p=new SyntaxCompiler().compile();
      p.parse(source, script, "script");
      long finish=System.currentTimeMillis();
      long duration=finish-start;
      System.err.println("Time taken to parse "+source+" was " + duration+" size was "+script.length() + " compressed to " + compressed + " and " + maxLine + " lines");
   }
   
   private static void checkMemory(String source, String script, int compressed, int maxLine) throws Exception {
      DecimalFormat format = new DecimalFormat("###,###,###,###,###");
      ThreadMXBean bean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
      Thread thread = Thread.currentThread();
      long id = thread.getId();
      System.gc();
      System.gc();
      Thread.sleep(100);
      long before = bean.getThreadAllocatedBytes(id);
      parseScript(source, script, compressed, maxLine);
      long after = bean.getThreadAllocatedBytes(id);
      System.out.println("parse memory=" + format.format(after - before));
   }
}
