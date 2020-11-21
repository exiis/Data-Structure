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
 *  1. 기본 동작
 *   사용자가 원하는 동작을 할 수 있도록 각 클래스들과 연결해준다.
 *  2. 특이사항
 *   - 사용자에 입력에 대한 동작 처리가 가능한지 자체적으로 검열한다.
 *      ( GUI 자체적인 각 기능에 대한 예외처리 )
 ------------------------------------------------*/

public class guiController implements Initializable {
	Scanner sc = new Scanner(System.in);
	/*-----------
	 * 명령어 버튼
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
	 * 메뉴 버튼
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
	 *  팝업창을 위한 멤버변수
	 ----------------------*/
	Stage mainStage;
	Stage pop;
	
	/* prompt */
	@FXML
	Label lbPrompt;
	@FXML
	Label lbInfor;
	
	/*------------
	 *  테이블뷰
	 -------------*/
	@FXML
	private TableView<tableDataModel> tableView;
	@FXML
	private TableColumn<tableDataModel, String> fieldNameColumn;
	@FXML
	private TableColumn<tableDataModel, String> dataColumn;

	ObservableList<tableDataModel> list = FXCollections.observableArrayList();
	
	
	/*------[ void popSet(Scene scene, String titleName) ]------------
	 * 팝업창의 기본 셋팅을 한다.
	 * initOwner 함수를 통해 팝업창을 수행해야 기본 화면으로 돌아갈 수 있다.
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
	 * Enter.fxml을 FXMLLoader를 통하여 불러온다 
	 ----------------------------------------*/
	@FXML
	private void Enter(ActionEvent event) {
		try {
			/* 필드에 요소가 없는데 실행할 경우 */
			if(Main.mg.fieldIsNull() == true) {
				lbPrompt.setText("데이터 입력을 위해 필드를 추가하세요");
				return;
			}
			/* 정렬 기준이 정해지지 않았다면 */
			if(Main.mg.sortField == -1) {
				lbPrompt.setText("데이터 입력을 위해 정렬 필드를 선택하세요");
				return;
			}
			Parent root = FXMLLoader.load(getClass().getResource("Enter.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Enter");
			lbPrompt.setText("Enter 수행");
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
			lbPrompt.setText("AddField 수행");
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
			/* 필드에 요소가 없는데 실행할 경우 */
			if(Main.mg.recNum == 0) {
				lbPrompt.setText("데이터 입력을 위해 필드를 추가하세요");
				return;
			}
			Parent root = FXMLLoader.load(getClass().getResource("Delete.fxml"));
			Scene scene = new Scene(root);
			popSet(scene, "Delete");
			lbPrompt.setText("Delete 수행");
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
			lbPrompt.setText("SortField 수행");
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
			lbPrompt.setText("Save 수행");
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
			lbPrompt.setText("Load 수행");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void updateTable(ActionEvent event) {
		if(Main.mg.recNum == 0) {
			lbPrompt.setText("표시할 데이터가 없습니다.");
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
		lbPrompt.setText("업데이트 완료");
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
		lbPrompt.setText("업데이트 완료");
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
			lbPrompt.setText("Modify 수행");
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
			lbPrompt.setText("Search 수행");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void back(ActionEvent event) {
		if(Main.mg.recNum == 0 ) {
			lbPrompt.setText("데이터가 없습니다.");
			return;
		}
		
		leafNode curNode = (leafNode)Main.mg.curNode;
		int curIndex = Main.mg.curIndex;
		
		//맨 왼쪽에서 왼쪽으로 이동
		if(curIndex == 0 ) {
			if(curNode.prior != null) {
				updateTable((leafNode)curNode.prior, curNode.prior.n-1);
				Main.mg.curNode = curNode.prior;
				Main.mg.curIndex = curNode.prior.n-1;
			}
			else {
				lbPrompt.setText("이전 레코드가 없습니다.");
				return;
			}
		}
		else if(curIndex-1>=0){
			curIndex--;
			Main.mg.curIndex--;
			updateTable(curNode, curIndex);
		}
		else {
			lbPrompt.setText("이전 레코드가 없습니다.");
			return;
		}
	}
	@FXML
	public void next(ActionEvent event) {
		if(Main.mg.recNum == 0 ) {
			lbPrompt.setText("데이터가 없습니다.");
			return;
		}
		
		leafNode curNode = (leafNode)Main.mg.curNode;
		int curIndex = Main.mg.curIndex;
		
		//맨 오른쪽에서 오른쪽으로 이동
		if(curIndex == curNode.n-1 ) {
			if(curNode.next != null) {
				updateTable((leafNode)curNode.next, 0);
				Main.mg.curNode = curNode.next;
				Main.mg.curIndex = 0;
			}
			else {
				lbPrompt.setText("다음 레코드가 없습니다.");
				return;
			}
		}
		else if(curIndex+1<=curNode.n){
			Main.mg.curIndex++;
			curIndex++;
			updateTable(curNode, curIndex);
		}
		else {
			lbPrompt.setText("다음 레코드가 없습니다.");
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
