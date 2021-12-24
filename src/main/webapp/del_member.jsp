<%-- 
    Document   : del_member
    Created on : 2021. 6. 3., 오전 10:28:01
    Author     : leeja
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원탈퇴</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="header.jspf"%>
        <div id="passchk">
            비밀번호를 입력해주십시오. <br> <br>

            <form name="DeleteUser" action="SignUp.do?menu=<%= CommandType.DELETE_USER_COMMAND2%>"
                  method="POST">
                <table border="0" align="left">
                    <tr>
                        <td>암호</td>
                        <td> <input type="password" name="password" value="" /> </td>
                    </tr>
                        <td colspan="2">
                            <input type="submit" value="삭제" name="delete" onclick="location='index.jsp'"/>
                            <input type="reset" value="초기화" name="reset" />
                        </td>
                    </tr>
                </table>

            </form>
        </div>
                  
                  <%@include file="footer.jspf"%>
    </body>
</html>
