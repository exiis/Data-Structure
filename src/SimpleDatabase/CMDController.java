package SimpleDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/*-----------CMDController Ŭ����-----------------
 * �˾� â���� �̷������ Ŀ�ǵ���� ���� ��ɾ��� �̷���� Ŭ����
 -----------------------------------------------*/

public class CMDController {
	@FXML
	private Button btCancel;
	
	Stage cmdPop;
	

	public void close() {
		cmdPop = (Stage)btCancel.getScene().getWindow();
		cmdPop.close();
	}
}
