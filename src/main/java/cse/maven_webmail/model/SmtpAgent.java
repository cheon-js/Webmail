/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import com.sun.mail.smtp.SMTPMessage;
import cse.maven_webmail.control.DBInfo;
import java.io.File;
import java.sql.SQLException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 *
 * @author jongmin
 */
public class SmtpAgent {

    protected String host = null;
    protected String userid = null;
    protected String to = null;
    protected String cc = null;
    protected String subj = null;
    protected String body = null;
    protected String file1 = null;


    public SmtpAgent(String host, String userid) {
        this.host = host;
        this.userid = userid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    // LJM 100418 -  ?????? ???????????? ???????????? ????????? ????????? ??????????????? ??????????????? ???. - test only
    // LJM 100419 - ?????? ??? ???????????? SMTP ????????? setFrom() ?????? ?????? ?????????.
    //              ?????? ?????? ?????? ????????? ??????????????? ????????? ?????????.
    public boolean sendMessage() throws SQLException{
        boolean status = false;

        // 1. property ??????
        Properties props = System.getProperties();
        props.put("mail.smtp.host", this.host);
        System.out.println("SMTP host : " + props.get("mail.smtp.host"));

        // 2. session ????????????
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        try {
            SMTPMessage msg = new SMTPMessage(session);

            // msg.setFrom(new InternetAddress(this.userid + "@" + this.host));
            msg.setFrom(new InternetAddress(this.userid));  // 200102 LJM - ????????? ???????????? ??????
            //msg.setFrom(new InternetAddress("jongmin@deu.ac.kr"));


            // setRecipient() can be called repeatedly if ';' or ',' exists
            if (this.to.indexOf(';') != -1) {
                this.to = this.to.replaceAll(";", ",");
            }
            msg.setRecipients(Message.RecipientType.TO, this.to);  // 200102 LJM - ??????

            if (this.cc.length() > 1) {
                if (this.cc.indexOf(';') != -1) {
                    this.cc = this.cc.replaceAll(";", ",");
                }
                msg.setRecipients(Message.RecipientType.CC, this.cc);
            }

            // msg.setSubject(MimeUtility.encodeText(this.subj, "euc-kr", "B"));
            msg.setSubject(this.subj);
            msg.setHeader("User-Agent", "LJM-WM/0.1");

            // body
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(this.body);

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);

            // ?????? ?????? ??????
            if (this.file1 != null) {
                MimeBodyPart a1 = new MimeBodyPart();
                DataSource src = new FileDataSource(this.file1);
                a1.setDataHandler(new DataHandler(src));
                int index = this.file1.lastIndexOf('/');
                String fileName = this.file1.substring(index + 1);
                // "B": base64, "Q": quoted-printable
                a1.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));
                mp.addBodyPart(a1);
            }
            msg.setContent(mp);

            // ?????? ??????
            Transport.send(msg);

            // ?????? ?????? ????????????????????? ????????? ?????????
            // ?????? ?????? ?????????
            if (this.file1 != null) {
                File f = new File(this.file1);
                if (!f.delete()) {
                    System.err.println(this.file1 + " ?????? ????????? ????????? ??? ???.");
                }
            }
            //?????? ?????? ???????????? ??????
            System.out.println("save the sentmail start");
            boolean sentinsertsuccess = savesentmail();
            System.out.println("Sent mail insert success = " + sentinsertsuccess);
            
            status = true;
        } catch (Exception ex) {
            System.out.println("sendMessage() error: " + ex);
        } finally {
            return status;
        }
    }  // sendMessage()
    boolean savesentmail() throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        Connection conn = null;


        try {
            String userId = userid;
            String toAddress = to;
            String ccAddress = cc;
            String subject = subj;
            String text = body;
            String fname = file1;
/*
            String filename = "";
            File attachedfile = null;

            String filename = fname.substring(fname.lastIndexOf("/")+1);
            File attachedfile = file;
       
            int fileLength = (int) attachedfile.length();
            InputStream ins = new FileInputStream(attachedfile);
       */     
            //DBCP?????????????????? ?????? ??????
            //?????????????????? ????????? context.xml??? ??????
            //Context ??? Datasource ??????
            System.out.println("try to connect the database to save sent mail...");
            Class.forName(DBInfo.JdbcDriver);
            //Connection ?????? ??????    
            conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);
            //Statement ?????? ??????
            stmt = conn.createStatement();

            System.out.println(userId);


            //SQL ?????? ??????
           /*String sql = "INSERT INTO sent_mail_inbox(sender, recipients, CarbonCopy, message_name, message_body, saveDate)" 
                   + "VALUES(HEX(AES_ENCRYPT('"+ userId +"', 'sender')),"
                   + "HEX(AES_ENCRYPT('"+ toAddress +"', 'recipients')),"
                   + "HEX(AES_ENCRYPT('"+ ccAddress +"', 'CarbonCorpy')),"
                   + "HEX(AES_ENCRYPT('"+ subject +"', 'message_name')),"
                   + "HEX(AES_ENCRYPT('"+ text +"', 'message_body')),"
                   + " now()); ";*/
             String sql = "INSERT INTO sent_mail_inbox(sender, recipients, CarbonCopy, message_name, message_body, saveDate) "
                     + "VALUES('"+ userId +"', '"+ toAddress +"', '"+ ccAddress +"',"
                     + "HEX(AES_ENCRYPT('"+ subject +"', 'message_name')),"
                     + "HEX(AES_ENCRYPT('"+ text +"', 'message_body')),"
                     + "now());";

            //String sql = "INSERT INTO sent_mail_inbox (sender, recipients, CarbonCopy, message_name, message_body, file_body, saveDate) VALUES(HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)),HEX(AES_ENCRYPT(?, ?)), ?, now());";
            // String sql = "INSERT INTO attachedfiletbl (filename, filebody) VALUES(?, ?);";
            stmt.executeUpdate(sql);
/*
            if (f == null) {
                pstmt.setNull(1, Types.VARCHAR);
                // pstmt.setBinaryStream(2, ins, fileLength);
                pstmt.setNull(2, Types.BLOB);
            } else {
                attachedfile = f;
                int fileLength = (int) attachedfile.length();
                InputStream ins = new FileInputStream(attachedfile);

                filename = fname.substring(fname.lastIndexOf("/") + 1);

                System.out.println(filename);

                pstmt.setString(1, filename);
                pstmt.setBinaryStream(2, ins, fileLength);
            }
*/
            System.out.println("sql = " + sql);

            

            System.out.println("database connect success");
            return true;
        } catch (SQLException e) {
            System.out.println("database connect failed");
            e.printStackTrace();
            //log.info(ex.getMessage());

            return false;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

    } 
}
