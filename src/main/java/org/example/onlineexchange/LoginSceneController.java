package org.example.onlineexchange;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;



import java.io.File;
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
    protected void loginBtnHandler(){
        if(!captchaCheck()){
            printErr("captcha is incorrect");
            return;
        }


        System.out.println("hi hadi");
    }

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
        if(!loginVb.getChildren().contains(errLbl))loginVb.getChildren().add(loginVb.getChildren().indexOf(loginBtn), errLbl);
        errLbl.setText(err);
        System.err.println(err);
    }
}