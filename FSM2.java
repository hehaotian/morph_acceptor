import java.io.*;
import java.util.*;

public class FSM2 {

   private Graph<String, String> expanded_fsm2;
   private String finalState;
   private String startState;
   private Map<String, String> outputs;
 
   public FSM2(Scanner input) {
   
      this.finalState = finalState;
      this.startState = startState;
      
      expanded_fsm2 = new Graph<String, String>();
      outputs = new HashMap<String, String>();
      
      startState = "q0";
      
      if (input.hasNextLine()) {
         finalState = input.nextLine();
      } else {
         System.out.println("Wrong Carmel FSA format!");
      }
      
      while(input.hasNextLine()) {
         if (input.hasNextLine()) {
            String line = input.nextLine();
            Scanner token = new Scanner(line);
            String sta = "";
            String nex = "";
            String cha = "";
            String tranin = "";
            String output = "";
            while (token.hasNext()) {
               if (token.hasNext()) {
                  sta = trim(token.next());
                  nex = trim(token.next());
                  cha = trim(token.next());
                  tranin = sta + nex + cha;
                  output = trim(token.next());
                  expanded_fsm2.addNode(sta);
                  expanded_fsm2.addNode(nex);
                  expanded_fsm2.addEdge(sta, nex, cha);
                  outputs.put(tranin, output);
               }
            }
         } else {
            System.out.println("Wrong Carmel FSA format!");
         }
      }
   }
   
   public String check(String line) {
         
      Set<String> from = new HashSet<String>();
      Set<String> next = new HashSet<String>();
      from.add(startState);
      
      String output = "";
      String findKey = "";
      
      boolean last = false;
      
      // for the given string, check each input (For loop 1) with every possible start
      // state (For loop 2) and the resulting next or final states (For loop 3)
      for(int i = 0; i < line.length(); i++) {
         
         String temp = "";
         String pos = "";
         
         for(String currentState : from) {
            
            // creates a map that contains all possible transitions from the current start state
            Map<String, Set<String>> transitions = expanded_fsm2.getAllChildren(currentState);
            
            for(String letter : transitions.keySet()) {
               // creates a set of all possible next states for each transition
               Set<String> value = transitions.get(letter);
               
               if (value.contains(line.charAt(i) + "")) {
                  findKey = currentState + letter + line.charAt(i);
                  temp = outputs.get(findKey);
                  next.add(letter);
               }
               if (value.contains("*e*")) {
                  String getAnswer = outputs.get(currentState + letter + "*e*");
                  if (!getAnswer.equals("*e*"))
                     pos = "/" + getAnswer + " ";
                  next.add(letter);
               }
            }
         }
         
         if (temp.length() == 0 || !temp.equals(line.charAt(i) + ""))
            return "*NONE*";
            
         output += pos;
         output += temp;
         
         // no next states coming up before inputs run out
         if (next.isEmpty()) return "*NONE*";
         
         // refresh the from and next sets of start and next states to move on
         from = next;
         next = new HashSet<String>();
         from = epsilon(from);
         if (i == line.length() - 1) last = true;
      }
      
      if (last == true) output += process(from);
      
      // after all inputs are checked, check whether the last "start" state is the final state
      if (from.contains(finalState)) return output;
      
      return "*NONE*";
   }
   
   public String trim(String word) {
      return word.replaceAll("[^A-Za-z0-9_*]", "");
   }
   
   private Set<String> epsilon(Set<String> from) {
		
      Set<String> newfrom = new HashSet<String>();
         
      for(String fromone : from) {
         newfrom.add(fromone);
			Map<String, Set<String>> results = expanded_fsm2.getAllChildren(fromone);
			
         for(String result : results.keySet()) {
				if(results.get(result).contains("*e*")) {
               newfrom.add(result);
            }
			}
		}
      if(newfrom.size() == from.size()) 
         return newfrom;
		else
			return epsilon(newfrom);
	}
   
   
   private String process(Set<String> from) {
       
      String getAnswer = "";
      
      for(String fromone : from) {
			Map<String, Set<String>> results = expanded_fsm2.getAllChildren(fromone);
         
         for(String result : results.keySet()) {
				if(results.get(result).contains("*e*")) {
               if (!outputs.get(fromone + result + "*e*").equals("*e*"))
                  getAnswer = outputs.get(fromone + result + "*e*");
            }
			}
		}
      return "/" + getAnswer;
	}
   
}