import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXMain extends Application {
    Group loginGroup;
    GridPane loginPane;
    Scene loginScene;
    TextField userField;
    TextField passField;
    Label loginTitle;
    Button loginBtn;

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

        loginTitle = new Label("Log In");
        loginTitle.getStyleClass().clear();
        loginTitle.getStyleClass().add("title");

        userField = new TextField("Username");
        passField = new TextField("Password");

        loginBtn = new Button("LOG IN");
        loginBtn.getStyleClass().clear();
        loginBtn.getStyleClass().add("buttons");

        //index format is: (column, row, takes up x cols, takes up x rows)
        loginPane.add(loginTitle, 0, 0);

        //TODO: Connect fields to logic
        loginPane.add(userField, 0, 1);
        loginPane.add(passField, 0, 2);
        loginPane.add(loginBtn, 0, 3);
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
