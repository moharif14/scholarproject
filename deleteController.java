package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;


public class deleteController implements Initializable{
	
	@FXML ComboBox<fullnames> studentCB = new ComboBox<fullnames>();
	@FXML ObservableList<fullnames>namedata;
	@FXML Button deleteButton, selectButton;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fetchFullnames();
	}
	
	
	/*
	 * 
	 *------------------Constructing the Window-------------- 
	 * 
	 */
	public void createWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteWindow.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Deletion Form");
        stage.getIcons().add(new Image("resources/recycle icon.png"));
        stage.show();
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
	 *------------------Selecting a Student to be deleted--------------
	 * 
	 */
	public void select() {
			if(studentCB.getValue()!=null) {
				deleteButton.setDisable(false);
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("WARNING");
					alert.setHeaderText("INVALID SELECTION ERROR");
					alert.setContentText("Please select a student first!");
					alert.showAndWait();
			}
	}
	

	/*
	 * 
	 *------------------Removing the selected student--------------
	 * 
	 */
	public void delete() {
		String selectedName = studentCB.getSelectionModel().getSelectedItem().getName();
		Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("WARNING");
			alert.setHeaderText("WARNING!");
			alert.setContentText("Are you sure you want to remove "+selectedName+" from this System? You cannot undo this action.");
			Optional<ButtonType>option = alert.showAndWait();
			
			if(option.get() == ButtonType.OK) {
				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
					Statement smt = conn.createStatement();
					smt.execute("DELETE FROM `studentprofile` WHERE `fullname` LIKE '%"+selectedName+"%'");
					Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setTitle("COMPLETE");
						alert2.setHeaderText("PROCESS COMPLETE!");
						alert2.setContentText("All data of "+selectedName+" have been successfully removed from the system!");
						alert2.showAndWait();
						studentCB.getSelectionModel().clearSelection();
				}
				catch(Exception e) {
					Alert erralert = new Alert(AlertType.ERROR);
						erralert.setTitle("mySQL SERVER ERROR");
						erralert.setHeaderText("");
						erralert.setContentText(e.getMessage());
						erralert.showAndWait();
				}
			}
			else {
				studentCB.getSelectionModel().clearSelection();
				deleteButton.setDisable(true);
		}		
	}
}








