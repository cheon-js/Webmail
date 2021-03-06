/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.UserAdminAgent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import cse.maven_webmail.control.DBInfo;

/**
 *
 * @author jongmin
 */
public class SignUpHandler extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            request.setCharacterEncoding("UTF-8");
            int select = Integer.parseInt((String) request.getParameter("menu"));

            switch (select) {
                case CommandType.ADD_USER_COMMAND2:
                    addUser(request, response, out);
                    break;
                    
                case CommandType.ADD_ADMIN_USER_COMMAND:
                    AdminaddUser(request, response, out);
                    break;

                case CommandType.DELETE_USER_COMMAND2:
                    deleteUser(request, response, out);
                    break;

                case CommandType.CHECK_USER_COMMAND:
                    IDCheck(request, response, out);
                    break;
                    
                case CommandType.CHANGE_PASSWORD_COMMAND:
                    changePassword(request, response, out);
                    break;

                default:
                    out.println("?????? ????????? ?????????????????????. ????????? ??? ?????? ???????????????????");
                    break;
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String server = "127.0.0.1";
        int port = 4555;
        try {
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."));
            String userid = request.getParameter("id");  // for test
            String password = request.getParameter("password");// for test
            String password2 = request.getParameter("password2");// for test
            String name = request.getParameter("name");// for test
            String email1 = request.getParameter("email1");// for test
            String email2 = request.getParameter("email2");// for test
            String email = email1 + "@" + email2;
            String phone = request.getParameter("phone");// for test



            // if (addUser successful)  ????????? ?????? ?????? ?????????
            // else ????????? ?????? ?????? ?????????
            if (!password.equals(password2)) {
                out.println(getSamePasswordFailurePopUp(1));
            } else if(userid.isEmpty()||password.isEmpty()||name.isEmpty()||email.isEmpty()||phone.isEmpty()){
                out.println(getUserRegistrationFailurePopUp());
            }else {
                if (agent.addUser(userid, password)) {
                    out.println(getUserRegistrationSuccessPopUp(1));
                    // 1. JDBC ???????????? ??????
                    Class.forName(DBInfo.JdbcDriver);

                    // 2. Connection ?????? ??????
                    Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                    // 3. PreparedStatement ?????? ?????? sql??????????????? ?????? ??????. ????????? ????????? ????????? ??????x
                    String sql = "INSERT INTO users(username,email,name,phone) VALUES(?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    // 4. SQL ??? ?????? 
                    request.setCharacterEncoding("UTF-8");  //?????? ??????
                    if (!(userid == null) && !email.equals("")) {
                        pstmt.setString(1, userid);
                        pstmt.setString(2, email);
                        pstmt.setString(3, name);
                        pstmt.setString(4, phone);

                        out.println("sql = " + sql + "<br>");
                        // 5. ?????? : PreparedStatement.executeUpdate()???
                        // INSERT, update ?????? DELETE??? ???????????????.
                        pstmt.executeUpdate();
                    }
                    // 6. ?????? ??????
                    pstmt.close();
                    conn.close();

                } else {
                    out.println(getUserRegistrationFailurePopUp());
                }
            }
            out.flush();
        } catch (Exception ex) {
            out.println(ex+"????????? ????????? ??????????????????.");
        }
    }
        private void AdminaddUser(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String server = "127.0.0.1";
        int port = 4555;
        try {
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."));
            String userid = request.getParameter("id");  // for test
            String password = request.getParameter("password");// for test
            String password2 = request.getParameter("password2");// for test
            String name = request.getParameter("name");// for test
            String email1 = request.getParameter("email1");// for test
            String email2 = request.getParameter("email2");// for test
            String email = email1 + "@" + email2;
            String phone = request.getParameter("phone");// for test



            // if (addUser successful)  ????????? ?????? ?????? ?????????
            // else ????????? ?????? ?????? ?????????
            if (!password.equals(password2)) {
                out.println(getSamePasswordFailurePopUp(1));
            } else if(userid.isEmpty()||password.isEmpty()||name.isEmpty()||email.isEmpty()||phone.isEmpty()){
                out.println(getAdminRegistrationFailurePopUp()+"1???");
            }else {
                if (agent.addUser(userid, password)) {
                    out.println(getAdminRegistrationSuccessPopUp(1));
                    // 1. JDBC ???????????? ??????
                    Class.forName(DBInfo.JdbcDriver);

                    // 2. Connection ?????? ??????
                    Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                    // 3. PreparedStatement ?????? ?????? sql??????????????? ?????? ??????. ????????? ????????? ????????? ??????x
                    String sql = "INSERT INTO users(username,email,name,phone) VALUES(?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    // 4. SQL ??? ?????? 
                    request.setCharacterEncoding("UTF-8");  //?????? ??????
                    if (!(userid == null) && !email.equals("")) {
                        pstmt.setString(1, userid);
                        pstmt.setString(2, email);
                        pstmt.setString(3, name);
                        pstmt.setString(4, phone);

                        out.println("sql = " + sql + "<br>");
                        // 5. ?????? : PreparedStatement.executeUpdate()???
                        // INSERT, update ?????? DELETE??? ???????????????.
                        pstmt.executeUpdate();
                    }
                    // 6. ?????? ??????
                    pstmt.close();
                    conn.close();

                } else {
                    out.println(getAdminRegistrationFailurePopUp());
                }
            }
            out.flush();
        } catch (Exception ex) {
            out.println(ex+"????????? ????????? ??????????????????.");
        }
    }

    private String getUserRegistrationSuccessPopUp(int chk) {
        String alertMessage = "??????????????? ??????????????????.";
        if(chk==2){
            alertMessage = "??????????????? ??????????????? ??????????????????.";
        }
        else if(chk==3){
            alertMessage = "??????????????? ??????????????? ??????????????????.";
        }
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>?????? ?????? ??????</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        if(chk ==1){
        successPopUp.append("window.location = \"sign_up.jsp\"; ");
        }
        else{
            successPopUp.append("window.location = \"index.jsp\"; ");
        }
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }

    private String getUserRegistrationFailurePopUp() {
        String alertMessage = "??????????????? ??????????????????.";
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>?????? ?????? ??????</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"sign_up.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }
        private String getAdminRegistrationSuccessPopUp(int chk) {
        String alertMessage = "????????? ????????? ????????? ??????????????????.";
        if(chk==2){
            alertMessage = "??????????????? ??????????????? ??????????????????.";
        }
        else if(chk==3){
            alertMessage = "??????????????? ??????????????? ??????????????????.";
        }
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>?????? ?????? ??????</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        if(chk ==1){
        successPopUp.append("window.location = \"add_user.jsp\"; ");
        }
        else{
            successPopUp.append("window.location = \"index.jsp\"; ");
        }
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }
        private String getAdminRegistrationFailurePopUp() {
        String alertMessage = "????????? ????????? ????????? ??????????????????.";
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>?????? ?????? ??????</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"add_user.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }

    private String getSamePasswordFailurePopUp(int chk) {
        String alertMessage = "??????????????? ???????????? ????????????.";
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>?????? ?????? ??????</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        if (chk == 1) {
            successPopUp.append("window.location = \"sign_up.jsp\"; ");
        } else if(chk == 2) {
            successPopUp.append("window.location = \"del_member.jsp\"; ");
        }
        else{
            successPopUp.append("window.location = \"changePassword.jsp\"; ");
        }
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }

    private String getSameIdSucessPopUp(int chk) {
        String alertMessage = "??????????????? ??????????????????.";
        if (chk == 1) {
            alertMessage = "????????? ???????????? ???????????????.";
        }
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>?????? ?????? ??????</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"sign_up.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }

    private void IDCheck(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String server = "127.0.0.1";
        int port = 4555;
        try {
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."));
            String userid = request.getParameter("id");  // for test
            String password = request.getParameter("password");// for test
            String password2 = request.getParameter("password2");// for test
            String name2 = request.getParameter("name2");// for test
            String email = request.getParameter("email");// for test
            String phone = request.getParameter("phone");// for test



            // 1. JDBC ???????????? ??????
            Class.forName(DBInfo.JdbcDriver);

            // 2. Connection ?????? ??????
            Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

            // 3. PreparedStatement ?????? ??????
            String sql = "SELECT username FROM users WHERE (username=?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            boolean x = false;
            // 4. SQL ??? ??????
            request.setCharacterEncoding("UTF-8");  //?????? ??????
            pstmt.setString(1, userid);

            out.println("sql = " + sql + "<br>");
            // 5. ?????? : PreparedStatement.executeUpdate()???
            // INSERT, update ?????? DELETE??? ???????????????.
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String dbuser = rs.getString("username");

                if (userid.equals(dbuser)) {
                    x = true;
                }
            }
            if (x) {
                out.println(getSameIdSucessPopUp(1));
            } else {
                out.println(getSameIdSucessPopUp(2));
            }

            // 6. ?????? ??????
            pstmt.close();
            conn.close();
            rs.close();

            out.flush();
        } catch (Exception ex) {
            out.println("????????? ????????? ??????????????????.");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String server = "127.0.0.1";
        int port = 4555;
        try {
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."));
            String deleteUserID = (String) request.getSession().getAttribute("userid");
            String passchk = (String) request.getSession().getAttribute("password");
            String[] deleteUserID2 = {deleteUserID};
            String password = request.getParameter("password");

            if (!password.equals(passchk)) {
                out.println(getSamePasswordFailurePopUp(2));
            } else {
                if (agent.deleteUsers(deleteUserID2)) {
                    // 1. JDBC ???????????? ??????
                    Class.forName(DBInfo.JdbcDriver);

                    // 2. Connection ?????? ??????
                    Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                    // 3. PreparedStatement ?????? ??????
                    String sql = "DELETE FROM users WHERE (username=?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    // 4. SQL ??? ??????
                    request.setCharacterEncoding("UTF-8");  //?????? ??????

                    pstmt.setString(1, deleteUserID);

                    out.println("sql = " + sql + "<br>");
                    // 5. ?????? : PreparedStatement.executeUpdate()???
                    // INSERT, update ?????? DELETE??? ???????????????.
                    pstmt.executeUpdate();

                    // 6. ?????? ??????
                    pstmt.close();
                    conn.close();
                } else {
                    out.println("??????????????? ???????????? ????????????.");
                }

                response.sendRedirect("index.jsp");
                out.flush();
            }
        } catch (Exception ex) {
            System.out.println(" SignUpHandler.deleteUser : exception = " + ex);
        }

    }
    
    private void changePassword(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String server = "127.0.0.1";
        int port = 4555;
        try {
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."));
            String userid = (String) request.getSession().getAttribute("userid");
            String password = request.getParameter("password");// for test
            String password2 = request.getParameter("password2");// for test

            final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
            final String JdbcUrl = "jdbc:mysql://127.0.0.1:3308/webmail?serverTimezone=Asia/Seoul";
            final String User = "jdbctester";
            final String Password = "rjgkr505";

            out.println("userid = " + userid + "<br>");
            out.println("password1 = " + password + "<br>");
            out.println("password2 = " + password2 + "<br>");

            // if (addUser successful)  ????????? ?????? ?????? ?????????
            // else ????????? ?????? ?????? ?????????
            if (!password.equals(password2)) {
                out.println(getSamePasswordFailurePopUp(3));
            } else {
                if (agent.changePassword(userid, password)) {
                    out.println(getUserRegistrationSuccessPopUp(2));
                } else {
                    out.println(getUserRegistrationSuccessPopUp(3));
                }
            }
            out.flush();
        } catch (Exception ex) {
            out.println("????????? ????????? ??????????????????.");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
