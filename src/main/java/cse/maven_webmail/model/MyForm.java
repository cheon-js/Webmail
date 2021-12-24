/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author songe
 */
public class MyForm {

    private HttpServletRequest request;
    private String adname = null;
    private String admemo = null;
    private String delname = null;
    private String upmemo = null;
    private String upname = null;
    private String group = null;
    private String searchname=null;

    public String getSearchname() {
        return searchname;
    }

    public void setSearchname(String searchname) {
        this.searchname = searchname;
    }
    

    public String getUpmemo() {
        return upmemo;
    }

    public void setUpmemo(String upmemo) {
        this.upmemo = upmemo;
    }

    public String getUpname() {
        return upname;
    }

    public void setUpname(String upname) {
        this.upname = upname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public MyForm(HttpServletRequest request) {
        this.request = request;
    }

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public String getAdmemo() {
        return admemo;
    }

    public void setAdmemo(String admemo) {
        this.admemo = admemo;
    }

    public String getDelname() {
        return delname;
    }

    public void setDelname(String delname) {
        this.delname = delname;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void parse() {
        try {
            request.setCharacterEncoding("UTF-8");
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // 2. 팩토리 제한사항 설정
            diskFactory.setSizeThreshold(10 * 1024 * 1024);
            //diskFactory.setRepository(new File(this.uploadTargetDir));
            // 3. 파일 업로드 핸들러 생성
            ServletFileUpload upload = new ServletFileUpload(diskFactory);
            List fileItems = upload.parseRequest(request);
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (fi.isFormField()) {  // 5. 폼 필드 처리
                    String fieldName = fi.getFieldName();
                    String item = fi.getString("UTF-8");

                    if (fieldName.equals("ad_name")) {
                        setAdname(item);  // 200102 LJM - @ 이후의 서버 주소 제거
                    } else if (fieldName.equals("ad_memo")) {
                        setAdmemo(item);
                    } else if (fieldName.equals("group")) {
                        setGroup(item);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("MyFormPaser.parse() : exception = " + ex);
        }
    }
    public void delparse() {
        try {
            request.setCharacterEncoding("UTF-8");
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // 2. 팩토리 제한사항 설정
            diskFactory.setSizeThreshold(10 * 1024 * 1024);
            //diskFactory.setRepository(new File(this.uploadTargetDir));
            // 3. 파일 업로드 핸들러 생성
            ServletFileUpload upload = new ServletFileUpload(diskFactory);
            List fileItems = upload.parseRequest(request);
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (fi.isFormField()) {  // 5. 폼 필드 처리
                    String fieldName = fi.getFieldName();
                    String item = fi.getString("UTF-8");

                    if (fieldName.equals("del_name")) {
                        setDelname(item);  // 200102 LJM - @ 이후의 서버 주소 제거
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("MyFormPaser.delparse() : exception = " + ex);
        }
    }
    public void upparse() {
        try {
            request.setCharacterEncoding("UTF-8");
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // 2. 팩토리 제한사항 설정
            diskFactory.setSizeThreshold(10 * 1024 * 1024);
            //diskFactory.setRepository(new File(this.uploadTargetDir));
            // 3. 파일 업로드 핸들러 생성
            ServletFileUpload upload = new ServletFileUpload(diskFactory);
            List fileItems = upload.parseRequest(request);
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (fi.isFormField()) {  // 5. 폼 필드 처리
                    String fieldName = fi.getFieldName();
                    String item = fi.getString("UTF-8");

                    if (fieldName.equals("up_name")) {
                        setUpname(item);  // 200102 LJM - @ 이후의 서버 주소 제거
                    }else if (fieldName.equals("up_memo")) {
                        setUpmemo(item);
                    }else if (fieldName.equals("group")) {
                        setGroup(item);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("MyFormPaser.upparse() : exception = " + ex);
        }
    }
    public void searchname(){
    try{request.setCharacterEncoding("UTF-8");
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // 2. 팩토리 제한사항 설정
            diskFactory.setSizeThreshold(10 * 1024 * 1024);
            //diskFactory.setRepository(new File(this.uploadTargetDir));
            // 3. 파일 업로드 핸들러 생성
            ServletFileUpload upload = new ServletFileUpload(diskFactory);
            List fileItems = upload.parseRequest(request);
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (fi.isFormField()) {  // 5. 폼 필드 처리
                    String fieldName = fi.getFieldName();
                    String item = fi.getString("UTF-8");

                    if (fieldName.equals("search_name")) {
                        setSearchname(item);  // 200102 LJM - @ 이후의 서버 주소 제거
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("MyFormPaser.upparse() : exception = " + ex);
        }
    }
    
}
