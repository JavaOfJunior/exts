<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String waterFilePath =""+session.getAttribute("waterFilePath");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>预览</title>
</head>

<style type="text/css">
    .pdfobject-container { height: 700px;}
    .pdfobject { border: 1px solid #666; }
</style>
<body>
<%=waterFilePath%>
<div id="example1"></div>
<script type="text/javascript" src="/pdfObject/pdfobject.js"></script>
<script type="text/javascript" src="/uploadify/jquery.min.js"></script>

<script>
    PDFObject.embed("/uploads/计算机组成原理白中英第四版课后习题参考答案BW12.pdf", "#example1", {fallbackLink: false});
    setTimeout(function(){
alert("dd");
//      $('#download').remove();
    },5000);

</script>
</body>
</html>
