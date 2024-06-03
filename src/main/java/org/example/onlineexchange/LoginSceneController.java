package org.example.onlineexchange;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        stage = (Stage) loginBtn.getScene().getWindow();



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
    protected void signUpBtnHandler(){
        if(!captchaCheck()){
            printErr("captcha is incorrect");
            return;
        }


        System.out.println("hi hadi");
    }

    @FXML
    void signUpLblHandler(MouseEvent e) throws IOException {
        Stage stage = (Stage) ((Label)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("signUp-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @FXML
    void loginLblHandler(MouseEvent e) throws IOException {
        Stage stage = (Stage) ((Label)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("login-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}