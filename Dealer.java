// The following code modified from from PrismTech HelloWorld example

/************************************************************************
 * LOGICAL_NAME:    Dealer.java
 * FUNCTION:        CR logic for the Dealer. Sends all messages to DealerPublisher.
 * DATE:            October 2016.
 * AUTHORS:         Group 6 - Anthony Devoz, Kritika Jain, Patrick Tennyson 
  Code borrowed from Group 8
 ************************************************************************/
package srcclassfiles;
import DDS.*;
import CR.*;
import java.util.*;

public class Dealer {

    public static void main(String[] args) {
        Dealer deal = new Dealer(); // Dynamic class object
        bjDealer bjDealerInstance = new bjDealer();
        DDSEntityManager mgr = new DDSEntityManager();
        String partitionName = "Casino Royale";
        mgr.createParticipant(partitionName);
        bjPlayerTypeSupport bjPlayerTS = new bjPlayerTypeSupport();
        mgr.registerType(bjPlayerTS);
        mgr.createTopic("CR_bjPlayer");
        mgr.createSubscriber();
        mgr.createReader();
        DataReader dreader2 = mgr.getReader();
        bjPlayerDataReader CRReader2 = bjPlayerDataReaderHelper.narrow(dreader2);
        bjPlayerSeqHolder bjPlayerSeq = new bjPlayerSeqHolder();
        SampleInfoSeqHolder infoSeq2 = new SampleInfoSeqHolder();
        System.out.println("Creating deck");
        ArrayList<card> deck = new ArrayList<card>();
        deck.add(new card('C', '2', false));
        deck.add(new card('H', '2', false));
        deck.add(new card('D', '2', false));
        deck.add(new card('S', '2', false));
        deck.add(new card('C', '3', false));
        deck.add(new card('H', '3', false));
        deck.add(new card('D', '3', false));
        deck.add(new card('S', '3', false));
        deck.add(new card('C', '4', false));
        deck.add(new card('H', '4', false));
        deck.add(new card('D', '4', false));
        deck.add(new card('S', '4', false));
        deck.add(new card('C', '5', false));
        deck.add(new card('H', '5', false));
        deck.add(new card('D', '5', false));
        deck.add(new card('S', '5', false));
        deck.add(new card('C', '6', false));
        deck.add(new card('H', '6', false));
        deck.add(new card('D', '6', false));
        deck.add(new card('S', '6', false));
        deck.add(new card('C', '7', false));
        deck.add(new card('H', '7', false));
        deck.add(new card('D', '7', false));
        deck.add(new card('S', '7', false));
        deck.add(new card('C', '8', false));
        deck.add(new card('H', '8', false));
        deck.add(new card('D', '8', false));
        deck.add(new card('S', '8', false));
        deck.add(new card('C', '9', false));
        deck.add(new card('H', '9', false));
        deck.add(new card('D', '9', false));
        deck.add(new card('S', '9', false));
        deck.add(new card('C', 'T', false));
        deck.add(new card('H', 'T', false));
        deck.add(new card('D', 'T', false));
        deck.add(new card('S', 'T', false));
        deck.add(new card('C', 'J', false));
        deck.add(new card('H', 'J', false));
        deck.add(new card('D', 'J', false));
        deck.add(new card('S', 'J', false));
        deck.add(new card('C', 'Q', false));
        deck.add(new card('H', 'Q', false));
        deck.add(new card('D', 'Q', false));
        deck.add(new card('S', 'Q', false));
        deck.add(new card('C', 'K', false));
        deck.add(new card('H', 'K', false));
        deck.add(new card('D', 'K', false));
        deck.add(new card('S', 'K', false));
        deck.add(new card('C', 'A', false));
        deck.add(new card('H', 'A', false));
        deck.add(new card('D', 'A', false));
        deck.add(new card('S', 'A', false));
        ArrayList<card> decktemp = deck;
        System.out.println("Deck Ready for Shuffle");
        Collections.shuffle(deck);
        System.out.println("Deck has been shuffled");
        bjDealerInstance.action = bjd_action.shuffling;
        bjDealerInstance.credits = 200;
        System.out.println("Dealer is stocked");
        Random rand = new Random();
        bjDealerInstance.uuid = rand.nextInt(100) + 1;
        bjDealerInstance.seqno = rand.nextInt(20) + 1;
        bjDealerInstance.active_players = 0;
        bjDealerInstance.action = bjd_action.waiting; // Dealer default waiting state
        bjDealerInstance.target_uuid = 0; // set the default target_uuid
        ArrayList<player_status> playerBets = new ArrayList<player_status>();


        int dealerTotal = 0;
        int playerTotal = 0;
        int cardNumber = 0;

        ArrayList<card> playerCards = new ArrayList<card>();
        ArrayList<card> dealerCards = new ArrayList<card>();


        Iterator<card> itr = deck.iterator();




        deal.publish(bjDealerInstance);

        boolean terminate = false;

        while (!terminate) {
            CRReader2.read(bjPlayerSeq, infoSeq2, LENGTH_UNLIMITED.value,
                    ANY_SAMPLE_STATE.value, 1,
                    ANY_INSTANCE_STATE.value);

            for (int i = 0; i < bjPlayerSeq.value.length; i++) {
                if (bjPlayerSeq.value[i].dealer_id == bjDealerInstance.uuid) {




                    bjDealerInstance.active_players += 1;

                    if (!itr.hasNext()) {
                        System.out.println("Out of cards Shuffling new deck");
                        deck = decktemp;
                        Collections.shuffle(deck);
                    }

                    playerCards.add(itr.next());
                    playerCards.add(itr.next());
                    dealerCards.add(itr.next());
                    dealerCards.add(itr.next());

                    card[] stockCard = new card[playerCards.size()];
                    stockCard = playerCards.toArray(stockCard);


                    Iterator<card> playerCheck = playerCards.iterator();
                    Iterator<card> dealerCheck = dealerCards.iterator();

                    playerBets.add(new player_status(bjPlayerSeq.value[i].uuid, bjPlayerSeq.value[i].wager, stockCard));


                    System.out.println("Check for winner");
                    cardNumber = 0;
                    while(playerCheck.hasNext()) {

                        switch (playerCards.get(cardNumber).base_value) {
                            case '2':
                                playerTotal += 2;
                                break;
                            case '3':
                                playerTotal += 3;
                                break;
                            case '4':
                                playerTotal += 4;
                                break;
                            case '5':
                                playerTotal += 5;
                                break;
                            case '6':
                                playerTotal += 6;
                                break;
                            case '7':
                                playerTotal += 7;
                                break;
                            case '8':
                                playerTotal += 8;
                                break;
                            case '9':
                                playerTotal += 9;
                                break;
                            case 'T':
                                playerTotal += 10;
                            case 'K':
                                playerTotal += 10;
                                break;
                            case 'Q':
                                playerTotal += 10;
                                break;
                            case 'J':
                                playerTotal += 10;
                                break;
                            case 'A':
                                if (playerTotal > 10) {
                                    playerTotal += 1;
                                } else
                                    playerTotal += 11;
                                break;
                            default:
                                System.out.println("Failed to find card player");
                                System.out.println(playerCards.get(cardNumber).base_value);

                        }

                        bjDealerInstance.players[i].uuid = bjPlayerSeq.value[i].uuid;
                        bjDealerInstance.players[i].wager = bjPlayerSeq.value[i].wager;



                        //bjDealerInstance.players[i].cards[1] = playerCards.toArray(bjDealerInstance.players[i].cards);

                        if (bjDealerInstance.action == bjDealerInstance.action.waiting)
                        {
                            //System.out.println("Never got player response");
                            //CRReader2.read(bjPlayerSeq, infoSeq2, LENGTH_UNLIMITED.value,
                             //       ANY_SAMPLE_STATE.value, 1,
                             //       ANY_INSTANCE_STATE.value);
                        }

                        if (bjPlayerSeq.value[i].action == bjp_action.requesting_a_card)
                        {
                            System.out.println("Giving Player Another Card");
                            playerCards.add(itr.next());
                            bjDealerInstance.action = bjDealerInstance.action.waiting;
                            bjPlayerSeq.value[i].action = bjp_action.none;

                            cardNumber += 1;
                            break;
                        }

                        if (cardNumber > 4)
                        {
                            break;
                        }

                        bjDealerInstance.target_uuid = playerTotal;

                        deal.publish(bjDealerInstance);



                    }

                    cardNumber = 0;
                    while(dealerCheck.hasNext()) {
                        System.out.println("CardNum =" + cardNumber);
                        switch (dealerCards.get(cardNumber).base_value) {
                            case '1':
                                dealerTotal += 1;
                                break;
                            case '2':
                                dealerTotal += 2;
                                break;
                            case '3':
                                dealerTotal += 3;
                                break;
                            case '4':
                                dealerTotal += 4;
                                break;
                            case '5':
                                dealerTotal += 5;
                                break;
                            case '6':
                                dealerTotal += 6;
                                break;
                            case '7':
                                dealerTotal += 7;
                                break;
                            case '8':
                                dealerTotal += 8;
                                break;
                            case '9':
                                dealerTotal += 9;
                                break;
                            case 'T':
                                dealerTotal += 10;
                                break;
                            case 'K':
                                dealerTotal += 10;
                                break;
                            case 'Q':
                                dealerTotal += 10;
                                break;
                            case 'J':
                                dealerTotal += 10;
                                break;
                            case 'A':
                                if (dealerTotal > 10) {
                                    dealerTotal += 1;
                                }
                                else
                                    dealerTotal += 11;
                                break;
                            default:
                                System.out.println("Failed to find card dealer");
                                System.out.println(dealerCards.get(cardNumber).base_value);
                        }
                        if (dealerTotal < 16){
                            dealerCards.add(itr.next());
                        }
                        else
                            break;
                        cardNumber += 1;
                    }

                    cardNumber = 0;

                    if (dealerTotal == playerTotal)
                    {
                        System.out.println(" Push no winner");
                    }

                    System.out.println("Current Player=" + playerTotal + "Current Dealer = " + dealerTotal);

                    if (playerTotal > 21){
                        System.out.println("Player Bust!");
                        bjDealerInstance.action = bjd_action.collecting;
                        bjPlayerSeq.value[i].credits -= bjPlayerSeq.value[i].wager;
                        bjDealerInstance.credits += bjPlayerSeq.value[i].wager;
                        break;
                    }
                    if (dealerTotal > 21){
                        System.out.println("Dealer Bust!");
                        bjDealerInstance.action = bjd_action.paying;
                        bjPlayerSeq.value[i].credits += bjPlayerSeq.value[i].wager;
                        bjDealerInstance.credits -= bjPlayerSeq.value[i].wager;
                        break;
                    }
                    if (playerTotal > dealerTotal) {
                        bjDealerInstance.action = bjd_action.paying;
                        bjPlayerSeq.value[i].credits += bjPlayerSeq.value[i].wager;
                        bjDealerInstance.credits -= bjPlayerSeq.value[i].wager;
                        break;
                    }
                    if (dealerTotal > playerTotal) {
                        bjDealerInstance.action = bjd_action.collecting;
                        bjPlayerSeq.value[i].credits -= bjPlayerSeq.value[i].wager;
                        bjDealerInstance.credits += bjPlayerSeq.value[i].wager;
                        break;
                    }

                    if (bjDealerInstance.credits < 20 )
                    {
                        System.out.println("Out of funds");
                        bjDealerInstance.credits = 500;
                    }
                    if (bjPlayerSeq.value[i].action == bjPlayerSeq.value[i].action.exiting) {
                        System.out.println("Player is leaving");
                        bjDealerInstance.active_players -= 1;
                    }

                    bjDealerInstance.seqno += 1;

                }
                deal.publish(bjDealerInstance);
                playerTotal = 0;
                dealerTotal = 0;
            }



            // slight delay for the dds
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Do nothing
            }

            // OpenSplice needs a slight delay so it doesn't repeat the message
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }// end of main

    // send the dealer msg to OpenSplice stream
    public void publish(bjDealer bjDealerInstance) {
        DealerPublisher our_dealer = new DealerPublisher(bjDealerInstance);
    } // end of publish
} // end of dealer class