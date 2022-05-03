package back;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class MainWindowController {

	@FXML
	private TableView<NFTCollectionView> collectionTableView;

	@FXML
	private TableColumn<NFTCollectionView, String> collectionNameCol;

	@FXML
	private TableColumn<NFTCollectionView, Float> openseaCol;

	@FXML
	private TableColumn<NFTCollectionView, Float> magicEdenCol;

	@FXML
	private TableColumn<NFTCollectionView, Float> diffCol;

	@FXML
	private Label headerLabel;

	@FXML
	private Button btnSaveRefreshTimer;

	@FXML
	private Button btnSaveEmailTimer;

	@FXML
	private Button btnSaveThreshold;

	@FXML
	private Text txtRefreshTimer;

	@FXML
	private Text txtEmailTimer;

	@FXML
	private Text txtThreshold;

	@FXML
	private Button btnSaveList;

	@FXML
	private Button btnUploadList;

	@FXML
	private TextField txtSearchBar;

	@FXML
	private ComboBox<Integer> cmboxEntries;

	@FXML
	private Button btnAddCollection;

	ObservableList<NFTCollectionView> data = FXCollections.observableArrayList();

	@FXML
	void OnRefreshBtnTimerClick(ActionEvent event) {
		int refreshTime = Integer.parseInt(txtRefreshTimer.getText());
		System.out.println(refreshTime);

		data.clear();
		for (int i = 0; i < 150; i++) {

			data.add(new NFTCollectionView("temp", new Float(5.5), new Float(5.5), new Float(i)));
			data.add(new NFTCollectionView("temp1", new Float(5.5), new Float(5.5), new Float(i + 1)));
			data.add(new NFTCollectionView("temp2", new Float(5.5), new Float(5.5), new Float(i + 1)));
			data.add(new NFTCollectionView("temp3", new Float(5.5), new Float(5.5), new Float(i + 1)));
		}
		initializeTable();
	}

	private void initializeTable() {
		collectionNameCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, String>("collection_name"));
		openseaCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("opensea_price"));
		magicEdenCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("magic_eden_price"));
		diffCol.setCellValueFactory(new PropertyValueFactory<NFTCollectionView, Float>("diff"));
		collectionTableView.setItems(data);
	}

	public void setComboBox() {
		cmboxEntries.getItems().addAll(10, 50, 100);
		cmboxEntries.setValue(10);
	}

}
