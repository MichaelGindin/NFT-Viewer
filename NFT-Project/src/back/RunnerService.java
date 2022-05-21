package back;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnerService implements Runnable {
	public ArrayList<Collection> Collections = null;
	public ConcurrentHashMap<String, Collection> safeCollection = new ConcurrentHashMap<String, Collection>();
	public ConcurrentHashMap<String, Collection> safeConstCollection = new ConcurrentHashMap<String, Collection>();
	public Set<String> constList = new HashSet<String>();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		StartFillingConstantData();
		StartFillingData();
	}
	
	public void addConstCollection(String name) {
		constList.add(name.toLowerCase().trim());
		
	}
	public void StartFillingConstantData() {
		if(constList.isEmpty())return;
		safeConstCollection = new ConcurrentHashMap<String, Collection>();
		MagicEdenManager magicEdenManager = new MagicEdenManager();
		OpenSeaManager openSeaManager = new OpenSeaManager();

		
		ArrayList<String> names = new ArrayList<String>(constList);
	
	

		Thread[] workers = new Thread[names.size()];

		for (int i = 0; i < names.size(); i++) {

			int index = i;
			workers[index]= new Thread(new Runnable() {

				@Override
				public void run() {
					String name = names.get(index);
					String symbol = name.trim().replace(' ','_').toLowerCase();
					
				

					Double magicPrice = magicEdenManager.getPriceMagicEden(symbol);
					Double openSeaPrice = openSeaManager.getPriceOpenSea(name);
					
					if (magicPrice == null && openSeaPrice == null) {
						return;
					}
					
					Collection newCollection =null;
					if(magicPrice==null) {
						newCollection = new Collection(symbol, name, 0, openSeaPrice);
					}
					else if(openSeaPrice==null) {
						newCollection = new Collection(symbol, name, magicPrice,0);
					}
					else {
						newCollection = new Collection(symbol, name, magicPrice, openSeaPrice);
//						safeCollection.put(name, newCollection);
					}
					safeConstCollection.put(name, newCollection);

				}
			});
			workers[index].start();
				
	}
	}
	
	
	public void StartFillingData() {
		safeCollection = new ConcurrentHashMap<String, Collection>();
		MagicEdenManager magicEdenManager = new MagicEdenManager();
		OpenSeaManager openSeaManager = new OpenSeaManager();

		// set symbol-name
		magicEdenManager.setTopCollectionsNamesSymbols();

		ArrayList<String> symbols = new ArrayList<>(magicEdenManager.SymbolNameMap.keySet());

		// we will create threads[#number of symbols]
		// each thread will update ConcurrentHashMap<String, String>() after calling
		// both api requests;
		ExecutorService es = Executors.newFixedThreadPool(symbols.size());
		Thread[] workers = new Thread[symbols.size()];

		for (int i = 0; i < symbols.size(); i++) {

			int index = i;
			workers[index]= new Thread(new Runnable() {

				@Override
				public void run() {
					String symbol = symbols.get(index);
					String name = magicEdenManager.SymbolNameMap.get(symbols.get(index));

					Double magicPrice = magicEdenManager.getPriceMagicEden(symbol);
					Double openSeaPrice = openSeaManager.getPriceOpenSea(name);
					
					if (magicPrice == null && openSeaPrice == null) {
						return;
					}
					
					Collection newCollection =null;
					if(magicPrice==null) {
						newCollection = new Collection(symbol, name, 0, openSeaPrice);
					}
					else if(openSeaPrice==null) {
						newCollection = new Collection(symbol, name, magicPrice,0);
					}
					else {
						newCollection = new Collection(symbol, name, magicPrice, openSeaPrice);
//						safeCollection.put(name, newCollection);
					}
					safeCollection.put(name, newCollection);

				}
			});
			workers[index].start();
			
			
//			es.execute(new Runnable() {
//
//				@Override
//				public void run() {
//					String symbol = symbols.get(index);
//					String name = magicEdenManager.SymbolNameMap.get(symbols.get(index));
//
//					Double magicPrice = magicEdenManager.getPriceMagicEden(symbol);
//					Double openSeaPrice = openSeaManager.getPriceOpenSea(name);
//					
//					if (magicPrice == null && openSeaPrice == null) {
//						return;
//					}
//					
//					Collection newCollection =null;
//					if(magicPrice==null) {
//						newCollection = new Collection(symbol, name, 0, openSeaPrice);
//					}
//					else if(openSeaPrice==null) {
//						newCollection = new Collection(symbol, name, magicPrice,0);
//					}
//					else {
//						newCollection = new Collection(symbol, name, magicPrice, openSeaPrice);
//						
//					}
//					safeCollection.put(name, newCollection);
//
//				}
//			});
			
			
		}
		for (Thread thread : workers) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println("join error");
			}
		}
		
		
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
//		Collections = CombineMapsWithDict(dict, magicMap, openSeaMap);
//
//		PrintCollections(Collections);
//
//		System.out.println("Finished");
	}

	// join 2 collections
	private static ArrayList<Collection> CombineMapsWithDict(HashMap<String, String> dict,
			HashMap<String, Double> magicmap, HashMap<String, Double> openSeaMap) {
		ArrayList<Collection> joinedCollections = new ArrayList<Collection>();

		for (String symbol : magicmap.keySet()) {
			String name = dict.get(symbol);
			if (openSeaMap.containsKey(name)) {
				double floorPriceMagic = magicmap.get(symbol);
				double floorPriceOpenSea = openSeaMap.get(name);
				joinedCollections.add(new Collection(symbol, name, floorPriceMagic, floorPriceOpenSea));
			}
		}

		return joinedCollections;
	}

	// helper print method
	private static void PrintCollections(ArrayList<Collection> collections) {
		for (Collection collection : collections) {
			System.out.println(collection.getName() + ": magic:" + collection.getFloorPriceMagicEden() + " open:"
					+ collection.getFloorPriceOpenSea() + " Diffrence:" + collection.getDiff() * 100 + "%");
		}
	}

}
