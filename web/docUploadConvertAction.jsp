<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="java.io.*"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="cn.henu.util.DocConverter"%>
<%
String saveDirectory =application.getRealPath("/")+"upload";

System.out.println(saveDirectory);
int maxPostSize = 50 * 1024 * 1024 ;
DefaultFileRenamePolicy dfp = new DefaultFileRenamePolicy();
MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,"UTF-8",dfp);
 Enumeration files = multi.getFileNames();
     while (files.hasMoreElements()) {
        System.err.println("ccc");
       String name = (String)files.nextElement();
       File f = multi.getFile(name);
       if(f!=null){
         String fileName = multi.getFilesystemName(name);
         //获取上传文件的扩展名
         String extName=fileName.substring(fileName.lastIndexOf(".")+1);
         //文件全路径
         String lastFileName= saveDirectory+"\\" + fileName;
         //获取需要转换的文件名,将路径名中的'\'替换为'/'
         String converfilename = saveDirectory.replaceAll("\\\\", "/")+"/"+fileName;
         System.out.println(converfilename);
         //调用转换类DocConverter,并将需要转换的文件传递给该类的构造方法
         DocConverter d = new DocConverter(converfilename);
         //调用conver方法开始转换，先执行doc2pdf()将office文件转换为pdf;再执行pdf2swf()将pdf转换为swf;
     	 d.conver();
     	 //调用getswfPath()方法，打印转换后的swf文件路径
         System.out.println(d.getswfPath());
     	 //生成swf相对路径，以便传递给flexpaper播放器
         String swfShowPath = "upload"+d.getswfPath().substring(d.getswfPath().lastIndexOf("/"));
         System.out.println(swfShowPath);
         //将相对路径放入sessio中保存
         session.setAttribute("swfShowPath", swfShowPath);
       }
     }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
	body {margin-top:100px;background:#fff;font-family: Verdana, Tahoma;}
    a {color:#CE4614;}
    #msg-box {color: #CE4614; font-size:0.9em;text-align:center;}
    #msg-box .logo {border-bottom:5px solid #ECE5D9;margin-bottom:20px;padding-bottom:10px;}
    #msg-box .title {font-size:1.4em;font-weight:bold;margin:0 0 30px 0;}
    #msg-box .nav {margin-top:20px;}
</style>
</head>
<body>
	<div>
		<form name="viewForm" id="form_swf" action="documentView.jsp" method="POST">
			<input type='submit' value='预览' class='BUTTON SUBMIT'/>
		</form>
	</div>
</body>
</html>