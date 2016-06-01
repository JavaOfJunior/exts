package cn.henu.controller;


import cn.henu.entity.CommonUtil;
import cn.henu.entity.DocConverters;
import cn.henu.entity.Wordxx;
import cn.henu.util.DocConverter;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("upload")
public class UploadController {

    @RequestMapping("uploads")
    public void upload(HttpServletRequest request, HttpServletResponse response)throws Exception{
      uplopMi(request);
      //uploGivethrows(request);
    }
    private  void uplopMi(HttpServletRequest request)throws  Exception{
        //文档文件夹
        String path=request.getSession().getServletContext().getRealPath("") + "\\uploads";
        File f = new File(path) ;
        if(!f.exists()) f.mkdirs() ;

        List<Map<String, String>> fileNames = CommonUtil.uploadWordFile(request, path) ;   //文档保存

        String savePath = request.getSession().getServletContext().getRealPath("")+"\\uploads\\";
        File f1 = new File(savePath);        //swf的文件夹
        if (!f1.exists()) {
            f1.mkdirs();
        }

        DocConverters d = new DocConverters() ;
        List<Wordxx> list = new ArrayList<Wordxx>() ;
        Wordxx wordxx = null ;
        for (Map<String, String> map : fileNames) {
            wordxx = new Wordxx() ;
            wordxx.setWordName(map.get("wordName")) ;
            wordxx.setPathName(map.get("pathName")) ;;
            d.ini(map.get("pathName"), savePath) ;
            if(d.conver())
                wordxx.setSwfShowPathName(d.getswfPath()) ;
            list.add(wordxx) ;
        }
        HttpSession session =request.getSession();
        String swfShowPath = d.getswfPath();
        session.setAttribute("swfShowPath",swfShowPath);
        System.out.println(session.getAttribute("swfShowPath")+"---------");
    }

    private  void uploGivethrows (HttpServletRequest request)throws Exception{
        String saveDirectory =request.getRealPath("/")+"upload";
        System.out.println(saveDirectory);
        int maxPostSize = 50 * 1024 * 1024 ;
        DefaultFileRenamePolicy dfp = new DefaultFileRenamePolicy();
        MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,"UTF-8",dfp);
        Enumeration files = multi.getFileNames();
        while (files.hasMoreElements()) {
            String name = (String)files.nextElement();
            File f = multi.getFile(name);
            if(f!=null) {
                String fileName = multi.getFilesystemName(name);
                //获取上传文件的扩展名
                String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
                //文件全路径
                String lastFileName = saveDirectory + "\\" + fileName;
                //获取需要转换的文件名,将路径名中的'\'替换为'/'
                String converfilename = saveDirectory.replaceAll("\\\\", "/") + "/" + fileName;
                System.out.println(converfilename);
                //调用转换类DocConverter,并将需要转换的文件传递给该类的构造方法
                String savePath = request.getSession().getServletContext().getRealPath("")+ "/upload/";
                DocConverter d = new DocConverter(converfilename,savePath);
                //调用conver方法开始转换，先执行doc2pdf()将office文件转换为pdf;再执行pdf2swf()将pdf转换为swf;
                d.conver();
                //调用getswfPath()方法，打印转换后的swf文件路径
                System.out.println(d.getswfPath());
                //生成swf相对路径，以便传递给flexpaper播放器
                String swfpath = d.getswfPath();
                System.out.println(swfpath);
                //将相对路径放入sessio中保存
                HttpSession session =request.getSession();
                String swfShowPath = d.getswfPath();
                session.setAttribute("swfShowPath",swfpath);
                System.out.println(session.getAttribute("swfShowPath")+"---------");
            }
        }
    }
}