package Controller;

import Model.MainModel;
import Model.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;

/**
 * Created by Vikno on 17.11.2015.
 */

/*
Контроллер для основного вікна програми
 */
public class MainController {

    /*
    Логер для MainController
     */
    private static final Logger log = Logger.getLogger(MainController.class);

    /*
    Поля для вводу анкетних даних викладача
     */
    @FXML
    TextField nameTF; //Ім'я

    @FXML
    TextField numberTF;//Номер телефону

    @FXML
    TextField expTF;//Досвід роботи

    @FXML
    TextField rateTF;//Місячна плата

    @FXML
    TextField subjectTF;//Предмет

    @FXML
    ChoiceBox typeBox;//Тип предмету(лекція чи практична)

    /*
    Кнопки
     */
    @FXML
    Button addButton;//Додавання викладача в табдицю

    @FXML
    Button clearButton;//Очищення всіх даних

    @FXML
    Button calculateButton;//Обислення ЗП

    @FXML
    Button updateButton;//Оновлення даних

    @FXML
    Button helpButton;//Довідка
    /*
    Створення екземпляру класу MainModel для подальшого обміну даними
     */
    MainModel mainModel;
    /*
    Список для зберігання всієї інформації
     */
    private ObservableList<Teacher> teacherData = FXCollections.observableArrayList();
    /*
    Таблиця і її колонки
     */
    @FXML
    private TableView<Teacher> tableTeachers;//Таблиця
    @FXML
    private TableColumn<Teacher, Integer> idColumn;//id
    @FXML
    private TableColumn<Teacher, String> nameColumn;//ім'я
    @FXML
    private TableColumn<Teacher, Long> numberColumn;//номер телефону
    @FXML
    private TableColumn<Teacher, Integer> expColumn;//досвід роботи
    @FXML
    private TableColumn<Teacher, String> typeColumn;//тип предмету
    @FXML
    private TableColumn<Teacher, String> subjectColumn;//назва предмету
    @FXML
    private TableColumn<Teacher, Double> rateColumn;//місячна оплата
    @FXML
    private TableColumn<Teacher, Double> salaryColumn;//фінальна зарплата

    /*
    Ініціалізація таблиці
     */
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
        Ініціалізація таблиці та блокування кнопок
         */

        tableTeachers.setItems(teacherData);

        /*
        Кнопки заблоковані доки не введено даних в таблицю
         */
        calculateButton.setDisable(true);
        clearButton.setDisable(true);
        updateButton.setDisable(true);


    }

    /*
    Зчитування анкетних двних з форми і вивід на таблицю
     */
    @FXML
    public void setData() {
        try {
            String name = nameTF.getText();
            long number = Long.parseLong(numberTF.getText());
            int exp = Integer.parseInt(expTF.getText());
            double rate = Double.parseDouble(rateTF.getText());
            String subject = subjectTF.getText();
            String type = (String) typeBox.getValue();

            /*
            Додавання даних в таблицю
            для id і зарплати автоматично ставиться 0.
             */
            teacherData.add(new Teacher(0, name, number, exp, type, subject, rate, 0));

            /*
            Активація кнопки обчислення
             */
            calculateButton.setDisable(false);
        } catch (Exception e) {
            /*
            Запис помилки в log файл та вивід інформації на екран
             */
            log.error("Введено некоректні дані!");
            errorAlert("Введено некоректні дані!", "ПОМИЛКА!");
        }

    }

    /*
    Обчислює ЗП, заносить в БД
     */
    @FXML
    public void calculateData() throws Exception {

        /*
        Блокування кнопок додавання та обчислення і активація оновлення.
         */
        addButton.setDisable(true);
        calculateButton.setDisable(true);
        updateButton.setDisable(false);


        mainModel = new MainModel();
        mainModel.setTable(teacherData);

    }

    /*
    Оновлення даних в таблиці
     */
    @FXML
    public void updateTable() throws ClassNotFoundException {


        updateButton.setDisable(true);//Блокування копки оновлення після кліку
        clearButton.setDisable(false);//Активація кнопки очищення даних

        /*
        Отримання таблиці з БД і оновлення її на GUI
         */
        mainModel = new MainModel();
        teacherData = mainModel.getTeacherDataModel();
        tableTeachers.setItems(teacherData);//Оновлює дані в TableView

    }

    /*
    Видалення даних з БД і таблиці
     */
    @FXML
    public void deleteTable() throws Exception {

        /*
        Видалення рядків колекції teacherData
         */
        int rows = teacherData.size();

        for (int i = 0; i < rows; i++) {
            teacherData.remove(0);
        }

        /*
        Очищення БД
         */
        mainModel.clearDB();

        /*
        Активаці кнопки додавання і блокування кнопки очищення
         */
        clearButton.setDisable(true);
        addButton.setDisable(false);

    }

    /*
    Методи меню
     */

    /*
    Перевірка з'єднання з БД
     */
    @FXML
    public void tryConnection() throws ClassNotFoundException {

        mainModel = new MainModel();
        mainModel.connAlert();//Виклик повідомлення про стан підключення до БД
    }

    /*
    Завантаження даних з БД
     */
    @FXML
    public void loadDB() throws ClassNotFoundException {

        /*
        Завантажуємо дані з БД і додаємо їх в таблицю на GUI
         */

        try {
            mainModel = new MainModel();
            teacherData = mainModel.getTeacherDataModel();
            tableTeachers.setItems(teacherData);
        } catch (Exception e) {
            errorAlert("Невдалося завантажити дані!", "Помилка");
        }

        /*
        Активація кнопки очищення даних і блокуаання кнопки додавання даних
         */
        clearButton.setDisable(false);
        addButton.setDisable(true);

    }

    /*
    Вихід з програми
     */
    @FXML
    public void exitApp() {
        System.exit(0);
    }

    /*
    Про програму
     */
    @FXML
    public void aboutAlert() {
        String about = "";
        String s1 = "Програма для обчислення заробітньої плати викладачів.\n";
        String s2 = "Виконав:    студент гр. КН-45 Мальчишин Роман.\n";
        String s3 = "Перевірив: Дупак Б.П.\n";

        about += s1 + s2 + s3;
        infoAlert(about, "Про програму");

    }

    /*
    Довідка
     */
    @FXML
    public void helpAlert() {
        String help = "";
        String s1 = "Програма для обчислення заробітньої плати викладачів.\n";
        String s2 = "Для коректної роботи програми необхідно:\n";
        String s3 = "1. Коректно ввести анкетні дані викладача:\n";
        String s4 = "\tІмя - тестове поле.\n";
        String s5 = "\tТелефон - числове поле.\n";
        String s6 = "\tСтаж - числове поле.\n";
        String s7 = "\tПредмет - текстове поле.\n";
        String s8 = "\tТип предмету - лекція або практична.\n";
        String s9 = "\tОплата - числове поле.\n\n";
        String s10 = "2.Натиснути на кнопку \"Додати\" для додавання даних у таблицю.\n";
        String s11 = "3.Натиснути на кнопку \"Обчислити\" для обчислення зарплати.\n";
        String s12 = "4.Щоб побачити зміни необхідно натиснути на кнопку \"Оновити\".\n";
        String s13 = "5.Для проведення нових розрахунку необхідно на кнопку \"Очистити дані\".\n";


        help += s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8 + s9 + s10 + s11 + s12 + s13;

        infoAlert(help, "Довідка");

    }

    /*
     Вивід повідомлення про різні помилки
    */
    private void errorAlert(String message, String title) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    /*
      Вивід повідомлення про певні дії
     */
    private void infoAlert(String message, String title) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
}

