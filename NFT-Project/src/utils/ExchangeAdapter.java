package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;



public class ExchangeAdapter {

	// currencies to usd
	HashMap<String, Double> currencies = new HashMap<String, Double>();

	public ExchangeAdapter() {
		readCryptoCurrenciesFromJson();
		readRegularCurrenciesFromJson();

		
	}
	public void fetchCryptoCurrenciesToJson() {
		try {	
			
		
		String url = "https://rest.coinapi.io/v1/exchangerate/USD?invert=false";
		HttpResponse<String> response = Unirest.get(url)
				.header("X-CoinAPI-Key", "DDD291C5-ECAA-49E1-BFC7-4FDC125BD914")
				.header("Accept", "application/json").asString();
		System.out.println(response.getBody());
		FileWriter file = new FileWriter("CryptoCurrencies.json");
		file.write(response.getBody()); 
		file.close();
		
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void readCryptoCurrenciesFromJson() {
		// read currencies.json file and put it in currencies hashmap
		Gson gson = new Gson();
		
		try {
			FileReader reader = new FileReader("CryptoCurrencies.json");
			
			LinkedHashMap CurrencyJson = gson.fromJson(reader, LinkedHashMap.class);
			
		
			
			JsonElement ratesElem = gson.toJsonTree(CurrencyJson.get("rates"));
			
			JsonArray rates = ratesElem.getAsJsonArray();
			
			ArrayList<String> asset_id_quote =new ArrayList<String>();
			asset_id_quote.add("BTC");
			asset_id_quote.add("ETH");
			asset_id_quote.add("SOL");
			
			rates.forEach(elem -> {
				LinkedTreeMap rate = gson.fromJson(elem, LinkedTreeMap.class);
				String name = (String) rate.get("asset_id_quote");
				if(asset_id_quote.contains(name)) {
					double value = (double) rate.get("rate");
					currencies.put(name.toLowerCase(), value);
				}
			
			});
//			System.out.println(rates.toString());
			
			
			reader.close();	
			
			
		}catch(FileNotFoundException e) {
			  e.printStackTrace();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
//		System.out.println(currencies.toString());
		

	}
	
	public void fetchRegularCurrenciesToJson() {
		
			
		try {	
			
			
			String url = "https://api.apilayer.com/exchangerates_data/latest?base=usd";
			HttpResponse<String> response = Unirest.get(url)
					.header("access_key", "fh3Y9laC3Q38pVRoKrD6JJJACf8tr1Uc")
					.header("apikey","fh3Y9laC3Q38pVRoKrD6JJJACf8tr1Uc" )
					.header("Accept", "application/json").asString();
			

			System.out.println(response.toString());
			System.out.println("\n\n\n");
			
			System.out.println(response.getBody());
			FileWriter file = new FileWriter("Currencies.json");
			file.write(response.getBody()); 
			file.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//finished
		System.out.println("Done getting Currencies");
		
	}

//	private void initCurrencies() {
//		
//		currencies.put("eth", 2002.21);
//		currencies.put("sol", 54.58);
//	}

	public void readRegularCurrenciesFromJson() {
		// read currencies.json file and put it in currencies hashmap
		Gson gson = new Gson();
		
		try {
			FileReader reader = new FileReader("Currencies.json");
			
			LinkedTreeMap CurrencyJson = gson.fromJson(reader, LinkedTreeMap.class);
			
			
			LinkedTreeMap ratesObject = (LinkedTreeMap) CurrencyJson.get("rates");
			
			ArrayList<String> rates = new ArrayList(ratesObject.keySet());
			reader.close();	
			
			for (String rateName : rates) {
				String name = rateName.toLowerCase();
				currencies.put(name, (Double) ratesObject.get(rateName));
			}
			
		}catch(FileNotFoundException e) {
			  e.printStackTrace();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		

	}
	
	
	public Double Convert(String from, String to, double amount) {
		from = from.toLowerCase();
		to = to.toLowerCase();
		if (!currencies.containsKey(from) || !currencies.containsKey(to)) {
			return null;
		}

		double fromRate = currencies.get(from);
		double toRate = currencies.get(to);

		return (amount / fromRate ) * toRate;

	}

}
