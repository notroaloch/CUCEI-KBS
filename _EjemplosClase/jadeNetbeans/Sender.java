/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handson3;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author roaloch
 */
public class Sender extends Agent {
    
    @Override
    protected void setup(){
        
        System.out.println("AID : " + getAID().getLocalName());
        
        ACLMessage acl1 = new ACLMessage(ACLMessage.INFORM);
        acl1.addReceiver(new AID("AMAZON", AID.ISLOCALNAME));
        acl1.setContent("¿HOLA COMO ESTAS?");
        send(acl1);
        
    }
    
    @Override
    protected void takeDown(){
        System.out.println("Agent Killed");        
    }
    
}
