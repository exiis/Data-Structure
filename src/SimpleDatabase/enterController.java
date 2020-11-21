package SimpleDatabase;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class enterController extends CMDController implements Initializable{
	@FXML
	Button btDone;
	@FXML
	Label lbState;
	@FXML
	Label lbFieldName;
	@FXML
	TextField tfData;
	
	
	//field Name 설정을 위한 변수
	String fieldName;
	field head;
	
	data dataList;
	int count=0;
	@FXML
	public void enter() {		
		/* 데이터 입력 단계 */
		if(typeCheck(head.fieldType, tfData.getText()) == -1) { 
			lbFieldName.setText(fieldName+" 입력을 확인하세요.");
			return;
		}
		else {
			//초기 입력시 list를 생성함
			if(count == 0) 
				dataList = new data(tfData.getText());

			else {
				data newData = new data(tfData.getText());
				dataList.addData(newData);
			}
			
			tfData.setText(null);
			if(head.next != null) {
				head = head.next;
				fieldName = head.name;
				lbFieldName.setText(fieldName);
				lbState.setText(fieldName+" 입력중");
			}
		}
		count++;
		
		/* 데이터 저장 단계 */
		if(count == Main.mg.fieldNum) {
			String inputData = dataList.getData(Main.mg.sortField);
			//들어갈 leaf에 접근
			leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);	
			//데이터를 삽입한다.
			leaf.addData(dataList);
			Main.mg.recIn();
			close();
		}
	}
	
	/* 초기 설정 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		head = Main.mg.fieldHead;
		fieldName = head.name;
		lbFieldName.setText(fieldName);
		lbState.setText(fieldName+" 입력중");
	}
	
	/*-------[ type check ] ----------
	 * 필드의 데이터 타입에 대한 데이터 확인
	 * 입력 가능시 1, 불가능시 -1 리턴
	 -------------------------------*/
	public int typeCheck(String fieldType, String newData) {
		// Double, Integer 타입의 확인을 위한 
		//NumberFormatException의 try-catch문
		try {
			if(newData == null) return 1;
			/* 먼저 미입력시 불가능을 처리 */
			if(newData.length() == 0) {
				lbState.setText("미입력 값을 확인하세요");
				return -1;
			}
			
			/* 나머지 필드 타입에 따른 데이터의 처리 */
		switch(fieldType) {
			case "String" : 
				if(newData.length() > 24) {
					lbState.setText("데이터의 길이를 확인 해 주세요 ");
					return -1;
				}
				else return 1; // END String
			case "Character" :
				if(newData.length() > 1) {
					lbState.setText("데이터의 길이를 확인 해 주세요 ");
					return -1;
				}
				else return 1; //END Character
			case "Integer" :
				Integer.parseInt(newData);
				return 1;//END Integer
			case "Double" :
				Double.parseDouble(newData);
				return 1;//END Double
			default:
				return 0;
			}
		/* 파싱 불가능시 수정 불가능 */ 
		} catch(NumberFormatException e) {
			lbState.setText("데이터 형식을 확인 해 주세요");
			return -1;
		}
	}
}
