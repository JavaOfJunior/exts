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
        String waterFilePath ="/uploads/"+d.getFileWaterName();

        session.setAttribute("swfShowPath",swfShowPath);
        session.setAttribute("waterFilePath",waterFilePath);
        System.out.println(session.getAttribute("swfShowPath")+"---------");
    }

}