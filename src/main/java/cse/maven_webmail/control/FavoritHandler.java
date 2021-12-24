/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import cse.maven_webmail.model.FormParser;
import cse.maven_webmail.model.SmtpAgent;
import cse.maven_webmail.model.MyForm;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author songe
 */
public class FavoritHandler extends HttpServlet{
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
                
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            request.setCharacterEncoding("UTF-8");
            int select = Integer.parseInt((String) request.getParameter("menu"));

            switch (select) {
                case CommandType.SAVE_USER_COMMAND:
                    boolean status = saveUser(request);
                    out.print(getFavoritTransportPopUp(status));
                    break;
                    
                case  CommandType.DELETE_FAVORITE_COMMAND:
                    boolean delstatus = delUser(request);
                    out.print(getDelFavoritTransportPopUp(delstatus));
                    break;
                    
                case CommandType.UPDATE_FAVORITE_COMMAND:
                    boolean upstatus = upUser(request);
                    out.print(getUpFavoritTransportPopUp(upstatus));
                    break;

                default:
                    out = response.getWriter();
                    out.println("없는 메뉴를 선택하셨습니다. 어떻게 이 곳에 들어오셨나요?");
                    break;
            }
        } catch (Exception ex) {
            out.println(ex.toString());
        } finally {
            out.close();
        }
    }
        private boolean saveUser(HttpServletRequest request) throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        boolean status = false;
        boolean overstate = true;
        MyForm parser = new MyForm(request);
        parser.parse();

        HttpSession session = (HttpSession) request.getSession();
        String userid = (String) session.getAttribute("userid");

            try {
                String adname = parser.getAdname();
                String admemo = parser.getAdmemo();
                String group = parser.getGroup();

                Class.forName(DBInfo.JdbcDriver);
                conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                String sql = "INSERT INTO webmail.favorite (username, F_username, memo, grouptable) VALUES('" + userid + "', '" + adname + "', '" + admemo + "', '"+ group + "')";
                String check = "SELECT F_username FROM webmail.favorite WHERE username='" + userid +"'";//내 즐겨찾기 목록에서 중복 체크
                String checkid = "SELECT username FROM webmail.users WHERE NOT username IN ('" + userid + "')";//자신 빼고 나머지 사용자 아이디 체크
                
                pstmt = conn.prepareStatement(check);
                rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String dbfuser = rs.getString("F_username");
                if (adname.equals(dbfuser)) {
                    overstate = false;
                    }
                }
                
                pstmt = conn.prepareStatement(checkid);
                rs = pstmt.executeQuery();
                
                if(overstate==true){

                    while (rs.next()) {
                        String chid = rs.getString("username");
                        if (adname.equals(chid)) {
                            pstmt = conn.prepareStatement(sql);
                            pstmt.executeUpdate();
                            status = true;
                        }
                }
                }
                pstmt.close();
                conn.close();
                
            } catch (SQLException e){
        System.out.println("DB연결 실패"); System.out.print("사유 : " + e.getMessage());
        }
        return status;
    }
    private boolean delUser(HttpServletRequest request)throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
            boolean delstate = false;
        MyForm parser = new MyForm(request);
        parser.delparse();

        HttpSession session = (HttpSession) request.getSession();
        String userid = (String) session.getAttribute("userid");

            try {
            String delname = parser.getDelname();                

                conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                String sql = "DELETE FROM webmail.favorite WHERE username = '"+ userid + "' AND F_username = '" + delname + "'";
                String check = "SELECT username, F_username FROM webmail.favorite WHERE username = '"+ userid + "' AND F_username = '" + delname + "'";
                
                pstmt = conn.prepareStatement(check);
                rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String dbfuser = rs.getString("F_username");
                    String dbuser = rs.getString("username");
                    if(dbuser.equals(userid) && dbfuser.equals(delname)){
                        pstmt = conn.prepareStatement(sql);
                        pstmt.executeUpdate();
                        delstate = true;
                    }
                }

                pstmt.close();
                conn.close();
                
            } catch (SQLException e){
        System.out.println("DB연결 실패"); System.out.print("사유 : " + e.getMessage());
        }
            return delstate;
        }
     private boolean upUser(HttpServletRequest request)throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
            boolean upstate = false;
        MyForm parser = new MyForm(request);
        parser.upparse();
        
        ResultSet rs = null;

        HttpSession session = (HttpSession) request.getSession();
        String userid = (String) session.getAttribute("userid");

            try {
                Connection conn = null;
                PreparedStatement pstmt = null;
                

                String upname = parser.getUpname();   
                String upmemo = parser.getUpmemo(); 
                String group = parser.getGroup();
                
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                String sql = "UPDATE webmail.favorite SET grouptable = '"+group+"', memo = '"+upmemo+"' WHERE username = '"+ userid +"' AND F_username = '"+upname+"'";
                String check = "SELECT username, F_username FROM webmail.favorite WHERE username = '"+ userid + "' AND F_username = '" + upname + "'";
                
                pstmt = conn.prepareStatement(check);
                rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String dbfuser = rs.getString("F_username");
                    String dbuser = rs.getString("username");
                    if(dbuser.equals(userid) && dbfuser.equals(upname)){
                        pstmt = conn.prepareStatement(sql);
                        pstmt.executeUpdate();
                        upstate = true;
                    }
                }
                pstmt.close();
                conn.close();
                
            } catch (SQLException e){
        System.out.println("DB연결 실패"); System.out.print("사유 : " + e.getMessage());
        }
            return upstate;
        }
    
        private String getFavoritTransportPopUp(boolean success) {
        String alertMessage = null;
        if (success) {
            alertMessage = "즐겨찾기 추가가 성공했습니다.";
        } else {
            alertMessage = "중복된 이름이 있거나 없는 사용자입니다.";
        }

        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>즐겨찾기 저장 결과</title>");
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
        private String getDelFavoritTransportPopUp(boolean delsuccess){
            String alertMessage = null;
        if (delsuccess) {
            alertMessage = "즐겨찾기 삭제를 성공했습니다.";
        } else {
            alertMessage = "주소록에 없는 사용자입니다.";
        }
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>즐겨찾기 삭제 결과</title>");
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
        private String getUpFavoritTransportPopUp(boolean upsuccess){
            String alertMessage = null;
        if (upsuccess) {
            alertMessage = "업데이트를 성공했습니다.";
        } else {
            alertMessage = "주소록에 없는 사용자입니다.";
        }

        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>업데이트 결과</title>");
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
