package SimpleDatabase;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	/* -------------
	 * 관리 클래스 정의
	 --------------*/
	public static management mg = new management();
	public void start(Stage primaryStage) throws IOException{
		/*----------------------------------
		 * FXML Loader를 이용하여 GUI를 불러온다.
		 ----------------------------------*/
		Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("SimpleDB");
		primaryStage.show();
	}

	public static void main(String[] args) {
		mg.init();
		launch(args);
	}
}
