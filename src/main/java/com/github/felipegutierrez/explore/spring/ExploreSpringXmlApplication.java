package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.basics.beans.QuickSortAlgorithm;
import com.github.felipegutierrez.explore.spring.basics.dao.PersonXmlDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class ExploreSpringXmlApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExploreSpringXmlApplication.class);

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            PersonXmlDao personXmlDAO01 = applicationContext.getBean(PersonXmlDao.class);
            PersonXmlDao personXmlDAO02 = applicationContext.getBean(PersonXmlDao.class);

            LOGGER.info("DAO Xml 01: {}, {}", personXmlDAO01.hashCode(), personXmlDAO01);
            LOGGER.info("DAO Xml 01 JDBCConnection: {}, {}", personXmlDAO01.getJdbcConnectionXml().hashCode(), personXmlDAO01.getJdbcConnectionXml());
            LOGGER.info("DAO Xml 02: {}, {}", personXmlDAO02.hashCode(), personXmlDAO02);
            LOGGER.info("DAO Xml 02 JDBCConnection: {}, {}", personXmlDAO02.getJdbcConnectionXml().hashCode(), personXmlDAO02.getJdbcConnectionXml());

            QuickSortAlgorithm quickSortAlgorithm = applicationContext.getBean(QuickSortAlgorithm.class);
            int[] result = quickSortAlgorithm.sort(new int[]{12, 4, 3, 70, 20, 0});
            Arrays.stream(result).forEach(i -> System.out.println(i));
        }
    }
}
