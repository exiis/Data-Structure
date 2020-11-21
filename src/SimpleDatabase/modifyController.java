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
	 * ������� �Է¿� ���� ���� ����Ʈ�並 �����Ѵ�
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
			
			//�ʵ带 �������� �ʾ��� ���
			if(cbField.getValue() == null) {
				lbState.setText("������ �ʵ带 �����ϼ���.");
				return;
			}
			
			//�ʵ� Index�� ã��
			int fieldIndex;
			for(fieldIndex = 0; fieldIndex<Main.mg.getFieldNum(); fieldIndex++) {
				//�ʵ带 ã���� ����, fieldType �ʱ�ȭ
				if(cbField.getValue().compareTo(tempField.name) == 0) {
					fieldType = tempField.fieldType;
					break;
				}
				else
					tempField = tempField.next;
			}
			tempField = Main.mg.fieldHead;//tempField �ٽ� head�� �ʱ�ȭ
			
			if(fieldType == null) System.out.println("Modify-Field Search Error");
			
			//������ �����Ͱ� �ʵ� Ÿ�Կ� �´��� Ȯ��
			if(typeCheck(fieldType, newData) == 1) {
				//target Data�� ã��
				targetData = targetNode.dataArr[recodeIndex];
				for(int i=0; i<fieldIndex; i++) 
					targetData = targetNode.dataArr[recodeIndex].next;
				
				//recode ����
				targetData.data = newData;
				//list view ����
				listItems.set(fieldIndex, newData);
			}
		} catch(NullPointerException e) {
		}
	}
	
	/*----------------[ Done ] ---------------
	 * ������ ��带 �����ϰ� ����Ʈ�並 ������� ���ο�
	 * ��带 �߰��Ͽ� Ʈ���� �߰��ϴ� ������� �����Ѵ�.
	----------------------------------------*/
	public void done() {
		String listItem[] = new String[Main.mg.fieldNum];
		
		//���ο� ������ ����Ʈ�� ����
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
		//�� leaf�� ����
		leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);	
		//�����͸� �����Ѵ�.
		leaf.addData(dataList);
		Main.mg.recIn();
		close();
	}
	
	/*-------[ type check ] ----------
	 * �ʵ��� ������ Ÿ�Կ� ���� ������ Ȯ��
	 * �Է� ���ɽ� 1, �Ұ��ɽ� -1 ����
	 -------------------------------*/
	public int typeCheck(String fieldType, String newData) {
		// Double, Integer Ÿ���� Ȯ���� ���� 
		//NumberFormatException�� try-catch��
		try {
			/* ���� ���Է½� �Ұ����� ó�� */
			if(newData.length() == 0) {
				lbState.setText("���Է� ���� Ȯ���ϼ���");
				return -1;
			}
			
			/* ������ �ʵ� Ÿ�Կ� ���� �������� ó�� */
		switch(fieldType) {
			case "String" : 
				if(newData.length() > 24) {
					lbState.setText("�������� ���̸� Ȯ�� �� �ּ��� ");
					return -1;
				}
				else return 1; // END String
			case "Character" :
				if(newData.length() > 1) {
					lbState.setText("�������� ���̸� Ȯ�� �� �ּ��� ");
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
		/* �Ľ� �Ұ��ɽ� ���� �Ұ��� */ 
		} catch(NumberFormatException e) {
			lbState.setText("������ ������ Ȯ�� �� �ּ���");
			return -1;
		}
	}

	
	
}
