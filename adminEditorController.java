package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class adminEditorController implements Initializable{
	
	@FXML ImageView seeIM, hideIM;
	@FXML Button saveButton;
	@FXML TextField seeCurPasswordField, seeNewPasswordField;
	@FXML PasswordField curPasswordField, newPasswordField;

	@Override
	public void initialize(URL ulr, ResourceBundle rb) {
		
	}
	
	/*
	 * 
	 *------------------Constructing the Window--------------
	 * 
	 */
	
	public void createWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("adminEditorWindow.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Administrative Password Editor");
        stage.getIcons().add(new Image("resources/usm seal icon.png"));
        stage.show();
	}
	
	
	/*
	 * 
	 *------------------Saving changes--------------
	 * 
	 */
	String oldPassword = "";
	public void save() {
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
				Statement smt = conn.createStatement();
				ResultSet rs = smt.executeQuery("SELECT * FROM `adminspecialpassword`");
				
				while(rs.next()) {
					String password = rs.getString("password");
					oldPassword = password;
				}
			}
			catch(Exception e) {
				Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("mySQL SERVER ERROR");
					alert.setHeaderText("");
					alert.setContentText(e.getMessage());
					alert.showAndWait();
			}
			saveFinal();
	}
	
	public void saveFinal() {
		String newPassword = newPasswordField.getText();
		String inputPassword = curPasswordField.getText();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
			Statement smt = conn.createStatement();
			
			if(decrypt(oldPassword).equals(inputPassword)) {
				smt.executeUpdate("UPDATE `adminspecialpassword` SET `password` = '"+encrypt(newPassword)+"'");
				Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("CONFIRMATION");
					alert.setHeaderText("PROCESS COMPLETE");
					alert.setContentText("Password changed successfully!");
					alert.showAndWait();
					clear();
			}
			else {
				Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("INVALID INPUT");
					alert.setContentText("The password you entered doesn't match with the current admin password. Please try again.");
					alert.showAndWait();
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
	 *------------------Encrypting the Password--------------
	 * 
	 */
	
	public static String encrypt(String input) {
		Base64.Encoder encoder = Base64.getMimeEncoder();
        String message = input;
        String key = encoder.encodeToString(message.getBytes());
        return key;
	}
	
	/*
	 * 
	 *------------------Decrypting the Password--------------
	 *
	 */
	public static String decrypt(String input) {
		  Base64.Decoder decoder = Base64.getMimeDecoder();
	      String key = new String(decoder.decode(input));
	      return key;
	}

	/*
	 * 
	 *------------------Viewing and Hiding Passwords--------------
	 * 
	 */
	public void showPassword() {
		hideIM.setVisible(true);
		seeIM.setVisible(false);
		curPasswordField.setVisible(false);
		newPasswordField.setVisible(false);
		seeCurPasswordField.setVisible(true);
		seeNewPasswordField.setVisible(true);
		seeCurPasswordField.setText(curPasswordField.getText());
		seeNewPasswordField.setText(newPasswordField.getText());
		
	}
	public void hidePassword() {
		hideIM.setVisible(false);
		seeIM.setVisible(true);
		curPasswordField.setVisible(true);
		newPasswordField.setVisible(true);
		seeCurPasswordField.setVisible(false);
		seeNewPasswordField.setVisible(false);
	}
	
	/*
	 * 
	 *------------------Clearing Everything--------------
	 * 
	 */
	public void clear() {
		hidePassword();
		curPasswordField.setText(null);
		newPasswordField.setText(null);
	}
	
	
	/*
	 * 
	 *------------------Hover Effects--------------
	 * 
	 */
	public void hoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			saveButton.setEffect(glow);
	}
	public void hoverOut() {
		Glow glow = new Glow();
			glow.setLevel(0);
			saveButton.setEffect(glow);
	}

}
