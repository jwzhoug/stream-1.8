package com.jwzhoug.mybatis.v1;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {

        File file = new File("F:\\learning\\E-Github\\stream-1.8\\target\\classes\\com\\jwzhoug\\mybatis\\v1\\Test.class");
        if (file.exists()){
            System.out.println(Test.class.getResource("/").getPath().replace("/","\\").replaceFirst("\\\\",""));
            System.out.println(Test.class.getResource("/").getPath());
            String classPath = file.getPath();
            // F:learning\E-Github\stream-1.8\src\main\java\com\jwzhoug\mybatis\v2\mapper\MapperRegistry.java
            classPath = classPath.replace(Test.class.getResource("/").getPath().replace("/","\\").replaceFirst("\\\\",""),"")
                    .replace("\\",".")
                    .replace(".class","")
            ;
//            classPath = classPath.replace(classPath.replace("/","\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");

            System.out.println(classPath);
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getCanonicalPath());
        }
    }
}
