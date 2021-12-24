<%-- 
    Document   : del_favorite
    Created on : 2021. 5. 29., 오후 2:12:05
    Author     : songe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>즐겨찾기(주소록) 삭제</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <div id="main">
            <form enctype="multipart/form-data" method="POST"
                  action="DelFavorit.do?menu=<%= CommandType.DELETE_FAVORITE_COMMAND%>" >
                <table border="1" style="float:left">
                <tr>
                    <th>이름</th>
                    <td><input type = "text" name = "del_name" maxlength = "30"></td>
                </tr>
                <tr>
                    <td colspan="2" align = "center">
                            <input type="submit" value="삭제">
                            <input type="reset" value="취소">
                    </td>
                </tr>
            </table>
            </form>
        </div>
    </body>
</html>
