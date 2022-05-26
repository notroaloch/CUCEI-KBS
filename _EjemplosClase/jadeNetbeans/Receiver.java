package handson3;
import jade.core.Agent;

public class Receiver extends Agent {
    
    @Override
    protected void setup(){
        
        System.out.println("AID : " + getAID().getLocalName());
        addBehaviour(new ReceiverBehaviour());
    }
    
}
