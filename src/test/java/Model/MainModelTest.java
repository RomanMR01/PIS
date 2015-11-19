package Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vikno on 19.11.2015.
 */

/*
Перевірка підключення до БД
 */
public class MainModelTest {

    MainModel mainModel;

    @Before
    public void setUp() throws Exception {
        System.out.println("Запуск тесту для класу MainModel!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Тест для класу MainModel завершено!");
    }

    @Test
    public void testClearDB() throws ClassNotFoundException {
        System.out.println("Перевірка підключення до БД:");
        mainModel = new MainModel();
        assertEquals(true, mainModel.connectionFlag);
        System.out.println("Тест перевірки підключення до БД завершено успішно");
    }

}