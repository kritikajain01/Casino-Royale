// The following code modified from from PrismTech HelloWorld example

/************************************************************************
 * LOGICAL_NAME:    Snooper.java
 * FUNCTION:        Reads both Dealer and Player topics for CR project.
 * DATE:            October 2016.
 * AUTHORS:         Group 6 - Anthony Devoz, Kritika Jain, Patrick Tennyson 
 * Code borrowed from Group 8
 ************************************************************************/
package srcclassfiles;
import DDS.*;
import CR.*;
import java.util.*;

public class Snooper {

	public static void main(String[] args) {
		DDSEntityManager mgr = new DDSEntityManager();
		DDSEntityManager mgr2 = new DDSEntityManager();
		String partitionName = "Casino Royale";

		// create Domain Participants
		mgr.createParticipant(partitionName);
		mgr2.createParticipant(partitionName);

		// create Types
		bjDealerTypeSupport bjDealerTS = new bjDealerTypeSupport();
		mgr.registerType(bjDealerTS);
		bjPlayerTypeSupport bjPlayerTS = new bjPlayerTypeSupport();
		mgr2.registerType(bjPlayerTS);

		// create Topics
		mgr.createTopic("CR_bjDealer");
		mgr2.createTopic("CR_bjPlayer");

		// create Subscribers
		mgr.createSubscriber();
		mgr2.createSubscriber();

		// create DataReaders
		mgr.createReader();
		mgr2.createReader();

		// Read Events

		DataReader dreader = mgr.getReader();
		bjDealerDataReader CRReader = bjDealerDataReaderHelper.narrow(dreader);
		DataReader dreader2 = mgr2.getReader();
		bjPlayerDataReader CRReader2 = bjPlayerDataReaderHelper.narrow(dreader2);

		bjDealerSeqHolder bjDealerSeq = new bjDealerSeqHolder();
		SampleInfoSeqHolder infoSeq = new SampleInfoSeqHolder();
		bjPlayerSeqHolder bjPlayerSeq = new bjPlayerSeqHolder();
		SampleInfoSeqHolder infoSeq2 = new SampleInfoSeqHolder();

		System.out.println ("=== [Snooper] Ready ...");
		boolean terminate = false;
		while (!terminate) {
			CRReader.read(bjDealerSeq, infoSeq, LENGTH_UNLIMITED.value,
					ANY_SAMPLE_STATE.value, 1,
					ANY_INSTANCE_STATE.value);
			CRReader2.read(bjPlayerSeq, infoSeq2, LENGTH_UNLIMITED.value,
					ANY_SAMPLE_STATE.value, 1,
					ANY_INSTANCE_STATE.value);
			for (int i = 0; i < bjDealerSeq.value.length; i++) {
				System.out.println("=== [Dealer] message received :");
				System.out.println("    uuid           : " + bjDealerSeq.value[i].uuid);
				System.out.println("    seqno          : " + bjDealerSeq.value[i].seqno);
				System.out.println("    active_players : " + bjDealerSeq.value[i].active_players);
				System.out.println("    players        : " + bjDealerSeq.value[i].players);
				System.out.println("    action         : " + bjDealerSeq.value[i].action.value());
				System.out.println("    cards          : " + bjDealerSeq.value[i].cards);
				System.out.println("    target_uuid    : " + bjDealerSeq.value[i].target_uuid);
			}
			for (int i = 0; i < bjPlayerSeq.value.length; i++) {
				System.out.println("=== [Player] message received :");
				System.out.println("    uuid           : " + bjPlayerSeq.value[i].uuid);
				System.out.println("    seqno          : " + bjPlayerSeq.value[i].seqno);
				System.out.println("    credits        : " + bjPlayerSeq.value[i].credits);
				System.out.println("    wager          : " + bjPlayerSeq.value[i].wager);
				System.out.println("    dealer_id      : " + bjPlayerSeq.value[i].dealer_id);
				System.out.println("    action         : " + bjPlayerSeq.value[i].action.value());
			}
			// slight delay for the dds
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// Do nothing
			}
		}

	}
}
