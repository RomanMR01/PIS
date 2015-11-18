package Controller;

import Model.MainModel;
import Model.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;

/**
 * Created by Vikno on 17.11.2015.
 */
public class MainController {

    private static final Logger log = Logger.getLogger(MainController.class);//lll

    @FXML
    TextField nameTF;

    @FXML
    TextField numberTF;

    @FXML
    TextField expTF;

    @FXML
    TextField rateTF;

    @FXML
    TextField subjectTF;

    @FXML
    ChoiceBox typeBox;

    @FXML
    Button addButton;

    @FXML
    Button clearButton;

    @FXML
    Button calculateButton;

    @FXML
    Button updateButton;

    @FXML
    Button helpButton;

    private ObservableList<Teacher> teacherData = FXCollections.observableArrayList();

    @FXML
    private TableView<Teacher> tableTeachers;

    @FXML
    private TableColumn<Teacher, Integer> idColumn;

    @FXML
    private TableColumn<Teacher, String> nameColumn;

    @FXML
    private TableColumn<Teacher, Long> numberColumn;

    @FXML
    private TableColumn<Teacher, Integer> expColumn;

    @FXML
    private TableColumn<Teacher, String> typeColumn;

    @FXML
    private TableColumn<Teacher, String> subjectColumn;

    @FXML
    private TableColumn<Teacher, Double> rateColumn;

    @FXML
    private TableColumn<Teacher, Double> salaryColumn;

    @FXML
    private void initialize() {
        /*
        Встановлення типів значень для колонок таблиці
         */
        idColumn.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Teacher, String>("name"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Teacher, Long>("phone"));
        expColumn.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("exp"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Teacher, String>("type"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<Teacher, String>("subject"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<Teacher, Double>("rate"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<Teacher, Double>("salary"));

        /*
        Ініціалізація таблиці
         */

        tableTeachers.setItems(teacherData);

        calculateButton.setDisable(true);
        clearButton.setDisable(true);
        updateButton.setDisable(true);



    }

    /*
    Зчитування з форми і вивід на таблицю
     */
    @FXML
    public void setData()
    {
        try {
            String name = nameTF.getText();
            long number = Long.parseLong(numberTF.getText());
            int exp = Integer.parseInt(expTF.getText());
            double rate = Double.parseDouble(rateTF.getText());
            String subject = subjectTF.getText();
            String type = (String) typeBox.getValue();

            teacherData.add(new Teacher(0, name, number, exp,type,subject,rate,0));//додавання в таблицю

            calculateButton.setDisable(false);
        }
        catch (Exception e)
        {
            log.info("Это информационное сообщение!");//lll

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Введено некоректні дані!");

            alert.showAndWait();
        }

    }

    MainModel mainModel;

    /*
    Обчислює ЗП, заносить в БД
     */
    @FXML
    public void calculateData ()throws Exception {

        addButton.setDisable(true);
        calculateButton.setDisable(true);
        updateButton.setDisable(false);


        mainModel = new MainModel();
        mainModel.setTable(teacherData);

    }

    /*
    Має оновити таблицю(Alert)
     */
    @FXML
    public void updateTable() throws ClassNotFoundException {
       /* int s = teacherData.size();

        for(int i=0;i<s;i++)
        {
            teacherData.remove(0);
        }
        */
        updateButton.setDisable(true);
        clearButton.setDisable(false);

        mainModel  =  new MainModel();
        teacherData = mainModel.getTeacherDataModel();

        tableTeachers.setItems(teacherData);

        System.out.println("UPDATE");
    }

    /*
    Видаляє дані з таблиці і БД
     */
    @FXML
    public void deleteTable() throws Exception {
        int s = teacherData.size();

        for(int i=0;i<s;i++) {
            teacherData.remove(0);
        }

        mainModel.clearDB();

        clearButton.setDisable(true);
        addButton.setDisable(false);

        System.out.println("DELETED");

    }

    /*
    Методи меню
     */

    /*
    Перевірка з'єднання з БД
     */
    @FXML
    public void tryConnection() throws ClassNotFoundException {

        mainModel  =  new MainModel();
        mainModel.connAlert();
        System.out.println("Connection");
    }

    /*
    Завантажити дані з Бд
     */
    @FXML
    public void loadDB() throws ClassNotFoundException {

        mainModel  =  new MainModel();
        teacherData = mainModel.getTeacherDataModel();

        for(Teacher teacher:teacherData)
            System.out.println(teacher.getName());


        tableTeachers.setItems(teacherData);
        clearButton.setDisable(false);
        addButton.setDisable(true);

        System.out.println("Load DB");

    }


    /*
    Вихід з програми
     */
    @FXML
    public void exitApp()
    {
        System.exit(0);
    }

    /*
    Про програму
     */
    @FXML
    public void aboutAlert()
    {
        System.out.println("ddd");
    }

    /*
    Довідка
     */
    @FXML
    public void helpAlert()
    {
        String helpMessage="A";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ДОВІДКА");
        alert.setHeaderText(null);
        alert.setContentText("Введено некоректні дані!");

        alert.showAndWait();

    }
}

