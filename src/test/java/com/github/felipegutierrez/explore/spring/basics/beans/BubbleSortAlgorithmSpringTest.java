package com.github.felipegutierrez.explore.spring.basics.beans;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = BubbleSortAlgorithm.class)
public class BubbleSortAlgorithmSpringTest {

    // ApplicationContext applicationContext = SpringApplication.run(ExploreSpringApplication.class);
    // ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BubbleSortAlgorithm.class);
    // StepExecution execution = MetaDataInstanceFactory.createStepExecution();

//    @Autowired
//    BubbleSortAlgorithm bubbleSortAlgorithm;
//    @Autowired
//    BubbleSortAlgorithm bubbleSortAlgorithm01;
//    @Autowired
//    BubbleSortAlgorithm bubbleSortAlgorithm02;
//
//    @Test
//    public void testIfBubbleSortAlgorithmCanSortArrayCorrectly() {
//        // BubbleSortAlgorithm bubbleSortAlgorithm = applicationContext.getBean(BubbleSortAlgorithm.class);
//        int[] result = bubbleSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
//        Arrays.stream(result).forEach(i -> System.out.println(i));
//        int[] expected = new int[]{0, 3, 4, 12, 20, 70};
//
//        assertArrayEquals(expected, result);
//    }
//
//    @Test
//    public void bubbleSortBeanMustBeSingleton() {
//        //        BubbleSortAlgorithm bubbleSortAlgorithm01 = applicationContext.getBean(BubbleSortAlgorithm.class);
//        //        BubbleSortAlgorithm bubbleSortAlgorithm02 = applicationContext.getBean(BubbleSortAlgorithm.class);
//
//        assertEquals(bubbleSortAlgorithm01.hashCode(), bubbleSortAlgorithm02.hashCode());
//    }
}
