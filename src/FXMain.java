import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.rgb;

public class FXMain extends Application {

    //login vars
    Group loginGroup;
    GridPane loginPane;
    Scene loginScene;
    TextField userField;
    TextField passField;
    Label loginTitle;
    Label loginSubtitle;
    Button loginBtn;
    Button signupBtn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage loginStage) {

        //** LOGIN WINDOW **

        loginGroup = new Group();
        loginPane = new GridPane();

        loginPane.setMaxSize(400, 450);			//layout dimensions
        loginPane.setMinSize(400, 450);
        loginPane.setVgap(15);
        loginPane.setHgap(10);


        loginTitle = new Label("Welcome to the \nClass Scheduling Assistant!");
        loginTitle.getStyleClass().clear();
        loginTitle.getStyleClass().add("title");

        loginSubtitle = new Label("LOG IN");
        loginSubtitle.getStyleClass().clear();
        loginSubtitle.getStyleClass().add("subtitle");


        userField = new TextField("Username");
        passField = new TextField("Password");

        //TODO: Login button action event
        loginBtn = new Button("Log In");
        loginBtn.getStyleClass().clear();
        loginBtn.getStyleClass().add("buttons");

        //TODO: Sign up button action event
        signupBtn = new Button("Sign Up");
        signupBtn.getStyleClass().clear();
        signupBtn.getStyleClass().add("buttons2");


        //index format is: (column, row, takes up x cols, takes up x rows)
        loginPane.add(loginTitle, 0, 0);
        loginPane.add(loginSubtitle, 0, 1);

        //TODO: Connect fields to logic
        loginPane.add(userField, 0, 2);
        loginPane.add(passField, 0, 3);
        loginPane.add(loginBtn, 0, 4);
        loginPane.add(signupBtn, 0, 5);

        loginPane.setPadding(new Insets(20));
        loginPane.setAlignment(Pos.CENTER);

        loginGroup.getChildren().add(loginPane);
        loginScene = new Scene(loginGroup, 400, 450);
        loginScene.getStylesheets().add("projStyles.css");
        loginStage.setTitle("Login");
        loginStage.setResizable(false);
        loginStage.setScene(loginScene);
        loginStage.show();
    }
}
