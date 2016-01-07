
1.pom引入：
```java 
<dependency>
            <groupId>com.xiaoju.kefu</groupId>
            <artifactId>mybatis-support</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```
2.web.xml 修改为：
```java 
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="3.0"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         metadata-complete="false">
```
3.新建db_config.properties
```java
masterUrl=jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
#有从库写入下面的salveUrl，查询请求会走salve进行，没有可以去掉salveUrl的配置
salveUrl=jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
username=root
password=
```
4.新建mybatis-config.xml配置
```java 
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="cacheEnabled" value="false"/>
        <setting name="lazyLoadingEnabled" value="true"/>
        <setting name="multipleResultSetsEnabled" value="true"/>
        <setting name="useColumnLabel" value="true"/>
        <setting name="useGeneratedKeys" value="false"/>
        <setting name="autoMappingBehavior" value="PARTIAL"/>
        <setting name="defaultExecutorType" value="SIMPLE"/>
        <setting name="defaultStatementTimeout" value="25"/>
        <setting name="safeRowBoundsEnabled" value="false"/>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="localCacheScope" value="STATEMENT"/>
        <setting name="jdbcTypeForNull" value="OTHER"/>
        <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
    </settings>
    <typeHandlers>
        <typeHandler javaType="string" handler="com.github.mybatis.handler.StringTypeUtf8mb4Handler"/>
    </typeHandlers>
    <plugins>
        <!-- 主从分离插件 -->
        <plugin interceptor="com.github.mybatis.interceptor.MasterSlaveInterceptor"/>
        <!-- 分页插件 -->
        <plugin interceptor="com.github.mybatis.interceptor.PaginationAutoMapInterceptor"/>
    </plugins>
</configuration>
```
5.新建spring-db.xml
```java 
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="proxyTransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="proxyDataSource"/>
    </bean>
    <bean id="proxyDataSource" class="com.github.mybatis.spring.DynamicDataSource">
    	<!--修改为你自己定义的数据库配置文件 -->
        <property name="configName" value="db_config.properties"/>
    </bean>
    <!-- define the SqlSessionFactory -->
    <bean id="proxySqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="proxyDataSource"/>
        <!--修改为你自己定义的pojo类路径 -->
        <property name="typeAliasesPackage" value="com.xiaoju.kefu.vectorsigma.entity"/>
        <!--修改为你自己定义的mybatis-config的配置文件名称 -->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <!--<property name="mapperLocations" value="classpath:mybatis/mappers/*.xml"/>-->
    </bean>
    <!-- scan for mapper and let them be autowired -->
    <bean class="com.github.mybatis.spring.ScannerConfigurer">
    	<!--修改为你自己定义的dao的包名称 -->
        <property name="basePackage" value="com.xiaoju.kefu.vectorsigma.mapper"/>
        <property name="sqlSessionFactoryBeanName" value="proxySqlSessionFactory"/>
    </bean>
</beans>
```
6.编码要点：

 6.1 pojo 需要继承IdEntity，例如：
 ```java 
@Table(name="vector_sigma_user")
public class User extends IdEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 4058901424378128073L;
    @Column(name="name")
    private String name;
    // getter and setter
    }
```
6.2 dao 需要extends ICrudMapper<T> 例如：
```java 

    public interface UserMapper extends ICrudMapper<User> {
    
}
```
