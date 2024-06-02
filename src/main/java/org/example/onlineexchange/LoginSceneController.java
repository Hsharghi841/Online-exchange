package org.example.onlineexchange;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



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
    protected void loginBtnHandler(){
        System.out.println(captchaCheck());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reloadCaptcha();
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
}