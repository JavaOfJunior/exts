package cn.henu.entity;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.*;
import java.util.UUID;
/**
 * Created by junwei on 2016/5/25.
 */
public class DocConverters {
    private String fileString;			//docx、pdf文件路径+文件名称+后缀名
    private String fileName;				//docx、pdf文件绝对路径+文件名称
    private String swfName;				//swf文件绝对路径+文件名称
    private String swfShowName;				//swf文件相对路径+文件名称
    private File docFile;
    private File pdfFile;
    private File swfFile;

    private static final String imageFilePath = "D:/11/111.png";   // 添加水印图片路径
    private String fileWaterName;   //转换水印后的docx、pdf文件绝对路径+文件名称
    private File pdfWaterFile;

    public void ini(String fileString, String swfName) {
        String newName = UUID.randomUUID().toString() ; //随机生成序列号，防止乱码
        this.fileString = fileString;
        this.fileName = fileString.substring(0,fileString.lastIndexOf("."));
        this.fileWaterName =this.fileName+"12";
        this.swfName = swfName + newName ;
        this.swfShowName = "uploads/" + newName + ".swf" ;
        docFile=new File(this.fileString);
        pdfFile=new File(this.fileName+".pdf");
        pdfWaterFile = new File(this.fileWaterName+".pdf");
        swfFile=new File(this.swfName+".swf");
    }

    private void doc2pdf() throws Exception
    {
        if(docFile.exists())
        {
            if(!pdfFile.exists())
            {
                OpenOfficeConnection connection=new SocketOpenOfficeConnection(8100);
                try
                {
                    connection.connect();
                    DocumentConverter converter=new OpenOfficeDocumentConverter(connection);
                    converter.convert(docFile,pdfFile);
                    connection.disconnect();
                    System.out.println("****pdf转换成功，PDF输出："+pdfFile.getPath()+"****");
                    if(docFile.exists())  //删除pdf
                    {
                        docFile.delete();
                    }
                }
                catch(java.net.ConnectException e)
                {
                    e.printStackTrace();
                    System.out.println("****swf转换器异常，openoffice服务未启动！****");
                    throw e;
                }
                catch(com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e)
                {
                    e.printStackTrace();
                    System.out.println("****swf转换器异常，读取转换文件失败****");
                    throw e;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    throw e;
                }
            }
            else
            {
                System.out.println("****已经转换为pdf，不需要再进行转化****");
                throw new RuntimeException("****已经转换为pdf，不需要再进行转化****") ;
            }
        }
        else
        {
            System.out.println("****swf转换器异常，需要转换的文档不存在，无法转换****");
            throw new RuntimeException("****swf转换器异常，需要转换的文档不存在，无法转换****") ;
        }
    }

    private void pdf2swf(File pdfFile) throws Exception
    {
        Runtime r=Runtime.getRuntime();
        if(!swfFile.exists())
        {
            if(pdfFile.exists())
            {   //windows环境转换
                try {
                    Process p=r.exec("D:\\Program Files\\SWFTools\\pdf2swf.exe \""+pdfFile.getPath()+"\"  -o \""+swfFile.getPath()+"\" -T 9  -f");
                    loadStream(p.getInputStream()) ;
                    if(!swfFile.exists())
                    {
                        p.waitFor();			//等待子进程的结束，子进程就是系统调用文件转换这个新进程
                    }
                    p.destroy() ;
                    System.err.println("****swf转换成功，文件输出："
                            + swfFile.getPath() + "****");
//                    if (pdfFile.exists()) {
//                        pdfFile.delete();
//                    }
                } catch (Exception e) {
                    throw new RuntimeException("****pdf转换为swf异常****") ;
                }
            }
            else {
                throw new RuntimeException("****pdf不存在，无法转换****") ;
            }
        }
        else {
            throw new RuntimeException("****swf已存在不需要转换****") ;
        }
    }


    private String loadStream(InputStream in) throws IOException
    {
        int ptr=0;
        in=new BufferedInputStream(in);
        StringBuffer buffer=new StringBuffer();

        while((ptr=in.read())!=-1)
        {
            buffer.append((char)ptr);
        }
        return buffer.toString();
    }
    //文档转换
    public boolean conver() throws Exception
    {
//        if(pdfFile.exists())
//            pdfFile.delete() ;
        if(swfFile.exists())
            swfFile.delete() ;

        try {
           // doc2pdf();         //转换成pdf
            System.out.println("fileString:"+fileString);
            PdfReader pdfReader = new PdfReader(fileString);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream( fileWaterName+".pdf"));
            addWatermark(pdfStamper, "请注意保密！");
            pdfStamper.close();
            pdfReader.close();
            pdf2swf(pdfWaterFile); //把需要转换的pdf转换成swf
        } catch (Exception e) {
            throw e ;
        }
        if(swfFile.exists())
        {
            return true;
        }
        else {
            return false;
        }
    }
    //得到swf的路径
    public String getswfPath()
    {
        return this.swfShowName ;
    }

    //在pdf上添加水印
    //inputFile 原始文件
    //outputFile 水印输出文件
    //warterMarkName 水印名
    private static void waterMark(String inputFile, String outputFile,String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            // Get the PdfStamper object
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream( outputFile));
            // 设置密码
            //stamper.setEncryption(userPassWord.getBytes(), ownerPassWord.getBytes(), permission, false);
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            int total = reader.getNumberOfPages() + 1;
//            Image image = Image.getInstance(imageFilePath);
//            image.setAbsolutePosition(200, 400);
            PdfContentByte under;
            int j = waterMarkName.length();
            char c = 0;
            int rise = 0;
            for (int i = 1; i < total; i++) {
                rise = 500;
                under = stamper.getUnderContent(i);
                // 添加图片
//                under.addImage(image);
                under.beginText();
                under.setColorFill(BaseColor.CYAN);
                under.setFontAndSize(base, 30);
                // 设置水印文字字体倾斜 开始
                if (j >= 15) {
                    under.setTextMatrix(200, 0);
                    for (int k = 0; k < j; k++) {
                        under.setTextRise(rise);
                        c = waterMarkName.charAt(k);
                        under.showText(c + "");
                        rise -= 20;
                    }
                } else {
                    under.setTextMatrix(180, 0);
                    for (int k = 0; k < j; k++) {
                        under.setTextRise(rise);
                        c = waterMarkName.charAt(k);
                        under.showText(c + "");
                        rise -= 18;
                    }
                }
                // 字体设置结束
                under.endText();
                // 画一个圆
//                under.ellipse(250, 450, 350, 550);
//                under.setLineWidth(1f);
//                under.stroke();
            }
            stamper.close();
            System.out.println("****pdf转换成水印成功"+outputFile+"*******");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addWatermark(PdfStamper pdfStamper, String waterMarkName) throws Exception {
        PdfContentByte content = null;
        BaseFont base = null;
        com.itextpdf.text.Rectangle pageRect = null;
        PdfGState gs = new PdfGState();
//        Image image = Image.getInstance(imageFilePath);
//        image.setAbsolutePosition(200, 400);
        try {
            // 设置字体
            base = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (base == null || pdfStamper == null) {
                return;
            }
            // 设置透明度为0.4
            gs.setFillOpacity(0.4f);
            gs.setStrokeOpacity(0.4f);
            int toPage = pdfStamper.getReader().getNumberOfPages();
            for (int i = 1; i <= toPage; i++) {
                pageRect = pdfStamper.getReader().
                        getPageSizeWithRotation(i);
                // 计算水印X,Y坐标
                float x = pageRect.getWidth() / 2;
                float y = pageRect.getHeight() / 2;
                //获得PDF最顶层
                content = pdfStamper.getOverContent(i);
                content.saveState();
//                content.addImage(image);
                content.setGState(gs);
                content.beginText();
                content.setColorFill(BaseColor.RED);  //设置颜色
                content.setFontAndSize(base, 60);
                // 水印文字成45度角倾斜
                content.showTextAligned(Element.ALIGN_CENTER,waterMarkName,x,y,45);
                content.endText();
            }
            pdfStamper.close();
            System.out.println("PDF水印添加成功");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            content = null;
            base = null;
            pageRect = null;
        }
    }


    public static void main(String[] args)throws Exception{
//        PdfReader pdfReader = new PdfReader("D:\\11\\chap5权限.pdf");
//        // Get the PdfStamper object
//        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream( "d:\\11\\itext1-demo22.pdf"));
//        addWatermark(pdfStamper, "公司内部文件，请注意保密！");
//
//        pdfStamper.close();
//   waterMark("d:/11/12.pdf","d:/11/p1roject.pdf","主要保111存");
//
//        DocConverters d= new DocConverters();
//      d.ini("D:/11/project.pdf","D:/11/");
      // d.pdf2swf();
    }
}
