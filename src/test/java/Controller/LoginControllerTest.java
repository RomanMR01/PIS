package Controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vikno on 18.11.2015.
 */

/*
Перевірка авторизації
 */
public class LoginControllerTest {

    LoginController loginController = new LoginController();

    @Before
    public void setUp() throws Exception {
        System.out.println("Запуск тесту для класу LogicController!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Тест для класу LogicController завершено!");
    }

    @Test
    public void testLoginController() {
        System.out.println("Старт перевірки логіна.");

        assertEquals(false, loginController.loginFlag);

        System.out.println("Тест перевірки логіна завершено успішно!");

    }

}