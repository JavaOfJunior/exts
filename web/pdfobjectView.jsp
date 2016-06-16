<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

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

<div id="example1"></div>

<script type="text/javascript" src="/pdfObject/pdfobject.js"></script>
<script type="text/javascript" src="/uploadify/jquery.min.js"></script>

<script>
    var options = {
        height: "400px",
//        $('#download').remove(),
        pdfOpenParams: { view: 'FitV', page: '2' }
    };
    PDFObject.embed("/uploads/计算机组成原理白中英第四版课后习题参考答案BW12.pdf", "#example1");
    setTimeout(function(){

        var dd = $('#example1').html();
        alert(dd);
        debugger;
        $('#download').remove();
    },5000);

</script>
</body>
</html>
