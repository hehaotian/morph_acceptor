import java.io.*;
import java.util.*;

public class expand_fsm2 {

   public static void main(String[] args) throws IOException {

      File lexicon = new File(args[0]);
      Scanner words = new Scanner(lexicon);
      File carmel = new File(args[1]);
      Scanner input = new Scanner(carmel);
      PrintStream ps = new PrintStream(args[2]);
      
      Tab expanded_fsm = new Tab(input, words);
      expanded_fsm.print_fsm2(ps);
   }
       
}