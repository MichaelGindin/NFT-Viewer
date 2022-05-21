package back;

import java.util.ArrayList;

public class EmailSender implements Runnable {

	String[] sendTo;
	ArrayList<NFTCollectionView> collections;
	float threshold;
	int sleepTime;
	ExternalServices ext = ExternalServices.getInstace();
	
	public EmailSender(String[] sendTo, ArrayList<NFTCollectionView> collections, float threshold, int sleepTime) {
		this.sendTo = sendTo;
		this.collections = collections;
		this.threshold = threshold;
		this.sleepTime = sleepTime;
	}

	public void updateFields(String[] sendTo, ArrayList<NFTCollectionView> collections,float threshold, int sleepTime) {
		this.sendTo = sendTo;
		this.collections = collections;
		this.threshold = threshold;
		this.sleepTime = sleepTime;
	}

	@Override
	public void run() {
		while (true) {
			String toSend = "Collection name			  OpenSea 		  Magic Eden		Diff[%]\n";
			int count = 0;
			for (NFTCollectionView col : collections) {
				if (col.getDiff().equals("-")) {
					continue;
				}
				if (Float.parseFloat(col.getDiff()) > threshold) {
					count++;
					toSend += col.getCollection_name() + "			" + col.getOpensea_price() + "		"
							+ col.getMagic_eden_price() + "			   " + col.getDiff() + "\n";
				}
			}
			if (count > 0)
				for (String email : sendTo)
					ext.sendMail(email, toSend);
			try {
				Thread.sleep(sleepTime * 1000);
				// Thread.sleep(sleepTime*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
