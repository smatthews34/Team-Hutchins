//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Insets;
//import javafx.geometry.Orientation;
//import javafx.geometry.Pos;
//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.CornerRadii;
//import javafx.scene.layout.GridPane;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import static javafx.scene.paint.Color.rgb;
//
//public class FXMain extends Application {
//    User user;
//    CourseList cl;
//
//    //login vars
//    boolean loggedIn;
//    Group loginGroup;
//    GridPane loginPane;
//    Scene loginScene;
//    TextField userField;
//    PasswordField passField;
//    Label loginTitle;
//    Label loginSubtitle;
//    Button loginBtn;
//    Button signupBtn;
//    Image undoImg;
//    Image redoImg;
//
//
//    public static void setProperties(GridPane gp, int sizeH, int sizeV, int vGap, int hGap, int insets){
//        gp.setMinSize(sizeH, sizeV);
//        gp.setMaxSize(sizeH, sizeV);
//
//        gp.setVgap(vGap);
//        gp.setHgap(hGap);
//
//        gp.setPadding(new Insets(insets));
//
//    }
//
//    final EventHandler<ActionEvent> loginHandler = new EventHandler<ActionEvent>(){
//        @Override
//        public void handle(ActionEvent event) {
//            //LOGIN LOGIC
//            String username = userField.getText();
//            String password = passField.getText();
//
//            System.out.println(username);
//            System.out.println(password);
//
//            Login l = new Login(username, password);
//            User potentialUser = l.loginSubmit();
//
//            if (potentialUser == null){
//                Label badLoginLbl = new Label("Username or Password are invalid, please try again");
//                loginPane.add(badLoginLbl, 0, 6);
//            }
//            else {
//                user = new User(potentialUser.username, potentialUser.password, potentialUser.name);
//                loggedIn = true;
//
//                //https://stackoverflow.com/questions/13567019/close-fxml-window-by-code-javafx
//                final Node source = (Node) event.getSource();
//                final Stage stage = (Stage) source.getScene().getWindow();
//                stage.close();
//            }
//
//            //SEARCH WINDOW
//            if (loggedIn) {
//
//                Group searchGroup = new Group();
//                Stage searchStage = new Stage();
//
//                SplitPane searchSplit = new SplitPane();
//                searchSplit.getStyleClass().add("pane");
//                searchSplit.setOrientation(Orientation.HORIZONTAL);
//
//                GridPane searchPane = new GridPane();
//                setProperties(searchPane, 400, 500, 15, 10, 0);
//                searchPane.setAlignment(Pos.CENTER);
//
//                Button undoBtn = new Button();
//                undoImg = new Image("undo.png");
//                ImageView undoView = new ImageView(undoImg);
//                redoImg = new Image("redo.png");
//                ImageView redoView = new ImageView(redoImg);
//                undoBtn.setGraphic(undoView);
//                Button redoBtn = new Button();
//                redoBtn.setGraphic(redoView);
//
//                GridPane btnPane = new GridPane();
//                btnPane.add(undoBtn, 0, 0);
//                btnPane.add(redoBtn, 1, 0);
//                btnPane.setHgap(5);
//                btnPane.setVgap(5);
//
//                undoBtn.getStyleClass().clear();
//                undoBtn.getStyleClass().add("mini-buttons");
//                redoBtn.getStyleClass().clear();
//                redoBtn.getStyleClass().add("mini-buttons");
//
//                Label searchLbl = new Label("Search for Courses");
//                searchLbl.getStyleClass().clear();
//                searchLbl.getStyleClass().add("title");
//
//                TextField searchField = new TextField();
//                searchField.setPromptText("Search...");
//
//                Button searchBtn = new Button("Search");
//                searchBtn.getStyleClass().clear();
//                searchBtn.getStyleClass().add("buttons");
//
//                Label filterLbl = new Label("Filter by...");
//
//                ObservableList<String> dayFilters =
//                        FXCollections.observableArrayList(
//                                "",
//                                "MWF",
//                                "TR"
//                        );
//                //TODO: Add all times
//                ObservableList<String> timeFilters =
//                        FXCollections.observableArrayList(
//                                "",
//                                "8:00:00",
//                                "9:00:00",
//                                "12:30:00"
//                        );
//                //TODO: Add all depts
//                ObservableList<String> deptFilters =
//                        FXCollections.observableArrayList(
//                                "",
//                                "HUMA",
//                                "COMP"
//                        );
//                ComboBox dayFilterBox = new ComboBox(dayFilters);
//                dayFilterBox.setPromptText("Day");
//                ComboBox timeFilterBox = new ComboBox(timeFilters);
//                timeFilterBox.setPromptText("Time");
//                ComboBox deptFilterBox = new ComboBox(deptFilters);
//                deptFilterBox.setPromptText("Dept");
//
//                GridPane filterPane = new GridPane();
//                setProperties(filterPane, 300, 25, 10, 5, 0);
//
//                filterPane.add(dayFilterBox, 0, 0);
//                filterPane.add(timeFilterBox, 1, 0);
//                filterPane.add(deptFilterBox, 2, 0);
//
//
//                searchPane.add(searchLbl, 0, 0);
//                searchPane.add(searchField, 0, 1, 2, 1);
//                searchPane.add(filterLbl, 0, 2);
//                searchPane.add(filterPane, 0, 3, 6, 1);
//                searchPane.add(searchBtn, 0, 4);
//
//                GridPane schedulePane = new GridPane();
//                Label scheduleLbl = new Label("CURRENT SCHEDULE:");
//                scheduleLbl.getStyleClass().clear();
//                scheduleLbl.getStyleClass().add("subtitle");
//
//                schedulePane.add(btnPane, 0, 0);
//                schedulePane.add(scheduleLbl, 0, 1);
//
//                setProperties(schedulePane, 200, 400, 10, 10, 10);
//
//                searchSplit.getItems().add(searchPane);
//                searchSplit.getItems().add(schedulePane);
//                searchGroup.getChildren().add(searchSplit);
//
//
//                Scene searchScene = new Scene(searchGroup, 600, 500);
//                searchScene.getStylesheets().add("projStyles.css");
//                searchStage.setScene(searchScene);
//                searchStage.setTitle("Search");
//                searchStage.show();
//            }
//        }
//    };
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage loginStage) {
//        cl = new CourseList();
//        user = null;
//        loggedIn = false;
//        //** LOGIN WINDOW **
//
//        loginGroup = new Group();
//        loginPane = new GridPane();
//        setProperties(loginPane, 400, 450, 10, 10, 0);
//
//        loginTitle = new Label("Welcome to the \nClass Scheduling Assistant!");
//        loginTitle.getStyleClass().clear();
//        loginTitle.getStyleClass().add("title");
//
//        loginSubtitle = new Label("LOG IN");
//        loginSubtitle.getStyleClass().clear();
//        loginSubtitle.getStyleClass().add("subtitle");
//
//        //TODO: Something is funky w/ the labels
//        userField = new TextField();
//        userField.setPromptText("Username");
//        userField.setOnAction(loginHandler);
//
//        passField = new PasswordField();
//        passField.setPromptText("Password");
//        passField.setOnAction(loginHandler);
//
//        loginBtn = new Button("Log In");
//        loginBtn.getStyleClass().clear();
//        loginBtn.getStyleClass().add("buttons");
//        loginBtn.setOnAction(loginHandler);
//
//
//        //TODO: Sign up button action event
//        signupBtn = new Button("Sign Up");
//        signupBtn.getStyleClass().clear();
//        signupBtn.getStyleClass().add("buttons2");
//
//        //index format is: (column, row, takes up x cols, takes up x rows)
//        loginPane.add(loginTitle, 0, 0);
//        loginPane.add(loginSubtitle, 0, 1);
//
//        //TODO: Connect fields to logic
//        loginPane.add(userField, 0, 2);
//        loginPane.add(passField, 0, 3);
//        loginPane.add(loginBtn, 0, 4);
//        loginPane.add(signupBtn, 0, 5);
//
//        loginPane.setPadding(new Insets(20));
//        loginPane.setAlignment(Pos.CENTER);
//
//        loginGroup.getChildren().add(loginPane);
//        loginScene = new Scene(loginGroup, 400, 450);
//        loginScene.getStylesheets().add("projStyles.css");
//        loginScene.setFill(rgb(245, 238, 238));
//        loginStage.setTitle("Login");
//        loginStage.setResizable(false);
//        loginStage.setScene(loginScene);
//        loginStage.show();
//    }
//}
