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
import org.example.onlineexchange.Request;
import org.example.onlineexchange.ClientApp.User;


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
    protected void loginBtnHandler(ActionEvent e) throws IOException {
        if(!captchaCheck()){
            printErr("captcha is incorrect");
            return;
        }


        ClientSocket cl = null;

        try {
            cl = ClientSocket.getClientSocket();
        } catch (IOException e1) {
            printErr("connect field");
            return;
        }

        cl.send(new Request("LOGIN", usernameTxf.getText(), passwordTxf.getText()).toString());

        Request r = Request.requestProcessor(cl.receive());

        if(Objects.equals(r.getCommand(), "FAILED")){
            printErr("a problem has happened. try again");
            return;
        }

        if(Objects.equals(r.getCommand(), "USER NOT FOUND")){
            printErr("This username is not registered");
            return;
        }

        if(Objects.equals(r.getCommand(), "PASSWORD NOT MATCH")){
            printErr("password is incorrect");
            return;
        }

        if(!Objects.equals(r.getCommand(), "SUCCESS"))return;


        System.out.println(STR."hi \{usernameTxf.getText()}");// this part is for forward app to user panel
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("home-page.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));

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

        User current = null;
        try {
            current = new User(firstNameTxf.getText(), lastNameTxf.getText(), phoneNumberTxf.getText(),
                    emailTxf.getText(), usernameTxf.getText(), passwordTxf.getText());
        }catch (RuntimeException e1){
            printErr(e1.getMessage());
            return;
        }

        ClientSocket clientSk = null;
        try {
            clientSk = ClientSocket.getClientSocket();
        } catch (IOException ex) {
            printErr("connection failed");
            return;
        }

        clientSk.send(new Request("SIGN IN",
                current.getFirstName(), current.getLastName(),
                current.getPhoneNumber(), current.getEmail(),
                current.getUsername(), current.getPassword()).toString());

        Request r = new Request(clientSk.receive());

        if(Objects.equals(r.getCommand(), "FAILED")){
            printErr("failed");
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
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("signUp-view.fxml")).load(),
                stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @FXML
    void loginLblHandler(Event e) throws IOException {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(ClientApplication.class.getResource("login-view.fxml")).load(),
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
            ClientSocket cs;
            try {
                cs = ClientSocket.getClientSocket();
            } catch (IOException ex) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setHeaderText("connection failed");
                err.showAndWait();
                return;
            }

            cs.send(new Request("FORGET PASSWORD", email[0]).toString());

            Request result = Request.requestProcessor(cs.receive());

            if(Objects.equals(result.getCommand(), "EMAIL NOT FOUND")) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setHeaderText("email not found");
                err.showAndWait();
                return;
            }

            if(Objects.equals(result.getCommand(), "SUCCESS")){
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText("We have sent you an email containing your username and password");
                info.showAndWait();
                return;
            }

            if(Objects.equals(result.getCommand(), "FAILED")){
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