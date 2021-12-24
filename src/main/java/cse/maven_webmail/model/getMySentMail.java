/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import cse.maven_webmail.control.CommandType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.NamingException;
import cse.maven_webmail.control.DBInfo;

/**
 *
 * @author 천정석
 */
public class getMySentMail {
    
    public String getMySentMessageList(String userid) throws SQLException, NamingException{
        
        Connection conn = null;
        Statement stmt = null;
        Statement newstmt = null;
        
        String userID = userid;
        ResultSet decrypt_rs = null;
        
        StringBuilder buffer = new StringBuilder();
        int num = 0;
        String fromAddress = null; //보낸사람
        String toAddress = null;//받은 사람! 수신자!
        String ccAddress = null;//참조자!
        String subject = null;
        String body = null;
        String saveDate = null;
        String id = userid;
        
        ResultSet count = null;



        try {
            Class.forName(DBInfo.JdbcDriver);
            //Connection 객체 생성                
            conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);
            //Statement 객체 생성
            stmt = conn.createStatement();
            newstmt = conn.createStatement();
            int cnum = 0;


            if (conn == null) {
                throw new Exception("DB Connect Fail");
            }
            //stmt = conn.createStatement();
            //복호화
            //ResultSet rs = stmt.executeQuery("SELECT * FROM test WHERE t_user");

            String sql = "SELECT num, "
                    + "sender, "
                    + "recipients, "
                    + "CAST(AES_DECRYPT(UNHEX(message_name), 'message_name') AS CHAR), "
                    + "CarbonCopy, "
                    + "saveDate FROM sent_mail_inbox WHERE sender ='" + userID + "' AND "
                    + "recipients='" + userID + "' order by saveDate desc;";
            
            String countsql = "select count(num) from sent_mail_inbox where sender ='" + userID + "'";
            decrypt_rs = stmt.executeQuery(sql);
            count = newstmt.executeQuery(countsql);
            
            while(count.next()){
                cnum = count.getInt("count(num)");
            }
            

            buffer.append("<table>");  // table start
            buffer.append("<tr> "
                    + " <th> No. </td> "
                    + " <th> 받은 사람 </td>"
                    + " <th> 참조자 </td>"
                    + " <th> 제목 </td>"
                    + " <th> 보낸 날짜 </td>"
                    +" <th> 삭제 </td>"
                    + " </tr>");
            

            while(decrypt_rs.next()){
                num = decrypt_rs.getInt("num");
                
                fromAddress = decrypt_rs.getString("sender");
                toAddress = decrypt_rs.getString("recipients");
                ccAddress = decrypt_rs.getString("CarbonCopy");
                subject = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(message_name), 'message_name') AS CHAR)");
                saveDate = decrypt_rs.getString("saveDate");
                
                
                buffer.append("<tr> "
                        + " <td id=no>" + cnum + " </td> "
                        + " <td id=sender>" + toAddress + "</td>"
                        + " <td id=cc>" + ccAddress + "</td>"
                        + " <td id=subject> "
                        + " <a href=show_sentmessage.jsp?msgid=" + num + " title=\"메일 보기\"> "
                        + subject + "</a> </td>"
                        + " <td id=date>" + saveDate + "</td>"
                        + " <td id=delete>"
                        + "<a href=ReadMail.do?menu="
                        + CommandType.DELETE_MYSENTMAIL_COMMAND
                        + "&msgid=" + num + "> 삭제 </a>" + "</td>"
                        + " </tr>");
                cnum--;
            }
            
            

            buffer.append("</table>");
            //decrypt_rs.close();
            
            if(toAddress != userid && fromAddress != userid){
                 buffer.append("<h1>      내게 쓴 메일이 없습니다.</h1>");
            }
            
            return buffer.toString();
        }
        catch(Exception ex){
            System.out.println("loading failed");
            System.out.println(ex.getMessage());
            
            return "fail";
        }
        finally{
            decrypt_rs.close();
            count.close();
            newstmt.close();
            stmt.close();
            conn.close();
            
         }        
    }
}

