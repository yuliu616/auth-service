package com.yu;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@MapperScan("com.yu.modelMapper")
@SpringBootApplication
public class Application {

    @Value("${auth-service.model-mapper-location}")
    private String modelMapperLocation;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(this.dataSource);
        Resource[] mapperFiles = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources(modelMapperLocation);
        logger.info("mybatis mapperFiles=[{}]", String.join(",",
                Arrays.stream(mapperFiles).map(r->r.getFilename())
                    .toArray(String[]::new))
        );
        sessionFactoryBean.setMapperLocations(mapperFiles);
        return sessionFactoryBean.getObject();
    }

}
