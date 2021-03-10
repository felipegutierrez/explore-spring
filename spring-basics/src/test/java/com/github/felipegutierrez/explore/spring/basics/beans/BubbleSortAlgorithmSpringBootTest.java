package com.github.felipegutierrez.explore.spring.basics.beans;

import com.github.felipegutierrez.explore.spring.basics.services.BubbleSortAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BubbleSortAlgorithmSpringBootTest {

    @Autowired
    BubbleSortAlgorithm bubbleSortAlgorithm01;

    @Autowired
    BubbleSortAlgorithm bubbleSortAlgorithm02;

    @Test
    public void testIfBubbleSortAlgorithmCanSortArrayCorrectly() {
        int[] result = bubbleSortAlgorithm01.sort(new int[]{12, 4, 3, 70, 20, 0});
        Arrays.stream(result).forEach(i -> System.out.println(i));
        int[] expected = new int[]{0, 3, 4, 12, 20, 70};

        assertArrayEquals(expected, result);
    }

    @Test
    public void bubbleSortBeanMustBeSingleton() {
        assertEquals(bubbleSortAlgorithm01.hashCode(), bubbleSortAlgorithm02.hashCode());
    }
}
