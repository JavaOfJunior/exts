package cn.henu.entity;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

public class CommonUtil {
    //上传文档
    public static List<Map<String, String>> uploadWordFile(HttpServletRequest request, String path){

        DiskFileItemFactory fac = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(fac);
        upload.setHeaderEncoding("utf-8");
        List fileList = null;
        try {
            fileList = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            ex.printStackTrace() ;
        }
        Iterator<FileItem> it = fileList.iterator();
        String name = "";
        String wordName = "";
        String pathName = "";
        List<Map<String, String>> fileNames = new ArrayList<Map<String, String>>() ;
        Map<String, String> fileName = null ;
        while (it.hasNext()) {			// 遍历所有的请求项
            FileItem item = it.next();

            if (!item.isFormField()) {		// 不是FormField
                name = item.getName();
                if (name == null || name.trim().equals("")) {
                    continue;
                }
                pathName = path + File.separator + name ;
                File saveFile = new File(pathName);
                try {
                    if(!saveFile.exists())
                        saveFile.createNewFile() ;
                    item.write(saveFile);
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
                wordName = saveFile.getName() ;
                fileName = new HashMap<String, String>();
                fileName.put("wordName", wordName);
                fileName.put("pathName", pathName);
                fileNames.add(fileName) ;
            }
        }
        return fileNames;
    }
}
