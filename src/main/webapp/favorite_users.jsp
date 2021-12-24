<%-- 
    Document   : favorite_users
    Created on : 2021. 5. 16., 오후 5:42:00
    Author     : songe
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<%@taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir = "/WEB-INF/tags/"  prefix="mytags"%>
<jsp:useBean id="pop3" scope="page" class="cse.maven_webmail.model.Pop3Agent" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>즐겨찾기(주소록) 추가</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>
        <div id="main">
            <form enctype="multipart/form-data" method="POST"
                  action="Favorit.do?menu=<%= CommandType.SAVE_USER_COMMAND%>" >
                <table border="1" style="float:left">
                    <tr>
                        <th>이름</th>
                        <td><input type = "text" name = "ad_name" maxlength = "30"></td>
                    </tr>
                    <tr>
                        <th>메모</th>
                        <td><input type = "text" name = "ad_memo" maxlength = "30"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align = "center">
                            <select id="group" name="group">
                                <option value="회사">회사</option>
                                <option value="친구">친구</option>
                                <option value="가족">가족</option>
                            </select>
                            <input type="submit" value="저장">
                            <input type="reset" value="취소">
                        </td>
                    </tr>
                </table>
            </form>
            <div>
                <jsp:include page="update_favorite.jsp"/>
            </div>
            <div id="delbar" >
                <jsp:include page="del_favorite.jsp" />
            </div>
            <div>
                <c:catch var="errorReason">
                    <mytags:addrbook user="root" password="wjdtjr0847" schema="webmail" table="favorite" userid="<%= pop3.getUserid()%>"/>
                </c:catch>
                ${empty errorReason ?"<noerror/>" : errorReason} 
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
