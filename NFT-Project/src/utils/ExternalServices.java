package utils;

import java.io.FileInputStream;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import back.NFTCollectionView;

public class ExternalServices {

	public static final String from = "designpatterns111@outlook.co.il";
	public static final String pass = "Ds123456+";
	private static final ExternalServices ext = new ExternalServices();

	public void sendMail(String sendTo, String toSend) {

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.live.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", "smtp-mail.outlook.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			}

		});

		// Used to debug SMTP issues
		// session.setDebug(true);

		try {

			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));

			// Set Subject: header field
			message.setSubject("New opportunity available");

			// Now set the actual message
			message.setText(toSend);

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public void listToExcel(ArrayList<NFTCollectionView> rows) {
		// Initialize a Workbook object
		Workbook workbook = new Workbook();

		// Obtaining the reference of the worksheet
		Worksheet worksheet = workbook.getWorksheets().get(0);

		String[][] array2D = new String[rows.size() + 1][4];

		array2D[0][0] = "Collection name";
		array2D[0][1] = "Opensea price[SOL]";
		array2D[0][2] = "Magic eden price[SOL]";
		array2D[0][3] = "Diff[%]";
		int j = 0;
		for (int i = 1; i < rows.size() + 1; i++) {
			array2D[i][j++] = rows.get(i - 1).getCollection_name();
			array2D[i][j++] = String.valueOf(rows.get(i - 1).getOpensea_price());
			array2D[i][j++] = String.valueOf(rows.get(i - 1).getMagic_eden_price());
			array2D[i][j] = String.valueOf(rows.get(i - 1).getDiff());
			j = 0;
		}
		// Exporting the array of names to first row and first column vertically
		try {
			worksheet.getCells().importArray(array2D, 0, 0);
			// Saving the Excel file
			workbook.save("collections.xlsx");
			System.out.println(("Exported the data to excel."));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<NFTCollectionView> uploadList(String listName, int rowNum, int colNum) throws Exception {
		FileInputStream fstream = new FileInputStream(listName + ".xlsx");

		// Instantiating a Workbook object
		Workbook workbook = new Workbook(fstream);

		// Accessing the first worksheet in the Excel file
		Worksheet worksheet = workbook.getWorksheets().get(0);

		Object dataTable[][] = worksheet.getCells().exportArray(0, 0, rowNum, colNum);

		// Closing the file stream to free all resources
		fstream.close();

		ArrayList<NFTCollectionView> list = new ArrayList<>();
		for (int i = 1; i < dataTable.length; i++) {
			if (dataTable[i][1] == null)
				break;
			NFTCollectionView temp;
			if (dataTable[i][1] instanceof Integer)
				temp = new NFTCollectionView((String) dataTable[i][0], ((Integer) dataTable[i][1]).toString(),
						((Integer) dataTable[i][2]).toString(), ((Integer) dataTable[i][3]).toString());
			else
				temp = new NFTCollectionView((String) dataTable[i][0], (String) dataTable[i][1],
						(String) dataTable[i][2], (String) dataTable[i][3]);
			list.add(temp);
		}

		return list;
	}

	public static ExternalServices getInstace() {
		return ext;
	}

	public static void main(String[] args) throws Exception {
		ExternalServices e = ExternalServices.getInstace();
		NFTCollectionView col = new NFTCollectionView("a", "5", "3", (5 / 3) + "");
		ArrayList<NFTCollectionView> a = new ArrayList<NFTCollectionView>();
		a.add(col);
		e.listToExcel(a);
		ArrayList<NFTCollectionView> list = e.uploadList("collections", a.size(), 4);
		for (NFTCollectionView colo : list)
			System.out.println(colo.getCollection_name() + " " + colo.getOpensea_price() + " "
					+ colo.getMagic_eden_price() + " " + colo.getDiff());

	}
}