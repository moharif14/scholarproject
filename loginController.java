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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Glow;

public class loginController implements Initializable{
	
	@FXML TextField usernameField;
	@FXML PasswordField passwordField;
	@FXML Label forgotPasswordLabel;
	@FXML Button signinButton, signupButton;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Platform.runLater(new Runnable() {
			public void run() {
				signinButton.requestFocus();
			}
		});
	}
	
	//--------------------Hover Effects----------------------//
	
	public void signinHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			signinButton.setEffect(glow);
	}
	public void signunHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			signupButton.setEffect(glow);
	}
	public void forgotHoverIn() {
		Glow glow = new Glow();
			glow.setLevel(0.5);
			forgotPasswordLabel.setEffect(glow);
			forgotPasswordLabel.setUnderline(true);
	}
	public void hoverOut() {
		Glow glow = new Glow();
			glow.setLevel(0);
			signinButton.setEffect(glow);
			signupButton.setEffect(glow);
			forgotPasswordLabel.setEffect(glow);
			forgotPasswordLabel.setUnderline(false);
	}
	
	

	//--------------------Login Process----------------------//
	public void login() throws IOException {
		if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("INVALID INPUTS ERROR");
				alert.setHeaderText("USERNAME/PASSWORD ERROR");
				alert.setContentText("Username or Password incorrect. Please check the spelling of your username and password, and try again.");
				alert.showAndWait();
		}
		else {
			String username = usernameField.getText();
			String password = passwordField.getText();
			String dbusername = "";
			String dbpassword = "";

				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
					Statement smt = conn.createStatement();
					ResultSet rs = smt.executeQuery("SELECT * FROM `users` WHERE `username` = '"+username+"'");
					
					while(rs.next()) {
						String name = rs.getString("username");
						String pw = rs.getString("password");
							dbusername = name;
							dbpassword = pw;
					}
					
					if(username.equals(dbusername) && password.equals(decrypt(dbpassword))) {
								updateSession();
								usernameField.setText(null);
								passwordField.setText(null);
								signinButton.getScene().getWindow().hide();
								mainUIController mainUI = new mainUIController();
									mainUI.createWindow();
					}
					else {
						Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("INVALID INPUTS ERROR");
							alert.setHeaderText("USERNAME/PASSWORD ERROR");
							alert.setContentText("Username or Password incorrect. Please check the spelling of your username and password, and try again.");
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
		
	}
	
	public void updateSession() {
		String username = usernameField.getText();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
			Statement smt = conn.createStatement();
			smt.executeUpdate("UPDATE `users` SET `session` = 'active' WHERE `username` = '"+username+"'");
		}
		catch(Exception e){
			Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("mySQL SERVER ERROR");
				alert.setHeaderText("");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
		}
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
	 *------------------Opening User Account Form-------------- 
	 *
	 */
	
	public void openAccountForm() throws IOException {
		accountController ac = new accountController();
			ac.createWindow();
	}
	
	
	/*
	 * 
	 *------------------Forgotten Password-------------- 
	 *
	 */
	public void forgotPassword() {
		String user = usernameField.getText();
		if(user.equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("EMPTY USERNAME");
				alert.setHeaderText("EMPTY USERNAME INPUT");
				alert.setContentText("Please enter your username first. If the 'Forgot Password' doesn't work even after you enter your "
						+ "username, it may be because you're not registered yet as a user. You might wanna sign up first.");
				alert.showAndWait();
		}
		else {
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/usmscholardb");
				Statement smt = conn.createStatement();
				ResultSet rs = smt.executeQuery("SELECT * FROM `users` WHERE `username` = '"+user+"'");
				
				if(rs != null) {	
					while(rs.next()) {
					String hint = rs.getString("hint");				
						Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("PASSWORD HINT");
							alert.setHeaderText("PASSWORD HINT");
							alert.setContentText("Your password hint is\n\n"+hint+"\n\nThis might help you remember your password.");
							alert.showAndWait();
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
		}
	}
	
}









