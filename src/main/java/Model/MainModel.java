package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by Vikno on 17.11.2015.
 */

/*
Модель реалізує методи для обміну інформацією з БД JDBC
та методи для обчислення зарплати.
 */

public class MainModel {

    /*
    Логер для MainModel
     */
    private static final Logger logger = Logger.getLogger(MainModel.class);
    /*
    Прапорець для перевірки підключення до БД
     */
    public boolean connectionFlag = false;
    /*
    dbConnection - для підключення до БД
    statement - для SQL запитів
     */
    private Connection dbConnection = null;
    private Statement statement = null;
    /*
    Колеція для зберігання інформації по вчителях
     */
    private ObservableList<Teacher> teacherDataModel = FXCollections.observableArrayList();

    /*
    Ініціалізація підключення до БД
     */
    public MainModel() throws ClassNotFoundException {
        initDB();
    }

    /*
    Ініціалізація БД
     */
    private void initDB() throws ClassNotFoundException {

        /*
        Підключення JDBC драйвера
         */
        Class.forName("org.sqlite.JDBC");

        /*
        Шлях до файлу БД
         */
        String dbFile = null;

        /*
        Блок підключення до БД DB.s3db
         */
        try {

            dbFile = "jdbc:sqlite:DB.s3db";                    //Шлях до файлу
            dbConnection = DriverManager.getConnection(dbFile);//Підключення до БД
            statement = dbConnection.createStatement();        //Створення SQL запиту для БД

            /*
            connectionFlag для відображення повідомлення в Alert
             */
            connectionFlag = true;


        } catch (Exception e) {

            connectionFlag = false;
            /*
            Виклик повідомлення про помилку
             */
            logger.error("Не вдалося підключитись до БД!");
            errorAlert("Не вдалося підключитись до БД!", "ПОМИЛКА");

        }
    }


    /*
    Алерт про стан підключення до БД, викликається тільки з меню:Файл-Підключитись до БД
     */
    public void connAlert() {

        if (connectionFlag) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Все добре!");
            alert.setHeaderText(null);
            alert.setContentText("Підключеня активне!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Помилка підключення до БД!");
            alert.showAndWait();
        }
    }

    /*
    ГОЛОВНИЙ МЕТОД
    для запуску обчислення ЗП та запису в БД
     */
    public void setTable(ObservableList<Teacher> teacherData) {
    /*
    Копіювання колекції з MainController в MainModel для подальшої обробки даних.
    Користувач вводить дані в GUI , далі вони записуються в колекію teacherData в класі MainController
    */
        teacherDataModel = teacherData;
        calсulateSalary();

    }

    /*
    Основний метод для виконання обчислень

    Коли викладач веде один предмет, то зарплата рахується тільки для одного предмету.
    Якщо викладач веде 2 і більше предметів(або лекцію і практичну) то обчислюється ЗП окремо для кожного предмету
    а потім додається.
    В результаті у кожному записі буде однакова зарплата для одного імені викладача:

     Name = Teacher1; Subject = S1; Rate = 100; Salary = 100+200 = 300;
     Name = Teacher1; Subject = S2; Rate = 200; Salary = 300;

     */
    private void calсulateSalary() {

        /*
        Колекція зберігає коефіцієнти (для обчислення ЗП) для кожного предмету
        тобто, коли є один викладач, що має два предмети, в колекції будуть зберігатися два записи.
         */
        ArrayList<ArrayList<Double>> allTeachers = new ArrayList<ArrayList<Double>>();

        /*
        Колекція зберігає остаточну обчислену ЗП для кожного викладача.
        Якщо викладач веде один предмет то буде один запис.
        Якщо викладач веде 2 і більше предмети то буде також один запис.
         */
        ArrayList<Double> oneTeacher = new ArrayList<Double>();//остаточна ЗП для кожного імені

        /*
        Колекція для унікального зберігання імені викладача,
        Якщо викладач веде 2 і більше предметів, то буде тільки один запис в колекції.
         */
        LinkedHashSet<String> teacherNameSet = new LinkedHashSet<String>();

        /*
        Додавання імен в колекцію з таблиці
        */
        try {

            for (Teacher teacher : teacherDataModel) {
                String teacherName = teacher.getName();
                teacherNameSet.add(teacherName);
            }
        } catch (Exception ex) {
            /*
            Виклик повідомлення про помилку
             */
            logger.error("Не вдалося завантажити дані з таблиці!");
            errorAlert("Не вдалося завантажити дані з таблиці!", "ПОМИЛКА");
        }

        /*
        Обчислення коефіцієнта для кожного предмету(запису в таблиці)
         */
        for (String str : teacherNameSet) {

            /*
            Зберігає ЗП тільки для одного предмету
             */
            ArrayList<Double> oneSubject = new ArrayList<Double>();

            /*
            -----------
            Тут обчислюється зп для одного предмету
            -----------
             */
            for (Teacher teacher : teacherDataModel) {

                /*
                Якщо ім'я викладача з таблиці(teacherDataModel) співпадає з іменем з колекції teacherNameSet.
                Тобто коли в teacherNameSet буде один запис, то в teacherDataModel може бути багато записів
                в залежності від кількості заисів, стільки буде відбуватись дії в циклі і обчислюватись коефіцієнти.
                 */
                if (teacher.getName().equals(str)) {

                    /*
                    Отримуємо:
                    досвід роботи( в роках)
                    тип предмету( лекція чи практична)
                    місячна оплата(ставка)
                     */
                    int experience = teacher.getExp();
                    String typeSubject = teacher.getType();
                    double rateInMonth = teacher.getRate();

                    /*
                    Коефіцієнти для типу предмету
                    Коли Лекція, то 2 в інших випадках 1
                     */
                    int coefficient = 1;
                    if (typeSubject.equals("Лекція")) {
                        coefficient = 2;
                    }

                    /*
                    Фінальне обчислення ЗП для одного предету
                    ЗП = Досвід(в роках) * Тип предмету(1 - Практична, 2 - Лекція) * Оплата за місяць
                     */
                    double finalSalaryForOneSubject = experience * coefficient * rateInMonth;

                    /*
                    Зберігаємо в колекції для одного предмету
                     */
                    oneSubject.add(finalSalaryForOneSubject);
                }
            }
            /*
            Зберігаємо список ЗП одного викладача

             */
            allTeachers.add(oneSubject);

        }

        /*
        Обчислення фінальної зарплати для кожного викладача.
        Якщо викладач веде кілька предметів, то все сумується
         */
        for (ArrayList<Double> one : allTeachers) {
            /*
            Значення кінцевої ЗП
             */
            double finalSalary = 0;

            /*
            Сумування ЗП по предметах для одного виклдача
             */
            for (Double salaryForSubject : one) {
                finalSalary += salaryForSubject;
            }

            /*
            Заносимо ЗП фінальний список зарплат
             */
            oneTeacher.add(finalSalary);
        }


        /*
        Записуємо всю інформацію в БД
         */
        writeToDB(teacherNameSet, oneTeacher);

        /*
        Записуємо всю інформацію в таблицю для відображення на GUI
         */
        writeToDataModel(teacherNameSet);
    }

    /*
    Запис всієї інформації в БД
     */
    public void writeToDB(LinkedHashSet<String> teacherNameSet, ArrayList<Double> oneTeacher) {

        /*
        Створення нової таблиці
         */
        try {
            String query = "CREATE TABLE [TEACHERS] ("
                    + "[ID] INTEGER DEFAULT '''''''0''''''' PRIMARY KEY NOT NULL,"
                    + "[NAME] TEXT  NOT NULL,"
                    + "[PHONE] NUMERIC  NOT NULL,"
                    + "[EXPERIENCE] INTEGER  NOT NULL,"
                    + "[TYPE] TEXT  NOT NULL,"
                    + "[SUBJECT] TEXT  NOT NULL,"
                    + "[RATE] FLOAT  NOT NULL,"
                    + "[SALARY] FLOAT DEFAULT '''0''' NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (SQLException ex) {
            /*
            Виклик повідомлення про помилку
             */
            logger.error("Не вдалося створити нову таблицю в базі даних!");
            errorAlert("Не вдалося створити нову таблицю в базі даних!", "ПОМИЛКА");
        }


        /*
        Зчитуємо ЗП з колекції oneTeacher.
        Решта даних читаємо з таблиці teacherDataModel.
        ID автоматично визначається в БД.
         */

        int i = 0;//Warning,для переходу по колекції з зарплат

        /*
        Проходимо по множині імен
         */
        for (String str : teacherNameSet) {
            /*
            Проходимо по всіх записах в таблиці
             */
            for (Teacher teacher : teacherDataModel) {
                /*
                Якщо імена співпадають, додаємо дані в БД
                 */
                if (teacher.getName().equals(str)) {
                    try {
                        String query = "insert into 'TEACHERS' ('NAME','PHONE','EXPERIENCE','TYPE','SUBJECT','RATE','SALARY')"
                                + " values ('" + teacher.getName() +
                                "','" + teacher.getPhone() +
                                "','" + teacher.getExp() +
                                "','" + teacher.getType() +
                                "','" + teacher.getSubject() +
                                "','" + teacher.getRate() +
                                "','" + oneTeacher.get(i) + "');";//Зчитує з колекції, в таблиці поки 0

                        statement.execute(query);

                    } catch (SQLException ex) {
                         /*
                           Виклик повідомлення про помилку
                          */
                        logger.error("Не вдалося записати дані в базу даних!");
                        errorAlert("Не вдалося записати дані в базу даних!", "ПОМИЛКА");
                    }
                }
            }
            i++;
        }
    }

    /*
    Запис даних з БД в таблицю на GUI
     */
    public void writeToDataModel(LinkedHashSet<String> teacherNameSet) {

        /*
        Проходимо по множині імен
         */
        for (String str : teacherNameSet) {
            /*
            Проходимо по всіх записах в таблиці
             */
            for (Teacher teacher : teacherDataModel) {
                /*
                Якщо імена співпадають, зчитуємо з БД і додаємо в таблицю
                 */
                if (teacher.getName().equals(str)) {

                    try {
                        ResultSet rs = statement.executeQuery("select * from TEACHERS where NAME ='" + str + "'");

                        /*
                        З БД зчитуємо тільки id та зарплату.
                        Решта даних є в teacherDataModel
                         */
                        int id = Integer.parseInt(rs.getString(1));
                        double salary = Double.parseDouble(rs.getString(8));

                        teacher.setId(id);
                        teacher.setSalary(salary);

                    } catch (SQLException ex) {
                        /*
                           Виклик повідомлення про помилку
                          */
                        logger.error("Не вдалося завантажити дані з БД!");
                        errorAlert("Не вдалося завантажити дані з БД!", "ПОМИЛКА");
                    }
                }

            }

        }

    }


    /*
    Для оновлення поточної інформації в табдиці на GUI
    і завантаження збереженої БД
    Отримання всієї інформації з БД і запис її в таблицю.
     */
    public ObservableList<Teacher> getTeacherDataModel() {

        /*
        Зчитуємо всю інформацію з БД і заносимо її в таблицю
         */
        try {
            ResultSet rs = statement.executeQuery("select * from TEACHERS");

            while (rs.next()) {
                int id = Integer.parseInt(rs.getString(1));
                String name = rs.getString(2);
                long number = Long.parseLong(rs.getString(3));
                int exp = Integer.parseInt(rs.getString(4));
                String type = rs.getString(5);
                String subject = rs.getString(6);
                double rate = Double.parseDouble(rs.getString(7));
                double salary = Double.parseDouble(rs.getString(8));

                teacherDataModel.add(new Teacher(id, name, number, exp, type, subject, rate, salary));
            }
        } catch (SQLException ex) {
            /*
             Виклик повідомлення про помилку
            */
            logger.error("Не вдалося завантажити дані з БД!");
            errorAlert("Не вдалося завантажити дані з БД!", "ПОМИЛКА");
        }

        return teacherDataModel;
    }

    /*
    Видалення всіх записів з БД
     */
    public void clearDB() throws ClassNotFoundException {

        try {
            String query = "DELETE FROM TEACHERS";
            statement.execute(query);
        } catch (SQLException ex) {
            /*
            Виклик повідомлення про помилку
             */
            logger.error("Не вдалося видалити дані з бази даних!");
            errorAlert("Не вдалося видалити дані з бази даних!", "ПОМИЛКА");
        }
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

}