package edu.fra.uas.beanexample;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.fra.uas.controller.BeanController;

@SpringBootTest
public class ControllerTest {

    @Autowired
    private BeanController beanController;

    @Test
    void testController(){
        assertThat(beanController.putMessage("Das ist ein Test")).isEqualTo(" put messgae Das ist ein Test");

    }

    @Test
    void testIncrement(){
        assertThat(beanController.getCounter().equals(0));
        
        beanController.increment();

        assertThat(beanController.getCounter().equals(1));

        beanController.increment();

        assertThat(beanController.getCounter().equals(2));
        
        System.out.println("INCREMENT SUCCESSFULL: "+ beanController.getCounter());
    }
}
