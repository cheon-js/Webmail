<%-- 
    Document   : admin_menu.jsp
    Author     : hyun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.model.UserAdminAgent"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<!DOCTYPE html>
<!--sql을 직접 연동해서 테이블에 사용자의 데이터를 삽입함.-->
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 관리 메뉴</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_admin_menu.jsp" />
        </div>
            <sql:setDataSource var="admintable" driver="com.mysql.cj.jdbc.Driver"
                               url="jdbc:mysql://localhost:3308/webmail?serverTimezone=Asia/Seoul"
                               user="root" password="wjdtjr0847"/>
            <sql:query dataSource="${admintable}" var="tableset">
                SELECT * FROM users;
            </sql:query>

        <div id="main">
            <h2> 메일 사용자 목록 </h2>
            <ul>
            <table border="1">
            <thead>
                <tr>
                    <th>아이디</th>
                    <th>이메일</th>
                    <th>휴대폰 번호</th>
                </tr>
            </thead>
            <c:forEach var = "row" items="${tableset.rows}">
            <tbody>
            <td><c:out value="${row.username}"/></td>
            <td><c:out value="${row.email}"/></td>
            <td><c:out value="${row.phone}"/></td>
            </tbody>
            </c:forEach>
            </table>
            </ul>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
