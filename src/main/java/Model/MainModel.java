package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by Vikno on 17.11.2015.
 */

public class MainModel {



    private Connection conn = null;
    private Statement st = null;
    private ObservableList<Teacher> teacherDataModel = FXCollections.observableArrayList();

    private boolean connectionFlag = false;


    public MainModel() throws ClassNotFoundException {
        initDB();
    }

    /*
    Ініціалізація БД
     */
    public void initDB() throws ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");
        String dbFile = null;

        try {
            dbFile = "jdbc:sqlite:DB.s3db";
            conn = DriverManager.getConnection(dbFile);

            st = conn.createStatement();
            System.out.println("Connect success");

            connectionFlag = true;

        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    public void connAlert() {
        if (connectionFlag) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("OK");
            alert.setHeaderText(null);
            alert.setContentText("Введено некоректні дані!");

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText(null);
            alert.setContentText("Введено некоректні дані!");

            alert.showAndWait();
        }
    }

    public void setTable(ObservableList<Teacher> teacherData) {
        teacherDataModel = teacherData;
        calulateSalary();

    }

    public void calulateSalary() {
        ArrayList<ArrayList<Double>> allTeachers = new ArrayList<ArrayList<Double>>();//Список коефіцієнтів для кожного викладача

        ArrayList<Double> oneTeacher = new ArrayList<Double>();//остаточна ЗП для кожного імені

        //START

        //Отримання списку імен викладачів(якщо є один викладач з кількома предметами то буде одне ім'я)
        LinkedHashSet<String> hashSet = new LinkedHashSet<String>();

        try {

            //Дадаємо імена в множину
            for (Teacher teacher : teacherDataModel) {
                String teacherName = teacher.getName();
                hashSet.add(teacherName);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        System.out.println(hashSet);

        //Отримання даних для кожного імені
        for (String str : hashSet) {

            ArrayList<Double> oneID = new ArrayList<Double>();//Для одного предмету

            for (Teacher teacher : teacherDataModel) {
                if (teacher.getName().equals(str)) {
                    int exp = teacher.getExp();
                    String type = teacher.getType();
                    double rate = teacher.getRate();

                    int t = 1;
                    if (type.equals("Лекція")) {
                        t = 2;
                    }

                    double finalK = exp * t * rate;//остаточна ЗП для одного предмету

                    oneID.add(finalK);//додаємо
                }
            }
            allTeachers.add(oneID);//Додаємо коефіцієнти для одного викладача

        }

        System.out.println(allTeachers);//коефіцієнти для кожного викладача, якщо викладач веде 2 предмети, то коефіцієнти сумуються

        for (ArrayList<Double> one : allTeachers) {

            double sum = 0;

            for (Double ar : one) {
                sum += ar;
            }

            oneTeacher.add(sum);
        }

        System.out.println(oneTeacher);//остаточна зп для викладча(одна,якщо веде два предмети)
        //Stop

        writeToDB(hashSet, oneTeacher);//Запис в БД
        writeToDataModel(hashSet);
    }

    //Запис в БД
    public void writeToDB(LinkedHashSet<String> hashSet, ArrayList<Double> oneTeacher) {
        //Очищення таблиці
        try {
            String query = "DELETE FROM TEACHERS";
            st.execute(query);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        int i = 0;
        for (String str : hashSet) {
            for (Teacher teacher : teacherDataModel) {
                if (teacher.getName().equals(str)) {
                    try {
                        String query = "insert into 'TEACHERS' ('NAME','PHONE','EXPERIENCE','TYPE','SUBJECT','RATE','SALARY')"
                                + " values ('" + teacher.getName() +
                                "','" + teacher.getPhone() +
                                "','" + teacher.getExp() +
                                "','" + teacher.getType() +
                                "','" + teacher.getSubject() +
                                "','" + teacher.getRate() +
                                "','" + oneTeacher.get(i) + "');";

                        st.execute(query);


                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                }
            }
            i++;
        }

        try {
            ResultSet rs = st.executeQuery("select * from TEACHERS");
            while (rs.next()) {
                System.out.print("ID" + rs.getString(1) + " ");
                System.out.print(rs.getString(2) + " ");
                System.out.print(rs.getString(3) + " ");
                System.out.print(rs.getString(4) + " ");
                System.out.print(rs.getString(5) + " ");
                System.out.print(rs.getString(6) + " ");
                System.out.print(rs.getString(7) + " ");
                System.out.println(rs.getString(8) + " ");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public void writeToDataModel(LinkedHashSet<String> hashSet) {
        for (String str : hashSet) {
            for (Teacher teacher : teacherDataModel) {
                if (teacher.getName().equals(str)) {
                    try {
                        ResultSet rs = st.executeQuery("select * from TEACHERS where NAME ='" + str + "'");

                        int id = Integer.parseInt(rs.getString(1));
                        double salary = Double.parseDouble(rs.getString(8));

                        teacher.setId(id);
                        teacher.setSalary(salary);

                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                }

            }

        }

    }

    public ObservableList<Teacher> getTeacherDataModel() {
        try {
            ResultSet rs = st.executeQuery("select * from TEACHERS");

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
            System.out.println(ex);
        }

        return teacherDataModel;
    }

    public void clearDB() throws ClassNotFoundException {
        //Очищення таблиці
        try {
            String query = "DELETE FROM TEACHERS";
            st.execute(query);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }


    //Запис в таблицю
    /*
    public void writeToDataModel(LinkedHashSet<String> hashSet,ArrayList<Double> oneTeacher)
    {
        int i=0;
        for(String str:hashSet) {

            for (Teacher teacher : teacherDataModel) {
                if (teacher.getName().equals(str)) {
                    teacher.setSalary(oneTeacher.get(i));
                }
            }
            i++;
        }

//
//        for(Teacher teacher:teacherDataModel)
//            System.out.println(teacher.getName() + " " + teacher.getSalary());
    }
    */





    /*
    Запис в таблицю з БД
     */
    /*
    public void writeToTable()
    {
        try {
            ResultSet rs = st.executeQuery("select * from TEACHERS");

            int id = Integer.parseInt(rs.getString(1));
            String name = rs.getString(2);
            long number = Long.parseLong(rs.getString(3));
            int exp = Integer.parseInt(rs.getString(4));
            String type = rs.getString(5);
            String subject = rs.getString(6);
            double rate = Double.parseDouble(rs.getString(7));
            double salary = Double.parseDouble(rs.getString(8));

            int i=0;
            while (rs.next()) {
                Teacher teacher = teacherDataModel.get(i);
                teacher.setId(id);
                teacher.setSalary(salary);
                i++;
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }





    }
    */


//
//    public static void main(String[] args) throws Exception {
//        MainModel mainModel = new MainModel();
//    }
}