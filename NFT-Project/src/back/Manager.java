package back;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Manager extends Application {
	
//	private static String urlTop = "https://api-mainnet.magiceden.io/all_collections_with_escrow_data";
//
//	// FXML controllers
//
//	private static String urlMagicEden500 = "https://api-devnet.magiceden.dev/v2/collections?offset=0&limit=500";
//	private static String urlMagicEden1000 = "https://api-devnet.magiceden.dev/v2/collections?offset=500&limit=500";
////	private String urlOpenSea300 = "https://api.opensea.io/api/v1/collections?offset=0&limit=300";

	public static void main(String[] args) {
		
		launch(args);
//		MagicEdenManager magicEdenManager = new MagicEdenManager();
//		OpenSeaManager openSeaManager = new OpenSeaManager();
//
//		// set symbol-name
//		magicEdenManager.setTopCollectionsNamesSymbols();
//
//		ArrayList<String> symbols = new ArrayList<>(magicEdenManager.SymbolNameMap.keySet());
//
//		HashMap<String, Double> magicMap = magicEdenManager.getCollectionPricesBySymbols_MagicEden(symbols);
////		ArrayList<Collection> magicCollections = new ArrayList<>();
//
//		System.out.println("Got magic eden collections");
//
//		// ArrayList<String> names = magicEdenManager.getTopCollectionsNames();
//		ArrayList<String> names = new ArrayList<>(magicEdenManager.SymbolNameMap.values());
//		HashMap<String, Double> openSeaMap = openSeaManager.getCollectionPricesByNames_OpenSea(names);
//
//		System.out.println("Got Opensea collections");
//
//		HashMap<String, String> dict = magicEdenManager.SymbolNameMap;
//		ArrayList<Collection> joinedCollections = CombineMapsWithDict(dict, magicMap, openSeaMap);
//
//		PrintCollections(joinedCollections);
//
//		System.out.println("Finished");

	}

	
	static String getJson(String url) throws IOException {

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
		MainWindowController mainWindowController = loader.getController();
		mainWindowController.setComboBox();
		mainWindowController.start_data_to_Table();
	}
}

/*
 * public static void main(String[] args) throws IOException {
 * 
 * Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());
 * System.out.println("Start :" + startTimestamp); // launch(args); String url =
 * "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=100";
 * 
 * //Get collection names ArrayList<String> symbols =
 * getTopCollectionsNameSymbols();
 * 
 * ArrayList<Collection> collections_magicEden =
 * getCollectionPricesBySymbols_MagicEden(symbols); ArrayList<Collection>
 * collections_opensea = getCollectionPricesBySymbols_OpenSea(symbols);
 * 
 * // for (Collection collection : collections_magicEden) { //
 * System.out.println(collections_magicEden.indexOf(collection) + ". symbol:" +
 * collection.getSymbol() // + " floorPrice:" + collection.getFloorPrice()); //
 * } System.out.println("\n\n\n\n"); for (Collection collection :
 * collections_opensea) {
 * System.out.println(collections_opensea.indexOf(collection) + ". symbol:" +
 * collection.getSymbol() + " floorPrice:" + collection.getFloorPrice()); }
 * Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
 * System.out.println("End :" + endTimestamp);
 * 
 * String json = getJson(url);
 * 
 * FileWriter file = new FileWriter("top100.json");
 * file.write(collections_opensea.toString()); file.close();
 * 
 * }
 */
