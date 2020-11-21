package SimpleDatabase;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class modifyController extends CMDController implements Initializable {
	@FXML
	Button btModify;
	@FXML
	Button btDone;
	@FXML
	Button btCancel;
	@FXML
	Label lbState;
	@FXML
	ListView<String> listview;
	@FXML
	ComboBox<String> cbField;
	@FXML
	TextField tfNewData;
	
	private ObservableList<String> listItems;        
	private ObservableList<String> comboboxItems;
	
	//make ListView Items ( data )
	@FXML
	public void initListview() {
		leafNode cur = (leafNode)Main.mg.curNode;
		int curIndex = Main.mg.curIndex;
		for(int i=0; i<Main.mg.fieldNum; i++)
			listItems.add(cur.dataArr[curIndex].getData(i));
	}
	
	//make ComboBox Items ( field )
	@FXML
	public void initCombobox() {
		field temp = Main.mg.fieldHead;
		comboboxItems.add(temp.name);
		while(temp.next != null) {
			temp = temp.next;
			comboboxItems.add(temp.name);
		}
	}
	
	//initialize item of ComboBox and ListView 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		listItems = FXCollections.observableArrayList();
		comboboxItems = FXCollections.observableArrayList();
		this.initListview();
		this.initCombobox();
		listview.setItems(listItems);
		cbField.setItems(comboboxItems);
	}
	
	/*------------[ modifyData ] ------------
	 * 사용자의 입력에 따라 먼저 리스트뷰를 수정한다
	----------------------------------------*/
	public void modifyData() {
		int recodeIndex = Main.mg.curIndex;
		String fieldType = null;
		String newData;
		leafNode targetNode = (leafNode)Main.mg.curNode;
		data targetData;
		field tempField = Main.mg.fieldHead;
		
		
		try {
			newData = tfNewData.getText();
			
			//필드를 선택하지 않았을 경우
			if(cbField.getValue() == null) {
				lbState.setText("수정할 필드를 선택하세요.");
				return;
			}
			
			//필드 Index를 찾음
			int fieldIndex;
			for(fieldIndex = 0; fieldIndex<Main.mg.getFieldNum(); fieldIndex++) {
				//필드를 찾으면 멈춤, fieldType 초기화
				if(cbField.getValue().compareTo(tempField.name) == 0) {
					fieldType = tempField.fieldType;
					break;
				}
				else
					tempField = tempField.next;
			}
			tempField = Main.mg.fieldHead;//tempField 다시 head로 초기화
			
			if(fieldType == null) System.out.println("Modify-Field Search Error");
			
			//수정할 데이터가 필드 타입에 맞는지 확인
			if(typeCheck(fieldType, newData) == 1) {
				//target Data를 찾음
				targetData = targetNode.dataArr[recodeIndex];
				for(int i=0; i<fieldIndex; i++) 
					targetData = targetNode.dataArr[recodeIndex].next;
				
				//recode 수정
				targetData.data = newData;
				//list view 수정
				listItems.set(fieldIndex, newData);
			}
		} catch(NullPointerException e) {
		}
	}
	
	/*----------------[ Done ] ---------------
	 * 기존의 노드를 삭제하고 리스트뷰를 기반으로 새로운
	 * 노드를 추가하여 트리에 추가하는 방식으로 수정한다.
	----------------------------------------*/
	public void done() {
		String listItem[] = new String[Main.mg.fieldNum];
		
		//새로운 데이터 리스트를 생성
		for(int i=0; i<Main.mg.fieldNum; i++) 
			listItem[i] = listItems.get(i);
		
		data dataList = new data(listItem[0]);
		for(int i=1; i<Main.mg.fieldNum; i++){
			dataList.addData(new data(listItem[i]));
		}

		
		leafNode cur = (leafNode)Main.mg.curNode;
		cur.deleteData(cur.dataArr[Main.mg.curIndex].getData(Main.mg.sortField), Main.mg.sortField);
		Main.mg.recOut();
		
		String inputData = dataList.getData(Main.mg.sortField);
		//들어갈 leaf에 접근
		leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);	
		//데이터를 삽입한다.
		leaf.addData(dataList);
		Main.mg.recIn();
		close();
	}
	
	/*-------[ type check ] ----------
	 * 필드의 데이터 타입에 대한 데이터 확인
	 * 입력 가능시 1, 불가능시 -1 리턴
	 -------------------------------*/
	public int typeCheck(String fieldType, String newData) {
		// Double, Integer 타입의 확인을 위한 
		//NumberFormatException의 try-catch문
		try {
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
