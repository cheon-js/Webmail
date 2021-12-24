<%-- 
    Document   : sentmail
    Created on : 2021. 5. 31., 오후 9:11:13
    Author     : 천정석
--%>

<%@page import="cse.maven_webmail.control.CommandType"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.ArrayList"%>

<!DOCTYPE html>

<jsp:useBean id="sentmail" scope="page" class="cse.maven_webmail.model.getSentmail" />
<%    
            String status = "";
            status = request.getParameter("status");
           
%>
<html>
     <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>보낸 메일함</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
     </head>
     <body>
        <jsp:include page="header.jsp" />
        
        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>
        <div id="main">

            <script>
                    history.replaceState({}, null, location.pathname);
                    function searchCheck(frm){
                          //검색
                         if(frm.keyWord.value ==""){
                                alert("검색 단어를 입력하세요.");
                                frm.keyWord.focus();
                                return;
                            }
                        frm.submit();      
                        }
            </script>
            
            <% if(status != null){
                    if(status.equals("1")){
                        out.println("<script>confirm('메일 삭제 성공');</script>");
                        status = null;
                    }
                    else if(status.equals("0")){
                        out.println("<script>alert('메일 삭제 성공');</script>");
                        status = null;
                    }
            }%>

            <% String userid = (String)session.getAttribute("userid");%>
            <%= sentmail.getSentMessageList(userid) %>

         <jsp:include page="footer.jsp" />
     </body>
        
       
</html>