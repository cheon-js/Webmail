<%-- 
    Document   : del_member
    Author     : hyun
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC
"-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>사용자 삭제</title>
</head>
<body>
	<jsp:useBean id="memSer" class="cse.maven_webmail.model.memberSerch" />

	<%     
        //넘어온 ID값 받아서 삭제하는 메소드 호출.
        //HANDLE을 사용하고 싶었으나 request, session등 여러 방법을 통해 데이터를 옮기는데 모두 실패함.
        int result = memSer.deleteUsers(request.getParameter("userid"));
        String message = "";
        if(result > 0 ){                       
            message = "사용자 제거 완료.";
        }
        else{
            message = "제거 실패.";
        }
    %>

	<script>   
        alert("<%=message%>");
        location.href="delete_user.jsp";  
    </script>
</body>
</html>