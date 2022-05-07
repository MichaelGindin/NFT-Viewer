package back;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.text.Text;
import javafx.util.Callback;
import sun.font.CreatedFontTracker;

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
	private Text txtEmailTimer;

	@FXML
	private Text txtRefreshTimer;

	@FXML
	private TextField txtSearchBar;

	@FXML
	private Text txtThreshold;

	private TableView<NFTCollectionView> collectionTableView = createTable();

	ObservableList<NFTCollectionView> data = FXCollections.observableArrayList();

	int rawPerPage = 10;
	int dataSize = 1000;
	RunnerService runner = null;
	Timer timer = new Timer();
	private final ExecutorService executorService = Executors.newFixedThreadPool(16);
	private final ExecutorService worker = Executors.newFixedThreadPool(1);
	@FXML
	void OnRefreshBtnTimerClick(ActionEvent event) {
		int refreshTime = Integer.parseInt(txtRefreshTimer.getText());
		System.out.println(refreshTime);
		timer.cancel();
		timer.purge();

		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				executorService.submit(new Runnable() {

					@Override
					public void run() {
						fillData();
					}
				});
			}
		}, 120 * 1000, refreshTime * 1000);
		
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

			runner.wait();
			data.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}

		Platform.runLater(() -> {
			for (String collectionName : runner.safeCollection.keySet()) {
				Collection collection = runner.safeCollection.get(collectionName);
				data.add(new NFTCollectionView(collection.getName(), (float) collection.getFloorPriceOpenSea(),
						(float) collection.getFloorPriceMagicEden(), (float) collection.getDiff()));
			}
		});

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
	public void Update() {
		this.rawPerPage = cmboxEntries.getValue();
		data.clear();
		Platform.runLater(() -> {
			for (String collectionName : runner.safeCollection.keySet()) {
				Collection collection = runner.safeCollection.get(collectionName);
				data.add(new NFTCollectionView(collection.getName(), (float) collection.getFloorPriceOpenSea(),
						(float) collection.getFloorPriceMagicEden(), (float) collection.getDiff()));
			}
			
		});

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
		this.rawPerPage = cmboxEntries.getValue();
		runner = new RunnerService();
		txtRefreshTimer.setText(120 + "");
		worker.submit(new Runnable() {
			
			@Override
			public void run() {
				int ammount = 0 ;
				int currentammount = 0;
				
				while(true) {
					try {
						Thread.sleep(10000);
						Update();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
					
				}
				
			}
		});
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				executorService.submit(new Runnable() {

					@Override
					public void run() {
						fillData();
					}
				});
			}
		}, 0 * 1000, 240 * 1000);
		data.clear();

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

}
