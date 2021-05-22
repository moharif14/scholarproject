package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class accountController implements Initializable{
	
	@FXML Button enterButton, createButton;
	@FXML TextField usernameField, seePWF1, seePWF2;
	@FXML PasswordField passwordField1, passwordField2, adminPF;
	@FXML RadioButton studentRB, adminRB;
	@FXML Label whatisLabel, adminLabel;
	@FXML TextArea passwordHintTA;
	@FXML ImageView seeIM, hideIM;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Platform.runLater(new Runnable() {
			public void run() {
				studentRB.requestFocus();
			}
		});
	}

	
	/*
	 * 
	 *------------------Constructing the Window--------------
	 * 
	 */
	
	public void createWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("accountWindow.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("User Account Form");
        stage.getIcons().add(new Image("resources/usm seal icon.png"));
        stage.show();
	}
	
	
	/*
	 * 
	 *------------------Selecting Sign up Option--------------
	 * 
	 */
	String userType = "";
	
	public void selectStudent() {
		userType = "student";
		adminRB.setSelected(false);
		adminPF.setVisible(false);
		enterButton.setVisible(false);
		whatisLabel.setVisible(false);
		adminLabel.setVisible(false);
		createButton.setDisable(false);
	}
	public void selectAdmin() {
		userType = "admin";
		studentRB.setSelected(false);
		adminPF.setVisible(true);
		enterButton.setVisible(true);
		whatisLabel.setVisible(true);
		adminLabel.setVisible(true);
		createButton.setDisable(true);
	}
	
	
	/*
	 * 
	 *------------------Hover Effects--------------
	 * 
	 */
	public void adminLabelHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			adminLabel.setEffect(glow);
	}
	public void hoverOut() {
		Glow glow = new Glow();
		glow.setLevel(0);
		adminLabel.setEffect(glow);
	}
	public void createButtonHoverIn() {
		createButton.setTextFill(Color.rgb(255, 69, 0));
	}
	public void createButtonHoverOut() {
		createButton.setTextFill(Color.BLACK);
	}
	public void enterButtonHoverIn() {
		enterButton.setTextFill(Color.rgb(255, 69, 0));
	}
	public void enterButtonHoverOut() {
		enterButton.setTextFill(Color.BLACK);
	}
	
	
	/*
	 * 
	 *------------------Showing Special Administrative Password Details--------------
	 * 
	 */
	public void showAdminPasswordDetails() {
		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ADMINISTRATIVE PASSWORD DETAILS");
			alert.setHeaderText("ABOUT THE SPECIAL ADMINISTRATIVE PASSWORD");
			alert.setContentText("When you are signing up to this system as an administrator, you need to enter a special password dedicated "
					+ "only to the individuals who are authorized to use this system as administrators. By that, you are not allowed to "
					+ "sign up as an administrator if you are a student, because the Special Administrative Password is only given by the "
					+ "system developer to the authorized individuals. Only the admins can change it if needed. Please be reminded that only administrators "
					+ "can access special features of this system such as, editing a student profile, adding a new student to "
					+ "the list of scholars, and removing a student information from the system's database.");
			alert.showAndWait();
	}
	
	
	/*
	 * 
	 *------------------Signing up Process--------------
	 * 
	 */
	public void signUp() {
		String username = usernameField.getText();
		String password = passwordField1.getText();
		String password2 = passwordField2.getText();
		String hint = passwordHintTA.getText();
		String finalPassword = encrypt(password);
		
			if(username.equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("EMPTY INPUTS ERROR");
					alert.setContentText("Please fill in the form completely.");
					alert.showAndWait();
			}
			else if(username!=null && !password.equals(password2)) {
				Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("PASSWORD MISSMATCHED ERROR");
					alert.setContentText("The password you entered didn't match with the password you confirmed. Please try again!");
					alert.showAndWait();
			}
			else if(username.equals(null) && password.equals(null) && password2.equals(null)) {
				Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setHeaderText("EMPTY INPUTS ERROR");
					alert.setContentText("Please fill the form completely.");
					alert.showAndWait();
			}
			else if(username!=null && password.equals(password2)) {
				
				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
					Statement smt = conn.createStatement();
					
					smt.execute("INSERT INTO `users`(`username`, `password`, `hint`, `usertype`, `session`)"
							+ "VALUES('"+username+"', '"+finalPassword+"', '"+hint+"', '"+userType+"', 'inactive')");
					
				}
				catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("");
						alert.setHeaderText("Database Error");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
				}
				
				Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("SIGN UP CONFIRMATION");
					alert.setHeaderText("PROCESS COMPLETE");
					alert.setContentText("Successfully signed up! You may login now.");
					alert.showAndWait();
					clear();
			}
			
	}
	
	/*
	 * 
	 *------------------Enable Create Account Button when user attempts to create an Administrator type account--------------
	 * 
	 */
	public void enableButton() {
		String inputPassword = adminPF.getText();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
			Statement smt = conn.createStatement();
			ResultSet rs = smt.executeQuery("SELECT * FROM `adminspecialpassword`");
			
			while(rs.next()) {
				String dbpassword = rs.getString("password");
					if(inputPassword.equals(decrypt(dbpassword))) {
						createButton.setDisable(false);
					}
					else {
						Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("WARNING");
							alert.setHeaderText("INVALID INPUT ERROR");
							alert.setContentText("The password you entered does not match with the current Admin Password. Please try again.");
							alert.showAndWait();
					}
			}
			
		}
		catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("");
				alert.setHeaderText("Database Error");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
		}
	}

	
	/*
	 * 
	 *------------------Peeking passwords--------------
	 * 
	 */
	public void see() {
		hideIM.setVisible(true);
		seeIM.setVisible(false);
		seePWF1.setVisible(true); seePWF1.setText(passwordField1.getText());
		seePWF2.setVisible(true); seePWF2.setText(passwordField2.getText());
		passwordField1.setVisible(false);
		passwordField2.setVisible(false);
	}
	public void hide() {
		hideIM.setVisible(false);
		seeIM.setVisible(true);
		seePWF1.setVisible(false);
		seePWF2.setVisible(false);
		passwordField1.setVisible(true);
		passwordField2.setVisible(true);
	}
	
	
	/*
	 * 
	 *------------------Clearing things up--------------
	 * 
	 */
	public void clear() {
		usernameField.setText(null);
		passwordField1.setText(null);
		passwordField2.setText(null);
		seePWF1.setVisible(false);
		seePWF2.setVisible(false);
		hideIM.setVisible(false);
		studentRB.setSelected(false);
		adminRB.setSelected(false);
		adminPF.setVisible(false);
		enterButton.setVisible(false);
		whatisLabel.setVisible(false);
		adminLabel.setVisible(false);
		createButton.setDisable(false);
		passwordHintTA.setText(null);
		createButton.setDisable(true);
	}
	
	
	/*
	 * 
	 *------------------Encrypting and Decrypting the Password--------------
	 * 
	 */
	
	public static String encrypt(String input) {
		Base64.Encoder encoder = Base64.getMimeEncoder();
        String message = input;
        String key = encoder.encodeToString(message.getBytes());
        return key;
	}
	
	public static String decrypt(String input) {
		  Base64.Decoder decoder = Base64.getMimeDecoder();
	      String key = new String(decoder.decode(input));
	      return key;
	}
	
}






