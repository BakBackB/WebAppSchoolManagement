package com.school_management;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class TomcatRunner {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        
        // 1. Initialize the connector
        tomcat.getConnector(); 
        
        String contextPath = "";
        // 2. Point exclusively to the fully assembled war directory
        String docBase = new File("target/school_management").getAbsolutePath();
        
        // 3. Mount the webapp context
        Context ctx = tomcat.addWebapp(contextPath, docBase);
        ctx.setPath("");
        
        System.out.println("Starting Tomcat 10 on http://localhost:8080/");
        tomcat.start();
        tomcat.getServer().await();
    }
}