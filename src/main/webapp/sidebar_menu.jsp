<%-- 
    Document   : sidebar_menu.jsp
    Author     : hasneulgi 내게쓰기 추가
    
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>웹메일 시스템 메뉴</title>
    </head>
    <body>
        <br> <br>
        
        <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>

        <p> <a href="main_menu.jsp"> 메일 읽기 </a> </p>
        <p> <a href="write_mail.jsp"> 메일 쓰기 </a> </p>
        <p> <a href="my_write_mail.jsp"> 내게 쓰기 </a> </p>
        <p> <a href="sentmail.jsp"> 보낸 메일함 </a> </p>
        <p> <a href="my_sent_mail.jsp"> 내게 쓴 메일함 </a> </p>
        <p> <a href="favorite_users.jsp"> 즐겨찾기 추가/삭제/수정</a> </p>
        <p> <a href="changePassword.jsp"> 비밀번호 변경 </a> </p>
        <p> <a href="del_member.jsp"> 회원 탈퇴 </a> </p>
        <p><a href="Login.do?menu=<%= CommandType.LOGOUT %>">로그아웃</a></p>
    </body>
</html>
