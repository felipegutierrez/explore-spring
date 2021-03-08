package com.github.felipegutierrez.explore.spring.basics.beans;

import com.github.felipegutierrez.explore.spring.ExploreSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class QuickSortAlgorithmTest {

    ApplicationContext applicationContext = SpringApplication.run(ExploreSpringApplication.class);

    @Test
    void testIfQuickSortAlgorithmCanSortArrayCorrectly() {
        QuickSortAlgorithm quickSortAlgorithm = applicationContext.getBean(QuickSortAlgorithm.class);
        int[] result = quickSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
        Arrays.stream(result).forEach(i -> System.out.println(i));
        int[] expected = new int[]{0, 3, 4, 12, 20, 70};

        assertArrayEquals(expected, result);
    }

    @Test
    public void quickSortBeanMustNotBeSingleton() {
        QuickSortAlgorithm quickSortAlgorithm01 = applicationContext.getBean(QuickSortAlgorithm.class);
        QuickSortAlgorithm quickSortAlgorithm02 = applicationContext.getBean(QuickSortAlgorithm.class);

        assertNotEquals(quickSortAlgorithm01.hashCode(), quickSortAlgorithm02.hashCode());
    }
}
