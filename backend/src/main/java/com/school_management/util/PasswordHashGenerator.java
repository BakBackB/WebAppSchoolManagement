package com.school_management.util;
import org.mindrot.jbcrypt.BCrypt;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {        
        String plainAdmin = "123admin";
        String plainTeacher = "123teacher";
        String plainStudent = "123student";        
        String hashAdmin = BCrypt.hashpw(plainAdmin, BCrypt.gensalt());
        String hashTeacher = BCrypt.hashpw(plainTeacher, BCrypt.gensalt());
        String hashStudent = BCrypt.hashpw(plainStudent, BCrypt.gensalt());
        System.out.println("Hash Admin: " + hashAdmin);
        System.out.println("Verify: " + BCrypt.checkpw(plainAdmin, hashAdmin));
        System.out.println("Hash Teacher: " + hashTeacher);
        System.out.println("Verify: " + BCrypt.checkpw(plainTeacher, hashTeacher));
        System.out.println("Hash Student: " + hashStudent);
        System.out.println("Verify: " + BCrypt.checkpw(plainStudent, hashStudent));
    }
    
}