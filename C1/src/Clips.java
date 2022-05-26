package clips;
import net.sf.clipsrules.jni.*;

public class Clips {
    public static void main(String args[]) throws Exception {
        

        Environment clips;
        
        clips = new Environment();
        
        clips.eval("(clear)");
        
        clips.load("/Users/roaloch/Desktop/KBS/C1/src/persons/load-persons.clp");
        clips.load("/Users/roaloch/Desktop/KBS/C1/src/persons/load-persons-rules.clp");

        clips.eval("(reset)");
        clips.eval("(facts)");
        clips.eval("(rules)");

        clips.run();
    }

}