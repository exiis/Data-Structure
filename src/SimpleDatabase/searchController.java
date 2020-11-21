package SimpleDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class searchController extends CMDController {
	@FXML
	private TextField tfData;
	@FXML
	private Button btDone;
	@FXML
	private Label lbState;
	
	ObservableList<String> list = FXCollections.observableArrayList();

	
	
	/* target data를 찾기위하여 생성한 temp */
	node temp = Main.mg.getRoot();
	
	/*----------------------------------
	 * 
	 -----------------------------------*/
	@FXML
	private void search() {
		String data = tfData.getText();
		leafNode leaf = (leafNode) temp.findLeaf(data);
		for(int i=0; i<Main.mg.fieldNum; i++) {
			if(leaf.dataArr[i].getData(Main.mg.getSortField()).compareTo(data)==0){
				Main.mg.curIndex = i;
				Main.mg.curNode = leaf;
			}
		}
		close();
	}
}
