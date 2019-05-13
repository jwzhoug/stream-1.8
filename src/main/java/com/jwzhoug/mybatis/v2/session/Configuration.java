package com.jwzhoug.mybatis.v2.session;

import com.jwzhoug.mybatis.v2.annotation.Entity;
import com.jwzhoug.mybatis.v2.annotation.Select;
import com.jwzhoug.mybatis.v2.executor.CachingExecutor;
import com.jwzhoug.mybatis.v2.executor.Executor;
import com.jwzhoug.mybatis.v2.executor.SimpleExecutor;
import com.jwzhoug.mybatis.v2.mapper.MapperRegistry;
import com.jwzhoug.mybatis.v2.plugin.Interceptor;
import com.jwzhoug.mybatis.v2.plugin.InterceptorChain;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Configuration {

    // SQL 映射关系
    public static final ResourceBundle sqlMappings;
    // 全局配置
    public static final ResourceBundle properties;
    public static final MapperRegistry MAPPER_REGISTRY = new MapperRegistry();
    public static final Map<String, String> mappedStatements = new HashMap<String, String>();

    private InterceptorChain interceptorChain = new InterceptorChain();
    private List<Class<?>> mapperList = new ArrayList<Class<?>>();
    private List<String> classPaths = new ArrayList<String>();

    // 读取 properties 配置
    static {
        sqlMappings = ResourceBundle.getBundle("sql");
        properties = ResourceBundle.getBundle("mybatis");
    }

    public Configuration() {
        // Note: 在properties和注解中重复配置SQL会覆盖
        // 1. 解析sql.properties
        for (String key : sqlMappings.keySet()) {
            Class mapper = null;
            String statement = null;
            String pojoStr = null;
            Class pojo = null;
            statement = sqlMappings.getString(key).split("--")[0];
            pojoStr = sqlMappings.getString(key).split("--")[1];
            try {
                mapper = Class.forName(key.substring(0, key.lastIndexOf(".")));
                pojo = Class.forName(pojoStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            MAPPER_REGISTRY.addMapper(mapper, pojo);
            mappedStatements.put(key, statement);
        }

        // 2. 解析Mapper接口配置，扫描注册
        String mapperPath = properties.getString("mapper.path");
        scanPackage(mapperPath);
        for (Class<?> mapper : mapperList) {
            parsingClass(mapper);
        }

        // 3.解析插件，可配置多个插件
        String pluginPathValue = properties.getString("plugin.path");
        String[] pluginPaths = pluginPathValue.split(",");
        if (pluginPaths != null) {
            for (String pluginPath : pluginPaths) {
                Interceptor interceptor = null;
                try {
                    interceptor = (Interceptor) Class.forName(pluginPath).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                interceptorChain.addInterceptor(interceptor);
            }
        }
    }

    /**
     * 根据statement判断是否存在映射SQL
     *
     * @param statementName
     * @return
     */
    public boolean hasStatement(String statementName) {
        return mappedStatements.containsKey(statementName);
    }

    /**
     * 根据statementName获取SQL
     *
     * @param statementName
     * @return
     */
    public String getMappedStatement(String statementName) {
        return mappedStatements.get(statementName);
    }

    public <T> T getMapper(Class<T> clazz, DefaultSqlSession sqlSession) {
        return MAPPER_REGISTRY.getMapper(clazz, sqlSession);
    }

    /**
     * 创建执行器,当开启缓存时使用缓存装饰
     * 当配置插件时，使用插件代理
     *
     * @return
     */
    public Executor newExecutor() {
        Executor executor = null;
        if (properties.getString("cache.enabled").equals("true")) {
            executor = new CachingExecutor(new SimpleExecutor());
        } else {
            return new SimpleExecutor();
        }

        // 目前只拦截Executor，所有插件都对Executor进行代理，并没有对拦截类和方法签名进行判断
        if (interceptorChain.hasPlugin()) {
            return (Executor) interceptorChain.pluginAll(executor);
        }
        return executor;
    }

    /**
     * 解析Mapper接口上的注解（SQL 语句）
     *
     * @param mapper
     */
    private void parsingClass(Class<?> mapper) {
        // 1.解析类上的注解
        if (mapper.isAnnotationPresent(Entity.class)) {
            for (Annotation annotation : mapper.getAnnotations()) {
                // 判断注解类别
                if (annotation.annotationType().equals(Entity.class)) {
                    MAPPER_REGISTRY.addMapper(mapper, ((Entity) annotation).value());
                }
            }
        }

        // 2.解析方法上的注解
        Method[] methods = mapper.getMethods();
        for (Method method : methods) {
            // 解析@Select注解的SQL语句
            if (method.isAnnotationPresent(Select.class)) {
                for (Annotation annotation : method.getDeclaredAnnotations()) {
                    if (annotation.annotationType().equals(Select.class)) {
                        // 注册接口类型+方法名和SQL语句的映射关系
                        String statement = method.getDeclaringClass().getName() + "." + method.getName();
                        mappedStatements.put(statement, ((Select) annotation).value());
                    }
                }
            }
        }
    }

    private void scanPackage(String mapperPath) {
        String mainPath = this.getClass().getResource("/").getPath();
        mapperPath = mapperPath.replace(".", File.separator);
        String itemPath = mainPath + mapperPath;
        doPath(new File(itemPath));
        for (String classPath : classPaths) {
            // className 获取 有问题
            classPath = classPath
                    .replace(classPath.replace("/", "\\").replaceFirst("\\\\", ""), "")
                    .replace("\\", ".")
                    .replace(".class", "");

            Class<?> clazz = null;
            try {
                clazz = Class.forName(classPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 判断是否是 接口
            if (clazz.isInterface()) {
                mapperList.add(clazz);
            }
        }

    }

    private void doPath(File file) {
        // 文件夹，遍历
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f1 : files) {
                doPath(f1);
            }
        } else {
            // .class 文件 添加
            if (file.getName().endsWith(".class")) {
                classPaths.add(file.getPath());
            }
        }
    }


}
