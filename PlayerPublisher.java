// The following code modified from from PrismTech HelloWorld example

/************************************************************************
 * LOGICAL_NAME:    PlayerPublisher.java
 * FUNCTION:        Publisher's main for the CR OpenSplice programming example.
 * DATE:            October 2016.
 * AUTHORS:         Group 6 - Anthony Devoz, Kritika Jain, Patrick Tennyson 
 * Code borrowed from Group 8
 ************************************************************************/
package srcclassfiles;
import DDS.*;
import CR.*;
import java.util.*;

public class PlayerPublisher {

	public PlayerPublisher(bjPlayer bjPlayerInstance) {
		DDSEntityManager mgr = new DDSEntityManager();
		String partitionName = "Casino Royale";

		// create Domain Participant
		mgr.createParticipant(partitionName);

		// create Type
		bjPlayerTypeSupport bjPlayerTS = new bjPlayerTypeSupport();
		mgr.registerType(bjPlayerTS);

		// create Topic
		mgr.createTopic("CR_bjPlayer");

		// create Publisher
		mgr.createPublisher();

		// create DataWriter
		mgr.createWriter();

		// Publish Events
		DataWriter dwriter = mgr.getWriter();
		bjPlayerDataWriter CRWriter = bjPlayerDataWriterHelper.narrow(dwriter);
		System.out.println("=== [PlayerPublisher] writing a message containing :");
		System.out.println("    uuid              : " + bjPlayerInstance.uuid);
		System.out.println("    seqno             : " + bjPlayerInstance.seqno);
		System.out.println("    credits        : " + bjPlayerInstance.credits);
		System.out.println("    wager          : " + bjPlayerInstance.wager);
		System.out.println("    dealer_id      : " + bjPlayerInstance.dealer_id);
		System.out.println("    action         : " + bjPlayerInstance.action.value());


		CRWriter.register_instance(bjPlayerInstance);
		int status = CRWriter.write(bjPlayerInstance, HANDLE_NIL.value);
		ErrorHandler.checkStatus(status, "bjPlayerDataWriter.write");

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
