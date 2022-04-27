package back;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.aspose.cells.Cells;
import com.aspose.cells.CellsFactory;
import com.aspose.cells.Color;
import com.aspose.cells.ExportRangeToJsonOptions;
import com.aspose.cells.JsonLayoutOptions;
import com.aspose.cells.JsonUtility;
import com.aspose.cells.Range;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import javax.activation.*;

public class ExternalServices {

	public static final String from = "designpatterns111@gmail.com";

	public static void sendMail(String sendTo, String toSend) {

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("designpatterns111@gmail.com", "Ds123456+");

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

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

	public static void jsonToExcel(String jsonName, String excelName) throws Exception {
		// Instantiating a Workbook object
		Workbook workbook = new Workbook();
		Worksheet worksheet = workbook.getWorksheets().get(0);

		// Read File
		File file = new File(jsonName + ".json");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String jsonInput = "";
		String tempString;
		while ((tempString = bufferedReader.readLine()) != null) {
			jsonInput = jsonInput + tempString;
		}
		bufferedReader.close();

		// Set Styles
		CellsFactory factory = new CellsFactory();
		Style style = factory.createStyle();
		style.setHorizontalAlignment(TextAlignmentType.CENTER);
		style.getFont().setColor(Color.getBlueViolet());
		style.getFont().setBold(true);

		// Set JsonLayoutOptions
		JsonLayoutOptions options = new JsonLayoutOptions();
		options.setTitleStyle(style);
		options.setArrayAsTable(true);

		// Import JSON Data
		JsonUtility.importData(jsonInput, worksheet.getCells(), 0, 0, options);

		// Save Excel file
		workbook.save(excelName + ".xlsx");
		System.out.println(excelName + ".xls file written successfully...\n");
	}

	public static void excelToJson(String jsonName, String excelName) throws Exception {
		// load XLSX file with an instance of Workbook
		Workbook workbook = new Workbook(excelName + ".xlsx");
		// access CellsCollection of the worksheet containing data to be converted
		Cells cells = workbook.getWorksheets().get(0).getCells();
		// create & set ExportRangeToJsonOptions for advanced options
		ExportRangeToJsonOptions exportOptions = new ExportRangeToJsonOptions();
		// create a range of cells containing data to be exported
		Range range = cells.createRange(0, 0, cells.getLastCell().getRow() + 1, cells.getLastCell().getColumn() + 1);
		// export range as JSON data
		String jsonData = JsonUtility.exportRangeToJson(range, exportOptions);
		// write data to disc in JSON format
		BufferedWriter writer = new BufferedWriter(new FileWriter(jsonName + ".json"));
		writer.write(jsonData);
		writer.close();
		System.out.println(jsonName + ".json file written successfully...\n");
	}

	public static void main(String[] args) {
		 sendMail("nitzansm92@gmail.com", "bbb");

		try {
			jsonToExcel("top1000", "top1000");
			excelToJson("top", "top1000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}