<%-- 
    Document   : write_mail.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<%@taglib uri ="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir = "/WEB-INF/tags/"  prefix="mytags"%>
<jsp:useBean id="pop3" scope="page" class="cse.maven_webmail.model.Pop3Agent" />

<!DOCTYPE html>

<%-- @taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" --%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script type="text/javascript">
            function check_subj() {
                //const subj= document.getElementsByName('subj');
                if (fom.subj.value == "") {
                    alert("제목을 입력하세요");
                    return false;
                } else {
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

        <div id="main">
            <form  name="fom" enctype="multipart/form-data" method="POST"
                   action="WriteMail.do?menu=<%= CommandType.SEND_MAIL_COMMAND%>" onsubmit="return check_subj()">
                <table style="float:left">
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
                        <td> <input type="text" name="subj" size="80" >  </td>
                    </tr>
                    <tr>
                        <td colspan="2">본  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 문</td>
                    </tr>
                    <tr>  <%-- TextArea    --%>
                        <td colspan="2">  <textarea rows="15" name="body" cols="80"></textarea> </td>
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
            <c:catch var="errorReason">
                <mytags:addrbook user="root" password="wjdtjr0847" schema="webmail" table="favorite" userid="<%= pop3.getUserid()%>"/>
            </c:catch>
            ${empty errorReason ?"<noerror/>" : errorReason}
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
