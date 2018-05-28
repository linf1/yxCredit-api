package com.zw.miaofuspd.file;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class FileController {

    @RequestMapping("/img/getImg.html")
    public void getimg(String saveAddress , HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            saveAddress = "";//Constants.UPLOAD_FILE_PATH+saveAddress;
            FileInputStream hFile = new FileInputStream(saveAddress); // 以byte流的方式打开文件 d:\1.gif
            int i=hFile.available(); //得到文件大小
            byte data[]=new byte[i];
            hFile.read(data); //读数据
            hFile.close();
            response.setContentType("image/*"); //设置返回的文件类型
            OutputStream toClient=response.getOutputStream(); //得到向客户端输出二进制数据的对象
            toClient.write(data); //输出数据
            toClient.close();
        }
        catch(IOException e) //错误处理
        {
            e.printStackTrace();
            PrintWriter toClient = response.getWriter(); //得到向客户端输出文本的对象
            response.setContentType("text/html;charset=gb2312");
            toClient.write("无法打开!");
            toClient.close();
        }
    }

    @RequestMapping("/file/get")
    public void getFile(String saveAddress,HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
        	/*saveAddress = URLEncoder.encode(saveAddress, "utf-8");*/
        	/*saveAddress = URLDecoder.decode(saveAddress, "utf-8");*/

            java.io.InputStream in = null;

            // 方式1: 以流方式读取视频文件. pic.jsp?src=c:\img\test.rm
            //if ( from.equals("") || from.equals("file") )
            //{
            in = new java.io.FileInputStream(saveAddress);
            //}
            //else // 方式2: 从数据库中读取流. pic.jsp?src=c:\img\232
            //{
            //    java.util.List list = DbWrapper.executeQuery(
            //        "select CONTENT from IMAGE where id = '" + request.getParameter("src").toString() + "'"
            //        );
            //
            //    if ( list.size() > 0 )
            //    {
            //        in = (InputStream)(((java.util.Map)list.get(0)).get("CONTENT"));
            //    }
            //}

            if ( in != null )
            {
                javax.servlet.ServletOutputStream OStream=response.getOutputStream();
                byte[] b = new byte[1024];
                int len=0;
                while( ( len = in.read(b)) != -1 )
                {
                    OStream.write(b);
                }

                OStream.close();
            }
            in.close();
        }
        catch(IOException e) //错误处理
        {
            e.printStackTrace();
            PrintWriter toClient = response.getWriter(); //得到向客户端输出文本的对象
            response.setContentType("text/html;charset=gb2312");
            toClient.write("无法打开资源!");
            toClient.close();
        }
    }


}
