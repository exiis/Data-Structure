package SimpleDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class loadController extends CMDController {
	@FXML
	TextField tfName;
	@FXML
	Label lbState;
	
	public void load() {
		Main.mg.Load(tfName.getText());
		close();
	}
}
