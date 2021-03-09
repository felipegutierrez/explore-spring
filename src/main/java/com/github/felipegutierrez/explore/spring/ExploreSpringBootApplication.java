package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.basics.beans.QuickSortAlgorithm;
import com.github.felipegutierrez.explore.spring.basics.business.SomeCdiBusiness;
import com.github.felipegutierrez.explore.spring.basics.dao.PersonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan({"com.github.felipegutierrez.explore.spring.basics.beans",
        "com.github.felipegutierrez.explore.spring.basics.dao",
        "com.github.felipegutierrez.explore.spring.basics.business"})
public class ExploreSpringBootApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExploreSpringBootApplication.class);

    public static void main(String[] args) {
        try (ConfigurableApplicationContext applicationContext =
                     SpringApplication.run(ExploreSpringBootApplication.class, args)) {
            QuickSortAlgorithm quickSortAlgorithm = applicationContext.getBean(QuickSortAlgorithm.class);
            int[] result = quickSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
            Arrays.stream(result).forEach(i -> System.out.println(i));

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
        }
    }
}