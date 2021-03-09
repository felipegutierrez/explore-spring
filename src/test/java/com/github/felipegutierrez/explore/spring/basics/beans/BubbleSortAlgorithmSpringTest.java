package com.github.felipegutierrez.explore.spring.basics.beans;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BubbleSortAlgorithmSpringTest {

    @Test
    public void testIfBubbleSortAlgorithmCanSortArrayCorrectly() {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BubbleSortAlgorithm.class)) {

            BubbleSortAlgorithm bubbleSortAlgorithm = applicationContext.getBean(BubbleSortAlgorithm.class);
            int[] result = bubbleSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
            Arrays.stream(result).forEach(i -> System.out.println(i));
            int[] expected = new int[]{0, 3, 4, 12, 20, 70};

            assertArrayEquals(expected, result);
        }
    }

    @Test
    public void bubbleSortBeanMustBeSingleton() {
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BubbleSortAlgorithm.class)) {
            BubbleSortAlgorithm bubbleSortAlgorithm01 = applicationContext.getBean(BubbleSortAlgorithm.class);
            BubbleSortAlgorithm bubbleSortAlgorithm02 = applicationContext.getBean(BubbleSortAlgorithm.class);

            assertEquals(bubbleSortAlgorithm01.hashCode(), bubbleSortAlgorithm02.hashCode());
        }
    }
}
