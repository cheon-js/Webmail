/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

/**
 *
 * @author 천정석
 */
//프로젝트에 사용되는 데이터 베이스 정보들을 한 곳에 관리하여 수정에 용이하고 불필요한 중복 방지를 위해 만들었음..
public class DBInfo {
    
     public final static String JdbcDriver = "com.mysql.cj.jdbc.Driver";
     public final static String Jdbcurl="jdbc:mysql://localhost:3308/webmail?serverTimezone=Asia/Seoul";
     public final static String user="root";
     public final static String password="wjdtjr0847";
    
}
