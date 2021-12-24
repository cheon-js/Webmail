<%-- 
    Document   : relay_write_mail
    Created on : 2021. 5. 26., 오후 11:45:26
    Author     : hanse
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html>

<%-- @taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" --%>

<jsp:useBean id="pop3" scope="page" class="cse.maven_webmail.model.Pop3Agent" />
<%
            pop3.setHost((String) session.getAttribute("host"));
            pop3.setUserid((String) session.getAttribute("userid"));
            pop3.setPassword((String) session.getAttribute("password"));
            
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>전달하기  화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script type="text/javascript">
            function check_subj(){
                //const subj= document.getElementsByName('subj');
                if(fom.subj.value ==""){ 
                    alert("제목을 입력하세요");
                    return false;
                }else{   
                    return true;
                }
            }
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        
        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />  
        </div>
            <div id="subject">
                전달하기
                <% pop3.setRequest(request); %>
                
                
            </div>
        <div id="main">
            <form name="fom" enctype="multipart/form-data" method="POST"
                  action="WriteMail.do?menu=<%= CommandType.SEND_MAIL_COMMAND%>" onsubmit="return check_subj()">
                <table>
                    <tr>
                        <td> 수신 </td>
                        <td> <input type="text" name="to" size="80"
                                    value=<%=request.getParameter("recv") == null ? "" : request.getParameter("recv")%>>  </td>
                        
                        
                        
                    </tr>
                    <tr>
                        <td>참조</td>
                        <td> <input type="text" name="cc" size="80">  </td>
                    </tr>
                    <tr>
                        <td> 메일 제목 </td>
                        <td> <input type="text" name="subj" size="80"  >  </td>
                    </tr>
                    <tr>
                        <td colspan="2">본  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 문</td>
                    </tr>
                    <tr>  <%-- TextArea    --%>
                        <td colspan="2">  
                            <textarea rows="15" name="body" cols="80"><%= pop3.getBody(Integer.parseInt((String) request.getParameter("msgid")))%>
                            </textarea>
                        </td>
                        
                    </tr>
                    <tr>
                        <td>첨부 파일</td>
                        <td> <input type="file" name="file1"  size="80">  </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="메일 보내기">
                            <input type="reset" value="다시 입력">
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
