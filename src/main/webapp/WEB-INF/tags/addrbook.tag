<%@tag description="JSTL" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="user" required="true" %>
<%@attribute name="password" required="true" %>
<%@attribute name="schema" required="true" %>
<%@attribute name="table" required="true" %>
<%@attribute name="userid" required="true" %>

<%-- any content can be specified here e.g.: --%>

<sql:setDataSource var="dataSrc"
                   url="jdbc:mysql://localhost:3308/${schema}?serverTimezone=Asia/Seoul"
                   driver="com.mysql.cj.jdbc.Driver"
                   user="${user}" password="${password}" />

<sql:query var="rs" dataSource="${dataSrc}">  
    SELECT F_username, memo, grouptable FROM ${table} WHERE username='${userid}';
</sql:query>

<table border="1" style="float:left">
    <thead>
        <tr>
            <th>그룹</th>
            <th>이름</th>
            <th>메모</th>
        </tr>
    </thead>
    <tbody >
        <c:forEach var="row" items="${rs.rows}">
            <tr>
                <td>${row.grouptable}</td>
                <td>${row.F_username}</td>
                <td>${row.memo}</td>
            </tr>
        </c:forEach>
    </tbody>
    </table>