package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class mainUIController implements Initializable{
	
	@FXML ImageView deleteIM, saveChangesIM, saveIM, editIM, lockIM;
	@FXML TextField searchField, idField, fnameField, lnameField, extnameField, mnameField, courseField, yrlevelField,
	ffnameField, fmnameField, flnameField, mfnameField, mmnameField, mlnameField, brgyField,
	townField, provField, zipField, contactField, emailField, householdnumField, householdincomeField,
	scholarshipField, yrstartField, yrendField;
	@FXML Label selectLabel, userLabel;
	@FXML Button selectButton, cancelButton, logoutButton, printButton;
	@FXML Tab adminTab;
	
	@FXML ComboBox <gender> genderCB = new ComboBox <gender>();
	@FXML ComboBox<fullnames> studentCB = new ComboBox<fullnames>();
	@FXML ObservableList<fullnames>namedata;
	@FXML DatePicker bdatePicker;
	@FXML ScrollBar sb;
	

	 private ObservableList<studentInfo> studentdata;
	@FXML private TableView <studentInfo> infoTable;
	 @FXML private TableColumn <studentInfo, Number> seqCol;
	 @FXML private TableColumn <studentInfo, String> idCol;
	 @FXML private TableColumn <studentInfo, String> lnameCol;
	 @FXML private TableColumn <studentInfo, String> fnameCol;
	 @FXML private TableColumn <studentInfo, String> extnameCol;
	 @FXML private TableColumn <studentInfo, String> mnameCol;
	 @FXML private TableColumn <studentInfo, String> genderCol;
	 @FXML private TableColumn <studentInfo, String> bdateCol;
	 @FXML private TableColumn <studentInfo, String> courseCol;
	 @FXML private TableColumn <studentInfo, String> yrlevelCol;
	 @FXML private TableColumn <studentInfo, String> flnameCol;
	 @FXML private TableColumn <studentInfo, String> ffnameCol;
	 @FXML private TableColumn <studentInfo, String> fmnameCol;
	 @FXML private TableColumn <studentInfo, String> mlnameCol;
	 @FXML private TableColumn <studentInfo, String> mfnameCol;
	 @FXML private TableColumn <studentInfo, String> mmnameCol;
	 @FXML private TableColumn <studentInfo, String> householdnumCol;
	 @FXML private TableColumn <studentInfo, String> householdincomeCol;
	 @FXML private TableColumn <studentInfo, String> brgyCol;
	 @FXML private TableColumn <studentInfo, String> townCol;
	 @FXML private TableColumn <studentInfo, String> provCol;
	 @FXML private TableColumn <studentInfo, String> zipCol;
	 @FXML private TableColumn <studentInfo, String> contactCol;
	 @FXML private TableColumn <studentInfo, String> emailCol;
	 @FXML private TableColumn <studentInfo, String> scholarshipCol;
	 @FXML private TableColumn <studentInfo, String> yrstartCol;
	 @FXML private TableColumn <studentInfo, String> yrendCol;
     
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		checkLogin();
		setGender();
		fetchData();
		setInitialValues();
		sb.setMin(-100);
		sb.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    infoTable.setLayoutX(-new_val.intValue()*18);
            }
        });
		
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(searchField.getText()!=null) {
				search();
			}
			else {
				fetchData();
			}
		});
	}
	
	/*
	 * 
	 *------------------Constructing the Window-------------- 
	 * 
	 */
	
	public void createWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        TabPane root = (TabPane) loader.load();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setTitle("USM Student Scholarship Information System_Data Window");
        stage.getIcons().add(new Image("resources/usm seal icon.png"));
        stage.show();
	}
	
	
	/*
	 * 
	 *------------------Setting up empty Strings as initial values to the text fields.
	 *This is to avoid saving "null" as database value on empty text fields-------------- 
	 * 
	 */
	public void setInitialValues() {
		idField.setText(""); fnameField.setText(""); lnameField.setText(""); extnameField.setText(""); mnameField.setText("");
		courseField.setText(""); yrlevelField.setText(""); ffnameField.setText(""); fmnameField.setText(""); flnameField.setText("");
		mfnameField.setText(""); mmnameField.setText(""); mlnameField.setText(""); brgyField.setText(""); townField.setText("");
		provField.setText(""); zipField.setText(""); contactField.setText(""); emailField.setText(""); householdnumField.setText("");
		householdincomeField.setText(""); scholarshipField.setText(""); yrstartField.setText(""); yrendField.setText("");
	}
	
	
	/*
	 * 
	 *------------------Setting ComboBox Data-------------- 
	 * 
	 */
	public void setGender() {
		genderCB.setItems(FXCollections.observableArrayList(
				new gender("Male"),
				new gender("Female")
				));
		
		genderCB.setConverter(new StringConverter<gender>() {
		    @Override
		    public String toString(gender object) {
		        return object.getGender();
		    }

		    @Override
		    public gender fromString(String string) {
		        return null;
		    }
		});
	}
	
	/*
	 * 
	 *------------------Hover Effects-------------- 
	 * 
	 */
	
	public void saveHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			saveIM.setEffect(glow);
	}
	public void deleteHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			deleteIM.setEffect(glow);
	}
	public void editHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			editIM.setEffect(glow);
	}
	public void lockHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			lockIM.setEffect(glow);
	}
	public void saveChangesHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			saveChangesIM.setEffect(glow);
	}
	public void logoutHoverIn() {
			logoutButton.setTextFill(Color.RED);
	}
	public void logoutHoverOut() {
		logoutButton.setTextFill(Color.BLACK);
}
	public void hoverOut() {
		Glow glow = new Glow();
			glow.setLevel(0);
			saveIM.setEffect(glow);
			deleteIM.setEffect(glow);
			editIM.setEffect(glow);
			saveChangesIM.setEffect(glow);
			lockIM.setEffect(glow);
	}
	
	
	/*
	 * 
	 *------------------Checking Login Status of the Current User--------------
	 * 
	 */
	public void checkLogin() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
			Statement smt = conn.createStatement();
			ResultSet rs = smt.executeQuery("SELECT * FROM `users` WHERE `session` = 'active'");
			
			while(rs.next()) {
				String name = rs.getString("username");
				String userType = rs.getString("usertype");
				userLabel.setText(name);
					if(userType.equals("admin")) {
						adminTab.setDisable(false);
					}
					else {
						adminTab.setDisable(true);
					}
			}
		}
		catch(Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("mySQL SERVER ERROR");
				alert.setHeaderText("");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
		}
	}
	
	
	/*
	 * 
	 *------------------Saving Data to Database--------------
	 * 
	 */

	public void save() {
		String id = idField.getText();
		String lname = lnameField.getText();
		String fname = fnameField.getText();
		String mname = mnameField.getText();
		String extname = extnameField.getText();
		String gender = genderCB.getSelectionModel().getSelectedItem().getGender();
		String bdate = bdatePicker.getValue().toString();
		String course = courseField.getText();
		String yrlevel = yrlevelField.getText();
		String fatherfname = ffnameField.getText();
		String fathermname = fmnameField.getText();
		String fatherlname = flnameField.getText();
		String motherfname = mfnameField.getText();
		String mothermname = mmnameField.getText();
		String motherlname = mlnameField.getText();
		String householdnum = householdnumField.getText();
		String householdincome = householdincomeField.getText();
		String barangay = brgyField.getText();
		String town = townField.getText();
		String province = provField.getText();
		String zip = zipField.getText();
		String contact = contactField.getText();
		String email = emailField.getText();
		String scholarship = scholarshipField.getText();
		String yearstart = yrstartField.getText();
		String yearend = yrendField.getText();
		String fullname = lname+" "+fname+" "+mname+" "+extname;
		
		if(!id.equals("") || !lname.equals("") || !fname.equals("") || !mname.equals("") || !extname.equals("") || genderCB.getSelectionModel().getSelectedIndex()!=0 || genderCB.getSelectionModel().getSelectedIndex()!=1) {
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
				Statement smt = conn.createStatement();
				
				smt.execute("INSERT INTO `studentprofile` (`idnumber`, `lastname`, `firstname`, `extname`, `middlename`, "
						+ "`gender`, `birthdate`, `course`, `yrlevel`, `fatherlastname`, `fatherfirstname`, `fathermiddlename`, "
						+ "`motherlastname`, `motherfirstname`, `mothermiddlename`, `householdnumber`, `householdincome`, "
						+ "`barangay`, `town`, `province`, `zipcode`, `contact`, `email`, `scholarship`, `yearstart`, `yearend`, `fullname`)"
						+ "VALUES('"+id+"', '"+lname+"', '"+fname+"', '"+extname+"', '"+mname+"', '"+gender+"', '"+bdate+"', '"
						+course+"', '"+yrlevel+"', '"+fatherlname+"', '"+fatherfname+"', '"+fathermname+"', '"+motherlname+"' ,'"
						+motherfname+"', '"+mothermname+"', '"+householdnum+"', '"+householdincome+"', '"+barangay+"', '"
						+town+"', '"+province+"', '"+zip+"', '"+contact+"', '"+email+"', '"+scholarship+"', '"+yearstart+"', '"+yearend+
						"', '"+fullname+"')");
				
				Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("");
					alert.setHeaderText("Confirmation");
					alert.setContentText("New student added successfully!");
					alert.showAndWait();
						clear();
						fetchData();
				
			}
			catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("");
					alert.setHeaderText("Database Error");
					alert.setContentText(e.getMessage());
					alert.showAndWait();
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("");
				alert.setHeaderText("Invalid Inputs Error");
				alert.setContentText("Please fill in the fields correctly and completely!");
				alert.showAndWait();
		}
	}
	
	
	/*
	 * 
	 *------------------Clearing all the fields-------------- 
	 * 
	 */
	
	public void clear() {
		idField.setText(null); fnameField.setText(null); lnameField.setText(null); extnameField.setText(null);
		mnameField.setText(null); courseField.setText(null); yrlevelField.setText(null); ffnameField.setText(null);
		fmnameField.setText(null); flnameField.setText(null); mfnameField.setText(null); mmnameField.setText(null);
		mlnameField.setText(null); brgyField.setText(null); townField.setText(null); provField.setText(null);
		zipField.setText(null); contactField.setText(null); emailField.setText(null); householdnumField.setText(null);
		householdincomeField.setText(null); scholarshipField.setText(null); yrstartField.setText(null); yrendField.setText(null);
		genderCB.getSelectionModel().clearSelection();
		bdatePicker.setValue(null);
	}
	
	
	/*
	 * 
	 *------------------Fetching data from Database to Table--------------
	 * 
	 */
	
	String query = "SELECT * FROM `studentprofile` ORDER BY `lastname` ASC";
	
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@FXML
		public void fetchData() {
			
			studentdata = FXCollections.observableArrayList();
			
				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
					Statement smt = conn.createStatement();
					ResultSet rs = smt.executeQuery(query);
					
					while(rs.next()) {
						String id = rs.getString("idnumber");
						String lname = rs.getString("lastname");
						String fname = rs.getString("firstname");
						String extname = rs.getString("extname");
						String mname = rs.getString("middlename");
						String gender = rs.getString("gender");
						String bdate = rs.getString("birthdate");
						String course = rs.getString("course");
						String yrlevel = rs.getString("yrlevel");
						String flname = rs.getString("fatherlastname");
						String ffname = rs.getString("fatherfirstname");
						String fmname = rs.getString("fathermiddlename");
						String mlname = rs.getString("motherlastname");
						String mfname = rs.getString("motherfirstname");
						String mmname = rs.getString("mothermiddlename");
						String householdnum = rs.getString("householdnumber");
						String householdincome = rs.getString("householdincome");
						String brgy = rs.getString("barangay");
						String town = rs.getString("town");
						String prov = rs.getString("province");
						String zip = rs.getString("zipcode");
						String contact = rs.getString("contact");
						String email = rs.getString("email");
						String scholarship = rs.getString("scholarship");
						String yrstart = rs.getString("yearstart");
						String yrend = rs.getString("yearend");
						
						studentdata.add(new studentInfo(id, lname, fname, extname, mname, gender, bdate, course, yrlevel, flname, ffname,
								fmname, mlname, mfname, mmname, householdnum, householdincome, brgy, town, prov, zip, contact, email, scholarship,
								yrstart, yrend));
						
					}
					
				}
				catch(Exception e) {
					Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("mySQL SERVER ERROR");
						alert.setHeaderText("");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
				}
				
				seqCol.setSortable(false);
				seqCol.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(infoTable.getItems().indexOf(column.getValue())+1));
				idCol.setCellValueFactory(new PropertyValueFactory("id"));
				lnameCol.setCellValueFactory(new PropertyValueFactory("lname"));
				fnameCol.setCellValueFactory(new PropertyValueFactory("fname"));
				extnameCol.setCellValueFactory(new PropertyValueFactory("extname"));
				mnameCol.setCellValueFactory(new PropertyValueFactory("mname"));
				genderCol.setCellValueFactory(new PropertyValueFactory("gender"));
				bdateCol.setCellValueFactory(new PropertyValueFactory("bdate"));
				courseCol.setCellValueFactory(new PropertyValueFactory("course"));
				yrlevelCol.setCellValueFactory(new PropertyValueFactory("yrlevel"));
				flnameCol.setCellValueFactory(new PropertyValueFactory("flname"));
				ffnameCol.setCellValueFactory(new PropertyValueFactory("ffname"));
				fmnameCol.setCellValueFactory(new PropertyValueFactory("fmname"));
				mlnameCol.setCellValueFactory(new PropertyValueFactory("mlname"));
				mfnameCol.setCellValueFactory(new PropertyValueFactory("mfname"));
				mmnameCol.setCellValueFactory(new PropertyValueFactory("mmname"));
				householdnumCol.setCellValueFactory(new PropertyValueFactory("householdnum"));
				householdincomeCol.setCellValueFactory(new PropertyValueFactory("householdincome"));
				brgyCol.setCellValueFactory(new PropertyValueFactory("brgy"));
				townCol.setCellValueFactory(new PropertyValueFactory("town"));
				provCol.setCellValueFactory(new PropertyValueFactory("province"));
				zipCol.setCellValueFactory(new PropertyValueFactory("zip"));
				contactCol.setCellValueFactory(new PropertyValueFactory("contact"));
				emailCol.setCellValueFactory(new PropertyValueFactory("email"));
				scholarshipCol.setCellValueFactory(new PropertyValueFactory("scholarship"));
				yrstartCol.setCellValueFactory(new PropertyValueFactory("yrstart"));
				yrendCol.setCellValueFactory(new PropertyValueFactory("yrend"));
				
				infoTable.setItems(null);
				infoTable.setItems(studentdata);
			
		}
		
		
		/*
		 * 
		 *------------------Searching for specific information--------------
		 * 
		 */
		
		public void search() {
			String input = searchField.getText();
			query = "SELECT * FROM `studentprofile` WHERE `idnumber` LIKE '%"+input+"%' || `fullname` LIKE '%"+input+"%' || `gender` LIKE '%"+input+"%' || `birthdate` LIKE '%"+input+"%' || `course` LIKE '%"
							+input+"%' || `yrlevel` LIKE '%"+input+"%' || `fatherlastname` LIKE '%"+input+"%' || `fatherfirstname` LIKE '%"+input+"%' || `fathermiddlename` LIKE '%"+
					input+"%' || `motherlastname` LIKE '%"+input+"%' || `motherfirstname` LIKE '%"+input+"%' || `mothermiddlename` LIKE '%"+input+"%' || "
							+ "`householdnumber` LIKE '%"+input+"%' || `householdincome` LIKE '%"+input+"%' || `barangay` LIKE '%"+input+"%' || `town` LIKE '%"+
					input+"%' || `province` LIKE '%"+input+"%' || `zipcode` LIKE '%"+input+"%' || `contact` LIKE '%"+input+"%' || `email` LIKE '%"+input+"%' || "
					+ "`scholarship` LIKE '%"+input+"%' || `yearstart` LIKE '%"+input+"%' || `yearend` LIKE '%"+input+"%'";
			fetchData();
		}
		
		
		/*
		 * 
		 *------------------Updating student information--------------
		 * 
		 */
		
		//Before Selection
		public void enableUpdate() {
			selectLabel.setVisible(true);
			studentCB.setVisible(true);
			selectButton.setVisible(true);
			cancelButton.setVisible(true);
			fetchFullnames();
		}
		
		//After Selection
		int no = 0;
		public void fetchSelected() {
			if(studentCB.getValue()!=null) {
				idField.requestFocus();
				saveChangesIM.setVisible(true);
				saveIM.setDisable(true);
				saveIM.setOpacity(0.46);
				String selected = studentCB.getSelectionModel().getSelectedItem().getName();
				saveChangesIM.setOpacity(1);
				saveChangesIM.setDisable(false);
				
				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
					Statement smt = conn.createStatement();
					ResultSet rs = smt.executeQuery("SELECT * FROM `studentprofile` WHERE `fullname` LIKE '%"+selected+"%'");
					
					while(rs.next()) {
						int num = rs.getInt("no");
						String id = rs.getString("idnumber");
						String lname = rs.getString("lastname");
						String fname = rs.getString("firstname");
						String extname = rs.getString("extname");
						String mname = rs.getString("middlename");
						String gender = rs.getString("gender");
						String bdate = rs.getString("birthdate");
						String course = rs.getString("course");
						String yrlevel = rs.getString("yrlevel");
						String flname = rs.getString("fatherlastname");
						String ffname = rs.getString("fatherfirstname");
						String fmname = rs.getString("fathermiddlename");
						String mlname = rs.getString("motherlastname");
						String mfname = rs.getString("motherfirstname");
						String mmname = rs.getString("mothermiddlename");
						String householdnum = rs.getString("householdnumber");
						String householdincome = rs.getString("householdincome");
						String brgy = rs.getString("barangay");
						String town = rs.getString("town");
						String prov = rs.getString("province");
						String zip = rs.getString("zipcode");
						String contact = rs.getString("contact");
						String email = rs.getString("email");
						String scholarship = rs.getString("scholarship");
						String yrstart = rs.getString("yearstart");
						String yrend = rs.getString("yearend");
						
						idField.setText(id); lnameField.setText(lname); fnameField.setText(fname); extnameField.setText(extname); mnameField.setText(mname);
						if(gender.equalsIgnoreCase("Male") ) {genderCB.getSelectionModel().select(0);} else {genderCB.getSelectionModel().select(1);}
						bdatePicker.setValue(LocalDate.parse(bdate)); courseField.setText(course); yrlevelField.setText(yrlevel); flnameField.setText(flname);
						ffnameField.setText(ffname); fmnameField.setText(fmname); mlnameField.setText(mlname); mfnameField.setText(mfname); mmnameField.setText(mmname);
						householdnumField.setText(householdnum); householdincomeField.setText(householdincome); brgyField.setText(brgy); townField.setText(town);
						provField.setText(prov); zipField.setText(zip); contactField.setText(contact); emailField.setText(email); scholarshipField.setText(scholarship);
						yrstartField.setText(yrstart); yrendField.setText(yrend);
						no = num;
					}
					
				}
				catch(Exception e) {
					Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("mySQL SERVER ERROR");
						alert.setHeaderText("");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
				}
			}
			else {
				Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("INVALID SELECTION ERROR");
					alert.setContentText("Please select a student first.");
					alert.showAndWait();
			}
			
		}
		
		//Canceling Edit
		public void cancel() {
			selectLabel.setVisible(false);
			studentCB.setVisible(false);
			selectButton.setVisible(false);
			cancelButton.setVisible(false);
			saveChangesIM.setVisible(false);
			saveIM.setDisable(false);
			saveIM.setOpacity(1);
			clear();
		}
		
		
		//After Editing
		public void saveChanges() {
			String id = idField.getText();
			String lname = lnameField.getText();
			String fname = fnameField.getText();
			String mname = mnameField.getText();
			String extname = extnameField.getText();
			String gender = genderCB.getSelectionModel().getSelectedItem().getGender();
			String bdate = bdatePicker.getValue().toString();
			String course = courseField.getText();
			String yrlevel = yrlevelField.getText();
			String fatherfname = ffnameField.getText();
			String fathermname = fmnameField.getText();
			String fatherlname = flnameField.getText();
			String motherfname = mfnameField.getText();
			String mothermname = mmnameField.getText();
			String motherlname = mlnameField.getText();
			String householdnum = householdnumField.getText();
			String householdincome = householdincomeField.getText();
			String barangay = brgyField.getText();
			String town = townField.getText();
			String province = provField.getText();
			String zip = zipField.getText();
			String contact = contactField.getText();
			String email = emailField.getText();
			String scholarship = scholarshipField.getText();
			String yearstart = yrstartField.getText();
			String yearend = yrendField.getText();
			String fullname = lname+" "+fname+" "+mname+" "+extname;
			
			if(id!=null || lname!=null || fname!=null || mname!=null || extname!=null || genderCB.getSelectionModel().isEmpty()) {
				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
					Statement smt = conn.createStatement();
					
					smt.executeUpdate("UPDATE `studentprofile` SET `idnumber` ='"+id+"', `lastname` = '"+lname+"', `firstname` = '"+fname+"', "
							+ "`extname` = '"+extname+"', `middlename` = '"+mname+"', `gender` = '"+gender+"', `birthdate` = '"+bdate+"', `course` = '"
							+course+"', `yrlevel` = '"+yrlevel+"', `fatherlastname` = '"+fatherlname+"', `fatherfirstname` = '"+fatherfname+"',"
							+ "`fathermiddlename` = '"+fathermname+"', `motherlastname` = '"+motherlname+"', `motherfirstname` = '"+motherfname+"',"
							+ "`mothermiddlename` = '"+mothermname+"', `householdnumber` = '"+householdnum+"', `householdincome` = '"+householdincome+"',"
							+ "`barangay` = '"+barangay+"', `town` = '"+town+"', `province` = '"+province+"', `zipcode` = '"+zip+"', `contact` = '"+
							contact+"', `email` = '"+email+"', `scholarship` = '"+scholarship+"', `yearstart` = '"+yearstart+"', `yearend` = '"+yearend+"',"
							+ "`fullname` = '"+fullname+"' WHERE `no` LIKE'%"+no+"%'");
					
					Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("UPDATE CONFIRMATION");
						alert.setHeaderText("PROCESS COMPLETE");
						alert.setContentText("Student information updated successfully!");
						alert.showAndWait();
							cancel();
							fetchData();
					
				}
				catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("WARNING");
						alert.setHeaderText("Database Error");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
				}
			}
			else {
				Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("Invalid Inputs Error");
					alert.setContentText("Please fill in the fields correctly and completely!");
					alert.showAndWait();
			}
		}
		
		
		
		/*
		 * 
		 *------------------Fetching Full Names of the Students to ComboBox--------------
		 * 
		 */
		
		public void fetchFullnames() {
			 namedata = FXCollections.observableArrayList();
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
				Statement smt = conn.createStatement();
				ResultSet rs = smt.executeQuery("SELECT `fullname` FROM `studentprofile`");
				
				if(rs != null) {	
					while(rs.next()) {
					String fullname = rs.getString("fullname");
					namedata.add(new fullnames(fullname));
					}
				}
				
			}
			catch(Exception e) {
				Alert erralert = new Alert(AlertType.ERROR);
					erralert.setTitle("mySQL SERVER ERROR");
					erralert.setHeaderText("");
					erralert.setContentText(e.getMessage());
					erralert.showAndWait();
			}
			studentCB.setItems(null);
			studentCB.setItems(namedata);
			
			studentCB.setConverter(new StringConverter<fullnames>() {
			    @Override
			    public String toString(fullnames object) {
			        return object.getName();
			    }

			    @Override
			    public fullnames fromString(String string) {
			        return null;
			    }
			});
		}
	
		
		/*
		 * 
		 *------------------Opening the Delete Form-------------- 
		 * 
		 */
		public void openDeleteForm() throws IOException {
			deleteController dc = new deleteController();
				dc.createWindow();
		}
		
		
		/*
		 * 
		 *------------------Opening the Administrative Password Editor Form--------------
		 * 
		 */
		public void openAdminForm() throws IOException {
			adminEditorController aec = new adminEditorController();
				aec.createWindow(); 
		}
	
		
		/*
		 * 
		 *------------------Logout Process--------------
		 * 
		 */
		public void logout() {
			logoutButton.getScene().getWindow().hide();
			String currentUser = userLabel.getText();
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
				Statement smt = conn.createStatement();
				smt.executeUpdate("UPDATE `users` SET `session` = 'inactive' WHERE `username` = '"+currentUser+"'");
			}
			
			catch(Exception e) {
				Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("mySQL SERVER ERROR");
					alert.setHeaderText("");
					alert.setContentText(e.getMessage());
					alert.showAndWait();
			}
			
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("loginWindow.fxml"));
				AnchorPane root = (AnchorPane) loader.load();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setResizable(false);
				stage.getIcons().add(new Image("resources/usm seal icon.png"));
				stage.setTitle("USM Student Scholarship Information System_Login Form");
				stage.show();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
	}
		
		public void export(@SuppressWarnings("rawtypes") TableView infoTable) {
			 HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
		        HSSFSheet hssfSheet=  hssfWorkbook.createSheet("Sheet1");
		        HSSFRow firstRow= hssfSheet.createRow(0);

		        ///set titles of columns
		        for (int i=0; i<infoTable.getColumns().size();i++){

		            firstRow.createCell(i).setCellValue(((Labeled) infoTable.getColumns().get(i)).getText());

		        }


		        for (int row=0; row<infoTable.getItems().size();row++){

		            HSSFRow hssfRow= hssfSheet.createRow(row+1);

		            for (int col=0; col<infoTable.getColumns().size(); col++){

		                @SuppressWarnings("unchecked")
						Object celValue = ((TableColumn<studentInfo, Number>) infoTable.getColumns().get(col)).getCellObservableValue(row).getValue();

		                try {
		                    if (celValue != null && Double.parseDouble(celValue.toString()) != 0.0) {
		                        hssfRow.createCell(col).setCellValue(Double.parseDouble(celValue.toString()));
		                    }
		                } catch (  NumberFormatException e ){

		                    hssfRow.createCell(col).setCellValue(celValue.toString());
		                }

		            }

		        }

		        //save excel file and close the workbook
		        try {
		            hssfWorkbook.write(new FileOutputStream("WorkBook.xls"));
		            hssfWorkbook.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		    
		}
}






