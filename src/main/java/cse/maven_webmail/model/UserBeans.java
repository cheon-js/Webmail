/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

/**
 *
 * @author hyun
 */
public class UserBeans {
    private String username;
    private String email;
    private String phone;
    
    public UserBeans(){}
    public UserBeans(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String geteMail() {
        return email;
    }

    public void seteMail(String eMail) {
        this.email = eMail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
