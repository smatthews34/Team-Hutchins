import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

import static javafx.scene.paint.Color.rgb;

public class FXMain extends Application {
    User user;
    CourseList cl;

    //login vars
    boolean loggedIn;
    boolean isGuest;
    Group loginGroup;
    GridPane loginPane;
    Scene loginScene;
    TextField userField;
    PasswordField passField;
    Label loginTitle;
    Label loginSubtitle;
    Button loginBtn;
    Button signupBtn;

    //search vars
    Image undoImg;
    Image redoImg;
    Stage searchStage;
    GridPane searchPane;
    GridPane btnPane;
    TextField searchField;
    Button searchBtn;
    ScrollPane resultsSPane;
    GridPane resultsPane;
    GridPane schedulePane;
    GridPane allCoursePane;


    //public static ArrayList<Course> getResultsList(String searchInput){


    //}

    public static void openQuickAlert(String alertTitle, String alertMsg){
        Group alertGroup = new Group();
        Scene alertScene = new Scene(alertGroup, 350, 200);
        Stage alertStg = new Stage();

        GridPane alertPane = new GridPane();
        alertPane.setAlignment(Pos.CENTER);

        Label alertTitleLbl = new Label(alertTitle);
        alertTitleLbl.getStyleClass().clear();
        alertTitleLbl.getStyleClass().add("subtitle");

        Label alertMsgLbl = new Label(alertMsg);
        Button okBtn = new Button("Okay");
        okBtn.getStyleClass().clear();
        okBtn.getStyleClass().add("buttons");

        setProperties(alertPane, 350, 200, 10, 10, 10);
        alertPane.add(alertTitleLbl, 0, 0);
        alertPane.add(alertMsgLbl, 0, 1);
        alertPane.add(okBtn, 0, 2);

        okBtn.setOnMouseClicked(event -> alertStg.close());
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();
    }

    public static void setProperties(GridPane gp, int sizeH, int sizeV, int vGap, int hGap, int insets){
        gp.setMinSize(sizeH, sizeV);
        gp.setMaxSize(sizeH, sizeV);

        gp.setVgap(vGap);
        gp.setHgap(hGap);

        gp.setPadding(new Insets(insets));

    }

    final EventHandler<ActionEvent> guestHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            //GUEST LOGIC

            user = new User("guest", "", "guest");
            loggedIn = true;
            isGuest = true;

            //https://stackoverflow.com/questions/13567019/close-fxml-window-by-code-javafx
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            searchStage.show();
            stage.close();
            openQuickAlert("You are now signed in with a guest account.", "You may create a schedule, " +
                    "but will not be able to save it\nuntil you sign up.");
        }
    };


    public void updateSearchDisplay(String searchInput){
        resultsSPane = new ScrollPane();
        resultsPane = new GridPane();
        ArrayList<Course> courses = new ArrayList<>();
        Search s = new Search(searchInput);
        courses = s.getResults(searchInput);

        searchPane.getChildren().clear();
        searchPane.add(searchField, 0, 0, 2, 1);
        searchPane.add(searchBtn, 0, 1, 1 ,1);


            for (int i = 0; i < courses.size(); i++){
                GridPane coursePane = new GridPane();
                setProperties(coursePane, 400, 30, 5, 5, 5);
                Label result = new Label(courses.get(i).toString());
                coursePane.add(result, 1, 0);
                Button addBtn = new Button("+");
                addBtn.setId(courses.get(i).courseCode);
                addBtn.setOnAction(addCourseHandler);
                addBtn.getStyleClass();
                addBtn.getStyleClass().add("add-buttons");
                coursePane.add(addBtn, 0, 0);
                resultsPane.add(coursePane, 0, i, 1, 1);
        }
        resultsSPane.setContent(resultsPane);
        searchPane.add(resultsSPane, 0, 2, 2, 1);
    }

    final EventHandler<ActionEvent> addCourseHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            String courseId = ((Button)event.getSource()).getId();
            Course course = cl.getCourse(courseId);

            cl.addClass(course, user.schedule);

            allCoursePane.getChildren().clear();
            schedulePane.getChildren().clear();

            Label scheduleLbl = new Label("CURRENT SCHEDULE:");
            scheduleLbl.getStyleClass().clear();
            scheduleLbl.getStyleClass().add("subtitle");
            schedulePane.add(btnPane, 0, 0);
            schedulePane.add(scheduleLbl, 0, 1);

            for(int i = 0; i < user.schedule.size(); i++){
                GridPane coursePane = new GridPane();
                setProperties(coursePane, 400, 25, 5, 5, 0);
                Course c = user.schedule.get(i);
                Label courseLbl = new Label(c.toString());

                Button removeBtn = new Button("-");
                removeBtn.setId(user.schedule.get(i).courseCode);
                removeBtn.getStyleClass().clear();
                removeBtn.getStyleClass().add("add-buttons");
                removeBtn.setOnAction(removeCourseHandler);

                coursePane.add(removeBtn, 0, i);
                coursePane.add(courseLbl, 1, i);
                allCoursePane.add(coursePane, 0, i);
            }

            schedulePane.add(allCoursePane, 0, 2);
            setProperties(schedulePane, 400, 450, 5, 10, 15);
            setProperties(allCoursePane, 400, 350, 5, 5, 10);


        }
    };

    final EventHandler<ActionEvent> removeCourseHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            String courseId = ((Button)event.getSource()).getId();
            Course course = user.getCourse(courseId);
            cl.removeClass(course, user.schedule);

            allCoursePane.getChildren().clear();
            schedulePane.getChildren().clear();

            Label scheduleLbl = new Label("CURRENT SCHEDULE:");
            scheduleLbl.getStyleClass().clear();
            scheduleLbl.getStyleClass().add("subtitle");
            schedulePane.add(btnPane, 0, 0);
            schedulePane.add(scheduleLbl, 0, 1);

            for(int i = 0; i < user.schedule.size(); i++){
                GridPane coursePane = new GridPane();
                setProperties(coursePane, 400, 25, 5, 5, 0);
                Course c = user.schedule.get(i);
                Label courseLbl = new Label(c.toString());

                Button removeBtn = new Button("-");
                removeBtn.setOnAction(removeCourseHandler);
                removeBtn.getStyleClass().clear();
                removeBtn.getStyleClass().add("add-buttons");
                removeBtn.setId(user.schedule.get(i).courseCode);

                coursePane.add(removeBtn, 0, i);
                coursePane.add(courseLbl, 1, i);
                allCoursePane.add(coursePane, 0, i);
            }

            schedulePane.add(allCoursePane, 0, 2);
            setProperties(schedulePane, 400, 450, 5, 10, 15);
            setProperties(allCoursePane, 400, 350, 5, 5, 10);


        }
    };

    final EventHandler<ActionEvent> loginHandler = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            //LOGIN LOGIC
            String username = userField.getText();
            String password = passField.getText();

            System.out.println(username);
            System.out.println(password);

            Login l = new Login(username, password);
            User potentialUser = l.loginSubmit();

            if (potentialUser == null){
                Label badLoginLbl = new Label("Username or Password are invalid, please try again");
                loginPane.add(badLoginLbl, 0, 7);
            }
            else {
                user = new User(potentialUser.username, potentialUser.password, potentialUser.name);
                cl = new CourseList();
                loggedIn = true;

                //https://stackoverflow.com/questions/13567019/close-fxml-window-by-code-javafx
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                searchStage.show();
                stage.close();
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage loginStage) {
        cl = new CourseList();
        user = null;
        loggedIn = false;
        isGuest = false; //Use to prevent a guest user from saving a schedule
        //** LOGIN WINDOW **

        loginGroup = new Group();
        loginPane = new GridPane();
        setProperties(loginPane, 400, 450, 10, 10, 0);

        loginTitle = new Label("Welcome to the \nClass Scheduling Assistant!");
        loginTitle.getStyleClass().clear();
        loginTitle.getStyleClass().add("title");

        loginSubtitle = new Label("LOG IN");
        loginSubtitle.getStyleClass().clear();
        loginSubtitle.getStyleClass().add("subtitle");

        userField = new TextField();
        userField.setPromptText("Username");
        userField.setOnAction(loginHandler);

        passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setOnAction(loginHandler);

        loginBtn = new Button("Log In");
        loginBtn.getStyleClass().clear();
        loginBtn.getStyleClass().add("buttons");
        loginBtn.setOnAction(loginHandler);

        Hyperlink guestLink = new Hyperlink("Continue as Guest");
        guestLink.setOnAction(guestHandler);



        //TODO: Sign up button action event
        signupBtn = new Button("Sign Up");
        signupBtn.getStyleClass().clear();
        signupBtn.getStyleClass().add("buttons2");

        //index format is: (column, row, takes up x cols, takes up x rows)
        loginPane.add(loginTitle, 0, 0);
        loginPane.add(loginSubtitle, 0, 1);

        loginPane.add(userField, 0, 2);
        loginPane.add(passField,0, 3);
        loginPane.add(loginBtn, 0, 4);
        loginPane.add(signupBtn, 0, 5);
        loginPane.add(guestLink, 0, 6);

        //SEARCH WINDOW

        Group searchGroup = new Group();
        searchStage = new Stage();

        SplitPane searchSplit = new SplitPane();
        searchSplit.getStyleClass().add("pane");
        searchSplit.setOrientation(Orientation.HORIZONTAL);

        searchPane = new GridPane();
        setProperties(searchPane, 550, 600, 15, 10, 10);
        searchPane.setAlignment(Pos.CENTER);

        Button undoBtn = new Button();
        Image undoImg = new Image("undo.png");
        ImageView undoView = new ImageView(undoImg);
        Image redoImg = new Image("redo.png");
        ImageView redoView = new ImageView(redoImg);
        undoBtn.setGraphic(undoView);
        Button redoBtn = new Button();
        redoBtn.setGraphic(redoView);

        btnPane = new GridPane();
        btnPane.add(undoBtn, 0, 0);
        btnPane.add(redoBtn, 1, 0);
        btnPane.setHgap(5);
        btnPane.setVgap(5);

        undoBtn.getStyleClass().clear();
        undoBtn.getStyleClass().add("mini-buttons");
        redoBtn.getStyleClass().clear();
        redoBtn.getStyleClass().add("mini-buttons");

        Label searchLbl = new Label("Search for Courses");
        searchLbl.getStyleClass().clear();
        searchLbl.getStyleClass().add("title");

        searchField = new TextField();
        searchField.setPromptText("Search...");

        searchBtn = new Button("Search");
        searchBtn.getStyleClass().clear();
        searchBtn.getStyleClass().add("buttons");
        searchBtn.setOnMouseClicked(event -> updateSearchDisplay(searchField.getText()));


        Label filterLbl = new Label("Filter by...");

        ObservableList<String> dayFilters =
                FXCollections.observableArrayList(
                        "",
                        "MWF",
                        "TR"
                );
        //TODO: Add all times
        ObservableList<String> timeFilters =
                FXCollections.observableArrayList(
                        "",
                        "8:00:00",
                        "9:00:00",
                        "12:30:00"
                );
        //TODO: Add all depts
        ObservableList<String> deptFilters =
                FXCollections.observableArrayList(
                        "",
                        "HUMA",
                        "COMP"
                );
        ComboBox dayFilterBox = new ComboBox(dayFilters);
        dayFilterBox.setPromptText("Day");
        ComboBox timeFilterBox = new ComboBox(timeFilters);
        timeFilterBox.setPromptText("Time");
        ComboBox deptFilterBox = new ComboBox(deptFilters);
        deptFilterBox.setPromptText("Dept");

        GridPane filterPane = new GridPane();
        setProperties(filterPane, 300, 25, 10, 5, 0);

        filterPane.add(dayFilterBox, 0, 0);
        filterPane.add(timeFilterBox, 1, 0);
        filterPane.add(deptFilterBox, 2, 0);

        searchPane.add(searchLbl, 0, 0);
        searchPane.add(searchField, 0, 1, 2, 1);
        searchPane.add(filterLbl, 0, 2);
        searchPane.add(filterPane, 0, 3, 6, 1);
        searchPane.add(searchBtn, 0, 4);

        schedulePane = new GridPane();
        Label scheduleLbl = new Label("CURRENT SCHEDULE:");
        scheduleLbl.getStyleClass().clear();
        scheduleLbl.getStyleClass().add("subtitle");

        allCoursePane = new GridPane();
        setProperties(allCoursePane, 400, 40, 0, 0, 0);

        schedulePane.add(btnPane, 0, 0);
        schedulePane.add(scheduleLbl, 0, 1);
        schedulePane.add(allCoursePane, 0, 2);

        setProperties(schedulePane, 450, 400, 5, 5, 10);

        searchSplit.getItems().add(searchPane);
        searchSplit.getItems().add(schedulePane);
        searchGroup.getChildren().add(searchSplit);

        Scene searchScene = new Scene(searchGroup, 925, 600);
        searchScene.getStylesheets().add("projStyles.css");
        searchStage.setScene(searchScene);
        searchStage.setTitle("Search");

        loginPane.setPadding(new Insets(20));
        loginPane.setAlignment(Pos.CENTER);

        loginGroup.getChildren().add(loginPane);
        loginScene = new Scene(loginGroup, 400, 450);
        loginScene.getStylesheets().add("projStyles.css");
        loginScene.setFill(rgb(245, 238, 238));
        loginStage.setTitle("Login");
        loginStage.setResizable(false);
        loginStage.setScene(loginScene);
        loginStage.show();
    }
}
