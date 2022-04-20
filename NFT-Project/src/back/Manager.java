package back;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;

public class Manager {

	// FXML controllers

	@FXML
	private TableView<?> collectionTableView;

	@FXML
	private TableColumn<?, ?> collectionNameCol;

	@FXML
	private TableColumn<?, ?> openseaCol;

	@FXML
	private TableColumn<?, ?> magicEdenCol;

	@FXML
	private TableColumn<?, ?> diffCol;

	@FXML
	private Label headerLabel;

	@FXML
	private Button btnSaveRefreshTimer;

	@FXML
	private Button btnSaveEmailTimer;

	@FXML
	private Button btnSaveThreshold;

	@FXML
	private Button btnSaveList;

	@FXML
	private Button btnUploadList;

	@FXML
	private TextField txtSearchBar;

	@FXML
	private ComboBox<?> cmboxEntries;

	@FXML
	private Button btnAddCollection;

	@FXML
	private TextField txtRefreshTimer;

	@FXML
	private TextField txtEmailTimer;

	@FXML
	private TextField txtThreshold;

	private String urlMagicEden500 = "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=500";
	private String urlMagicEden1000 = "https://api-mainnet.magiceden.dev/v2/collections?offset=500&limit=500";
	private String urlOpenSea300 = "https://api.opensea.io/api/v1/collections?offset=0&limit=300";

	public static void main(String[] args) throws IOException {

		String url = "https://api-mainnet.magiceden.dev/v2/collections?offset=0&limit=500";

		String json = getJson(url);

		FileWriter file = new FileWriter("top1000.json");
		file.write(json);
		file.close();

	}

	public static String getJson(String url) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

		return response.toString();

	}
}
