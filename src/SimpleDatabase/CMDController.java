package SimpleDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/*-----------CMDController 클래스-----------------
 * 팝업 창에서 이루어지는 커맨드들의 공통 명령어들로 이루어진 클래스
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
