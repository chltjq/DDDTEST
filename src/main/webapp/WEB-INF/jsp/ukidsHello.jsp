<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Spring Boot UKIDS Application with JSP</title>

    <script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
    <style>
      div {
        padding: 10px;
      }
    </style>
  </head>

  <body>
    <script type="text/javascript">
      $(document).ready(() => {
        $("#search").click((e) => {
          //샘플조회
          $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "http://localhost:7071/v1/samples",
            success: (response) => {
              console.log(response);
              response.data.map((sample, index) => {
                $("#result").append(
                  "<div>" +
                    (index + 1) +
                    ", id : " +
                    sample.id +
                    ", name : " +
                    sample.name +
                    "</div>"
                );
              });
            },
          });
        });
      });
    </script>

    <div>UKIDS JSP 샘플입니다.</div>
    <div>입력한 view는 <c:out value="${view}" /> 입니다.</div>
    <div><button id="search">샘플조회</button></div>
    <div>조회결과</div>
    <div id="result"></div>
  </body>
</html>
