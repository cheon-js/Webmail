/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.Pop3Agent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author jongmin
 */
public class ReadMailHandler extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        request.setCharacterEncoding("UTF-8");
        int select = Integer.parseInt((String) request.getParameter("menu"));

        switch (select) {
            case CommandType.DELETE_MAIL_COMMAND:
                try (PrintWriter out = response.getWriter()) {
                    deleteMessage(request);
                    //response.sendRedirect("main_menu.jsp");
                    out.print(getDeleteMessage());
                }
                break;

            case CommandType.DOWNLOAD_COMMAND: // 파일 다운로드 처리
                download(request, response);
                break;

                
            case CommandType.DELETE_SENTMAIL_COMMAND: // 보낸메일 삭제
                try (PrintWriter out = response.getWriter()){
                    if(deleteSentMessage(request))
                    {
                       System.out.println("success!!");
                       response.sendRedirect("sentmail.jsp?status=1");
                    }
                    else
                        response.sendRedirect("sentmail.jsp?status=0");
                }
                break;
             case CommandType.DELETE_MYSENTMAIL_COMMAND: // 보낸메일 삭제
                try (PrintWriter out = response.getWriter()){
                    if(deleteSentMessage(request))
                    {
                       System.out.println("success!!");
                       response.sendRedirect("my_sent_mail.jsp?status=1");
                    }
                    else
                        response.sendRedirect("my_sent_mail.jsp?status=0");
                }
                break;           
            default:
                try (PrintWriter out = response.getWriter()) {
                    out.println("없는 메뉴를 선택하셨습니다. 어떻게 이 곳에 들어오셨나요?");
                }
                break;

        }
    }

    private void download(HttpServletRequest request, HttpServletResponse response) { //throws IOException {
        response.setContentType("application/octet-stream");

        ServletOutputStream sos = null;

        try {
            /* TODO output your page here */
            request.setCharacterEncoding("UTF-8");
            // LJM 041203 - 아래와 같이 해서 한글파일명 제대로 인식되는 것 확인했음.
            String fileName = request.getParameter("filename");
            System.out.println(">>>>>> DOWNLOAD: file name = " + fileName);

            String userid = request.getParameter("userid");
            //String fileName = URLDecoder.decode(request.getParameter("filename"), "utf-8");

            // download할 파일 읽기

            // 윈도우즈 환경 사용시
            String downloadDir = "C:/jsp/download";
            if (System.getProperty("os.name").equals("Linux")) {
                downloadDir = request.getServletContext().getRealPath("/WEB-INF") 
                        + File.separator + "download";
                File f = new File(downloadDir);
                if (!f.exists()) {
                    f.mkdir();
                }
            }

            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(fileName, "UTF-8") + ";");

            File f = new File(downloadDir + File.separator + userid + File.separator + fileName);
            byte[] b = new byte[(int) f.length()];
            // try-with-resource 문은 fis를 명시적으로 close해 주지 않아도 됨.
            try (FileInputStream fis = new FileInputStream(f)) {
                fis.read(b);
            };

            // 다운로드
            sos = response.getOutputStream();
            sos.write(b);
            sos.flush();
            sos.close();
        } catch (Exception ex) {
            System.out.println("====== DOWNLOAD exception : " + ex);
        }
    }

    private boolean deleteMessage(HttpServletRequest request) {
        int msgid = Integer.parseInt((String) request.getParameter("msgid"));

        HttpSession httpSession = request.getSession();
        String host = (String) httpSession.getAttribute("host");
        String userid = (String) httpSession.getAttribute("userid");
        String password = (String) httpSession.getAttribute("password");

        Pop3Agent pop3 = new Pop3Agent(host, userid, password);
        boolean status = pop3.deleteMessage(msgid, true);
        return status;
    }
    private String getDeleteMessage() {
        String alertMessage = null;
        alertMessage = "해당 메일이 삭제되었습니다.";

        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>메일 전송 결과</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"main_menu.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }
  private boolean deleteSentMessage(HttpServletRequest request) throws NamingException, SQLException, ClassNotFoundException, Exception {
        int msgid = Integer.parseInt((String) request.getParameter("msgid"));
        boolean status = false;
        Connection conn = null;
        Statement stmt = null;
        
        HttpSession httpSession = request.getSession();
        String host = (String) httpSession.getAttribute("host");
        String userid = (String) httpSession.getAttribute("userid");
        String password = (String) httpSession.getAttribute("password");
        
       

         try{
            Class.forName(DBInfo.JdbcDriver);
            //Connection 객체 생성                
            conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);
            //Statement 객체 생성
            stmt = conn.createStatement();

            String sql = "delete from sent_mail_inbox where num = " + msgid;
            stmt.execute(sql);
            
            if(stmt.getUpdateCount() == 1){
            status = true;
            System.out.println("delete success");
        }
            
         }catch(Exception ex){
             System.out.println("DB Connect Fail");
             System.out.println(ex.getMessage());
         }
         finally{
             stmt.close();
             conn.close();
         }
        
        
        return status;
    }
     //보낸 메일 삭제 결과 출력
     private String getDeletSentMessagePopUp(String alertMessage){
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>메일 삭제 결과</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"sentmail.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
     }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         try {
            processRequest(request, response);
        } catch (NamingException ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         try {
            processRequest(request, response);
        } catch (NamingException ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReadMailHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
