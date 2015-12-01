# mybatis-brick
提供spring环境下对mybatis的基础支撑,本工程基于Servlet 3.0规范提供的web fragment特性做的模块化开发
具体详细情况参考：http://www.xdemo.org/web-fragment-module-springmvc/
使用方法如下：
前提条件：工程需要使用spring进行管理,servlet需要3.0以上,
1.下载源码到本地，mvn install 安装lib包到本地maven仓库
2.引入pom文件
<dependency>
			<groupId>com.github.wmq534</groupId>
			<artifactId>mybatis-brick</artifactId>
			<version>1.0.0</version>
</dependency>
3.在applicationContext.xml中配置
<context:annotation-config/>
<bean id="proxyTransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="proxyDataSource"/>
    </bean>
    <bean id="proxyDataSource" class="com.github.mybatis.spring.DynamicDataSource">
        <property name="configName" value="db-config-proxy"/>
        <!--db-config-proxy 是文件名称 -->
    </bean>
    <!-- define the SqlSessionFactory -->
    <bean id="proxySqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="proxyDataSource"/>
        <!--com.test.mvc.entity 需要修改为自己工程具体bo对象路径 -->
        <property name="typeAliasesPackage" value="com.test.mvc.entity"/>
        <!--classpath:mybatis-config.xml 需要修改为自己工程具体mybatis配置文件路径 -->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>
    <!-- scan for mapper and let them be autowired -->
    <bean class="com.github.mybatis.spring.ScannerConfigurer">
      <!--com.test.mvc.mapper 需要修改为自己工程具体mapper类实例文件路径 -->
        <property name="basePackage" value="com.test.mvc.mapper"/>
        <property name="sqlSessionFactoryBeanName" value="proxySqlSessionFactory"/>
    </bean>
4.src/main/resources中新建db-config-proxy文件，内容如下：
#主库地址链接
masterUrl=jdbc:mysql://127.0.0.1:3306/http_proxy?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
#从库地址链接，如果没有可以删除该配置项，系统会默认使用masterUrl进行数据库链接
salveUrl=jdbc:mysql://127.0.0.1:3306/http_proxy?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
username=work
password=123456
5.在src/main/resources中新建mybatis-config.xml
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
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="org.h2.Driver"/>
                <property name="url" value="jdbc:h2:mem:blog;DB_CLOSE_DELAY=-1"/>
                <property name="username" value="sa"/>
                <property name="password" value=""/>
            </dataSource>
        </environment>
    </environments>
</configuration>
6.完成配置，具体细节请查看galaxy-protals项目中的配置
