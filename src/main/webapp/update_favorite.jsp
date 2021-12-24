<%-- 
    Document   : update_favorite
    Created on : 2021. 6. 2., 오후 6:52:21
    Author     : songe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>즐겨찾기(주소록) 업데이트</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <div id="main">
            <form enctype="multipart/form-data" method="POST"
                  action="UpdateFavorit.do?menu=<%= CommandType.UPDATE_FAVORITE_COMMAND%>" >
                <table border="1" style="float:left">
                    <tr>
                        <th>이름</th>
                        <td><input type = "text" name = "up_name" maxlength = "30"></td>
                    </tr>
                    <tr>
                        <th>메모</th>
                        <td><input type="text" name="up_memo" maxlength="30"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align = "center">
                    <select id="group" name="group">
                        <option value="회사">회사</option>
                        <option value="친구">친구</option>
                        <option value="가족">가족</option>
                    </select>
                    
                        <input type="submit" value="수정">
                        <input type="reset" value="취소">
                    </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
