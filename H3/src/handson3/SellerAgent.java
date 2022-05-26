/**
 * ***************************************************************
 * HandsOn 3 - Comunicación Directa entre Agentes
 * Sistemas Basados en Conocimiento - CUCEI 2022A
 * Ochoa Herrera Rodrigo Alejandro
 * **************************************************************
 */

package handson3;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.clipsrules.jni.*;

public class SellerAgent extends Agent {
    
        Environment clips;

        @Override
	protected void setup() {
            
            // Iniciar entorno CLIPS
            clips = new Environment();
            addBehaviour(new SellerBehaviour());
            
            System.out.println(getLocalName() + " esperando CFP");

            MessageTemplate template = MessageTemplate.and(
                            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                            MessageTemplate.MatchPerformative(ACLMessage.CFP) );

            addBehaviour(new ContractNetResponder(this, template) {

                @Override
                protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {

                    System.out.println(getLocalName()+ " recibió un CFP de " + cfp.getSender().getLocalName() + ". CFP: " + cfp.getContent());

                    boolean acceptProposal = decideIfAcceptProposal();
                    
                    if (true) {
                            
                            // Aceptamos el CFP y enviamos una propuesta
                            
                            String proposal = cfp.getContent();
                            String product = proposal.split(";")[0];
                            String paymentMethod = proposal.split(";")[1];
                            
                            String price = "";
                            String offer = "";
                            String offerContent = "";
                            
                            try{
                                
                                String assertStr = "(order (order-id " + cfp.getSender().getLocalName() + ") (product " + product + ") (payment-method " + paymentMethod + "))";
                                clips.assertString(assertStr);
                                clips.run();
                                                               
                                List<FactAddressValue> offers = clips.findAllFacts("offer");
                                
                                System.out.println("NUMERO DE OFERTAS: " + offers.size());
                                
                                if(offers.size() < 1){
                                    price = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 35000 + 1));
                                    offerContent = product + ";" + price + ";" + "NO HAY OFERTAS";
                                }else{
                                    for(FactAddressValue currentOffer : offers){
                                    
                                        String sender = cfp.getSender().getLocalName();
                                        String orderID = currentOffer.getSlotValue("order-id").toString();

                                        if(sender.equals(orderID)){

                                            String currentProduct = currentOffer.getSlotValue("product").toString();
                                            String currentOfferMsg = currentOffer.getSlotValue("message").toString();
                                            String currentPrice = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 35000 + 1));

                                            price = currentPrice;
                                            offer = currentOfferMsg;
                                            offerContent = currentProduct + ";" + currentPrice + ";" + currentOfferMsg;
                                        }
                                    }
                                }
                                

                                
                            } catch (CLIPSException e){
                                System.out.println(e);
                            }

                             
                            String msg = getLocalName() + " propone " + product + " a $" + price + "MXN más la oferta " +  offer + " al pagar con " + paymentMethod; 
                            System.out.println(msg);
                            
                            ACLMessage propose = cfp.createReply();
                            propose.setPerformative(ACLMessage.PROPOSE);
                            propose.setContent(offerContent);
                            
                            try {
                                clips.reset();
                                clips.run();
                            } catch (CLIPSException ex) {
                                System.out.println(ex);
                            }
                            
                            return propose;
                            
                    }
                    else {
                            throw new RefuseException("evaluation-failed");
                    }
                }

                @Override
                protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
                    
                    System.out.println(getLocalName() + " aceptó la negociación");
                    
                    if (decideIfAcceptProposal()) {
                        ACLMessage inform = accept.createReply();
                        inform.setPerformative(ACLMessage.INFORM);
                        return inform;
                    }
                    
                    else {
                        System.out.println(getLocalName() + " falló en realizar la petición");
                        throw new FailureException("unexpected-error");
                    }	
                }

                @Override
                protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
                    System.out.println(getLocalName() + " rechazó la propuesta");
                }
                
            } );
	}

	private boolean decideIfAcceptProposal() {
            // Genera un número random para decidir si aceptar o rechazar el CFP
            // return (Math.random() > 0.2);
            return true;
	}
        
        private class SellerBehaviour extends OneShotBehaviour{

        @Override
        public void action() {
            
            try{
                
                clips.clear();
                clips.load("/Users/roaloch/Desktop/KBS/H3/HandsOn3/src/load-templates.clp");
                clips.load("/Users/roaloch/Desktop/KBS/H3/HandsOn3/src/load-facts.clp");
                clips.load("/Users/roaloch/Desktop/KBS/H3/HandsOn3/src/load-rules.clp");
                clips.reset();
                clips.run();
                                
            } catch(CLIPSException e){
                System.out.println(e);
            }
            
        }
            
    }
}

