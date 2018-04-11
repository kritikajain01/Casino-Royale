// The following code modified from from PrismTech HelloWorld example

/************************************************************************
 * LOGICAL_NAME:    DealerPublisher.java
 * FUNCTION:        Publisher's main for the CR OpenSplice programming example.
 * DATE:            October 2016.
 * AUTHORS:         Group 6 - Anthony Devoz, Kritika Jain, Patrick Tennyson 
 Code borrowed from Group 8
 ************************************************************************/
package srcclassfiles;
import DDS.*;
import CR.*;
import java.util.*;

public class DealerPublisher {

	public DealerPublisher(bjDealer bjDealerInstance) {
		DDSEntityManager mgr = new DDSEntityManager();
		String partitionName = "Casino Royale";

		// create Domain Participant
		mgr.createParticipant(partitionName);

		// create Type
		bjDealerTypeSupport bjDealerTS = new bjDealerTypeSupport();
		mgr.registerType(bjDealerTS);

		// create Topic
		mgr.createTopic("CR_bjDealer");

		// create Publisher
		mgr.createPublisher();

		// create DataWriter
		mgr.createWriter();

		// Publish Events
		DataWriter dwriter = mgr.getWriter();
		bjDealerDataWriter CRWriter = bjDealerDataWriterHelper.narrow(dwriter);
		System.out.println("=== [DealerPublisher] writing a message containing :");
		System.out.println("    uuid              : " + bjDealerInstance.uuid);
		System.out.println("    seqno             : " + bjDealerInstance.seqno);
		System.out.println("    active_players    : " + bjDealerInstance.active_players);
		System.out.println("    players           : " + bjDealerInstance.players);
		System.out.println("    action            : " + bjDealerInstance.action.value());
		System.out.println("    cards             : " + bjDealerInstance.cards);
		System.out.println("    target_uuid       : " + bjDealerInstance.target_uuid);
		CRWriter.register_instance(bjDealerInstance);
		int status = CRWriter.write(bjDealerInstance, HANDLE_NIL.value);
		ErrorHandler.checkStatus(status, "bjDealerDataWriter.write");

		// OpenSplice needs a slight delay so it doesn't repeat the message
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// nothing to do
		}

		// clean up
		mgr.getPublisher().delete_datawriter(CRWriter);
		mgr.deletePublisher();
		mgr.deleteTopic();
		mgr.deleteParticipant();

	}
}
