package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.basics.beans.QuickSortAlgorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ExploreSpringApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ExploreSpringApplication.class, args);
        QuickSortAlgorithm quickSortAlgorithm = applicationContext.getBean(QuickSortAlgorithm.class);
        int[] result = quickSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
        Arrays.stream(result).forEach(i -> System.out.println(i));
    }
}
