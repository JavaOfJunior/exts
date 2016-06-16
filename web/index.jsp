<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);
%>
<html>
<head>
    <title>上传</title>
</head>
<body>
<div style="padding:20px;">
    <input id="file_upload" name="file_upload" type="file" multiple="true">
    <a  href="javascript:$('#file_upload').uploadify('upload','*')">开始上传</a>|
    <a  href="javascript:$('#file_upload').uploadify('cancel','*')">取消全部</a>
</div>
<script type="text/javascript" src="/uploadify/jquery.min.js"></script>
<link rel="stylesheet" href="/uploadify/uploadify.css" type="text/css" />
<script type="text/javascript" src="/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript">
    $(function() {
        $("#file_upload").uploadify({
            swf: '/uploadify/uploadify.swf',
            uploader: '/upload/uploads.do',
            height  : 30,
            width   : 120,
            // 设置为不自动上传
            auto    : false,
            // 允许上传的文件后缀
            fileTypeExts:'*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.pdf',
            'onUploadComplete' : function(file) {
                location.href="/generic/web/viewPdf.jsp?url="+file.name;
            }
        });
    });
</script>


</html>
