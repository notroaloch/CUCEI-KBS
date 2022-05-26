
package handson3;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiverBehaviour extends CyclicBehaviour {
    
    @Override
    public void action() {
        
        ACLMessage msg = getAgent().receive();
        if(msg != null){
            String str = "SOY EL AGENTE " + getAgent().getAID() + " Y RECIBÍ UN MENSAJE DE " + msg.getSender();
            String content = "EL MENSAJE DICE: " + msg.getContent();
            System.out.println(str);
            System.out.println(content);
        }
        
    }
    
}
