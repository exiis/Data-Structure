package SimpleDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class deleteController extends CMDController {
	@FXML
	private Button btDelete; 
	@FXML
	private Button btCancel;
	

	/* ���� recode�� �����ϰ� �˾�â�� �ݴ´� */
	public void delete() {
		leafNode curLeaf = (leafNode) Main.mg.curNode;
		curLeaf.deleteData(curLeaf.dataArr[Main.mg.curIndex].getData(0), Main.mg.sortField);
		Main.mg.recOut();
		close();
	}

	
}
