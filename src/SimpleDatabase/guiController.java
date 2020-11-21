package SimpleDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*------------------------------------------------
 *  GUI CONTROLLER CLASS
 *  1. �⺻ ����
 *   ����ڰ� ���ϴ� ������ �� �� �ֵ��� �� Ŭ������� �������ش�.
 *  2. Ư�̻���
 *   - ����ڿ� �Է¿� ���� ���� ó���� �������� ��ü������ �˿��Ѵ�.
 *      ( GUI ��ü���� �� ��ɿ� ���� ����ó�� )
 ------------------------------------------------*/

public class guiController implements Initializable {
	Scanner sc = new Scanner(System.in);
	/*-----------
	 * ��ɾ� ��ư
	 -----------*/
	@FXML
	private Button btEnter;
	@FXML
	private Button btDelete;
	@FXML
	private Button btModify;
	@FXML
	private Button btSearch;
	@FXML
	private Button btCancel;
	@FXML
	private Button btBack, btNext;
	
	/*---------
	 * �޴� ��ư
	 ----------*/
	@FXML
	private MenuItem menuAddField;
	@FXML
	private MenuItem menuSave;
	@FXML
	private MenuItem menuLoad;
	@FXML
	private MenuItem menuPrint;
	@FXML
	private MenuItem menuDevelop;
	@FXML
	private MenuItem menuSortField;

	
	// Tabel View
	@FXML
	private TableView<String> Table;
	
	/*----------------------
	 *  �˾�â�� ���� �������
	 ----------------------*/
	Stage mainStage;
	Stage pop;
	
	/* prompt */
	@FXML
	Label lbPrompt;
	@FXML
	Label lbInfor;
	
	/*------------
	 *  ���̺��
	 -------------*/
	@FXML
	private TableView<tableDataModel> tableView;
	@FXML
	private TableColumn<tableDataModel, String> fieldNameColumn;
	@FXML
	private TableColumn<tableDataModel, String> dataColumn;

	ObservableList<tableDataModel> list = FXCollections.observableArrayList();
	
	
	/*------[ void popSet(Scene scene, String titleName) ]------------
	 * �˾�â�� �⺻ ������ �Ѵ�.
	 * initOwner �Լ��� ���� �˾�â�� �����ؾ� �⺻ ȭ������ ���ư� �� �ִ�.
	 ----------------------------------------------------------------*/
	void popSet(Scene scene, String titleName) {
		mainStage = (Stage)btEnter.getScene().getWindow();	
		Stage pop = new Stage(StageStyle.DECORATED);
		
		pop.initOwner(mainStage);
		pop.initModality(Modality.WINDOW_MODAL);
		pop.setScene(scene);
		pop.setTitle(titleName);
		pop.setResizable(false);
		pop.show();	
	}
	
	/* --------------[ Enter ] ---------------
	 * Enter.fxml�� FXMLLoader�� ���Ͽ� �ҷ��´� 
	 ----------------------------------------*/
	@FXML
	private void Enter(ActionEvent event) {
		try {
			/* �ʵ忡 ��Ұ� ���µ� ������ ��� */
			if(Main.mg.fieldIsNull() == true) {
				lbPrompt.setText("������ �Է��� ���� �ʵ带 �߰��ϼ���");
				return;
			}
			/* ���� ������ �������� �ʾҴٸ� */
			if(Main.mg.sortField == -1) {
				lbPrompt.setText("������ �Է��� ���� ���� �ʵ带 �����ϼ���");
				return;
			}
			Parent root = FXMLLoader.load(getClass().getResource("Enter.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Enter");
			lbPrompt.setText("Enter ����");
		} catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	
	
	/* --------[ AddField ] ----------
	 * 
	 -----------------------------*/
	@FXML
	private void AddField(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("AddField.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "AddField");
			lbPrompt.setText("AddField ����");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/* --------------[ Delete ] ---------------
	 * 
	 ----------------------------------------*/
	@FXML
	private void Delete(ActionEvent event) {
		try {
			/* �ʵ忡 ��Ұ� ���µ� ������ ��� */
			if(Main.mg.recNum == 0) {
				lbPrompt.setText("������ �Է��� ���� �ʵ带 �߰��ϼ���");
				return;
			}
			Parent root = FXMLLoader.load(getClass().getResource("Delete.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Delete");
			lbPrompt.setText("Delete ����");
		} catch(IOException e) {
			e.printStackTrace();
		}	
	}
	@FXML
	public void SortField(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("SortField.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "SortField");
			lbPrompt.setText("SortField ����");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void Save(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Save.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Save");
			lbPrompt.setText("Save ����");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void Load(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Load.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Load");
			lbPrompt.setText("Load ����");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void updateTable(ActionEvent event) {
		if(Main.mg.recNum == 0) {
			lbPrompt.setText("ǥ���� �����Ͱ� �����ϴ�.");
			return;
		}
		
		if(Main.mg.curNode == null)Main.mg.setCurNode();
		list.clear();
		tableDataModel table;
		leafNode curNode = (leafNode)Main.mg.curNode;
		int curIndex = Main.mg.curIndex;
		field head = Main.mg.fieldHead;
		for(int i=0; i<Main.mg.getFieldNum(); i++) {
			table = new tableDataModel(new SimpleStringProperty(head.name), new SimpleStringProperty(curNode.dataArr[curIndex].getData(i)));
			list.add(table);
			head = head.next;
		}
		tableView.setItems(list);
		lbPrompt.setText("������Ʈ �Ϸ�");
	}
	
	
	public void updateTable(leafNode curNode, int curIndex) {
		list.clear();
		tableDataModel table;
		field head = Main.mg.fieldHead;
		for(int i=0; i<Main.mg.getFieldNum(); i++) {
			table = new tableDataModel(new SimpleStringProperty(head.name), new SimpleStringProperty(curNode.dataArr[curIndex].getData(i)));
			list.add(table);
			head = head.next;
		}
		lbInfor.setText("Field Num : "+Main.mg.fieldNum);
		lbPrompt.setText("������Ʈ �Ϸ�");
	}
	
	@FXML
	public void setLabel() {
		
	}
	
	
	@FXML
	public void resetTable(ActionEvent event) {
		Main.mg.init();
		lbPrompt.setText("reset");
		list.clear();
		lbInfor.setText("Information....");
	}
	
	@FXML
	public void Modify(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Modify.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Modify");
			lbPrompt.setText("Modify ����");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void Search(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Search.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Search");
			lbPrompt.setText("Search ����");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void back(ActionEvent event) {
		if(Main.mg.recNum == 0 ) {
			lbPrompt.setText("�����Ͱ� �����ϴ�.");
			return;
		}
		
		leafNode curNode = (leafNode)Main.mg.curNode;
		int curIndex = Main.mg.curIndex;
		
		//�� ���ʿ��� �������� �̵�
		if(curIndex == 0 ) {
			if(curNode.prior != null) {
				updateTable((leafNode)curNode.prior, curNode.prior.n-1);
				Main.mg.curNode = curNode.prior;
				Main.mg.curIndex = curNode.prior.n-1;
			}
			else {
				lbPrompt.setText("���� ���ڵ尡 �����ϴ�.");
				return;
			}
		}
		else if(curIndex-1>=0){
			curIndex--;
			Main.mg.curIndex--;
			updateTable(curNode, curIndex);
		}
		else {
			lbPrompt.setText("���� ���ڵ尡 �����ϴ�.");
			return;
		}
	}
	@FXML
	public void next(ActionEvent event) {
		if(Main.mg.recNum == 0 ) {
			lbPrompt.setText("�����Ͱ� �����ϴ�.");
			return;
		}
		
		leafNode curNode = (leafNode)Main.mg.curNode;
		int curIndex = Main.mg.curIndex;
		
		//�� �����ʿ��� ���������� �̵�
		if(curIndex == curNode.n-1 ) {
			if(curNode.next != null) {
				updateTable((leafNode)curNode.next, 0);
				Main.mg.curNode = curNode.next;
				Main.mg.curIndex = 0;
			}
			else {
				lbPrompt.setText("���� ���ڵ尡 �����ϴ�.");
				return;
			}
		}
		else if(curIndex+1<=curNode.n){
			Main.mg.curIndex++;
			curIndex++;
			updateTable(curNode, curIndex);
		}
		else {
			lbPrompt.setText("���� ���ڵ尡 �����ϴ�.");
			return;
		}
	}
	
	
	
	/* --------[ initialize : for Table ] ----------
	 * 
	 ----------------------------------------------*/

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fieldNameColumn.setCellValueFactory(cellData->cellData.getValue().fieldNameProperty());
		dataColumn.setCellValueFactory(cellData->cellData.getValue().dataProperty());
		tableView.setItems(list);
	}
	
	
	/*----------------------------
	 * develop kits
	 ---------------------------*/
	public void develop() {
		try {
			Scanner sc = new Scanner(System.in);
			develope dv = new develope();
	
			int input=-1;
			
			do {
				System.out.println("--------develop kits-----------");
				System.out.println("0. exit -----------------------");
				System.out.println("1. print field information ----");
				System.out.println("2. field Add Macro ------------");
				System.out.println("3. Select Sort Field ----------");
				System.out.println("4. Add Data Macro -------------");
				System.out.println("5. Show tree ------------------");
				System.out.println("6. Delete Recode --------------");
				System.out.println("-------------------------------");
				System.out.print("Select : ");
				input = sc.nextInt();
				
				
				switch(input) {
				case 0 :
					System.out.println("exit.");
					return;
				case 1:
					dv.showField();
					break;
				case 2:
					System.out.print("Field Num : ");
					int temp = sc.nextInt();
					dv.fieldAddMacro(temp);
					break;
				case 3:
					System.out.print("Field Num : ");
					int selectField = sc.nextInt();
					dv.setSort(selectField);
					break;
				case 4:
					System.out.println("Data Num : ");
					int dataNum = sc.nextInt();
					dv.dataAddMacro(dataNum);
					break;
				case 5:
					dv.show();
					break;
				case 6:
					String str;
					System.out.print("delete recode : ");
					str = sc.next();
					leafNode leaf = (leafNode) Main.mg.root.findLeaf(str);
					leaf.deleteData(str, Main.mg.sortField);
					break;
				case 7:
					Main.mg.textLoad("t.scv");
					break;
				case 8:
					Main.mg.Save("d");
					break;
				case 9:
					Main.mg.Load("d");
				}
			}while(input != 0);
			sc.close();
		}catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
	
}
