import java.io.*;
import java.util.*;
import com.google.common.collect.*;

public class Tab {
      
   private Table<String, String, LinkedList> fsaLinked = HashBasedTable.create();
   private Set<String> row = new TreeSet<String>();
   private String finalState;
      
   public Tab(Scanner input, Scanner words) {
      
      Map<String, Collection<String>> p = new HashMap<String, Collection<String>>();
      ArrayList<String> allGrammarRules = new ArrayList<String>();
      
      this.finalState = "";
      String originalfsaLine = "";
      
      if (input.hasNextLine()) finalState = input.nextLine();
      else System.out.println("Wrong Carmel FSA format!");
      
      while (input.hasNextLine()) {
         if (input.hasNextLine()) {
            String curline = input.nextLine();
            if (curline.length() > 1) {
               originalfsaLine = curline;
               String grammarRule = originalfsaLine;
               grammarRule = grammarRule.substring(7).replaceAll("\\)", "");
               if (!p.containsKey(grammarRule)) {
                  ArrayList<String> temp = new ArrayList<String>();
                  temp.add(cutIt(originalfsaLine)); 
                  p.put(grammarRule, temp);
               } else if (p.containsKey(grammarRule)) {
                  p.get(grammarRule).add(cutIt(originalfsaLine));
               }
            }
         }
      }
      
      String transitionTemp = "";
      String arc = "";
      for (String j : p.keySet()) {
         Iterator<String> iterator = p.get(j).iterator();
         while(iterator.hasNext()) {
            arc = iterator.next() + j;
            arc = arc.replaceAll("\\s", "");
            transitionTemp += arc + " ";
         }
      }
      String[] ordered = transitionTemp.split("\\ ");
      Arrays.sort(ordered);
      
      for (String e : ordered) {
         String startLevel = e.substring(0, 1);
         row.add(startLevel);
         String end = e.substring(1, 2);
         e = e.substring(2) + e.substring(0, 1) + e.substring(1, 2);
         fsaLinked.put(startLevel, e, new LinkedList(startLevel + end));
      }
      
      while (words.hasNextLine()) {
         if (words.hasNextLine()) {
            String firstToken = words.nextLine();
            String secondToken = firstToken;
            firstToken = firstToken.replaceAll("\\s(.*)", "");    // words
            secondToken = secondToken.replaceAll("(.*)\\s", "");  // grammar
            
            for (String k : row) {    // level
               for (String g : fsaLinked.row(k).keySet()){ // column
                  if (cut(g).equals(secondToken)) {   // add it to the value
                     fsaLinked.get(k, g).listOfTokens.add(firstToken);
                  }
               }
            }
         }
      }
   }
     
   public void print_fsm1(PrintStream ps) {
      int states = 4;
      ps.println(finalState);
      for (String k : row) {    // level state
         for (String g : fsaLinked.row(k).keySet()){ // column grammar rules
            Iterator<String> itr = fsaLinked.get(k, g).listOfTokens.iterator();
            char[] chars;
            if (!itr.hasNext()) {
               ps.println("(q" + fsaLinked.get(k, g).start + " (q" + fsaLinked.get(k, g).destination + " *e*))");
            }
            while (itr.hasNext()) {
               if (itr.hasNext()) {
                  String word = itr.next();
                  chars = word.toCharArray();
                  if (chars.length == 1) {
                     ps.println("(q" + fsaLinked.get(k, g).start + " (q" + fsaLinked.get(k, g).destination + " \"" + chars[0] + "\"))");
                     states ++;   
                  } else {
                     ps.println("(q" + fsaLinked.get(k, g).start + " (q" + states + " \"" + chars[0] + "\"))");
                     states ++;
                     for (int i = 1; i < chars.length - 1; i ++) {
                        ps.println("(q" + (states - 1) + " (q" + states + " \"" + chars[i] + "\"))");
                        states ++;
                     }
                     ps.println("(q" + (states - 1) + " (q" + fsaLinked.get(k, g).destination + " \"" + chars[chars.length - 1] + "\"))");
                  }
               }
            }  
         }
      }
   }
   
   public void print_fsm2(PrintStream ps) {
      int states = 4;
      ps.println(finalState);
      for (String k : row) {    // level state
         for (String g : fsaLinked.row(k).keySet()){ // column grammar rules
            Iterator<String> itr = fsaLinked.get(k, g).listOfTokens.iterator();
            char[] chars;
            if (!itr.hasNext()) {
               ps.println("(q" + fsaLinked.get(k, g).start + " (q" + fsaLinked.get(k, g).destination + " *e* *e*))");
            }
            while (itr.hasNext()) {
               if (itr.hasNext()) {
                  String word = itr.next();
                  chars = word.toCharArray();
                  if (chars.length == 1) {
                     ps.println("(q" + fsaLinked.get(k, g).start + " (q" + states + " \"" + chars[0] + "\" \"" + chars[0] + "\"))");
                     states ++;   
                  } else {
                     ps.println("(q" + fsaLinked.get(k, g).start + " (q" + states + " \"" + chars[0] + "\" \"" + chars[0] + "\"))");
                     states ++;
                     for (int i = 1; i < chars.length; i ++) {
                        ps.println("(q" + (states - 1) + " (q" + states + " \"" + chars[i] + "\" \"" + chars[i] + "\"))");
                        states ++;
                     }
                  }
                  ps.println("(q" + (states - 1) + " (q" + fsaLinked.get(k, g).destination + " *e* \"" + g.substring(0, g.length() - 2) + "\"))");
               }
            }  
         }
      }
   }

   
   public String cut(String g) {
      return g.substring(0, g.length() - 2);
   }
   
   public String cutIt(String k) {
      return k.substring(2,3) + k.substring(6,7);
   }
}

class LinkedList {

   public LinkedList next;
   public int start;
   public int destination;
   public ArrayList<String> listOfTokens;
   
   public LinkedList(String startEnd) {
      this.next = null;
      this.listOfTokens = new ArrayList<String>();
      this.next = null;
      this.start = Integer.parseInt(startEnd.substring(0,1));
      this.destination = Integer.parseInt(startEnd.substring(1));
   }
 
   public LinkedList(String startEnd, LinkedList next) {
      this.next = next;
      this.listOfTokens = new ArrayList<String>();
      this.next = null;
      this.start = Integer.parseInt(startEnd.substring(0,1));
      this.destination = Integer.parseInt(startEnd.substring(1));
   }
 
}