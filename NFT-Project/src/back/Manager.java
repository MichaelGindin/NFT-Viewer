package back;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import front.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Manager extends Application {

	public static void main(String[] args) {

		launch(args);

	}

	static String getJson(String url) throws IOException {

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
		Gson gson = new Gson();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			LinkedTreeMap[] collections = gson.fromJson(inputLine, LinkedTreeMap[].class);

		}
		in.close();

		// print result
		// System.out.println(response.toString());

		return response.toString();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainWindowController.class.getResource("MainWindow.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("NFT-Viewer");
		primaryStage.setScene(sc);

		primaryStage.setMinWidth(1000);
		primaryStage.setMinHeight(700);
		primaryStage.show();
		primaryStage.setResizable(false);
		MainWindowController mainWindowController = loader.getController();
		mainWindowController.setComboBox();
		mainWindowController.setCurrencyComboBox();
		mainWindowController.start_data_to_Table();
		mainWindowController.SetImage();

	}
}


