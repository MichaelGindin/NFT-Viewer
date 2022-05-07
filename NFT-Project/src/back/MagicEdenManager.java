package back;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class MagicEdenManager {
	private static String urlMagicEden500 = "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=100";
	private static String urlMagicEden1000 = "https://api-mainnet.magiceden.dev/v2/collections?offset=500&limit=20";
	public HashMap<String, String> SymbolNameMap = new HashMap<>();

	// Getting all symbols collections names
	public void setTopCollectionsNamesSymbols() {

		ArrayList<String> collections = new ArrayList<String>();

		try {

			setCollectionNamesSymbols(urlMagicEden500);
			setCollectionNamesSymbols(urlMagicEden1000);
			// System.out.println(collections);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Getting collection symbols by URL
	private void setCollectionNamesSymbols(String url) throws IOException {

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		con.setRequestProperty("Accept", "application/json");
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

			String symbol = (String) collection.get("symbol");
			String name = (String) collection.get("name");
			SymbolNameMap.put(symbol, name);
		}

		// print result
//		System.out.println(response.toString());

	}

	// get all collections prices of magic eden
	public HashMap<String, Double> getCollectionPricesBySymbols_MagicEden(ArrayList<String> symbols) {
		
	
		
		HashMap<String, Double> map1 = new HashMap<String, Double>();
		HashMap<String, Double> map2 = new HashMap<String, Double>();
		HashMap<String, Double> map3 = new HashMap<String, Double>();
		ArrayList<String> symbols1 = new ArrayList<String>(symbols.subList(0, symbols.size() / 3));
		ArrayList<String> symbols2 = new ArrayList<String>(
				symbols.subList(symbols.size() / 3, (2 * symbols.size()) / 3));
		ArrayList<String> symbols3 = new ArrayList<String>(
				symbols.subList((2 * symbols.size()) / 3, symbols.size() - 1));

		Callable<Void> callable1 = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				HashMap<String, Double> map = get_subCollectionPricesBySymbols_MagicEden(symbols1);
				map1.putAll(map);

				System.out.println("finish 1");
				return null;

			}
		};
		Callable<Void> callable2 = new Callable<Void>() {
			@Override
			public Void call() throws Exception {

				HashMap<String, Double> map = get_subCollectionPricesBySymbols_MagicEden(symbols2);
				map2.putAll(map);
				System.out.println("finish 2");
				return null;

			}
		};
		Callable<Void> callable3 = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				HashMap<String, Double> map= get_subCollectionPricesBySymbols_MagicEden(symbols3);
				map3.putAll(map);
				System.out.println("finish 3");
				return null;

			}
		};

		List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();
		taskList.add(callable1);
		taskList.add(callable2);
		taskList.add(callable3);

		ExecutorService executor = Executors.newFixedThreadPool(3);
		////
		try {
			// start the threads and wait for them to finish
			executor.invokeAll(taskList);

		} catch (InterruptedException ie) {
			// do something if you care about interruption;
		}

		System.out.println("finish tasks");
		map1.putAll(map2);
		map1.putAll(map3);
		return map1;
	}

	
	
	// get all sub collection prices of magic eden (worker)
	public HashMap<String, Double> get_subCollectionPricesBySymbols_MagicEden(ArrayList<String> symbols) {
		// Magic-Eden
		HashMap<String, Double> map = new HashMap<String, Double>();
		int i = 0;
		for (String symbol : symbols) {
			String priceURLmagicEden = "https://api-mainnet.magiceden.dev/v2/collections/" + symbol + "/stats";
			double floorPrice;

			floorPrice = getFloorPrice_magicEden(priceURLmagicEden);

			if (floorPrice >= 0) {
				
				
				map.put(symbol,floorPrice);

			}
			i++;
			if (i > 30) {
				i = 0;
				System.out.println("30 was sent");
			}
		}

		return map;
	}

	// get floor price
	public double getFloorPrice_magicEden(String url) {
		try {
			URL obj;

			obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'GET' request to URL : " + url);
//			System.out.println("Response Code : " + responseCode);
			if (responseCode != 200) {
				return -3;
			}
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
				return floorPrice / 1000000000;//getting wrong number
			} else {
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -2;

	}

}
