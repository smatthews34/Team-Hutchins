import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    Stage loginStageC;
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
    TextField emailField;
    String email;
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
    Button addActBtn;
    ScrollPane resultsSPane;
    GridPane resultsPane;
    GridPane schedulePane;
    GridPane allCoursePane;
    GridPane headerPane;
    GridPane bottomPane;
    Label userLbl;
    Button luckyBtn;
    ButtonBar searchBar;
    Search s;
    ContextMenu autoMenu;
    Hyperlink autoLink;
    boolean auto;
    boolean hasConflict;
    boolean hasMax;
    GridPane conflictsPane;
    GridPane logOutPane;
    Image appImg;

    ComboBox dayFilterBox;
    ComboBox timeFilterBox;
    ComboBox deptFilterBox;
    final int MAX_SIZE = 8;


    Logging lg;

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
            lg.logConflict(user.username + " added the course: " + cl.getCourse(courseId).toString() + " that conflicts with a course on their schedule.");
            cl.addClass(cl.getCourse(courseId), user.schedule);
            if(user.schedule.size() > MAX_SIZE){
                hasMax = true;
            }
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

        nBtn.setOnMouseClicked(event -> {
            alertStg.close();
            lg.logConflict(user.schedule + " has attempted to add the course: " + cl.getCourse(courseId).toString() + " that conflicts with their schedule but elected not to add it.");
        });
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();
    }

    public void updateResolutionScreen(){

        conflictsPane.getChildren().clear();
        ArrayList<Course> con = cl.conflictResolution(user.schedule);
        if(!con.isEmpty()) {
            for (int p = 0; p < con.size(); p++) {
                GridPane coursePane = new GridPane();
                setProperties(coursePane, 350, 35, 0, 5, 0);
                Course c = con.get(p);
                Label courseLbl = new Label(c.toString());

                Button removeBtn = new Button("-");
                removeBtn.setId(con.get(p).courseCode);
                removeBtn.getStyleClass().clear();
                removeBtn.getStyleClass().add("add-buttons");
                removeBtn.setOnAction(event -> {
                    String courseId = ((Button) event.getSource()).getId();
                    Course course = user.getCourse(courseId);
                    cl.removeClass(course, user.schedule);
                    lg.Action(user.username + " Successfuly removed the course: " + course);
                    updateResolutionScreen();
                    updateScheduleDisplay();
                    lg.Action(user.username + " Successfuly resolved a time conflict from the course: " + c);
                });

                coursePane.add(removeBtn, 0, p);
                coursePane.add(courseLbl, 1, p);
                conflictsPane.add(coursePane, 0, p);
            }
        }

        else{
            GridPane coursePane = new GridPane();
            setProperties(coursePane, 350, 35, 0, 0, 0);
            Label emptyLbl = new Label("All conflicts resolved.\n");
            hasConflict = false;
            coursePane.add(emptyLbl, 0, 0);
            conflictsPane.add(coursePane, 0, 3);
            updateScheduleDisplay();

        }
    }


    public void openResolutionScreen() {
        Group alertGroup = new Group();
        Scene alertScene = new Scene(alertGroup, 400, 450);
        Stage alertStg = new Stage();
        ButtonBar confirmBar = new ButtonBar();

        GridPane alertPane = new GridPane();
        alertPane.setAlignment(Pos.CENTER);

        Label alertTitleLbl = new Label("RESOLVE CONFLICTS");
        alertTitleLbl.getStyleClass().clear();
        alertTitleLbl.getStyleClass().add("subtitle");
        GridPane topPane = new GridPane();
        topPane.add(alertTitleLbl, 0, 0);
        topPane.setAlignment(Pos.TOP_LEFT);

        conflictsPane = new GridPane();

        ArrayList<Course> con = cl.conflictResolution(user.schedule);
        for (int p = 0; p < con.size(); p++){
            GridPane coursePane = new GridPane();
            setProperties(coursePane, 350, 35, 0, 5, 0);
            Course c = con.get(p);
            Label courseLbl = new Label(c.toString());

            Button removeBtn = new Button("-");
            removeBtn.setId(con.get(p).courseCode);
            removeBtn.getStyleClass().clear();
            removeBtn.getStyleClass().add("add-buttons");
            removeBtn.setOnAction(event->{
                String courseId = ((Button) event.getSource()).getId();
                Course course = user.getCourse(courseId);
                cl.removeClass(course, user.schedule);
                lg.Action(user.username + " Successfuly removed the course: " + course);
                System.out.println("removed " + courseId);
                updateResolutionScreen();
                updateScheduleDisplay();
                lg.Action(user.username + " Successfuly resolved a time conflict from the course: " + c);
            });

            coursePane.add(removeBtn, 0, p);
            coursePane.add(courseLbl, 1, p);
            conflictsPane.add(coursePane, 0, p);
        }
        Label alertMsgLbl = new Label("Current Conflicts (remove to resolve):");
        alertMsgLbl.getStyleClass().clear();
        alertMsgLbl.getStyleClass().add("subtitle-black");
        Button yBtn = new Button("Done");
        yBtn.getStyleClass().clear();
        yBtn.getStyleClass().add("buttons");
        yBtn.setOnAction(event -> {

            alertStg.close();
            updateScheduleDisplay();
        });
        ButtonBar.setButtonData(yBtn, ButtonBar.ButtonData.LEFT);

        setProperties(alertPane, 400, 450, 10, 5, 0);
        alertPane.add(topPane, 0, 0);
        alertPane.add(alertMsgLbl, 0, 1);
        alertPane.add(conflictsPane, 0, 2);
        alertPane.add(yBtn, 0, 3);

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
            try {
                lg = new Logging(user.username);
                lg.Action("A Guest has started to schedule");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            lg.Action(user.username + " has undone their last action");
            updateScheduleDisplay();
        }
    };
    final EventHandler<ActionEvent> redoHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            cl.redo(user.schedule);
            lg.Action(user.username + " has redone their last action");
            updateScheduleDisplay();
        }
    };

    public void updateSearchDisplay(ArrayList<Course> courseList) {
        resultsSPane = new ScrollPane();
        resultsPane = new GridPane();

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

        GridPane addActPane = new GridPane();
        setProperties(addActPane, 400, 20, 0, 5, 0 );
        Label actLbl = new Label("Create Activity");
        addActBtn = new Button("+");
        backBtn = new Button();
        addActBtn.getStyleClass().clear();
        addActBtn.getStyleClass().add("add-buttons");
        addActBtn.setOnMouseClicked(event-> {
            launchActivity();
        });
        addActPane.add(addActBtn, 0, 0);
        addActPane.add(actLbl, 1, 0);
        searchPane.add(addActPane, 0, 2);

        for (int i = 0; i < courseList.size(); i++) {
            GridPane coursePane = new GridPane();
            setProperties(coursePane, 400, 30, 5, 5, 5);
            Label result = new Label(courseList.get(i).toString());
            coursePane.add(result, 1, 0);
            Button addBtn = new Button("+");
            addBtn.setId(courseList.get(i).courseCode);
            addBtn.setOnAction(addCourseHandler);
            addBtn.getStyleClass().clear();
            addBtn.getStyleClass().add("add-buttons");
            coursePane.add(addBtn, 0, 0);
            resultsPane.add(coursePane, 0, i, 1, 1);
        }

        if(courseList.isEmpty()){
            Label emptyLbl = new Label("\t\t\t\t\tNo results\t\t\t\t\t");
            emptyLbl.setPadding(new Insets(15));
            resultsPane.add(emptyLbl, 0, 0);
        }
        resultsSPane.setContent(resultsPane);
        searchPane.add(resultsSPane, 0, 3, 2, 1);
    }

    public void updateScheduleDisplay() {
        bottomPane = new GridPane();
        //bottomPane.getStyleClass().clear();
        //bottomPane.getStyleClass().add("test");
        setProperties(bottomPane, 400, 40, 5, 0, 0);
        bottomPane.getChildren().clear();
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
        Hyperlink logOutLink = new Hyperlink("Log Out");
        logOutLink.setOnAction(event->{
            user = null;
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            launchLogin();
            stage.close();

        });
        Label scheduleLbl = new Label("CURRENT SCHEDULE:");
        scheduleLbl.getStyleClass().clear();
        scheduleLbl.getStyleClass().add("subtitle");

        if(!user.username.equals("guest")) {
            logOutPane = new GridPane();
            logOutPane.add(logOutLink, 0, 0);
            schedulePane.add(logOutPane, 0, 0);

            logOutLink.setOnAction(event->{
                user = null;
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                try {
                    start(loginStageC);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.close();

            });
        }
        schedulePane.add(btnPane, 0, 1);
        schedulePane.add(scheduleLbl, 0, 2);

        if (user.schedule.isEmpty()) {
            Label emptyLbl = new Label("(There's nothing here!\nAdd some courses from Search.)");
            emptyLbl.setOpacity(.6);
            schedulePane.add(emptyLbl, 0, 3);
        }

        else {
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
            schedulePane.add(allCoursePane, 0, 3);
            if(hasMax) {
                Label maxLbl = new Label("Schedule size limit reached.");
                maxLbl.setTextFill(rgb(211, 47, 47));
                bottomPane.add(maxLbl, 0, 0);
            }

            if (hasConflict){
                Hyperlink conResLink = new Hyperlink("Resolve Conflicts");
                conResLink.setOnAction(event-> openResolutionScreen());
                if(hasMax) {
                    bottomPane.add(conResLink, 0, 1);
                }
                else{
                    bottomPane.add(conResLink, 0, 0);
                }
                schedulePane.add(bottomPane, 0, 4);
            }

        }

        setProperties(schedulePane, 450, 450, 10, 10, 15);
        setProperties(allCoursePane, 400, 350, 5, 5, 10);

    }


    final EventHandler<ActionEvent> addCourseHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String courseId = ((Button) event.getSource()).getId();
            Course course = cl.getCourse(courseId);

            if (user.schedule.size() < MAX_SIZE) {

                if (cl.checkConfliction(course, user.schedule) && !cl.checkDouble(course, user.schedule)) {
                    hasConflict = cl.checkConfliction(course, user.schedule);
                    lg.logConflict(user.username + " added the course: " + course + " that conflicts with a course on their schedule.");
                    openConflictAlert(courseId);
                } else if (cl.checkDouble(course, user.schedule)) {
                    lg.logConflict(user.username + " has attempted to add the course: " + course + ", that is a duplicate of a course on their current schedule.");
                    openQuickAlert("DUPLICATE COURSE", "That course already is on your schedule,\nso it cannot be added.");
                } else {
                    cl.addClass(course, user.schedule);
                    updateScheduleDisplay();
                }
            }
            else {
                hasMax = true;
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
            if(user.schedule.size() < MAX_SIZE){
                hasMax = false;
            }
            lg.Action(user.username + " Successfuly removed the course: " + course);
            updateScheduleDisplay();
        }
    };
    final EventHandler<ActionEvent> signUpHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String email = emailField.getText();
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
            }
            else if(!email.contains(".") || !email.contains("@")){
                msgLbl.setText("Email is invalid, please try again");
            }
            else { //Username, password, and name are valid
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
                user.setEmail(email);
                cl = new CourseList();
                try {
                    lg = new Logging(user.username);
                    lg.Action(user.username + " has logged in and begun scheduling.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        signUpStage.getIcons().add(appImg);

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

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setOnAction(signUpHandler);

        newUserField = new TextField();
        newUserField.setPromptText("Username");
        newUserField.setOnAction(signUpHandler);

        newPassField = new PasswordField();
        newPassField.setPromptText("Password");
        newPassField.setOnAction(signUpHandler);

        msgLbl = new Label("");

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
        signUpPane.add(emailField, 0, 3);
        signUpPane.add(newUserField, 0, 4);
        signUpPane.add(newPassField, 0, 5);
        signUpPane.add(newSignupBtn, 0, 6);
        signUpPane.add(returnBtn, 0, 7);
        signUpPane.add(msgLbl, 0, 8);

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

    public void checkApplyFilters() {
        ArrayList<Course> filteredResults = new ArrayList<>();

        if (!dayFilterBox.getSelectionModel().isEmpty()) {
            s = new Search(searchField.getText());
            ArrayList<Course> courses = s.getResults(searchField.getText());
            
            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).meets.contains(dayFilterBox.getValue().toString())) {
                    filteredResults.add(courses.get(i));
                }
            }
            if (!timeFilterBox.getSelectionModel().isEmpty()) {
                ArrayList<Course> prevCourses = new ArrayList<>();
                for (Course c : filteredResults) {
                    prevCourses.add(c);
                }
                filteredResults.clear();
                for (int i = 0; i < prevCourses.size(); i++) {
                    if (prevCourses.get(i).startTime.equals(timeFilterBox.getValue().toString())) {
                        filteredResults.add(prevCourses.get(i));
                    }
                }
                updateSearchDisplay(filteredResults);
            } else {
                updateSearchDisplay(filteredResults);
            }
        }

        if (!timeFilterBox.getSelectionModel().isEmpty() && dayFilterBox.getSelectionModel().isEmpty()) {
            s = new Search(searchField.getText());
            ArrayList<Course> courses = s.getResults(searchField.getText());
            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).startTime.contains(timeFilterBox.getValue().toString())) {
                    filteredResults.add(courses.get(i));
                }
            }
            updateSearchDisplay(filteredResults);
        }

        if (dayFilterBox.getSelectionModel().isEmpty() && timeFilterBox.getSelectionModel().isEmpty()) {
            s = new Search(searchField.getText());
            ArrayList<Course> courses = s.getResults(searchField.getText());
            updateSearchDisplay(courses);
        }
    }

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
                ButtonBar.setButtonData(okBtn, ButtonBar.ButtonData.LEFT);
                confirmBar.getButtons().add(okBtn);
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
       if (user.username != "guest") {
            CheckBox cb = new CheckBox("Send to my email");
            /*cb.setOnAction(event-> {
                try {
                    sendToEmail();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });*/
            alertPane.add(cb, 0, 2);
            alertPane.add(confirmBar, 0, 3);
        }
        else{
            alertPane.add(confirmBar, 0, 2);
        }


        nBtn.setOnMouseClicked(event -> alertStg.close());
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();
    }

    public void sendToEmail() throws Exception {
        email e = new email();
        e.emailSender(email);
    }
    public void launchSearch(){

        //SEARCH WINDOW
        Group searchGroup = new Group();
        searchStage = new Stage();
        searchStage.getIcons().add(appImg);

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
                checkApplyFilters();
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
                    else if (count < 10 && count != 0) {
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
            ArrayList<Course> courses = new ArrayList<>();
            if (event.getCode() == KeyCode.ENTER) {
                s = new Search(autoSearchField.getText());
                courses = s.getResults(autoSearchField.getText());
                updateSearchDisplay(courses);
            }
            updateSearchDisplay(courses);
        });



        searchBtn = new Button("Search");
        searchBtn.getStyleClass().clear();
        searchBtn.getStyleClass().add("buttons");
        searchBtn.setOnMouseClicked(event -> {
            checkApplyFilters();
        });


        resultsSearchBtn = new Button("Search");
        resultsSearchBtn.getStyleClass().clear();
        resultsSearchBtn.getStyleClass().add("buttons");
        resultsSearchBtn.setOnMouseClicked(event -> {
            ArrayList<Course> courses = s.getResults(searchField.getText());
            updateSearchDisplay(courses);
        });

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
                        "MWF",
                        "TR",
                        "M",
                        "T",
                        "W",
                        "R",
                        "F"
                );
        ObservableList<String> timeFilters =
                FXCollections.observableArrayList(
                        "8:00:00",
                        "9:00:00",
                        "9:15:00",
                        "10:00:00",
                        "10:05:00",
                        "11:00:00",
                        "11:30:00",
                        "12:00:00",
                        "13:00:00",
                        "14:00:00",
                        "14:30:00",
                        "15:00:00",
                        "16:00:00",
                        "18:30:00",
                        "19:00:00"
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
        //filterPane.add(deptFilterBox, 2, 0);

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
        Hyperlink logOutLink = new Hyperlink("Log Out");
        logOutLink.setOnAction(event->{
            user = null;
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            try {
                start(loginStageC);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();

        });
        if(!user.username.equals("guest")) {
            logOutPane = new GridPane();
            logOutPane.add(logOutLink, 0, 0);
            schedulePane.add(logOutPane, 0, 0);
        }
        Label scheduleLbl = new Label("CURRENT SCHEDULE:");
        scheduleLbl.getStyleClass().clear();
        scheduleLbl.getStyleClass().add("subtitle");

        Label emptyLbl = new Label("(There's nothing here!\nAdd some courses from Search.)");
        emptyLbl.setOpacity(.6);

        allCoursePane = new GridPane();
        setProperties(allCoursePane, 400, 40, 0, 0, 0);

        schedulePane.add(btnPane, 0, 1);
        schedulePane.add(scheduleLbl, 0, 2);
        //schedulePane.add(allCoursePane, 0, 2);
        schedulePane.add(emptyLbl, 0, 3);

        setProperties(schedulePane, 450, 450, 15, 10, 15);

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
        Scene alertScene = new Scene(alertGroup, 400, 270);
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

        setProperties(alertPane, 400, 270, 15, 10, 10);
        alertPane.add(alertTitleLbl, 0, 0);
        alertPane.add(yearLbl, 0, 1);
        alertPane.add(yearRadioBox, 0, 2);
        alertPane.add(semesterLbl, 0, 3);
        alertPane.add(semesterRadioBox, 0, 4);
        alertPane.add(okCancelBar, 0, 5);


        okBtn.setOnMouseClicked(event -> {
            String year = ((RadioButton)yearRadios.getSelectedToggle()).getId();
            String semester = ((RadioButton)semesterRadios.getSelectedToggle()).getId();
            if (auto !=true) {
                cl.autoFill(year, semester, user.schedule);
                auto = true;
                lg.Action(user.username + " has auto-generated their course schedule for " + year + " in the " + semester + " semester.");
                alertStg.close();
                updateScheduleDisplay();
                autoLink.setDisable(false);
            }
            else{
                Label lbl = new Label("You have already completed an auto schedule.");
                alertPane.add(lbl, 0,6);
                lg.logger.warning(user.username + " attempted to auto-generate their schedule but they already have.");
            }

        });
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();

    }

    public void launchActivity(){
        Group alertGroup = new Group();
        Scene alertScene = new Scene(alertGroup, 400, 460);
        Stage alertStg = new Stage();

        GridPane alertPane = new GridPane();
        alertPane.getStyleClass().clear();
        alertPane.getStyleClass().add("pane");
        alertPane.setAlignment(Pos.CENTER);

        TextField nameActField = new TextField();
        Label nameActLbl = new Label("Enter name of activity:");

        TextField startTimeField = new TextField();
        Label startTimeLbl = new Label("Enter start time (in military time; ex. 8:00:00):");

        TextField endTimeField = new TextField();
        Label endTimeLbl = new Label("Enter end time (in military time; ex. 13:00:00):");

        TextField daysField = new TextField();
        Label daysLbl = new Label("Enter the day(s) occurring (ex. MWF):");

        Label alertTitleLbl = new Label("CREATE ACTIVITY");
        alertTitleLbl.getStyleClass().clear();
        alertTitleLbl.getStyleClass().add("subtitle");

        ButtonBar confirmBar = new ButtonBar();
        Button yBtn = new Button("Create");
        yBtn.getStyleClass().clear();
        yBtn.getStyleClass().add("buttons");
        yBtn.setOnAction(event -> {
            String title = nameActField.getText();
            String start = startTimeField.getText();
            String end = endTimeField.getText();
            String meets = daysField.getText();

            Course c = new Course(title, start, end, meets);
            Boolean cc = cl.checkConfliction(c,user.schedule);
            Boolean d = cl.checkDouble(c, user.schedule);

            if(d){
                lg.logConflict(user.username + " has attempted to add a personal activity: " + c + " that they already have on their schedule.");
            }else if(cc && user.schedule.contains(c)){
                lg.logConflict(user.username + " added the activity: " + c + " that conflicts with a course/activity on their schedule.");
                hasConflict = cc;
            }else if(cc && !user.schedule.contains(c)){
                lg.logConflict(user.schedule + " has attempted to add the activity: " + c + ", that conflicts with their schedule but elected not to add it.");
            }else{
                lg.Action(user.username + " has added the activity " + c);
            }

            cl.addClass(c, user.schedule);
            alertStg.close();
            updateScheduleDisplay();
        });


        Button nBtn = new Button("Cancel");
        nBtn.getStyleClass().clear();
        nBtn.getStyleClass().add("buttons2");
        ButtonBar.setButtonData(yBtn, ButtonBar.ButtonData.LEFT);

        setProperties(alertPane, 400, 460, 12, 5, 10);
        alertPane.add(alertTitleLbl, 0, 0, 2, 1);
        alertPane.add(nameActLbl, 0, 1);
        alertPane.add(nameActField, 0, 2);

        alertPane.add(startTimeLbl, 0, 3);
        alertPane.add(startTimeField, 0, 4);

        alertPane.add(endTimeLbl, 0, 5);
        alertPane.add(endTimeField, 0, 6);

        alertPane.add(daysLbl, 0, 7);
        alertPane.add(daysField, 0, 8);

        confirmBar.getButtons().addAll(yBtn, nBtn);
        alertPane.add(confirmBar, 0, 9);

        nBtn.setOnMouseClicked(event -> {
            alertStg.close();
        });

        nBtn.setOnMouseClicked(event -> alertStg.close());
        alertGroup.getChildren().add(alertPane);
        alertScene.getStylesheets().add("projStyles.css");
        alertStg.setScene(alertScene);
        alertStg.show();

    }



    TableView calendarPane;
    Group calendarGroup;
    Scene calendarScene;
    Stage calendarStage;

    //static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    public void launchCalendar(){
        calendarStage = new Stage();
        calendarGroup = new Group();
        calendarPane = new TableView<Course>();
        //setProperties(calendarPane, 500, 550, 3, 10, 0);

        TableColumn timeCol = new TableColumn<Course, String>("  ");
        timeCol.setCellValueFactory(new PropertyValueFactory<Course,String>("startTime"));
        TableColumn monCol = new TableColumn<Course, String>("Monday");
        monCol.setCellValueFactory(new PropertyValueFactory<Course,String>("courseCode"));
        TableColumn tueCol = new TableColumn<Course, String>("Tuesday");
        monCol.setCellValueFactory(new PropertyValueFactory<Course,String>("courseCode"));
        TableColumn wedCol = new TableColumn<Course, String>("Wednesday");
        monCol.setCellValueFactory(new PropertyValueFactory<Course,String>("courseCode"));
        TableColumn thuCol = new TableColumn<Course, String>("Thursday");
        monCol.setCellValueFactory(new PropertyValueFactory<Course,String>("courseCode"));
        TableColumn friCol = new TableColumn<Course, String>("Friday");
        monCol.setCellValueFactory(new PropertyValueFactory<Course,String>("courseCode"));
        TableColumn satCol = new TableColumn<Course, String>("Saturday");
        monCol.setCellValueFactory(new PropertyValueFactory<Course,String>("courseCode"));
        TableColumn sunCol = new TableColumn<Course, String>("Sunday");
        monCol.setCellValueFactory(new PropertyValueFactory<Course,String>("courseCode"));

        calendarPane.getColumns().add(timeCol);
        calendarPane.getColumns().add(monCol);
        calendarPane.getColumns().add(tueCol);
        calendarPane.getColumns().add(wedCol);
        calendarPane.getColumns().add(thuCol);
        calendarPane.getColumns().add(friCol);
        calendarPane.getColumns().add(satCol);
        calendarPane.getColumns().add(sunCol);

        VBox table = new VBox(calendarPane);

        calendarGroup.getChildren().add(table);
        calendarScene = new Scene(calendarGroup, 645, 400);
        calendarScene.getStylesheets().add("projStyles.css");
        calendarScene.setFill(rgb(245, 238, 238));

        calendarStage.setScene(calendarScene);
        calendarStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage loginStage) throws IOException {
        cl = new CourseList();
        user = null;
        auto = false;
        hasConflict = false;
        hasMax = false;
        loginStageC = loginStage;
        appImg = new Image("app-icn.png");

        launchLogin();


        loginStage.setTitle("Login");
        loginStage.setResizable(false);
        loginStage.setScene(loginScene);
        loginStage.getIcons().add(appImg);
        loginStage.show();


    }
}
