package com.zw.miaofuspd.file.controller;

import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class FileController {
    @Autowired
    private ISystemDictService iSystemDictService;

    @RequestMapping("/img/get")
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
            response.setContentType("text/html;charset=utf-8");
            toClient.write("无法打开!");
            toClient.close();
        }
    }

    @RequestMapping("/file/get")
    public void getFile(String saveAddress,HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{
            String fileBasePath = iSystemDictService.getInfo("file.path");
            java.io.InputStream in = new java.io.FileInputStream(fileBasePath+saveAddress);
        	if(saveAddress.endsWith(".html")){
                if ( in != null )
                {
                    InputStreamReader is=new InputStreamReader(in);
                    PrintWriter out=response.getWriter();
                    response.setContentType("text/html;charset=utf-8");
                    BufferedReader br=new BufferedReader(is);
                    String txtLine=null;
                    while((txtLine=br.readLine())!=null)
                    {
                        out.println(txtLine);
                    }

                    br.close();
                    out.close();
                }
                in.close();
            }else{
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

        }
        catch(IOException e) //错误处理
        {
            e.printStackTrace();
            PrintWriter toClient = response.getWriter(); //得到向客户端输出文本的对象
            response.setContentType("text/html;charset=utf-8");
            toClient.write("无法打开资源!");
            toClient.close();
        }
    }


}
