package cn.henu.util;

import org.apache.pdfbox.util.PDFMergerUtility;

import java.io.File;
import java.io.IOException;

/**
 * Created by junwei on 2016/6/17.
 * 合并PDF文件
 */
public class MergeFile {
    private static String[] getFiles(String folder) throws IOException
    {
        File _folder = new File(folder);
        String[] filesInFolder;

        if(_folder.isDirectory())
        {
            filesInFolder = _folder.list();
            return filesInFolder;
        }
        else
        {
            throw new IOException("Path is not a directory");
        }
    }

    public static void main(String[] args) throws Exception {

        PDFMergerUtility mergePdf = new PDFMergerUtility();

        String folder = "D:/11";
        String destinationFileName = "mergedTest-a.pdf";

//        String[] filesInFolder = getFiles(folder);
//
//        for(int i = 0; i < filesInFolder.length; i++)
//            mergePdf.addSource(folder + File.separator + filesInFolder[i]);
        mergePdf.addSource(folder+File.separator+ "04Servlet1.pdf");
        mergePdf.addSource(folder+File.separator+ "345.pdf");

        mergePdf.setDestinationFileName(folder + File.separator + destinationFileName);
        mergePdf.mergeDocuments();

        System.out.print("合并完成！");

    }
}
