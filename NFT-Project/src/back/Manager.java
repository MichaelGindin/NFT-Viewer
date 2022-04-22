package back;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Manager extends Application {

	// FXML controllers

	private static String urlMagicEden500 = "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=500";
	private static String urlMagicEden1000 = "https://api-mainnet.magiceden.dev/v2/collections?offset=500&limit=500";
	private String urlOpenSea300 = "https://api.opensea.io/api/v1/collections?offset=0&limit=300";

	public static void main(String[] args) throws IOException {

		// launch(args);
		String url = "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=500";
		ArrayList<String> symbols = getTopCollectionsNameSymbols();

		ArrayList<Collection> collections = getCollectionPricesBySymbols(symbols);

		for (Collection collection : collections) {
			System.out.println(collections.indexOf(collection) + ". symbol:" + collection.getSymbol() + " floorPrice:"
					+ collection.getFloorPrice());
		}

		/*
		 * String json = getJson(url);
		 * 
		 * FileWriter file = new FileWriter("top1000.json"); file.write(json);
		 * file.close();
		 */
	}

	private static ArrayList<Collection> getCollectionPricesBySymbols(ArrayList<String> symbols) {
		// Magic-Eden
		ArrayList<Collection> collections = new ArrayList<Collection>();

		for (String symbol : symbols) {
			String priceURLmagicEden = "https://api-mainnet.magiceden.dev/v2/collections/" + symbol + "/stats";
			double floorPrice;

			floorPrice = getFloorPrice(priceURLmagicEden);

			collections.add(new Collection(symbol, floorPrice));
		}

		return collections;
	}

	public static double getFloorPrice(String url) {
		try {
			URL obj;

			obj = new URL(url);

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
			Gson gson = new Gson();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}
			in.close();
			LinkedTreeMap collection = gson.fromJson(response.toString(), LinkedTreeMap.class);
			if (collection.containsKey("floorPrice")) {
				double floorPrice = (double) collection.get("floorPrice");
				return floorPrice;
			} else {
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

	}

	public static ArrayList<String> getTopCollectionsNameSymbols() {

		ArrayList<String> collections = new ArrayList<String>();

		try {
			collections.addAll(getCollectionNames(urlMagicEden500));
			collections.addAll(getCollectionNames(urlMagicEden1000));
			System.out.println(collections);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return collections;
	}

	public static ArrayList<String> getCollectionNames(String url) throws IOException {

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
		Gson gson = new Gson();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);

		}
		in.close();
		LinkedTreeMap[] collections = gson.fromJson(response.toString(), LinkedTreeMap[].class);
		ArrayList<String> collectionNames = new ArrayList<>();
		for (LinkedTreeMap collection : collections) {
			collectionNames.add((String) collection.get("symbol"));
		}

		// print result
		// System.out.println(response.toString());

		return collectionNames;

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
		Gson gson = new Gson();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			LinkedTreeMap[] collections = gson.fromJson(inputLine, LinkedTreeMap[].class);

		}
		in.close();

		// print result
		// System.out.println(response.toString());

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