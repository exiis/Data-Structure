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
	 * Done ��ư�� �������� �� ����
	-----------------------------*/
	@FXML
	private void sortField() {
		try {
			field temp = Main.mg.fieldHead;
			int i;
			for(i=0; i<Main.mg.fieldNum; i++) {
				if(temp.name.compareTo(tfFieldName.getText()) == 0) {
					if(temp.redundancy == true) {
						lbPrompt.setText("�ߺ������� �ʵ�� �Ұ����մϴ�.");
						return;
					}
					Main.mg.sortField = i;
					lbPrompt.setText("���� �Ϸ�");
					break;
				}
				if(temp.next != null) temp = temp.next;
			}
			if(i == Main.mg.fieldNum)
				lbPrompt.setText("�ʵ带 ã�� �� �����ϴ�.");
		} catch(NumberFormatException e) {
			
		}
	}
}
