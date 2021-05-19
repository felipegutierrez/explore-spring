package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.basics.dao.PersonDao;
import com.github.felipegutierrez.explore.spring.basics.services.BubbleSortAlgorithm;
import com.github.felipegutierrez.explore.spring.basics.services.QuickSortAlgorithm;
import com.github.felipegutierrez.explore.spring.basics.services.SomeCdiBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan({"com.github.felipegutierrez.explore.spring.basics.beans",
        "com.github.felipegutierrez.explore.spring.controller",
        "com.github.felipegutierrez.explore.spring.factory",
        "com.github.felipegutierrez.explore.spring.singleton",
        "com.github.felipegutierrez.explore.spring.basics.dao",
        "com.github.felipegutierrez.explore.spring.basics.services"})
public class ExploreSpringApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExploreSpringApplication.class);

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ExploreSpringApplication.class)) {
            QuickSortAlgorithm quickSortAlgorithm = applicationContext.getBean(QuickSortAlgorithm.class);
            int[] result = quickSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
            Arrays.stream(result).forEach(i -> System.out.println(i));

            BubbleSortAlgorithm bubbleSortAlgorithm = applicationContext.getBean(BubbleSortAlgorithm.class);
            int[] result01 = bubbleSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
            Arrays.stream(result01).forEach(i -> System.out.println(i));

            PersonDao personDAO01 = applicationContext.getBean(PersonDao.class);
            PersonDao personDAO02 = applicationContext.getBean(PersonDao.class);

            LOGGER.info("DAO 01: {}, {}", personDAO01.hashCode(), personDAO01);
            LOGGER.info("DAO 01 JDBCConnection proxy target: {}, {}", personDAO01.getJdbcConnectionProxyTarget().hashCode(), personDAO01.getJdbcConnectionProxyTarget());
            LOGGER.info("DAO 01 JDBCConnection proxy interface: {}, {}", personDAO01.getJdbcConnectionProxyInterface().hashCode(), personDAO01.getJdbcConnectionProxyInterface());
            LOGGER.info("DAO 02: {}, {}", personDAO02.hashCode(), personDAO02);
            LOGGER.info("DAO 02 JDBCConnection proxy target: {}, {}", personDAO02.getJdbcConnectionProxyTarget().hashCode(), personDAO02.getJdbcConnectionProxyTarget());
            LOGGER.info("DAO 02 JDBCConnection proxy interface: {}, {}", personDAO02.getJdbcConnectionProxyInterface().hashCode(), personDAO02.getJdbcConnectionProxyInterface());

            SomeCdiBusiness someCdiBusiness = applicationContext.getBean(SomeCdiBusiness.class);
            LOGGER.info("using CDI instead of Spring annotations: {}, DAO: {}", someCdiBusiness, someCdiBusiness.getPersonCdiDao());
            LOGGER.info("Using the @Value(\"${app.hostname}\") property: {}", someCdiBusiness.getHostname());
        }
    }
}
