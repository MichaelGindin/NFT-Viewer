package back;

import java.util.ArrayList;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
	private Label headerLabel;

	@FXML
	private Pagination pagination;

	@FXML
	private TextField  txtEmailTimer;

	@FXML
	private TextField  txtRefreshTimer;

	@FXML
	private TextField txtSearchBar;

	@FXML
	private TextField  txtThreshold;

	@FXML
	private TextField emails;

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
	private Thread filler= null;
	private int refreshTime;
	
	@FXML
	void OnRefreshBtnTimerClick(ActionEvent event) {
		refreshTime = Integer.parseInt(txtRefreshTimer.getText());
		System.out.println(refreshTime);

//		timer.purge();
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
//				try {
//					executorService.awaitTermination(1000, null);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}, 120 * 1000, refreshTime * 1000);

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

		TableColumn<NFTCollectionView, String> collectionNameCol = new TableColumn<>("Collection name");
		collectionNameCol.setCellValueFactory(new PropertyValueFactory<>("collection_name"));
		collectionNameCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, Float> openseaCol = new TableColumn<>("Opensea price[SOL]");
		openseaCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("opensea_price"));
		openseaCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, Float> magicEdenCol = new TableColumn<>("Magic eden price[SOL]");
		magicEdenCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("magic_eden_price"));
		magicEdenCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, Float> diffCol = new TableColumn<>("Diff[%]");
		diffCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("diff"));
		diffCol.setPrefWidth(200);
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
		Platform.runLater(() -> {
			List<String> collectionNames = new ArrayList<String>(runner.safeCollection.keySet());
			Collections.sort(collectionNames);

			for (String collectionName : collectionNames) {
				Collection collection = runner.safeCollection.get(collectionName);
				data.add(new NFTCollectionView(collection.getName(), (float) collection.getFloorPriceOpenSea(),
						(float) collection.getFloorPriceMagicEden(), (float) collection.getDiff() * 100));
			}
		});
		System.out.println(pagination.getPageCount());
		Platform.runLater(() -> pagination.setPageCount(collectionTableView.getItems().size() / rawPerPage));
		Platform.runLater(() -> System.out.println(pagination.getPageCount()));
		Platform.runLater(() -> pagination.setMaxPageIndicatorCount((data.size() / (rawPerPage))));
		
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
			for (String collectionName : runner.safeCollection.keySet()) {
				Collection collection = runner.safeCollection.get(collectionName);


				data.add(new NFTCollectionView(collection.getName(), (float) collection.getFloorPriceOpenSea(),
						(float) collection.getFloorPriceMagicEden(), (float) collection.getDiff() * 100));
			}

		});
		Platform.runLater(() -> pagination.setPageCount((data.size() / (rawPerPage))));
		Platform.runLater(() -> pagination.setMaxPageIndicatorCount((data.size() / (rawPerPage))));
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
		refreshTime= 60;
		pagination.autosize();
		pagination.setPageCount((data.size() / (rawPerPage)));
		pagination.setMaxPageIndicatorCount((data.size() / (rawPerPage)));
		this.rawPerPage = cmboxEntries.getValue();
		runner = new RunnerService();
		txtRefreshTimer.setText(60 + "");
		
		filler= new Thread(new Runnable() {
			
			@Override
			public void run() {

				while(true) {
					fillData();
					System.out.println("Start to update");
					Update();
					
					try {

						System.out.println("Referesh time is:"+refreshTime);
						Thread.sleep(refreshTime*1000);
						

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
		collectionTableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
		return collectionTableView;
	}

	@FXML
	void saveAsList(MouseEvent event) {
		ExternalServices.listToExcel(new ArrayList<NFTCollectionView>(collectionTableView.getItems()));
	}

	@FXML
	void uploadList(MouseEvent event) {
		try {
			ObservableList<NFTCollectionView> list = FXCollections
					.observableList(ExternalServices.uploadList("collections", rawPerPage, 4));

			collectionTableView.setItems(list);
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("There is not any list in this name");
		}
	}

	@FXML
	void saveEmails(MouseEvent event) {
		if (emails.getText().length() != 0&& txtEmailTimer.getText().length() != 0&&txtThreshold.getText().length() != 0) {
			String text = emails.getText();
			if (text.contains(";"))
				emailArray = text.split(";");
			else
				emailArray[0] = text;

			ArrayList<NFTCollectionView> collections = new ArrayList<NFTCollectionView>(collectionTableView.getItems());
			int threshold = Integer.parseInt(txtThreshold.getText());
			int sleepTime = Integer.parseInt(txtEmailTimer.getText());
			send = new EmailSender(emailArray, collections, threshold, sleepTime);
			Thread t;
			if (!emailTheradFlag) {
				emailTheradFlag = true;// doesn't need another thread
				t = new Thread(send);
				t.start();
			}
			else
				updateEmailFields();
		}
	}
	
	@FXML
    void updateEmailTimer(MouseEvent event) {
		if(emailTheradFlag)
			updateEmailFields();
    }
	
	void updateEmailFields() {
		ArrayList<NFTCollectionView> collections = new ArrayList<NFTCollectionView>(collectionTableView.getItems());
		int threshold = Integer.parseInt(txtThreshold.getText());
		int sleepTime = Integer.parseInt(txtEmailTimer.getText());
		send.updateFields(emailArray, collections, threshold, sleepTime);
	}
	
	 @FXML
	    void updateThreshold(MouseEvent event) {
		 if(emailTheradFlag)
				updateEmailFields();
	    }
}
