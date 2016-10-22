/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

/**
 *
 * @author Duc Dung Dan
 */
public class User {
    private Number idUser;
    private String name;
    private Boolean sex;
    private String birth;
    private String adress;
    private String city;
    private String email;
    private Boolean admin;
    private byte[] image;
    
    public void setIdUser(Number idUser) {
        this.idUser = idUser;
    }
    
    public Number getIdUser() {
        return this.idUser;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setSex(Boolean sex) {
        this.sex = sex;
    }
    
    public Boolean getSex() {
        return this.sex;
    }
    
    public void setBirth(String birth) {
        this.birth = birth;
    }
    
    public String getBirth() {
        return this.birth;
    }
    
    public void setAdress(String adress) {
        this.adress = adress;
    }
    
    public String getAdress() {
        return this.adress;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    
    public Boolean getAdmin() {
        return this.admin;
    }
    
    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return this.image;
    }
}
