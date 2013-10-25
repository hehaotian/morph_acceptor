import java.io.*;
import java.util.*;

public class FSM1 {

   private Graph<String, String> expanded_fsm1;
   private String finalState;
   private String startState;
 
   public FSM1(Scanner input) {
   
      this.finalState = finalState;
      this.startState = startState;
      
      expanded_fsm1 = new Graph<String, String>();
      
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
            while (token.hasNext()) {
               if (token.hasNext()) {
                  sta = trim(token.next());
                  nex = trim(token.next());
                  cha = trim(token.next());
                  expanded_fsm1.addNode(sta);
                  expanded_fsm1.addNode(nex);
                  expanded_fsm1.addEdge(sta, nex, cha);
               }
            }
         } else {
            System.out.println("Wrong Carmel FSA format!");
         }
      }
   }
   
   public boolean check(String line) {
         
      Set<String> from = new HashSet<String>();
      Set<String> next = new HashSet<String>();
      from.add(startState);
      
      // for the given string, check each input (For loop 1) with every possible start
      // state (For loop 2) and the resulting next or final states (For loop 3)
      for(int i = 0; i < line.length(); i++) {
         
         from = epsilon(from);
         for(String currentState : from) {
            // creates a map that contains all possible transitions from the current start state
            Map<String, Set<String>> transitions = expanded_fsm1.getAllChildren(currentState);
            
            for(String letter : transitions.keySet()) {
               // creates a set of all possible next states for each transition
               Set<String> value = transitions.get(letter);
               if(value.contains(line.charAt(i) + "")) {     
                  next.add(letter);
               }
            }   
         }
         
         // no next states coming up before inputs run out
         if (next.isEmpty()) return false;
         
         // refresh the from and next sets of start and next states to move on
         from = next;
         next = new HashSet<String>();
         from = epsilon(from);
      }
      
      // after all inputs are checked, check whether the last "start" state is the final state
      if(from.contains(finalState)) return true;
      
      return false;
   }
   
   public String trim(String word) {
      return word.replaceAll("[^A-Za-z0-9*]", "");
   }
   
   private Set<String> epsilon(Set<String> from) {
		Set<String> newfrom = new HashSet<String>();
		for(String fromone : from) {
			newfrom.add(fromone);
			Map<String, Set<String>> results = expanded_fsm1.getAllChildren(fromone);
			for(String result : results.keySet()) {
				if(results.get(result).contains("*e*")) {
					newfrom.add(result);
				}
			}
		}
		if(newfrom.size() == from.size()) {
			return newfrom;
		} else {
			return epsilon(newfrom);
		}
	}
}