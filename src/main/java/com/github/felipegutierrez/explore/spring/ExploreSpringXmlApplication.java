package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.basics.dao.PersonXmlDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
@ComponentScan({"com.github.felipegutierrez.explore.spring.basics.beans",
        "com.github.felipegutierrez.explore.spring.basics.dao",
        "com.github.felipegutierrez.explore.spring.basics.business"})
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
        }
    }
}
