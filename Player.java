/************************************************************************
 * LOGICAL_NAME:    Player.java
 * FUNCTION:        CR logic for the Dealer. Sends all messages to DealerPublisher.
 * DATE:            October 2016.
 * AUTHORS:         Group 6 - Anthony Devoz, Kritika Jain, Patrick Tennyson 
  Code borrowed from Group 8
 ************************************************************************/
package srcclassfiles;
 import DDS.*;
import CR.*;
import java.util.*;

public class Player {

	public static void main(String[] args) {
	Player play = new Player(); // Dynamic class object
	bjPlayer bjPlayerInstance = new bjPlayer();
	
   //creating subscriber
   DDSEntityManager mgr = new DDSEntityManager();
   String partitionName = "Casino Royale";
   
   mgr.createParticipant(partitionName);
   bjDealerTypeSupport bjDealerTS = new bjDealerTypeSupport();
   
   mgr.registerType(bjDealerTS);
   mgr.createTopic("CR_bjDealer");
   mgr.createSubscriber();
   mgr.createReader();   
   
   DataReader dreader = mgr.getReader();
   bjDealerDataReader CRReader = bjDealerDataReaderHelper.narrow(dreader);
   bjDealerSeqHolder bjDealerSeq = new bjDealerSeqHolder();
   SampleInfoSeqHolder infoSeq = new SampleInfoSeqHolder();   
   
   boolean terminate = false;
   while (!terminate) {
   CRReader.read(bjDealerSeq, infoSeq, LENGTH_UNLIMITED.value,
      ANY_SAMPLE_STATE.value, 1,
      ANY_INSTANCE_STATE.value);
	  
   for (int i = 0; i < bjDealerSeq.value.length; i++) {

   Random rand = new Random();
	bjPlayerInstance.uuid = rand.nextInt(100) + 1;
	bjPlayerInstance.seqno = rand.nextInt(20) + 1;
	bjPlayerInstance.credits = 100;
	bjPlayerInstance.wager = 20;
    bjPlayerInstance.action = bjPlayerInstance.action.joining;
	bjPlayerInstance.dealer_id = bjDealerSeq.value[i].uuid;
	play.publish(bjPlayerInstance);
   }
   //table joined
   for (int i = 0; i < bjDealerSeq.value.length; i++) {
          if (bjDealerSeq.value[i].target_uuid <= 11){
              bjPlayerInstance.action = bjPlayerInstance.action.wagering;
              bjPlayerInstance.wager = 25;
              bjPlayerInstance.action = bjPlayerInstance.action.requesting_a_card;
              play.publish(bjPlayerInstance);
          }
          if (bjDealerSeq.value[i].target_uuid > 11){
              bjPlayerInstance.action = bjPlayerInstance.action.requesting_a_card;
              play.publish(bjPlayerInstance);             
          }   }

	// OpenSplice needs a slight delay so it doesn't repeat the message
	try {
		Thread.sleep(200);
	} catch (InterruptedException e) {
		// do nothing
	}
   }
} // end of main
	
	// send the player msg to OpenSplice stream
	public void publish(bjPlayer bjPlayerInstance) {
		PlayerPublisher our_player = new PlayerPublisher(bjPlayerInstance);
	} // end of publish

} // end of player class