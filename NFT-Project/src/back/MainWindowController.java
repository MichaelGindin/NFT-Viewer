package back;

import java.time.LocalDate;

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

	@FXML
	void OnRefreshBtnTimerClick(ActionEvent event) {
		int refreshTime = Integer.parseInt(txtRefreshTimer.getText());
		System.out.println(refreshTime);

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
				start_data_to_Table();

			}
		});
	}

	@SuppressWarnings("unchecked")
	private TableView<NFTCollectionView> createTable() {
		TableView<NFTCollectionView> table = new TableView<>();

		TableColumn<NFTCollectionView, String> collectionNameCol = new TableColumn<>("collection_name");
		collectionNameCol.setCellValueFactory(new PropertyValueFactory<>("collection_name"));
		collectionNameCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, Float> openseaCol = new TableColumn<>("opensea_price");
		openseaCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("opensea_price"));
		openseaCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, Float> magicEdenCol = new TableColumn<>("magic_eden_price");
		magicEdenCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("magic_eden_price"));
		magicEdenCol.setPrefWidth(200);

		TableColumn<NFTCollectionView, Float> diffCol = new TableColumn<>("diff");
		diffCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("diff"));
		diffCol.setPrefWidth(200);
		table.getColumns().addAll(collectionNameCol, openseaCol, magicEdenCol, diffCol);
		return table;
	}

	public void start_data_to_Table() {
		this.rawPerPage = cmboxEntries.getValue();

		data.clear();
		for (int i = 0; i < 150; i++) {
			data.add(new NFTCollectionView("temp", new Float(5.5), new Float(5.5), new Float(i)));
			data.add(new NFTCollectionView("temp1", new Float(5.5), new Float(5.5), new Float(i + 1)));
			data.add(new NFTCollectionView("temp2", new Float(5.5), new Float(5.5), new Float(i + 1)));
			data.add(new NFTCollectionView("temp3", new Float(5.5), new Float(5.5), new Float(i + 1)));
		}
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
