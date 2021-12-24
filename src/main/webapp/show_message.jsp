<%-- 
    Document   : show_message.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<jsp:useBean id="pop3" scope="page" class="cse.maven_webmail.model.Pop3Agent" />
<%
            pop3.setHost((String) session.getAttribute("host"));
            pop3.setUserid((String) session.getAttribute("userid"));
            pop3.setPassword((String) session.getAttribute("password"));
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 보기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>
            
        <div>
          
                
                <%--<%=request.getParameter("msgid")%>--%>
                <% pop3.setRequest(request); %>
                <%--<%= pop3.getFromAddr(Integer.parseInt((String) request.getParameter("msgid")))%>--%>
                <button onclick="location='my_write_mail.jsp'"value="msgid">내게쓰기</button>
                <button onclick="location='reply_write_mail.jsp?msgid=<%=request.getParameter("msgid")%>'">답장</button>
                <button onclick="location='relay_write_mail.jsp?msgid=<%=request.getParameter("msgid")%>'">전달</button>
                <button onclick="location='ReadMail.do?menu=41&msgid=<%=request.getParameter("msgid")%>'">삭제</button>
                
                        
                <%--<input type="submit" onclick="location.href='reply_write_mail.jsp?msgid=5'">--%>
                
                
                
  
        </div>
        <div id="msgBody">
            <% pop3.setRequest(request); %>
            <%= pop3.getMessage(Integer.parseInt((String) request.getParameter("msgid")))%>
        </div>


        <jsp:include page="footer.jsp" />


    </body>
</html>
