package SimpleDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SortFieldController extends CMDController {
	@FXML
	Button btDone;
	@FXML
	TextField tfFieldName;
	@FXML
	Label lbPrompt;
	
	/*----------------------------
	 * Done 버튼이 눌려졌을 때 실행
	-----------------------------*/
	@FXML
	private void sortField() {
		try {
			field temp = Main.mg.fieldHead;
			int i;
			for(i=0; i<Main.mg.fieldNum; i++) {
				if(temp.name.compareTo(tfFieldName.getText()) == 0) {
					if(temp.redundancy == true) {
						lbPrompt.setText("중복가능한 필드는 불가능합니다.");
						return;
					}
					Main.mg.sortField = i;
					lbPrompt.setText("설정 완료");
					break;
				}
				if(temp.next != null) temp = temp.next;
			}
			if(i == Main.mg.fieldNum)
				lbPrompt.setText("필드를 찾을 수 없습니다.");
		} catch(NumberFormatException e) {
			
		}
	}
}
