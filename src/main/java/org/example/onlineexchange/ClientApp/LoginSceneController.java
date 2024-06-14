package org.example.onlineexchange.ClientApp;


import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.onlineexchange.EmailSender;
import org.example.onlineexchange.User;


import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginSceneController implements Initializable {

    private static final String OPERATOR_EMAIL = "online.exchange.project@gmail.com";
    private static final String OPERATOR_EMAIL_PASSWORD = "pnixokhcnqrixqmp";
    @FXML
    Button loginBtn;
    @FXML
    ImageView captchaImageView;
    @FXML
    TextField captchaTxf;
    @FXML
    Label errLbl;
    @FXML
    VBox loginVb;
    @FXML
    Button signupBtn;

    @FXML
    TextField firstNameTxf;
    @FXML
    TextField lastNameTxf;
    @FXML
    TextField phoneNumberTxf;
    @FXML
    TextField emailTxf;
    @FXML
    TextField usernameTxf;
    @FXML
    TextField passwordTxf;
    @FXML
    TextField repeatPasswordTxf;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        reloadCaptcha();

        loginVb.getChildren().remove(errLbl);
    }

    void reloadCaptcha(){
        Random random = new Random();
        File file = new File("src\\main\\resources\\captcha\\samples");
        File[] captchaFiles = file.listFiles();
        assert captchaFiles != null;
        String path = captchaFiles[random.nextInt(captchaFiles.length)].getAbsolutePath();
        captchaImageView.setImage(new Image("file:/" + path));
    }

    @FXML
    void captchaMouseClicked(){
        reloadCaptcha();
    }

    boolean captchaCheck(){
        String captcha = captchaImageView.getImage().getUrl();
        captcha = captcha.substring(captcha.lastIndexOf('/') + 1, captcha.lastIndexOf('.'));
        return captcha.equals(captchaTxf.getText());
    }

    void printErr(String err){
        if(!loginVb.getChildren().contains(errLbl))loginVb.getChildren().add(loginVb.getChildren().indexOf(captchaTxf) + 1, errLbl);
        errLbl.setText(err);
        System.err.println(err);
    }

    @FXML
    protected void loginBtnHandler(){
        if(!captchaCheck()){
            printErr("captcha is incorrect");
            return;
        }

        User currentUser = null;

//        for(User user : User.allUsers){
//            if(Objects.equals(user.getUsername(), usernameTxf.getText())){
//                currentUser = user;
//                break;
//            }
//        }

//        if (currentUser == null){
//            printErr("User not found");
//            return;
//        }
//
//        if(!Objects.equals(passwordTxf.getText(), currentUser.getPassword())){
//            printErr("Password not match");
//            return;
//        }

        ClientSocket cl = null;

        try {
            cl = ClientSocket.getClientSocket();
        } catch (IOException e) {
            printErr("connect field");
            return;
        }

        cl.send("[LOGIN]," + usernameTxf.getText() + "," + passwordTxf.getText());

//        currentUser = User.getFromSocket(cl);


        System.out.println("hi " + currentUser.getFirstName());// this part is for forward app to user panel
    }

    @FXML
    protected void signUpBtnHandler(ActionEvent e) {
        if(!captchaCheck()){
            printErr("captcha is incorrect");
            return;
        }

        if (!Objects.equals(passwordTxf.getText(), repeatPasswordTxf.getText())){
            printErr("passwords not mach");
        }

        try {
            new User(firstNameTxf.getText(), lastNameTxf.getText(), phoneNumberTxf.getText(),
                    emailTxf.getText(), usernameTxf.getText(), passwordTxf.getText());
        }catch (RuntimeException e1){
            printErr(e1.getMessage());
            return;
        }

        printErr("sign up successfully done");
        errLbl.setTextFill(Color.GREEN);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));
        pauseTransition.setOnFinished(actionEvent -> {
            try {
                loginLblHandler(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        pauseTransition.play();

    }

    @FXML
    void signUpLblHandler(MouseEvent e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(clientApplication.class.getResource("signUp-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @FXML
    void loginLblHandler(Event e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(clientApplication.class.getResource("login-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @FXML
    void forgetLblHandler(MouseEvent e){
        final String[] email = new String[1];
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Forget password");
        dialog.setHeaderText("Enter your email: ");

        dialog.showAndWait().ifPresent(s -> email[0] = s);

        if(email[0].trim().matches("[a-zA-z\\.0-9_-]+@[a-zA-Z0-9]+\\.[a-z]+")){
            User currentUser = null;

            for (User user : User.allUsers){
                if(Objects.equals(user.getEmail(), email[0])){
                    currentUser = user;
                    break;
                }
            }
            if(currentUser == null){
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setHeaderText("email not found");
                err.showAndWait();
                return;
            }

            EmailSender emailSender = new EmailSender(OPERATOR_EMAIL, OPERATOR_EMAIL_PASSWORD);
            try {
                emailSender.send("Your Requested Password",
                        """                
                                Dear""" + ' ' + currentUser.getUsername() + '\n' +
                                """
                                
                                We received a request to send you the password for your account associated with this email address. Please find your password below:
                                                            
                                Password:""" + ' ' + currentUser.getPassword() + '\n' +
                                """                     
                                
                                For security reasons, we recommend that you keep your password confidential and do not share it with anyone. If you have any concerns about the security of your account, please consider changing your password.
                                                            
                                If you have any questions or need further assistance, please do not hesitate to contact us.
                                                            
                                Best regards,
                                Support Team
                                """, currentUser.getEmail());
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText("We have sent you an email containing your username and password");
                info.showAndWait();
                return;


            } catch (MessagingException ex) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setHeaderText("There is a Problem, try again");
                err.showAndWait();
                return;
            }
        }

        Alert err = new Alert(Alert.AlertType.ERROR);
        err.setHeaderText("this email is not valid, try again");
        err.showAndWait();
    }
}