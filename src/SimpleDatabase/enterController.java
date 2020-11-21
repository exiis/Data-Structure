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
	
	
	//field Name ������ ���� ����
	String fieldName;
	field head;
	
	data dataList;
	int count=0;
	@FXML
	public void enter() {		
		/* ������ �Է� �ܰ� */
		if(typeCheck(head.fieldType, tfData.getText()) == -1) { 
			lbFieldName.setText(fieldName+" �Է��� Ȯ���ϼ���.");
			return;
		}
		else {
			//�ʱ� �Է½� list�� ������
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
				lbState.setText(fieldName+" �Է���");
			}
		}
		count++;
		
		/* ������ ���� �ܰ� */
		if(count == Main.mg.fieldNum) {
			String inputData = dataList.getData(Main.mg.sortField);
			//�� leaf�� ����
			leafNode leaf = (leafNode)Main.mg.root.findLeaf(inputData);	
			//�����͸� �����Ѵ�.
			leaf.addData(dataList);
			Main.mg.recIn();
			close();
		}
	}
	
	/* �ʱ� ���� */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		head = Main.mg.fieldHead;
		fieldName = head.name;
		lbFieldName.setText(fieldName);
		lbState.setText(fieldName+" �Է���");
	}
	
	/*-------[ type check ] ----------
	 * �ʵ��� ������ Ÿ�Կ� ���� ������ Ȯ��
	 * �Է� ���ɽ� 1, �Ұ��ɽ� -1 ����
	 -------------------------------*/
	public int typeCheck(String fieldType, String newData) {
		// Double, Integer Ÿ���� Ȯ���� ���� 
		//NumberFormatException�� try-catch��
		try {
			if(newData == null) return 1;
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
