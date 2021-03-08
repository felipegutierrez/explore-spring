package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.basics.beans.QuickSortAlgorithm;
import com.github.felipegutierrez.explore.spring.basics.dao.PersonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ExploreSpringApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExploreSpringApplication.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ExploreSpringApplication.class, args);
        QuickSortAlgorithm quickSortAlgorithm = applicationContext.getBean(QuickSortAlgorithm.class);
        int[] result = quickSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
        Arrays.stream(result).forEach(i -> System.out.println(i));

        PersonDAO personDAO01 = applicationContext.getBean(PersonDAO.class);
        PersonDAO personDAO02 = applicationContext.getBean(PersonDAO.class);

        LOGGER.info("DAO 01: {}, {}", personDAO01.hashCode(), personDAO01);
        LOGGER.info("DAO 01 JDBCConnection: {}, {}", personDAO01.getJdbcConnection().hashCode(), personDAO01.getJdbcConnection());
        LOGGER.info("DAO 02: {}, {}", personDAO02.hashCode(), personDAO02);
        LOGGER.info("DAO 02 JDBCConnection: {}, {}", personDAO02.getJdbcConnection().hashCode(), personDAO02.getJdbcConnection());
    }
}
