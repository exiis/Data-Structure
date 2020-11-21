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
	 * 필드 갯수를 콤보박스를 이용하여 1에서 10까지 설정한다.
	 --------------------------------------------*/
	ObservableList<String> list = FXCollections.observableArrayList("String", "Integer", "Double", "Character");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		combobox.setItems(list);
	}
	
	/*----------------------------
	 * Done 버튼이 눌려졌을 때 실행
	-----------------------------*/
	@FXML
	private void addField() {
		try {
			/* field의 위치를 설정, 필드 타입과 필드 이름을 저장한다 */
			int fieldPosition = Main.mg.getFieldNum();
			String fieldType = combobox.getValue();
			String fieldName = tfFieldName.getText();
			boolean redundancy;
			
			if(cbRedundancy.isSelected()) 
				redundancy = true;
			else redundancy = false;
			
			
			/* fieldType 미설정시 입력 불가능 */
			if(fieldType == null) {
				lbPrompt.setText("필드타입을 선택하세요");
				return;
			}
			
			/* 파싱 불가능시 예외처리된다 */
			int fieldLength = Integer.parseInt(tfFieldLength.getText());;
			
			/* 길이에 대한 예외처리 */
			if(fieldName.length()>41 || fieldLength>25) {
				lbPrompt.setText("길이를 확인 해 주세요 ");
				return;
			}
			
			/* 정보를 기반으로 새로운 필드를 만들고 Management Class에 필드 정보를 추가한다 */
			new field(fieldPosition, fieldLength, fieldType, fieldName, redundancy);
			close();
		} catch(NumberFormatException e) {
			lbPrompt.setText("필드 길이를 확인하세요.");
			return;
		}
	}
}
