import java.io.*;
import java.util.*;

public class morph_acceptor1 {
   public static void main(String[] args) throws IOException {
      
      File fsm1 = new File(args[0]);
      Scanner expanded = new Scanner(fsm1);
      File word = new File(args[1]);
      Scanner wordlist = new Scanner(word);
      PrintStream ps = new PrintStream(args[2]);
   
      FSM1 expanded_fsm1 = new FSM1(expanded);
      
      String line = "";
      String oriLine = "";
      
      // reads string lines from the ex file
      while (wordlist.hasNextLine()) {
         if (wordlist.hasNextLine()) {
            line = wordlist.nextLine();
            oriLine = line;
            
            // checks whether the string line passes through the created fsa and prints the results
            if (expanded_fsm1.check(line)) {
               ps.println(oriLine + " => yes");
            } else {
               ps.println(oriLine + " => no");
            }
         } else {
            ps.println("No test strings in this file.");
         }
      }  
   }
   
   public static void trim(String line) {
      line = line.substring(1);
   }
   
}