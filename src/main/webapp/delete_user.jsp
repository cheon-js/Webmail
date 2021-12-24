<%-- 
    Document   : delete_user.jsp
    Author     : hyun
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<%@page import="cse.maven_webmail.model.UserAdminAgent" %>
<%@page import="cse.maven_webmail.model.UserBeans" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

<script type="text/javascript">
    function idDelete(delUser){
        location.href = "delete_check.jsp?userid=" + delUser;   //사용자 정보의 값을 할당해 주기 위함.
    }
</script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 제거 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script>
            function searchCheck(form){
                //검색
                if(form.keyWord.value ==""){
                    alert("검색 단어를 입력하세요.");
                    form.keyWord.focus();
                    return;
                }
                form.submit();      
            }
        </script>
    </head>
    <body>            
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <%-- 사용자 추가때와 동일하므로 같은 메뉴 사용함. --%>
            <jsp:include page="sidebar_admin_previous_menu.jsp" />
        </div>        
        <jsp:useBean id="serchmem" class="cse.maven_webmail.model.memberSerch" />
        <%    
            request.setCharacterEncoding("UTF-8");
            String keyField = request.getParameter("keyField");
            String keyWord = request.getParameter("keyWord");
            ArrayList<UserBeans> list = serchmem.getMemberlist(keyField, keyWord);
        %>

        <div id="main">
            <h2> 삭제할 사용자를 선택해 주세요. </h2> <br>
        <table>
        <tr>
            <th>사용자</th><th>이메일</th><th>폰번호</th><th>&nbsp;</th>
        </tr>
        <%
        for(UserBeans ub : list){
        %> 
            <tr>
                <td><%=ub.getUserName()%></td>
                <td><%=ub.geteMail()%></td>
                <td><%=ub.getPhone()%></td>
                <td><input type="button" value="제거" name="delete_command" onclick="idDelete('<%=ub.getUserName()%>');"></td>

            </tr>              
        <%
            }
         %>
        <tr>   
            <%-- 옵션을 통해 검색하기 원하는 속성을 고를 수 있다.. --%>
            <td colspan="5"> <br/>
                <form name="serach" method ="post">
                <select name="keyField">
                    <option value="0"> ----선택----</option>
                    <option value="username">사용자</option>
                    <option value="email">이메일</option>
                    <option value="phone">폰번호</option>   
                </select>
                <input type="text" name="keyWord" />
                <input type="button" value="검색" onclick="searchCheck(form)" />           
                </form>           
            </td>      
        </tr>
            </table>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
