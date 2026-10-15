<%--
  Created by IntelliJ IDEA.
  User: Norio
  Date: 15.10.2026
  Time: 11:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Каталог книг</title>
</head>
<body>
<h1>Каталог книг</h1>
<ul>
    <c:forEach var="book" items="${books}">
        <li>
            <a href="${pageContext.request.contextPath}/comments?bookId=${book.id}">
                    ${book.title} — ${book.author} (${book.pubYear})
            </a>
        </li>
    </c:forEach>
</ul>
</body>
</html>
