package com.ahoy.parsetest;

import com.ahoy.parser.util.GetStackElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

@WebServlet("/saveimgbyurl")
public class SaveImageByUrl extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SaveImageByUrl.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
			
		BufferedReader br =null;
		PrintWriter out = null;
		String resp = null;
		try{
			out = response.getWriter();
			br = new BufferedReader(new FileReader("C:\\Users\\Vivek\\Desktop\\gmax.txt"));
			
			String line = null;
			int i = 0;
			while((line=br.readLine())!=null){
				String splt[]=line.split("~");
				String imageName = splt[0].trim().replaceAll("\\|", " ");			
				String url = splt[1].trim();
				imageName = imageName.trim().replaceAll(" ", "-").replaceAll("\"", "-").replaceAll("/", "-").replaceAll("\\*", "-")+url.substring(url.lastIndexOf("."));
				saveImage(url, "F:\\GrocermaxImages\\"+imageName);
				logger.info("[SaveImageByUrl][process] "+(++i)+" | "+imageName+" | "+url);
				
			}
			resp = "Finished";
		}catch(Exception e){
			resp = "Internal Error";
			logger.error("[SaveImageByUrl][process] Exception: "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			out.println(resp);
			br.close();
			logger.info("[SaveImageByUrl][process] resp: "+resp);
		}
	}
	
	public void saveImage(String imageUrl, String destinationFile){
		InputStream is = null;
		OutputStream os=null;
		try{
			URL url = new URL(imageUrl);
			is = url.openStream();
			os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			is.close();
			os.close();	
		}catch(Exception e){
			logger.error("[SaveImageByUrl][saveImage] Exception: "+GetStackElements.getRootCause(e, getClass().getName()));
		}		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
