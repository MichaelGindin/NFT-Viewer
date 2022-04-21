package back;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

	@FXML
	void OnRefreshBtnTimerClick(ActionEvent event) {
		int refreshTime = Integer.parseInt(txtRefreshTimer.getText());
		System.out.println(refreshTime);
	}

}
