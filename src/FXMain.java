import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXMain extends Application {
    Group loginGroup;
    GridPane loginPane;
    Scene loginScene;
    TextField userField;
    TextField passField;

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

        loginGroup.getChildren().add(loginPane);
        loginScene = new Scene(loginGroup, 400, 450);
        loginStage.setTitle("Login");
        loginStage.setResizable(false);
        loginStage.setScene(loginScene);
        loginStage.show();
    }
}
