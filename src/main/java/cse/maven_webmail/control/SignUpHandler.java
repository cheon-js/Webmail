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
                    out.println("없는 메뉴를 선택하셨습니다. 어떻게 이 곳에 들어오셨나요?");
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



            // if (addUser successful)  사용자 등록 성공 팦업창
            // else 사용자 등록 실패 팝업창
            if (!password.equals(password2)) {
                out.println(getSamePasswordFailurePopUp(1));
            } else if(userid.isEmpty()||password.isEmpty()||name.isEmpty()||email.isEmpty()||phone.isEmpty()){
                out.println(getUserRegistrationFailurePopUp());
            }else {
                if (agent.addUser(userid, password)) {
                    out.println(getUserRegistrationSuccessPopUp(1));
                    // 1. JDBC 드라이버 적재
                    Class.forName(DBInfo.JdbcDriver);

                    // 2. Connection 객체 생성
                    Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                    // 3. PreparedStatement 객체 생성 sql쿼리문에서 오류 발생. 데이터 갯수와 컬럼이 일치x
                    String sql = "INSERT INTO users(username,email,name,phone) VALUES(?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    // 4. SQL 문 완성 
                    request.setCharacterEncoding("UTF-8");  //한글 인식
                    if (!(userid == null) && !email.equals("")) {
                        pstmt.setString(1, userid);
                        pstmt.setString(2, email);
                        pstmt.setString(3, name);
                        pstmt.setString(4, phone);

                        out.println("sql = " + sql + "<br>");
                        // 5. 실행 : PreparedStatement.executeUpdate()는
                        // INSERT, update 또는 DELETE시 사용가능함.
                        pstmt.executeUpdate();
                    }
                    // 6. 자원 해제
                    pstmt.close();
                    conn.close();

                } else {
                    out.println(getUserRegistrationFailurePopUp());
                }
            }
            out.flush();
        } catch (Exception ex) {
            out.println(ex+"시스템 접속에 실패했습니다.");
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



            // if (addUser successful)  사용자 등록 성공 팦업창
            // else 사용자 등록 실패 팝업창
            if (!password.equals(password2)) {
                out.println(getSamePasswordFailurePopUp(1));
            } else if(userid.isEmpty()||password.isEmpty()||name.isEmpty()||email.isEmpty()||phone.isEmpty()){
                out.println(getAdminRegistrationFailurePopUp()+"1번");
            }else {
                if (agent.addUser(userid, password)) {
                    out.println(getAdminRegistrationSuccessPopUp(1));
                    // 1. JDBC 드라이버 적재
                    Class.forName(DBInfo.JdbcDriver);

                    // 2. Connection 객체 생성
                    Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                    // 3. PreparedStatement 객체 생성 sql쿼리문에서 오류 발생. 데이터 갯수와 컬럼이 일치x
                    String sql = "INSERT INTO users(username,email,name,phone) VALUES(?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    // 4. SQL 문 완성 
                    request.setCharacterEncoding("UTF-8");  //한글 인식
                    if (!(userid == null) && !email.equals("")) {
                        pstmt.setString(1, userid);
                        pstmt.setString(2, email);
                        pstmt.setString(3, name);
                        pstmt.setString(4, phone);

                        out.println("sql = " + sql + "<br>");
                        // 5. 실행 : PreparedStatement.executeUpdate()는
                        // INSERT, update 또는 DELETE시 사용가능함.
                        pstmt.executeUpdate();
                    }
                    // 6. 자원 해제
                    pstmt.close();
                    conn.close();

                } else {
                    out.println(getAdminRegistrationFailurePopUp());
                }
            }
            out.flush();
        } catch (Exception ex) {
            out.println(ex+"시스템 접속에 실패했습니다.");
        }
    }

    private String getUserRegistrationSuccessPopUp(int chk) {
        String alertMessage = "회원가입이 성공했습니다.";
        if(chk==2){
            alertMessage = "비밀번호를 성공적으로 변경했습니다.";
        }
        else if(chk==3){
            alertMessage = "비밀번호를 변경하는데 실패했습니다.";
        }
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
        String alertMessage = "회원가입이 실패했습니다.";
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
        successPopUp.append("window.location = \"sign_up.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }
        private String getAdminRegistrationSuccessPopUp(int chk) {
        String alertMessage = "관리자 사용자 등록이 성공했습니다.";
        if(chk==2){
            alertMessage = "비밀번호를 성공적으로 변경했습니다.";
        }
        else if(chk==3){
            alertMessage = "비밀번호를 변경하는데 실패했습니다.";
        }
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
        String alertMessage = "관리자 사용자 등록이 실패했습니다.";
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
        successPopUp.append("window.location = \"add_user.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }

    private String getSamePasswordFailurePopUp(int chk) {
        String alertMessage = "비밀번호가 일치하지 않습니다.";
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
        String alertMessage = "사용가능한 아이디입니다.";
        if (chk == 1) {
            alertMessage = "중복된 아이디가 존재합니다.";
        }
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



            // 1. JDBC 드라이버 적재
            Class.forName(DBInfo.JdbcDriver);

            // 2. Connection 객체 생성
            Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

            // 3. PreparedStatement 객체 생성
            String sql = "SELECT username FROM users WHERE (username=?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            boolean x = false;
            // 4. SQL 문 완성
            request.setCharacterEncoding("UTF-8");  //한글 인식
            pstmt.setString(1, userid);

            out.println("sql = " + sql + "<br>");
            // 5. 실행 : PreparedStatement.executeUpdate()는
            // INSERT, update 또는 DELETE시 사용가능함.
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

            // 6. 자원 해제
            pstmt.close();
            conn.close();
            rs.close();

            out.flush();
        } catch (Exception ex) {
            out.println("시스템 접속에 실패했습니다.");
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
                    // 1. JDBC 드라이버 적재
                    Class.forName(DBInfo.JdbcDriver);

                    // 2. Connection 객체 생성
                    Connection conn = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);

                    // 3. PreparedStatement 객체 생성
                    String sql = "DELETE FROM users WHERE (username=?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    // 4. SQL 문 완성
                    request.setCharacterEncoding("UTF-8");  //한글 인식

                    pstmt.setString(1, deleteUserID);

                    out.println("sql = " + sql + "<br>");
                    // 5. 실행 : PreparedStatement.executeUpdate()는
                    // INSERT, update 또는 DELETE시 사용가능함.
                    pstmt.executeUpdate();

                    // 6. 자원 해제
                    pstmt.close();
                    conn.close();
                } else {
                    out.println("비밀번호가 일치하지 않습니다.");
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

            // if (addUser successful)  사용자 등록 성공 팦업창
            // else 사용자 등록 실패 팝업창
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
            out.println("시스템 접속에 실패했습니다.");
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
