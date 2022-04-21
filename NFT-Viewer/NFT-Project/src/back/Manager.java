package back;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Manager extends Application{

	// FXML controllers

	

	private String urlMagicEden500 = "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=500";
	private String urlMagicEden1000 = "https://api-mainnet.magiceden.dev/v2/collections?offset=500&limit=500";
	private String urlOpenSea300 = "https://api.opensea.io/api/v1/collections?offset=0&limit=300";

	public static void main(String[] args) throws IOException {
		
		launch(args);
		/*String url = "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=500";

		String json = getJson(url);

		FileWriter file = new FileWriter("top1000.json");
		file.write(json);
		file.close();*/

	}

	public static String getJson(String url) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

		return response.toString();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainWindowController.class.getResource("MainWindow.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("NFT-Viewer");
		primaryStage.setScene(sc);
		primaryStage.show();
		primaryStage.setResizable(false);		
	}
}