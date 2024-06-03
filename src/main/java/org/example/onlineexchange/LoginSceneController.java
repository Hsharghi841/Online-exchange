package org.example.onlineexchange;


import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginSceneController implements Initializable {

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


        System.out.println("hi hadi");
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
        stage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("signUp-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @FXML
    void loginLblHandler(Event e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("login-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}