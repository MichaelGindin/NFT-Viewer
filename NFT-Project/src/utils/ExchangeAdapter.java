package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.aspose.cells.DateTime;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class ExchangeAdapter {

	// currencies to usd
	static HashMap<String, Double> currencies = new HashMap<String, Double>();
	private static final ExchangeAdapter INSTANCE = new ExchangeAdapter();
	
	public static ExchangeAdapter getInstance() {
		return INSTANCE;
	}
	
	private ExchangeAdapter() {
		updateCurrencies();

	}
	private void updateCurrencies() {
		readCryptoCurrenciesFromJson();
		readRegularCurrenciesFromJson();
	}

	private void fetchCryptoCurrenciesToJson() {
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

	private void readCryptoCurrenciesFromJson() {
		if(isRequestForCryptoCurrenciesNeeded()) {
			fetchCryptoCurrenciesToJson();
		}
		
		// read currencies.json file and put it in currencies hashmap
		Gson gson = new Gson();

		try {
			FileReader reader = new FileReader("CryptoCurrencies.json");

			LinkedHashMap CurrencyJson = gson.fromJson(reader, LinkedHashMap.class);

			JsonElement ratesElem = gson.toJsonTree(CurrencyJson.get("rates"));

			JsonArray rates = ratesElem.getAsJsonArray();

			ArrayList<String> asset_id_quote = new ArrayList<String>();
			asset_id_quote.add("BTC");
			asset_id_quote.add("ETH");
			asset_id_quote.add("SOL");

			rates.forEach(elem -> {
				LinkedTreeMap rate = gson.fromJson(elem, LinkedTreeMap.class);
				String name = (String) rate.get("asset_id_quote");
				if (asset_id_quote.contains(name)) {
					double value = (double) rate.get("rate");
					currencies.put(name.toLowerCase(), value);
				}

			});
//			System.out.println(rates.toString());

			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

//		System.out.println(currencies.toString());

	}

	private boolean isRequestForCryptoCurrenciesNeeded() {

		boolean exist = new File("CryptoCurrencies.json").exists();
		if (!exist)
			return true;
		Gson gson = new Gson();

		try {
			FileReader reader = new FileReader("CryptoCurrencies.json");

			LinkedHashMap CurrencyJson = gson.fromJson(reader, LinkedHashMap.class);

			reader.close();
			JsonElement ratesElem = gson.toJsonTree(CurrencyJson.get("rates"));

			JsonArray rates = ratesElem.getAsJsonArray();
			LinkedTreeMap rate = gson.fromJson(rates.get(0), LinkedTreeMap.class);
			String timeString = (String) rate.get("time");
//			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			String date = (timeString.split("T"))[0];
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp currencyTimestamp = new Timestamp((dateFormat.parse(date)).getTime());

			Timestamp currentTimestamp = new Timestamp((dateFormat.parse(LocalDate.now().toString())).getTime());

			System.out.println(currentTimestamp.getTime() - currencyTimestamp.getTime());
			System.out.println(TimeUnit.DAYS.toMillis(1));

			if (currentTimestamp.getTime() - currencyTimestamp.getTime() < TimeUnit.DAYS.toMillis(1)) {
				return false;
			}
			return true;


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return true;

	}

	private void fetchRegularCurrenciesToJson() {

		try {

			String url = "https://api.apilayer.com/exchangerates_data/latest?base=usd";
			HttpResponse<String> response = Unirest.get(url).header("access_key", "fh3Y9laC3Q38pVRoKrD6JJJACf8tr1Uc")
					.header("apikey", "fh3Y9laC3Q38pVRoKrD6JJJACf8tr1Uc").header("Accept", "application/json")
					.asString();

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

		// finished
		System.out.println("Done getting Currencies");

	}

	private void readRegularCurrenciesFromJson() {
		if(isRequestForRegularCurrenciesNeeded()) {
			fetchRegularCurrenciesToJson();
		}
		
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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private boolean isRequestForRegularCurrenciesNeeded() {

		boolean exist = new File("Currencies.json").exists();
		if (!exist)
			return true;

		// read currencies.json file and put it in currencies hashmap
		Gson gson = new Gson();

		try {
			FileReader reader = new FileReader("Currencies.json");

			LinkedTreeMap CurrencyJson = gson.fromJson(reader, LinkedTreeMap.class);

			String DateString = (String) CurrencyJson.get("date");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp currencyTimestamp = new Timestamp((dateFormat.parse(DateString)).getTime());

			Timestamp currentTimestamp = new Timestamp((dateFormat.parse(LocalDate.now().toString())).getTime());

			System.out.println(currentTimestamp.getTime() - currencyTimestamp.getTime());
			System.out.println(TimeUnit.DAYS.toMillis(1));

			if (currentTimestamp.getTime() - currencyTimestamp.getTime() < TimeUnit.DAYS.toMillis(1)) {
				return false;
			}
			return true;

		} catch (FileNotFoundException e) {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	public Double Convert(String from, String to, double amount) {
		from = from.toLowerCase();
		to = to.toLowerCase();
		if (!currencies.containsKey(from) || !currencies.containsKey(to)) {
			return null;
		}

		double fromRate = currencies.get(from);
		double toRate = currencies.get(to);

		return (amount / fromRate) * toRate;

	}

}
