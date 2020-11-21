package SimpleDatabase;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class addFieldController extends CMDController implements Initializable{
	@FXML
	private ComboBox<String> combobox;
	@FXML
	private Button btDone;
	@FXML
	private TextField tfFieldName;
	@FXML
	private Label lbPrompt;
	@FXML
	private TextField tfFieldLength;
	@FXML
	private Button btCancel;
	@FXML
	private CheckBox cbRedundancy;
	/*-------------------------------------------
	 * �ʵ� ������ �޺��ڽ��� �̿��Ͽ� 1���� 10���� �����Ѵ�.
	 --------------------------------------------*/
	ObservableList<String> list = FXCollections.observableArrayList("String", "Integer", "Double", "Character");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		combobox.setItems(list);
	}
	
	/*----------------------------
	 * Done ��ư�� �������� �� ����
	-----------------------------*/
	@FXML
	private void addField() {
		try {
			/* field�� ��ġ�� ����, �ʵ� Ÿ�԰� �ʵ� �̸��� �����Ѵ� */
			int fieldPosition = Main.mg.getFieldNum();
			String fieldType = combobox.getValue();
			String fieldName = tfFieldName.getText();
			boolean redundancy;
			
			if(cbRedundancy.isSelected()) 
				redundancy = true;
			else redundancy = false;
			
			
			/* fieldType �̼����� �Է� �Ұ��� */
			if(fieldType == null) {
				lbPrompt.setText("�ʵ�Ÿ���� �����ϼ���");
				return;
			}
			
			/* �Ľ� �Ұ��ɽ� ����ó���ȴ� */
			int fieldLength = Integer.parseInt(tfFieldLength.getText());;
			
			/* ���̿� ���� ����ó�� */
			if(fieldName.length()>41 || fieldLength>25) {
				lbPrompt.setText("���̸� Ȯ�� �� �ּ��� ");
				return;
			}
			
			/* ������ ������� ���ο� �ʵ带 ����� Management Class�� �ʵ� ������ �߰��Ѵ� */
			new field(fieldPosition, fieldLength, fieldType, fieldName, redundancy);
			close();
		} catch(NumberFormatException e) {
			lbPrompt.setText("�ʵ� ���̸� Ȯ���ϼ���.");
			return;
		}
	}
}
