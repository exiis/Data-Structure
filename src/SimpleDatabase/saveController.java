package SimpleDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class saveController extends CMDController {
	@FXML
	TextField tfName;
	@FXML
	Label lbState;
	
	public void save() {
		Main.mg.Save(tfName.getText());
		close();
	}
}
