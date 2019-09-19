package com.candidjava.servlet.upload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String UPLOAD_DIRECTORY = System.getProperty("UPLOAD_DIRECTORY");
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(ServletFileUpload.isMultipartContent(request)){
            try {
            	String fname = null;
            	String fsize = null;
            	String ftype = null;
                String filename = null;
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        fname = new File(item.getName()).getName();
                        fsize = new Long(item.getSize()).toString();
                        ftype = item.getContentType();


                        //System.out.println("UPLOAD_DIRECTORY:" + UPLOAD_DIRECTORY + File.separator + fname);
                        if (UPLOAD_DIRECTORY == null || UPLOAD_DIRECTORY.isEmpty() ){
                            UPLOAD_DIRECTORY="/java/home/ebx/ebxRepository/archives/";
                            System.out.println("UPLOAD_DIRECTORY: " + UPLOAD_DIRECTORY);
                        }
                        item.write( new File(UPLOAD_DIRECTORY + File.separator + fname));
                    }
                    else
                    {
                        String fieldname = item.getFieldName();
                        String fieldvalue = item.getString();
                        if (fieldname.equals("filename")){
                            filename=fieldvalue;
                        }
                    }
                    if ( filename != null && !filename.isEmpty() ){
                        System.out.println("Moving from "+ UPLOAD_DIRECTORY + File.separator + fname + " to " + UPLOAD_DIRECTORY + File.separator + filename);
                        File sourceFile= new File(UPLOAD_DIRECTORY + File.separator + fname);
                        File destFile=new File(UPLOAD_DIRECTORY + File.separator + filename);
                        sourceFile.renameTo(destFile);
                        fname=filename;
                    }
                }
               //File uploaded successfully
               request.setAttribute("message", "File Uploaded Successfully");
               request.setAttribute("name", fname);
               request.setAttribute("size", fsize);
               request.setAttribute("type", ftype);
            } catch (Exception ex) {
               request.setAttribute("message", "File Upload Failed due to " + ex.getMessage());
            }          
         
        }else{
            request.setAttribute("message","Sorry this Servlet only handles file upload request");
        }
    
        request.getRequestDispatcher("/result.jsp").forward(request, response);
     
    }		
		// TODO Auto-generated method stub
	}