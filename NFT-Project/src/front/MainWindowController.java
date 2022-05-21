package front;

import java.util.ArrayList;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import back.Collection;
import back.NFTCollectionView;
import back.RunnerService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import utils.EmailSender;
import utils.ExchangeAdapter;
import utils.ExternalServices;

public class MainWindowController {
	/*
	 * @FXML private TableView<NFTCollectionView> collectionTableView;
	 * 
	 * @FXML private TableColumn<NFTCollectionView, String> collectionNameCol;
	 * 
	 * @FXML private TableColumn<NFTCollectionView, Float> openseaCol;
	 * 
	 * @FXML private TableColumn<NFTCollectionView, Float> magicEdenCol;
	 * 
	 * @FXML private TableColumn<NFTCollectionView, Float> diffCol;
	 * 
	 * @FXML private Label headerLabel;
	 * 
	 * @FXML private Button btnSaveRefreshTimer;
	 * 
	 * @FXML private Button btnSaveEmailTimer;
	 * 
	 * @FXML private Button btnSaveThreshold;
	 * 
	 * @FXML private Text txtRefreshTimer;
	 * 
	 * @FXML private Text txtEmailTimer;
	 * 
	 * @FXML private Text txtThreshold;
	 * 
	 * @FXML private Button btnSaveList;
	 * 
	 * @FXML private Button btnUploadList;
	 * 
	 * @FXML private TextField txtSearchBar;
	 * 
	 * @FXML private ComboBox<Integer> cmboxEntries;
	 * 
	 * @FXML private Button btnAddCollection;
	 * 
	 * ObservableList<NFTCollectionView> data = FXCollections.observableArrayList();
	 */
	@FXML
	private Button btnAddCollection;

	@FXML
	private Button btnSaveEmailTimer;

	@FXML
	private Button btnSaveList;

	@FXML
	private Button btnSaveRefreshTimer;

	@FXML
	private Button btnSaveThreshold;

	@FXML
	private Button btnUploadList;

	@FXML
	private ComboBox<Integer> cmboxEntries;

    @FXML
    private ComboBox<String> cmboxCurrenciesEntries;
    
	@FXML
	private Label headerLabel;

	@FXML
	private Pagination pagination;

	@FXML
	private TextField txtEmailTimer;

	@FXML
	private TextField txtRefreshTimer;

	@FXML
	private TextField txtSearchBar;

	@FXML
	private TextField txtThreshold;

	@FXML
	private TextField emails;
    @FXML
    private Button btnSaveEmail;
    @FXML
    private TextField txtAddCollection;
    @FXML
    private Label lblShowingCounter;

	private TableView<NFTCollectionView> collectionTableView = createTable();

	ObservableList<NFTCollectionView> data = FXCollections.observableArrayList();

	int rawPerPage = 10;
	int dataSize = 1000;
	RunnerService runner = null;
	Timer timer = new Timer();
	private final ExecutorService executorService = Executors.newFixedThreadPool(16);
	private final ExecutorService worker = Executors.newFixedThreadPool(1);
	private static String[] emailArray = new String[1];
	private EmailSender send;
	private boolean emailTheradFlag = false;
	private Thread filler = null;
	private int refreshTime;
	private ExternalServices external = ExternalServices.getInstace();
	private String CoinType ="SOL";
	private ExchangeAdapter exchangeAdapter = ExchangeAdapter.getInstance();
    @FXML
    void OnAddCollection(ActionEvent event) {
    	String collectionName = txtAddCollection.getText();
    	System.out.println("Event triggered value:" +collectionName);
    	if(collectionName==null ||collectionName.isEmpty() ) {
    		return;
    	}
    	
   
    	System.out.println("Before adding");
    //add values for collection 
    	//call for runner service to update
    	runner.addConstCollection(collectionName);
    	System.out.println("suppose to be added");
    	if(runner.safeConstCollection.containsKey(collectionName.toLowerCase().trim())){
    		System.out.println("Added");
    	}
    	
    	
    	
    	
    	
    }

	@FXML
	void OnRefreshBtnTimerClick(ActionEvent event) {
		refreshTime = Integer.parseInt(txtRefreshTimer.getText());
		System.out.println(refreshTime);
	

	}
	
	public void ChangeCurrency() {
		ObservableList<TableColumn<NFTCollectionView, ?>>  cols = collectionTableView.getColumns();
		TableColumn<NFTCollectionView, ?> openSeaCol = cols.get(1);
		TableColumn<NFTCollectionView, ?> magicEdenCol = cols.get(2);
		
		openSeaCol.setText("Opensea price["+CoinType+"]");
		magicEdenCol.setText("Magic eden price["+CoinType+"]");
		
	}
	
	public void setCurrencyComboBox() {
	
		cmboxCurrenciesEntries.getItems().addAll("SOL","ETH", "USD","EUR");
		cmboxCurrenciesEntries.setValue("SOL");
		cmboxCurrenciesEntries.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				CoinType=newValue;
				
				
				ChangeCurrency();
			}
			
		}

		);
	}
	/*
	 * private void initializeTable() { collectionNameCol.setCellValueFactory(new
	 * PropertyValueFactory<NFTCollectionView, String>("collection_name"));
	 * openseaCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView,
	 * Float>("opensea_price")); magicEdenCol.setCellValueFactory(new
	 * PropertyValueFactory<NFTCollectionView, Float>("magic_eden_price"));
	 * diffCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView,
	 * Float>("diff")); collectionTableView.setItems(data); }
	 */

	public void setComboBox() {
		cmboxEntries.getItems().addAll(10, 25, 50, 100);
		cmboxEntries.setValue(10);
		cmboxEntries.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				Update();

			}
		});
	}

	@SuppressWarnings("unchecked")
	private TableView<NFTCollectionView> createTable() {
		TableView<NFTCollectionView> table = new TableView<>();
		CoinType ="SOL";
		TableColumn<NFTCollectionView, String> collectionNameCol = new TableColumn<>("Collection name");
		collectionNameCol.setSortable(false);
		collectionNameCol.setCellValueFactory(new PropertyValueFactory<>("collection_name"));
		collectionNameCol.setPrefWidth(200);
		
		TableColumn<NFTCollectionView, String> openseaCol = new TableColumn<>("Opensea price["+CoinType+"]");
		openseaCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, String>("opensea_price"));
		openseaCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, String> magicEdenCol = new TableColumn<>("Magic eden price["+CoinType+"]");
		magicEdenCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, String>("magic_eden_price"));
		magicEdenCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, String> diffCol = new TableColumn<>("Diff[%]");
		diffCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, String>("diff"));
		diffCol.setPrefWidth(200);
		diffCol.setCellFactory(new Callback<TableColumn<NFTCollectionView,String>,
				TableCell<NFTCollectionView,String>>() {
			
			@Override
			public TableCell<NFTCollectionView, String> call(
					TableColumn<NFTCollectionView, String> param) {
				return new TableCell<NFTCollectionView, String>(){
					  @Override
	                   protected void updateItem(String item, boolean empty) {
						  if (!empty) {
							  int currentIndex = indexProperty()
	                                    .getValue() < 0 ? 0
	                                    : indexProperty().getValue();
							  if(item!=null) {
								  if(item=="-") {
									  setStyle(" -fx-text-fill: white");
								  }
								  else if(item.contains("+")&& item.contains(".")) {
									 
		                              setStyle(" -fx-text-fill: green");
								  }
								  else if(item.contains("-") && item.contains(".")) {
									 
									  setStyle(" -fx-text-fill: red");
								  }
								  else{
									  setStyle(" -fx-text-fill: white");
								  }
								  setText(item);
							  }
							 
						  }
					  }
				};
				
				// TODO Auto-generated method stub
				
			}
		});
		
		table.getColumns().addAll(collectionNameCol, openseaCol, magicEdenCol, diffCol);
		
		return table;
	}

	public void fillData() {
		this.rawPerPage = cmboxEntries.getValue();

		try {
			runner.run();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		/*
		 * Platform.runLater(() -> { List<String> collectionNames = new
		 * ArrayList<String>(runner.safeCollection.keySet());
		 * Collections.sort(collectionNames);
		 * 
		 * for (String collectionName : collectionNames) { Collection collection =
		 * runner.safeCollection.get(collectionName); data.add(new
		 * NFTCollectionView(collection.getName(), (float)
		 * collection.getFloorPriceOpenSea(), (float)
		 * collection.getFloorPriceMagicEden(), (float) collection.getDiff() * 100)); }
		 * }); System.out.println(pagination.getPageCount()); Platform.runLater(() ->
		 * pagination.setPageCount(collectionTableView.getItems().size() / rawPerPage));
		 * Platform.runLater(() -> System.out.println(pagination.getPageCount()));
		 * Platform.runLater(() -> pagination.setMaxPageIndicatorCount((data.size() /
		 * (rawPerPage))));
		 * 
		 */
//		for (int i = 0; i < 150; i++) {
//			data.add(new NFTCollectionView("temp", new Float(5.5), new Float(5.5), new Float(i)));
//			data.add(new NFTCollectionView("temp1", new Float(5.5), new Float(5.5), new Float(i + 1)));
//			data.add(new NFTCollectionView("temp2", new Float(5.5), new Float(5.5), new Float(i + 1)));
//			data.add(new NFTCollectionView("temp3", new Float(5.5), new Float(5.5), new Float(i + 1)));
//		}
//		Platform.runLater(() -> pagination.setPageFactory(this::createPage));
//		Platform.runLater(() -> System.out.println("done filling"));
		// initializeTable();
	}

	public void Update() {

		this.rawPerPage = cmboxEntries.getValue();
		
		data.clear();
		Platform.runLater(() -> {
			for (String collectionName : runner.safeConstCollection.keySet()) {
				Collection collection = runner.safeConstCollection.get(collectionName);
				String collection_name;
				String opensea_price;
				String magic_eden_price;
				String diff;
				collection_name = collection.getName();
				if (collection.getFloorPriceOpenSea() == 0)
					opensea_price = "N/A";
				else
					opensea_price = String.format("%.3f", exchangeAdapter.Convert( "SOL",CoinType,collection.getFloorPriceOpenSea()));
				if (collection.getFloorPriceMagicEden() == 0)
					magic_eden_price = "N/A";
				else
					magic_eden_price = String.format("%.3f", exchangeAdapter.Convert("SOL",CoinType,collection.getFloorPriceMagicEden()));
				if (collection.getFloorPriceOpenSea() == 0 || collection.getFloorPriceMagicEden() == 0)
					diff = "-";
				else if (collection.getDiff() > 0) {
					String temp = String.format("%.3f", collection.getDiff());
					diff = "+" + temp;
				} else {
					diff = String.format("%.3f", collection.getDiff());
				}
				data.add(new NFTCollectionView(collection_name, opensea_price, magic_eden_price, diff));
			}

		});
		Platform.runLater(() -> {
			for (String collectionName : runner.safeCollection.keySet()) {
				Collection collection = runner.safeCollection.get(collectionName);
				String collection_name;
				String opensea_price;
				String magic_eden_price;
				String diff;
				collection_name = collection.getName();
				if (collection.getFloorPriceOpenSea() == 0)
					opensea_price = "N/A";
				else
					opensea_price = String.format("%.3f", exchangeAdapter.Convert("SOL",CoinType ,collection.getFloorPriceOpenSea()));
				if (collection.getFloorPriceMagicEden() == 0)
					magic_eden_price = "N/A";
				else
					magic_eden_price = String.format("%.3f", exchangeAdapter.Convert("SOL", CoinType,collection.getFloorPriceMagicEden()));
				if (collection.getFloorPriceOpenSea() == 0 || collection.getFloorPriceMagicEden() == 0)
					diff = "-";
				else if (collection.getDiff() > 0) {
					String temp = String.format("%.3f", collection.getDiff());
					diff = "+" + temp;
				} else {
					diff = String.format("%.3f", collection.getDiff());
				}
				data.add(new NFTCollectionView(collection_name, opensea_price, magic_eden_price, diff));
			}

		});
		Platform.runLater(() -> pagination.setPageCount((int)Math.ceil(data.size() / ((double)rawPerPage))));
		Platform.runLater(() -> pagination.setMaxPageIndicatorCount((int)Math.ceil(data.size() / ((double)rawPerPage))));
//		for (int i = 0; i < 150; i++) {
//			data.add(new NFTCollectionView("temp", new Float(5.5), new Float(5.5), new Float(i)));
//			data.add(new NFTCollectionView("temp1", new Float(5.5), new Float(5.5), new Float(i + 1)));
//			data.add(new NFTCollectionView("temp2", new Float(5.5), new Float(5.5), new Float(i + 1)));
//			data.add(new NFTCollectionView("temp3", new Float(5.5), new Float(5.5), new Float(i + 1)));
//		}
		Platform.runLater(() -> pagination.setPageFactory(this::createPage));
		Platform.runLater(() -> System.out.println("done filling"));
		
		// initializeTable();
	}

	public void start_data_to_Table() {
		refreshTime = 60;
		pagination.autosize();
		pagination.setPageCount((int)Math.ceil(data.size() / ((double)rawPerPage)));
		pagination.setMaxPageIndicatorCount((int)Math.ceil(data.size() / ((double)rawPerPage)));

		this.rawPerPage = cmboxEntries.getValue();
		runner = new RunnerService();
		txtRefreshTimer.setText(60 + "");

		filler = new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					fillData();
					System.out.println("Start to update");
					Update();

					try {

						System.out.println("Referesh time is:" + refreshTime);
						Thread.sleep(refreshTime * 1000);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.err.println("Thread error");
					}

				}

			}
		});

		filler.start();

//		
//		worker.submit(new Runnable() {
//
//			@Override
//			public void run() {
//				int ammount = 0;
//				int currentammount = 0;
//
//				while (true) {
//					try {
//						Thread.sleep(10000);
//						Update();
//
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//					}
//
//				}
//
//			}
//		});
//		
//		
//		timer.scheduleAtFixedRate(new TimerTask() {
//			@Override
//			public void run() {
//				executorService.submit(new Runnable() {
//
//					@Override
//					public void run() {
//						fillData();
//					}
//				});
//			}
//		}, 0 * 1000, 240 * 1000);
//		data.clear();

//		for (int i = 0; i < 150; i++) {
//			data.add(new NFTCollectionView("temp", new Float(5.5), new Float(5.5), new Float(i)));
//			data.add(new NFTCollectionView("temp1", new Float(5.5), new Float(5.5), new Float(i + 1)));
//			data.add(new NFTCollectionView("temp2", new Float(5.5), new Float(5.5), new Float(i + 1)));
//			data.add(new NFTCollectionView("temp3", new Float(5.5), new Float(5.5), new Float(i + 1)));
//		}
		pagination.setPageFactory(this::createPage);
		// initializeTable();
	}

	private Node createPage(int pageIndex) {
		int fromIndex = pageIndex * rawPerPage;
		int toIndex = Math.min(fromIndex + rawPerPage, data.size());
		int fromIndexToShow;
		collectionTableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
		fromIndexToShow=toIndex==0? 0:(fromIndex+1);
		lblShowingCounter.setText("Showing "+ (fromIndexToShow) +" to "+ toIndex +" of "+data.size()+" entries");
		
		return collectionTableView;
	}

	@FXML
	void saveAsList(MouseEvent event) {
		external.listToExcel(new ArrayList<NFTCollectionView>(collectionTableView.getItems()));
	}

	@FXML
	void uploadList(MouseEvent event) {
		try {
			ArrayList<NFTCollectionView> temp = external.uploadList("collections", rawPerPage, 4);
			
			for (NFTCollectionView nftCollectionView : temp) {
				runner.addConstCollection(nftCollectionView.getCollection_name().toLowerCase());
			}
//			
//			ObservableList<NFTCollectionView> list = FXCollections
//					.observableList(external.uploadList("collections", rawPerPage, 4));
//
//			collectionTableView.setItems(list);
		} catch (Exception e) {
			System.out.println("Save a list and try again");
		}
	}

	@FXML
	void saveEmails(MouseEvent event) {
		if (emails.getText().length() != 0 && txtEmailTimer.getText().length() != 0
				&& txtThreshold.getText().length() != 0) {
			String text = emails.getText();
			if (text.contains(";"))
				emailArray = text.split(";");
			else
				emailArray[0] = text;

			ArrayList<NFTCollectionView> collections = new ArrayList<NFTCollectionView>(collectionTableView.getItems());
			float threshold = 0;
			int sleepTime = 60;
			try {
				threshold = Float.parseFloat(txtThreshold.getText());
				sleepTime = Integer.parseInt(txtEmailTimer.getText());
			} catch (NumberFormatException e) {
				System.out.println("please enter only numbers");
			}
			send = new EmailSender(emailArray, collections, threshold, sleepTime);
			Thread t;
			if (!emailTheradFlag) {
				emailTheradFlag = true;// doesn't need another thread
				t = new Thread(send);
				t.start();
			} else
				updateEmailFields();
		}
	}

	@FXML
	void updateEmailTimer(MouseEvent event) {
		if (emailTheradFlag)
			updateEmailFields();
	}

	void updateEmailFields() {
		float threshold = 0;
		int sleepTime = 60;
		ArrayList<NFTCollectionView> collections = new ArrayList<NFTCollectionView>(collectionTableView.getItems());
		try {
			threshold = Float.parseFloat(txtThreshold.getText());
			sleepTime = Integer.parseInt(txtEmailTimer.getText());
		} catch (NumberFormatException e) {
			System.out.println("please enter only numbers");
		}
		send.updateFields(emailArray, collections, threshold, sleepTime);
	}

	 public void SetImage (){
	        ImageView imageViewSave = new ImageView(getClass().getResource("../Icons/SavListIcon_1.png").toExternalForm());
	        btnSaveList.setGraphic(imageViewSave);
	        ImageView imageViewUpload = new ImageView(getClass().getResource("../Icons/up.png").toExternalForm());
	        btnUploadList.setGraphic(imageViewUpload);
	        ImageView imageViewSettings1 = new ImageView(getClass().getResource("../Icons/save.png").toExternalForm());
	        ImageView imageViewSettings2 = new ImageView(getClass().getResource("../Icons/save.png").toExternalForm());
	        ImageView imageViewSettings3 = new ImageView(getClass().getResource("../Icons/save.png").toExternalForm());
	        ImageView imageViewSettings4 = new ImageView(getClass().getResource("../Icons/save.png").toExternalForm());
	        btnSaveEmailTimer.setGraphic(imageViewSettings1);
	        btnSaveRefreshTimer.setGraphic(imageViewSettings2);
	        btnSaveThreshold.setGraphic(imageViewSettings3);
	        btnSaveEmail.setGraphic(imageViewSettings4);
	    }
	 


	    @FXML
	    void SearchTable(KeyEvent event) {
	    	String SearchText = txtSearchBar.getText().toLowerCase();
	    	//System.out.println(SearchText);
	    	
	    	
	    	TableColumn<NFTCollectionView, String> collectionNameCol = new TableColumn<>("Collection name");
			TableColumn<NFTCollectionView, Float> openseaCol = new TableColumn<>("Opensea price["+CoinType+"]");
			TableColumn<NFTCollectionView, Float> magicEdenCol = new TableColumn<>("Magic eden price["+CoinType+"]");
			TableColumn<NFTCollectionView, Float> diffCol = new TableColumn<>("Diff[%]");
			
			
			collectionNameCol.setCellValueFactory(new PropertyValueFactory<>("collection_name"));
			openseaCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("opensea_price"));
			magicEdenCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("magic_eden_price"));
			diffCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("diff"));
			
	    	FilteredList<NFTCollectionView> filteredData = new FilteredList<>(data, p -> true);
	    	txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
	    		
	    		filteredData.setPredicate(data -> {
	    		// If filter text is empty, display all data.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				if (data.getCollection_name().toLowerCase().contains(SearchText)) {
					return true; // Filter matches collection name.
				} 
				else if (data.getMagic_eden_price().toLowerCase().contains(SearchText)) {
					return true; // Filter matches magic eden price .
				}
	    		 else if (data.getOpensea_price().toLowerCase().contains(SearchText)) {
					return true; // Filter matches open sea price .
	    		 }
	    		 else if (data.getDiff().toLowerCase().contains(SearchText)) {
						return true; // Filter matches diff .
	    		 }
				return false; // Does not match.
			});
		});
	    	
	    	SortedList<NFTCollectionView> sortedData = new SortedList<>(filteredData);
			sortedData.comparatorProperty().bind(collectionTableView.comparatorProperty());
			
			collectionTableView.setItems(sortedData);
	    
	    }



	@FXML
	void updateThreshold(MouseEvent event) {
		if (emailTheradFlag)
			updateEmailFields();
	}

}

