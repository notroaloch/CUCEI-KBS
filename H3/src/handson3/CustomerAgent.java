/**
 * ***************************************************************
 * HandsOn 3 - Comunicación Directa entre Agentes
 * Sistemas Basados en Conocimiento - CUCEI 2022A
 * Ochoa Herrera Rodrigo Alejandro
 * **************************************************************
 */

package handson3;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.domain.FIPANames;

import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

public class CustomerAgent extends Agent {
        
    String[] sellers = {"AMAZON", "MERCADO LIBRE"};
        
    @Override
    protected void setup() {     

        // Obtener los productos a comprar, así como el método de pago
        // [producto, producto, ..., metodo de pago]
     
        int nResponders;
        Object[] args = getArguments();

        if (args != null && args.length > 1) {

            String[] productList = new String[args.length - 1];
            String paymentMethod = args[args.length - 1].toString().trim().toUpperCase();

            for(int i = 0; i < args.length - 1; i++){
                String product = (String) args[i];
                productList[i] = product.trim().toUpperCase();
            }

            nResponders = sellers.length;
            System.out.println("Enviando CFP a " + nResponders + " vendedores");

            // Construir el mensaje CFP

            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));

            for (int i = 0; i < nResponders; ++i) {
                    msg.addReceiver(new AID((String) sellers[i], AID.ISLOCALNAME));
            }

            String order = productList[0] + ";" + paymentMethod;
            msg.setContent(order);

            addBehaviour(new ContractNetInitiator(this, msg) {

                @Override
                protected void handlePropose(ACLMessage propose, Vector v) {
                    System.out.println(propose.getSender().getLocalName() + " propuso " + propose.getContent());
                }

                @Override
                protected void handleRefuse(ACLMessage refuse) {
                    System.out.println(refuse.getSender().getLocalName() + " rechazó");
                }

                @Override
                protected void handleFailure(ACLMessage failure) {
                    if (failure.getSender().equals(myAgent.getAMS())) {
                        String msg = "El vendedor no existe";
                        System.out.println(msg);
                    }
                    else {
                        String msg = failure.getSender().getLocalName()+" falló";
                        System.out.println(msg);
                    }
                }

                @Override
                protected void handleAllResponses(Vector responses, Vector acceptances) {

                    if (responses.size() < nResponders) {
                        String msg = "Tiempo de espera agotado: faltaron " + (nResponders - responses.size()) + " respuestas";
                        System.out.println(msg);
                    }

                    // Evaluar las propuestas

                    int bestPrice = 99999;
                    String bestProposal = "";
                    AID bestProposer = null;
                    ACLMessage accept = null;

                    Enumeration e = responses.elements();

                    while (e.hasMoreElements()) {
                        ACLMessage msg = (ACLMessage) e.nextElement();

                        if (msg.getPerformative() == ACLMessage.PROPOSE) {

                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                            acceptances.addElement(reply);

                            String proposal = msg.getContent();

                            String product = proposal.split(";")[0];
                            int price = Integer.parseInt(proposal.split(";")[1]);
                            String offer = proposal.split(";")[2];

                            if (price < bestPrice) {
                                bestPrice = price;
                                bestProposal = proposal;
                                bestProposer = msg.getSender();
                                accept = reply;
                            }
                        }
                    }

                    // Aceptar la mejor propuesta (precio menor)

                    if (accept != null) {
                        String msg = "ACEPTANDO propuesta de " + bestProposer.getLocalName() + " (" + bestProposal + ")";
                        System.out.println(msg);
                        accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    }						
                }

                @Override
                protected void handleInform(ACLMessage inform) {
                    String msg = inform.getSender().getLocalName() + " realizó la petición correctamente";
                    System.out.println(msg);
                }
            } );
        }
        else {
                System.out.println("ERROR: No se recibieron argumentos de compra");
        }
    } 
}

