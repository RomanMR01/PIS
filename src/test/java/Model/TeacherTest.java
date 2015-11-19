package Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created by Vikno on 19.11.2015.
 */

/*
Перевірка геттерів і сеттерів
 */
public class TeacherTest {

    Teacher teacher = new Teacher();

    @Before
    public void setUp() throws Exception {
        System.out.println("Запуск тесту для класу Teacher!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Тест для класу Teacher завершено!");
    }


    @Test
    public void testGetNumber() throws Exception {
        System.out.println("Перевірка вводу номера телефону:");
        teacher.setPhone(12345);
        assertEquals(12345, teacher.getPhone());
        System.out.println("Тест перевірки вводу номера телефону завершено успішно");
    }


    @Test
    public void testGetName() throws Exception {
        System.out.println("Перевірка вводу імені:");
        teacher.setName("Викладач_1");
        assertEquals("Викладач_1", teacher.getName());

        teacher.setName("Викладач_2");
        assertNotSame("Викладач_3", teacher.getName());

        System.out.println("Тест перевірки вводу імені завершено успішно");
    }
}