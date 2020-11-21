package SimpleDatabase;

import javafx.beans.property.StringProperty;

public class tableDataModel {
	private StringProperty fieldName;
	private StringProperty data;
	
	public tableDataModel(StringProperty fieldName, StringProperty data) {
		this.fieldName = fieldName;
		this.data = data;
	}
	
	public StringProperty fieldNameProperty() {
		return fieldName;
	}
	
	public StringProperty dataProperty() {
		return data;
	}


}
