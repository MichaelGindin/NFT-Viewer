package back;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class OpenSeaManager {

	public HashMap<String, Double> getCollectionPricesByNames_OpenSea(ArrayList<String> symbols) {

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
				HashMap<String, Double> map = get_subCollectionPricesByNames_OpenSea(symbols1);
				map1.putAll(map);

				System.out.println("finish 1");
				return null;

			}
		};
		Callable<Void> callable2 = new Callable<Void>() {
			@Override
			public Void call() throws Exception {

				HashMap<String, Double> map = get_subCollectionPricesByNames_OpenSea(symbols2);
				map2.putAll(map);
				System.out.println("finish 2");
				return null;

			}
		};
		Callable<Void> callable3 = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				HashMap<String, Double> map = get_subCollectionPricesByNames_OpenSea(symbols3);
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

	public HashMap<String, Double> get_subCollectionPricesByNames_OpenSea(ArrayList<String> symbols) {

		HashMap<String, Double> map = new HashMap<String, Double>();

		for (String symbol : symbols) {
			ArrayList<String> names = convertNameString(symbol);
			for (String name : names) {
				String priceURLopenSea = "https://api.opensea.io/api/v1/collection/" + name + "/stats";
				double floorPrice;
				// System.out.println("\nSending 'GET' request to URL : " + priceURLopenSea);

				floorPrice = getFloorPrice_openSea(priceURLopenSea);

				if (floorPrice >= 0) {
					map.put(symbol, floorPrice);
					break;
				}
			}

		}

		return map;
	}

	private double getFloorPrice_openSea(String url) {
		try {

			HttpResponse<String> response = Unirest.get(url).header("Accept", "application/json").asString();

//			System.out.println("\nSending 'GET' request to URL : " + url);
//			System.out.println("Response Code : " + responseCode);

//			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//			Gson gson = new Gson();
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//
//			}
//			in.close();
//			LinkedTreeMap collection = gson.fromJson(response.toString(), LinkedTreeMap.class);
//			if (collection.containsKey("floor_price")) {
//				double floorPrice = (double) collection.get("floor_price");
//				return floorPrice;
//			} else {
//				return -1;
//			}
			if (response.getStatus() == 200) {

				Gson gson = new Gson();
				LinkedTreeMap collection = gson.fromJson(response.getBody().toString(), LinkedTreeMap.class);
				if (collection.containsKey("stats")) {
					LinkedTreeMap stats = gson.fromJson(collection.get("stats").toString(), LinkedTreeMap.class);
					if (stats.containsKey("floor_price")) {
						if (stats.get("floor_price") == null)
							return -1;
						double floorPrice = (double) stats.get("floor_price");
						return floorPrice * 32.895425;
					} else {
						return -4;
					}

				} else {
					return -2;
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -3;

	}

	private ArrayList<String> convertNameString(String symbol) {
		ArrayList<String> names = new ArrayList<>();
		symbol = symbol.toLowerCase();

		names.add(symbol.replace(' ', '-'));
		names.add(symbol.replace(' ', '_'));
		return names;
	}
}
