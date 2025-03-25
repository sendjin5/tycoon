package com.bs;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@MapperScan("com.bs.spring")
public class MyBatisConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("oracle.jdbc.OracleDriver");
//        ds.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
//        ds.setUsername("gigadev1");
//        ds.setPassword("rlrkepqm1");
        ds.setDriverClassName("org.mariadb.jdbc.Driver");
        ds.setUrl("jdbc:mariadb://localhost:3306/tycoon");
        ds.setUsername("root");
        ds.setPassword("1234");
        return ds;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // 여기에 VO 클래스들이 있는 패키지를 등록
        sessionFactory.setTypeAliasesPackage("com.bs.spring");

        Resource[] res = new PathMatchingResourcePatternResolver()
                .getResources("classpath:/mappers/*-mapper.xml");
        sessionFactory.setMapperLocations(res);

        return sessionFactory.getObject();
    }


    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
