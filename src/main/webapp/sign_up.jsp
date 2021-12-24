<%-- 
    Document   : sign_up.jsp
    Author     : jaehong
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/ch06/" prefix="mytags" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원 가입 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        

    </head>
    <body>
        <jsp:include page="header.jsp" />
        
        <div id="main">
            추가로 등록할 사용자 정보를 입력해 주시기 바랍니다. <br> <br>

            <form name="AddUser" action="SignUp.do?menu=<%= CommandType.ADD_USER_COMMAND2%>"
                  method="POST" onsubmit="return checkValue()">
                <table border="0" align="left">
                    <tr>
                        <td>사용자 ID</td>
                        <td> <input type="text" name="id" value="" size="20" /> 
                            <input type="submit" value="확인" name = "DB" formaction="SignUp.do?menu=<%= CommandType.CHECK_USER_COMMAND%>">

                        </td>
                    </tr>
                    <tr>
                        <td>암호</td>
                        <td> <input type="password" name="password" value="" /> </td>
                    </tr>
                    <tr>
                        <td>암호 확인</td>
                        <td> <input type="password" name="password2" value="" /> </td>
                    </tr>
                    <tr>
                        <td>이름</td>
                        <td> <input type="text" name="name" value="" size="20" /> </td>
                    </tr>
                    <tr>
                        <td>이메일</td>
                        <td> <input type="text" name="email1" value="" size="20" >@
                            <select name="email2">
                                <option>deu.ac.kr</option>
                                <option>naver.com</option>
                                <option>daum.net</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>전화 번호</td>
                        <td> <input type="text" name="phone" value="" size="20" /> </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="등록" name="register" />
                            <input type="reset" value="초기화" name="reset" />
                        </td>
                    </tr>
                </table>

            </form>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
