import javafx.application.Application;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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

    GridPane signUpPane;
    Group signUpGroup;
    Scene signUpScene;
    Stage signUpStage;
    TextField newNameField;
    TextField newUserField;
    TextField newPassField;
    Label msgLbl;

    //search vars
    Image undoImg;
    Image redoImg;
    Stage searchStage;
    GridPane searchPane;
    GridPane btnPane;
    TextField searchField;
    TextField autoSearchField;
    Button searchBtn;
    Button resultsSearchBtn;
    Button undoBtn;
    Button redoBtn;
    Button confirmBtn;
    Button calendarBtn;
    Button backBtn;
    ScrollPane resultsSPane;
    GridPane resultsPane;
    GridPane schedulePane;
    GridPane allCoursePane;
    GridPane headerPane;
    Label userLbl;
    Button luckyBtn;
    ButtonBar searchBar;
    Search s;
    ContextMenu autoMenu;
    Hyperlink autoLink;

    ComboBox dayFilterBox;
    ComboBox timeFilterBox;
    ComboBox deptFilterBox;


    //public static ArrayList<Course> getResultsList(String searchInput){


    //}


    public void openConflictAlert(String courseId) {
        Group alertGroup = new Group();
        Scene alertScene = new Scene(alertGroup, 350, 250);
        Stage alertStg = new Stage();
        ButtonBar confirmBar = new ButtonBar();

        GridPane alertPane = new GridPane();
        alertPane.setAlignment(Pos.CENTER);

        Label alertTitleLbl = new Label("TIME CONFLICT DETECTED");
        alertTitleLbl.getStyleClass().clear();
        alertTitleLbl.getStyleClass().add("subtitle");

        Label alertMsgLbl = new Label(courseId + " is causing a conflict.\nWould you like to add anyway?");
        Button yBtn = new Button("Yes");
        yBtn.setId(courseId);
        yBtn.getStyleClass().clear();
        yBtn.getStyleClass().add("buttons");
        yBtn.setOnAction(event -> {
            cl.addClass(cl.getCourse(courseId), user.schedule);
            alertStg.close();
            updateScheduleDisplay();
        });


        Button nBtn = new Button("No");
        nBtn.getStyleClass().clear();
        nBtn.getStyleClass().add("buttons2");
        ButtonBar.setButtonData(yBtn, ButtonBar.ButtonData.LEFT);

        setProperties(alertPane, 350, 250, 25, 20, 10);
        alertPane.add(alertTitleLbl, 0, 0, 2, 1);
        alertPane.add(alertMsgLbl, 0, 1, 2, 1);
        confirmBar.getButtons().addAll(yBtn, nBtn);
        alertPane.add(confirmBar, 0, 2);

        nBtn.setOnMouseClicked(event -> alertStg.close());
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();
    }

    public static void openQuickAlert(String alertTitle, String alertMsg) {
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
        ButtonBar.setButtonData(okBtn, ButtonBar.ButtonData.LEFT);
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

    public static void setProperties(GridPane gp, int sizeH, int sizeV, int vGap, int hGap, int insets) {
        gp.setMinSize(sizeH, sizeV);
        gp.setMaxSize(sizeH, sizeV);

        gp.setVgap(vGap);
        gp.setHgap(hGap);

        gp.setPadding(new Insets(insets));

    }

    final EventHandler<ActionEvent> guestHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            //GUEST LOGIC

            user = new User("guest", "", "guest");
            loggedIn = true;
            isGuest = true;

            //https://stackoverflow.com/questions/13567019/close-fxml-window-by-code-javafx
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            launchSearch();
            stage.close();
            openQuickAlert("You are now signed in with a guest account.", "You may create a schedule, " +
                    "but will not be able to save it\nuntil you sign up.");
            userLbl = new Label("WELCOME, " + user.name.toUpperCase() + "!");
            userLbl.getStyleClass().clear();
            userLbl.getStyleClass().add("subtitle");
            headerPane.add(userLbl, 0, 0);
            searchPane.add(headerPane, 0, 1);
        }
    };

    final EventHandler<ActionEvent> undoHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            cl.undo(user.schedule);
            updateScheduleDisplay();
        }
    };
    final EventHandler<ActionEvent> redoHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            cl.redo(user.schedule);
            updateScheduleDisplay();
        }
    };

    public void updateSearchDisplay(String searchInput) {
        resultsSPane = new ScrollPane();
        resultsPane = new GridPane();
        ArrayList<Course> courses = new ArrayList<>();
        s = new Search(searchInput);
        courses = s.getResults(searchInput);

        searchPane.getChildren().clear();
        searchPane.setAlignment(Pos.TOP_CENTER);
        headerPane.getChildren().clear();
        Image backImg = new Image("back-arrow.png");
        ImageView backView = new ImageView(backImg);
        backBtn = new Button();
        backBtn.getStyleClass().clear();
        backBtn.getStyleClass().add("mini-buttons");
        backBtn.setGraphic(backView);
        backBtn.setOnMouseClicked(event-> {
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            launchSearch();
            updateScheduleDisplay();
            stage.close();

        });

        searchPane.add(backBtn,0, 0);
        searchPane.add(autoSearchField, 0, 1, 2, 1);
        //searchPane.add(resultsSearchBtn, 0, 1, 1, 1);

        for (int i = 0; i < courses.size(); i++) {
            GridPane coursePane = new GridPane();
            setProperties(coursePane, 400, 30, 5, 5, 5);
            Label result = new Label(courses.get(i).toString());
            coursePane.add(result, 1, 0);
            Button addBtn = new Button("+");
            addBtn.setId(courses.get(i).courseCode);
            addBtn.setOnAction(addCourseHandler);
            addBtn.getStyleClass().clear();
            addBtn.getStyleClass().add("add-buttons");
            coursePane.add(addBtn, 0, 0);
            resultsPane.add(coursePane, 0, i, 1, 1);
        }

        if(courses.isEmpty()){
            Label emptyLbl = new Label("\t\t\t\t\tNo results\t\t\t\t\t");
            emptyLbl.setPadding(new Insets(15));
            resultsPane.add(emptyLbl, 0, 0);
        }
        resultsSPane.setContent(resultsPane);
        searchPane.add(resultsSPane, 0, 3, 2, 1);
    }

    public void updateScheduleDisplay() {

        allCoursePane.getChildren().clear();
        schedulePane.getChildren().clear();

        if (!cl.commandHist.isEmpty() && !cl.courseHist.isEmpty()) {
            Tooltip undoTip = new Tooltip("undo" + " " + cl.commandHist.peek() + " " + cl.courseHist.peek().courseCode);
            Tooltip.install(undoBtn, undoTip);
        }
        if (!cl.undoCommandHist.isEmpty() && !cl.undoCourseHist.isEmpty()) {
            Tooltip redoTip = new Tooltip("redo" + " " + cl.undoCommandHist.peek() + " " + cl.undoCourseHist.peek().courseCode);
            Tooltip.install(redoBtn, redoTip);
        }

        Label scheduleLbl = new Label("CURRENT SCHEDULE:");
        scheduleLbl.getStyleClass().clear();
        scheduleLbl.getStyleClass().add("subtitle");
        schedulePane.add(btnPane, 0, 0);
        schedulePane.add(scheduleLbl, 0, 1);

        if (user.schedule.isEmpty()) {
            Label emptyLbl = new Label("(There's nothing here!\nAdd some courses from Search.)");
            emptyLbl.setOpacity(.6);
            schedulePane.add(emptyLbl, 0, 2);
        } else {
            for (int i = 0; i < user.schedule.size(); i++) {
                GridPane coursePane = new GridPane();
                setProperties(coursePane, 400, 35, 0, 5, 0);
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
        }


        setProperties(schedulePane, 450, 400, 15, 10, 15);
        setProperties(allCoursePane, 400, 350, 5, 5, 10);

    }


    final EventHandler<ActionEvent> addCourseHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String courseId = ((Button) event.getSource()).getId();
            Course course = cl.getCourse(courseId);
            if (cl.checkConfliction(course, user.schedule) && !cl.checkDouble(course, user.schedule)) {
                openConflictAlert(courseId);
            } else if (cl.checkDouble(course, user.schedule)) {
                openQuickAlert("DUPLICATE COURSE", "That course already is on your schedule,\nso it cannot be added.");
            } else {
                cl.addClass(course, user.schedule);
                updateScheduleDisplay();
            }

        }
    };

    final EventHandler<ActionEvent> removeCourseHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String courseId = ((Button) event.getSource()).getId();
            Course course = user.getCourse(courseId);
            cl.removeClass(course, user.schedule);
            System.out.println("removed " + courseId);
            updateScheduleDisplay();
        }
    };
    final EventHandler<ActionEvent> signUpHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            String username = newUserField.getText();
            String password = newPassField.getText();
            String name = newNameField.getText();

            Signup s = new Signup(username, password, name);

            if (username.equals("")) {
                msgLbl.setText("Username is null, please try again");
            } else if (password.equals("")) {
                msgLbl.setText("Password is null, please try again");
            } else if (name.equals("")) {
                msgLbl.setText("Name is null, please try again");
            } else { //Username, password, and name are valid
                int errno = s.signupSubmit();
                if (errno == 0) {
                    msgLbl.setText("Successfully Registered, please return to log in");
                } else if (errno == -1) { //There is already a registered account with these credentials
                    msgLbl.setText("User already exists. Please return to log in.");

                }
            }
            //Scan user input for username and password, check for validity
            //If valid complete sign up & log in, if not valid redo prompt or exit
        }

    };

    final EventHandler<ActionEvent> loginHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            //LOGIN LOGIC
            String username = userField.getText();
            String password = passField.getText();

            Login l = new Login(username, password);
            User potentialUser = l.loginSubmit();

            if (potentialUser == null) {
                Label badLoginLbl = new Label("Username or Password are invalid, please try again");
                loginPane.add(badLoginLbl, 0, 7);
            } else {
                user = new User(potentialUser.username, potentialUser.password, potentialUser.name);
                cl = new CourseList();
                loggedIn = true;

                //https://stackoverflow.com/questions/13567019/close-fxml-window-by-code-javafx
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                launchSearch();
                stage.close();

                Label userLbl = new Label("WELCOME, " + (user.name.toUpperCase()) + "!");
                userLbl.getStyleClass().clear();
                userLbl.getStyleClass().add("subtitle");
                headerPane.add(userLbl, 0, 0);
                searchPane.add(headerPane, 0, 1);
            }
        }
    };

    public void launchLogin() {

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
        userField.setText("Username");
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
        signupBtn.setOnMouseClicked(event -> launchSignUp());

        //index format is: (column, row, takes up x cols, takes up x rows)
        loginPane.add(loginTitle, 0, 0);
        loginPane.add(loginSubtitle, 0, 1);

        loginPane.add(userField, 0, 2);
        loginPane.add(passField, 0, 3);
        loginPane.add(loginBtn, 0, 4);
        loginPane.add(signupBtn, 0, 5);
        loginPane.add(guestLink, 0, 6);
        loginPane.setPadding(new Insets(20));
        loginPane.setAlignment(Pos.CENTER);

        loginGroup.getChildren().add(loginPane);
        loginScene = new Scene(loginGroup, 400, 450);
        loginScene.getStylesheets().add("projStyles.css");
        loginScene.setFill(rgb(245, 238, 238));

    }

    public void launchSignUp() {
        signUpStage = new Stage();
        signUpGroup = new Group();
        signUpPane = new GridPane();
        setProperties(signUpPane, 400, 450, 10, 10, 0);

        Label signUpTitle = new Label("Create New Account");
        signUpTitle.getStyleClass().clear();
        signUpTitle.getStyleClass().add("title");

        Label signUpSubtitle = new Label("SIGN UP");
        signUpSubtitle.getStyleClass().clear();
        signUpSubtitle.getStyleClass().add("subtitle");

        newNameField = new TextField();
        newNameField.setText("Your Name");
        newNameField.setPromptText("Your Name");
        newNameField.setOnAction(signUpHandler);

        newUserField = new TextField();
        newUserField.setPromptText("Username");
        newUserField.setOnAction(signUpHandler);

        newPassField = new PasswordField();
        newPassField.setPromptText("Password");
        newPassField.setOnAction(signUpHandler);

        msgLbl = new Label("");

        //TODO: Sign up button action event
        Button newSignupBtn = new Button("Sign Up");
        Button returnBtn = new Button("Return to Login");

        returnBtn.getStyleClass().clear();
        returnBtn.getStyleClass().add("buttons2");
        returnBtn.setOnMouseClicked(event -> signUpStage.close());

        newSignupBtn.getStyleClass().clear();
        newSignupBtn.getStyleClass().add("buttons");
        newSignupBtn.setOnAction(signUpHandler);

        //index format is: (column, row, takes up x cols, takes up x rows)
        signUpPane.add(signUpTitle, 0, 0);
        signUpPane.add(signUpSubtitle, 0, 1);

        signUpPane.add(newNameField, 0, 2);
        signUpPane.add(newUserField, 0, 3);
        signUpPane.add(newPassField, 0, 4);
        signUpPane.add(newSignupBtn, 0, 5);
        signUpPane.add(returnBtn, 0, 6);
        signUpPane.add(msgLbl, 0, 7);

        signUpPane.setPadding(new Insets(20));
        signUpPane.setAlignment(Pos.CENTER);

        signUpGroup.getChildren().add(signUpPane);
        signUpScene = new Scene(signUpGroup, 400, 450);
        signUpScene.getStylesheets().add("projStyles.css");
        signUpScene.setFill(rgb(245, 238, 238));

        signUpStage.setScene(signUpScene);
        signUpStage.show();

    }

    //TODO: Filter handler
    /*final EventHandler<ActionEvent> filterHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String filter = ((ComboBox)event.getSource()).getId();;

            if (filter.equals("days")) {
                Search.filterTxtDays();
                //lg.Action(user.username + " applied a filter to all of the courses by day(s) the course meets");
            } else if (filter.equals("time")) {
                Search.filterTxtTimes();
                //lg.Action(user.username + " applied a filter to all of the courses by the time the course occurs.");
            } else if (filter.equals("dept")) {
                Search.filterTxtDepts();
                //lg.Action(user.username + " applied a filter to all of the courses by the department of the courses.");
            }
        }
    };*/

    public void openConfirmAlert(){
        Group alertGroup = new Group();
        Scene alertScene = new Scene(alertGroup, 350, 250);
        Stage alertStg = new Stage();
        ButtonBar confirmBar = new ButtonBar();

        GridPane alertPane = new GridPane();
        alertPane.setAlignment(Pos.CENTER);

        Label alertTitleLbl = new Label("SCHEDULE CONFIRM");
        alertTitleLbl.getStyleClass().clear();
        alertTitleLbl.getStyleClass().add("subtitle");

        Label alertMsgLbl;
        Button yBtn = new Button("Yes");
        yBtn.getStyleClass().clear();
        yBtn.getStyleClass().add("buttons");
        ButtonBar.setButtonData(yBtn, ButtonBar.ButtonData.LEFT);


        int conflicts = ConfirmSchedule.countConflicts(user.schedule);
        if (conflicts > 0) {
            alertMsgLbl = new Label(conflicts + " conflict(s) exist, would you still like to" +
                    " confirm?");

            yBtn.setOnMouseClicked(event -> {
                try {
                    ConfirmSchedule.scheduleFile(user.schedule);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                confirmBar.getButtons().clear();
                Button okBtn = new Button("Okay");

                okBtn.setOnMouseClicked(event2->alertStg.close());
                okBtn.getStyleClass().clear();
                okBtn.getStyleClass().add("buttons");
                confirmBar.getButtons().add(okBtn);
                ButtonBar.setButtonData(okBtn, ButtonBar.ButtonData.LEFT);
                alertMsgLbl.setText("Your schedule has been confirmed. See file.");

            });
        }

        else{
            alertMsgLbl = new Label("Would you like to confirm this schedule\nby saving to a file?");
            yBtn.setOnMouseClicked(event -> {
                try {
                    ConfirmSchedule.scheduleFile(user.schedule);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                confirmBar.getButtons().clear();
                Button okBtn = new Button("Okay");

                okBtn.setOnMouseClicked(event2->alertStg.close());
                okBtn.getStyleClass().clear();
                okBtn.getStyleClass().add("buttons");
                confirmBar.getButtons().add(okBtn);
                ButtonBar.setButtonData(okBtn, ButtonBar.ButtonData.LEFT);
                alertMsgLbl.setText("Your schedule has been confirmed. See file.");            });
        }


        Button nBtn = new Button("No");
        nBtn.getStyleClass().clear();
        nBtn.getStyleClass().add("buttons2");

        confirmBar.getButtons().addAll(yBtn, nBtn);

        setProperties(alertPane, 350, 250, 25, 20, 5);
        alertPane.add(alertTitleLbl, 0, 0, 2, 1);
        alertPane.add(alertMsgLbl, 0, 1, 2, 1);
        alertPane.add(confirmBar, 0, 2);

        nBtn.setOnMouseClicked(event -> alertStg.close());
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();
    }

    public void launchSearch(){

        //SEARCH WINDOW

        Group searchGroup = new Group();
        searchStage = new Stage();

        SplitPane searchSplit = new SplitPane();
        searchSplit.getStyleClass().add("pane");
        searchSplit.setOrientation(Orientation.HORIZONTAL);

        searchPane = new GridPane();
        setProperties(searchPane, 550, 600, 20, 10, 10);
        searchPane.setAlignment(Pos.CENTER);

        headerPane = new GridPane();

        undoBtn = new Button();
        Image undoImg = new Image("undo.png");
        ImageView undoView = new ImageView(undoImg);
        undoBtn.setGraphic(undoView);
        undoBtn.setOnAction(undoHandler);


        Image redoImg = new Image("redo.png");
        ImageView redoView = new ImageView(redoImg);
        redoBtn = new Button();
        redoBtn.setGraphic(redoView);
        redoBtn.setOnAction(redoHandler);

        Image confirmImg = new Image("confirm.png");
        ImageView confirmView = new ImageView(confirmImg);
        confirmBtn = new Button();
        confirmBtn.setGraphic(confirmView);
        confirmBtn.setOnMouseClicked(event->openConfirmAlert());

        Image calendarImg = new Image("cal.png");
        ImageView calendarView = new ImageView(calendarImg);
        calendarBtn = new Button();
        calendarBtn.setGraphic(calendarView);
        calendarBtn.setOnMouseClicked(event-> launchCalendar());

        Tooltip confirmTip = new Tooltip("confirm schedule");
        Tooltip.install(confirmBtn, confirmTip);

        Tooltip calendarTip = new Tooltip("view calendar");
        Tooltip.install(calendarBtn, calendarTip);

        btnPane = new GridPane();
        btnPane.add(undoBtn, 0, 0);
        btnPane.add(redoBtn, 1, 0);
        btnPane.add(confirmBtn, 2, 0);
        btnPane.add(calendarBtn, 3, 0);
        btnPane.setHgap(5);
        btnPane.setVgap(5);

        undoBtn.getStyleClass().clear();
        undoBtn.getStyleClass().add("mini-buttons");
        redoBtn.getStyleClass().clear();
        redoBtn.getStyleClass().add("mini-buttons");
        confirmBtn.getStyleClass().clear();
        confirmBtn.getStyleClass().add("mini-buttons");
        calendarBtn.getStyleClass().clear();
        calendarBtn.getStyleClass().add("mini-buttons");

        Label searchLbl = new Label("Search for Courses");
        searchLbl.getStyleClass().clear();
        searchLbl.getStyleClass().add("title");

        searchField = new TextField();
        autoSearchField = new TextField();
        searchField.setPromptText("Search...");
        autoSearchField.setPromptText("Search...");
        autoMenu = new ContextMenu();
        searchField.setContextMenu(autoMenu);

        searchField.setOnKeyPressed(event-> {
            if (event.getCode() == KeyCode.ENTER) {
                updateSearchDisplay(searchField.getText());
            }
            else if (event.getCode().isLetterKey() || event.getCode().isDigitKey() || event.getCode() == KeyCode.BACK_SPACE){
                autoMenu.getItems().clear();
                //autoMenu.getItems().add(new MenuItem("test"));
                //searchField.setContextMenu(autoMenu);

                ArrayList<Course> autoList = s.getKeystroke(searchField.getText());
                int count = 0;
                for (Course c : autoList){
                    if (count == 0){
                        MenuItem currItem = new MenuItem();
                        autoMenu.getItems().add(currItem);
                    }
                    else if (count < 10) {
                        MenuItem currItem = new MenuItem(c.toString());
                        currItem.setOnAction(event2-> {
                            searchField.setText(c.courseCode);
                        });

                        autoMenu.getItems().add(currItem);
                    }
                    count++;
                    searchField.setFocusTraversable(true);

                }
                autoMenu.show(searchField, 270.0, 310.0);

            }
        });

        autoSearchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateSearchDisplay(searchField.getText());
            }
            updateSearchDisplay(autoSearchField.getText());
        });



        searchBtn = new Button("Search");
        searchBtn.getStyleClass().clear();
        searchBtn.getStyleClass().add("buttons");
        searchBtn.setOnMouseClicked(event -> updateSearchDisplay(searchField.getText()));

        resultsSearchBtn = new Button("Search");
        resultsSearchBtn.getStyleClass().clear();
        resultsSearchBtn.getStyleClass().add("buttons");
        resultsSearchBtn.setOnMouseClicked(event -> updateSearchDisplay(autoSearchField.getText()));

        luckyBtn = new Button("I'm Feeling Lucky");
        luckyBtn.getStyleClass().clear();
        luckyBtn.getStyleClass().add("buttons2");

        luckyBtn.setOnMouseClicked(event-> {
            cl.FeelingLucky(user.schedule);
            updateScheduleDisplay();
        });


        Label filterLbl = new Label("Filter by...");
        filterLbl.getStyleClass().clear();
        filterLbl.getStyleClass().add("subtitle-black");

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
        dayFilterBox = new ComboBox(dayFilters);
        dayFilterBox.setPromptText("Day");
        dayFilterBox.setId("days");

        timeFilterBox = new ComboBox(timeFilters);
        timeFilterBox.setPromptText("Time");
        timeFilterBox.setId("time");

        deptFilterBox = new ComboBox(deptFilters);
        deptFilterBox.setPromptText("Dept");
        deptFilterBox.setId("dept");

        GridPane filterPane = new GridPane();
        setProperties(filterPane, 300, 25, 10, 5, 0);

        filterPane.add(dayFilterBox, 0, 0);
        filterPane.add(timeFilterBox, 1, 0);
        filterPane.add(deptFilterBox, 2, 0);

        //TODO: Make this another way later
            autoLink = new Hyperlink("Auto Generate Courses");
            autoLink.setOnMouseClicked(event-> {
                launchAutoQs();
            });


        searchPane.add(searchLbl, 0, 2);
        searchPane.add(searchField, 0, 3);
        searchPane.add(filterLbl, 0, 4);
        searchPane.add(filterPane, 0, 5, 6, 1);

        ButtonBar searchBar = new ButtonBar();
        searchBar.getButtons().addAll(searchBtn, luckyBtn);
        ButtonBar.setButtonData(searchBtn, ButtonBar.ButtonData.LEFT);

        searchPane.add(searchBar, 0, 6);
        searchPane.add(autoLink, 0, 7);

        schedulePane = new GridPane();
        //schedulePane.getStyleClass().clear();
        //schedulePane.getStyleClass().add("pane");
        Label scheduleLbl = new Label("CURRENT SCHEDULE:");
        scheduleLbl.getStyleClass().clear();
        scheduleLbl.getStyleClass().add("subtitle");

        Label emptyLbl = new Label("(There's nothing here!\nAdd some courses from Search.)");
        emptyLbl.setOpacity(.6);

        allCoursePane = new GridPane();
        //allCoursePane.getStyleClass().clear();
       // allCoursePane.getStyleClass().add("pane");
        setProperties(allCoursePane, 400, 40, 0, 0, 0);

        schedulePane.add(btnPane, 0, 0);
        schedulePane.add(scheduleLbl, 0, 1);
        //schedulePane.add(allCoursePane, 0, 2);
        schedulePane.add(emptyLbl, 0, 2);

        setProperties(schedulePane, 450, 400, 15, 10, 15);

        searchSplit.getItems().add(searchPane);
        searchSplit.getItems().add(schedulePane);
        searchGroup.getChildren().add(searchSplit);

        Scene searchScene = new Scene(searchGroup, 925, 600);
        searchScene.getStylesheets().add("projStyles.css");
        searchStage.setScene(searchScene);
        searchStage.setResizable(false);
        searchStage.setTitle("Search");
        searchStage.show();
    }

    public void launchAutoQs(){
        autoLink.setDisable(true);
        Group alertGroup = new Group();
        Scene alertScene = new Scene(alertGroup, 400, 250);
        Stage alertStg = new Stage();

        GridPane alertPane = new GridPane();
        alertPane.setAlignment(Pos.CENTER);

        Label alertTitleLbl = new Label("AUTO GENERATE COURSES");
        alertTitleLbl.getStyleClass().clear();
        alertTitleLbl.getStyleClass().add("subtitle");

        Label yearLbl = new Label("Select Your Year:");
        yearLbl.getStyleClass().clear();
        yearLbl.getStyleClass().add("subtitle-black");

        RadioButton year1Radio = new RadioButton("Freshman");
        year1Radio.setId("Fresh");
        RadioButton year2Radio = new RadioButton("Sophomore");
        year2Radio.setId("Soph");
        RadioButton year3Radio = new RadioButton("Junior");
        year3Radio.setId("Junior");
        RadioButton year4Radio = new RadioButton("Senior");
        year4Radio.setId("Senior");

        Label semesterLbl = new Label("Select Your Semester:");
        semesterLbl.getStyleClass().clear();
        semesterLbl.getStyleClass().add("subtitle-black");
        RadioButton semester1Radio = new RadioButton("Fall");
        semester1Radio.setId("F");
        RadioButton semester2Radio = new RadioButton("Spring");
        semester2Radio.setId("S");

        HBox yearRadioBox = new HBox(10, year1Radio, year2Radio, year3Radio, year4Radio);
        HBox semesterRadioBox = new HBox(10, semester1Radio, semester2Radio);

        ToggleGroup yearRadios = new ToggleGroup();
        yearRadios.getToggles().addAll(year1Radio, year2Radio, year3Radio, year4Radio);


        yearRadios.getSelectedToggle();

        ToggleGroup semesterRadios = new ToggleGroup();
        semesterRadios.getToggles().addAll(semester1Radio, semester2Radio);

        ButtonBar okCancelBar = new ButtonBar();
        Button okBtn = new Button("Okay");
        ButtonBar.setButtonData(okBtn, ButtonBar.ButtonData.LEFT);
        okBtn.getStyleClass().clear();
        okBtn.getStyleClass().add("buttons");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().clear();
        cancelBtn.getStyleClass().add("buttons2");
        cancelBtn.setOnAction(event->{
            alertStg.close();
            autoLink.setDisable(false);
        });


        okCancelBar.getButtons().addAll(okBtn, cancelBtn);

        setProperties(alertPane, 400, 250, 15, 10, 10);
        alertPane.add(alertTitleLbl, 0, 0);
        alertPane.add(yearLbl, 0, 1);
        alertPane.add(yearRadioBox, 0, 2);
        alertPane.add(semesterLbl, 0, 3);
        alertPane.add(semesterRadioBox, 0, 4);
        alertPane.add(okCancelBar, 0, 5);


        okBtn.setOnMouseClicked(event -> {
            String year = ((RadioButton)yearRadios.getSelectedToggle()).getId();
            String semester = ((RadioButton)semesterRadios.getSelectedToggle()).getId();
            cl.autoFill(year,semester, user.schedule);
            alertStg.close();
            updateScheduleDisplay();
            autoLink.setDisable(false);

        });
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();

    }

    public void launchCalendar(){

    }

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage loginStage) {
            cl = new CourseList();
            user = null;
            //Logging lg;

            launchLogin();


            loginStage.setTitle("Login");
            loginStage.setResizable(false);
            loginStage.setScene(loginScene);
            loginStage.show();


            }
    }
