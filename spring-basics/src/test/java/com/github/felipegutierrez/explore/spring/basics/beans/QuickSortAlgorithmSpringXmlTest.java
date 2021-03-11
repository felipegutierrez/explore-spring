package com.github.felipegutierrez.explore.spring.basics.beans;

import com.github.felipegutierrez.explore.spring.ExploreSpringApplication;
import com.github.felipegutierrez.explore.spring.ExploreSpringXmlApplication;
import com.github.felipegutierrez.explore.spring.basics.services.QuickSortAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
// @ContextConfiguration(locations = "/applicationContext.xml")
@ContextConfiguration(classes = ExploreSpringApplication.class)
public class QuickSortAlgorithmSpringXmlTest {

    @Autowired
    QuickSortAlgorithm quickSortAlgorithm01;

    @Autowired
    QuickSortAlgorithm quickSortAlgorithm02;

    @Test
    void testIfQuickSortAlgorithmCanSortArrayCorrectly() {
        int[] result = quickSortAlgorithm01.sort(new int[]{12, 4, 3, 70, 20, 0});
        Arrays.stream(result).forEach(i -> System.out.println(i));
        int[] expected = new int[]{0, 3, 4, 12, 20, 70};

        assertArrayEquals(expected, result);
    }

    @Test
    public void quickSortBeanMustNotBeSingleton() {
        assertNotEquals(quickSortAlgorithm01.hashCode(), quickSortAlgorithm02.hashCode());
    }
}
