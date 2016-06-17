package cn.henu.controller;


import cn.henu.entity.CommonUtil;
import cn.henu.entity.DocConverters;
import cn.henu.entity.Wordxx;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("upload")
public class UploadController {

    @RequestMapping("uploads")
    public void upload(HttpServletRequest request)throws Exception{
      uplopMi(request);
    }
    private  void uplopMi(HttpServletRequest request)throws  Exception{
        //上传原文件文件夹
        String path=request.getSession().getServletContext().getRealPath("") + "\\generic\\web\\origion\\";
        File f = new File(path) ;
        if(!f.exists()) f.mkdirs() ;

        //添加水印的文件夹
        String waterFileP=request.getSession().getServletContext().getRealPath("") + "\\generic\\web\\waterFile\\";
        File wf= new File(waterFileP);
        if(!wf.exists()) wf.mkdirs();

        List<Map<String, String>> fileNames = CommonUtil.uploadWordFile(request, path) ;   //文档保存

        String savePath = request.getSession().getServletContext().getRealPath("")+"\\generic\\web\\";
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
            d.ini(map.get("pathName"), savePath,waterFileP,map.get("wordName")) ;
            if(d.conver())
                wordxx.setSwfShowPathName(d.getswfPath()) ;
            list.add(wordxx) ;
        }
    }

    /**
     * 返回页面并构造文件名
     * @param url
     * @return
     */
    @RequestMapping("list")
    public ModelAndView jsp(String url,HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/generic/web/viewPdf.jsp");
        url= "../generic/web/"+"waterFile/"+url.substring(0,url.lastIndexOf("."))+"-water.pdf";
        System.out.println("url:"+url);
        HttpSession session =request.getSession();
        session.setAttribute("url",url);
        System.out.println("sessionUrl:"+session.getAttribute("url"));
        return mv;
    }
}