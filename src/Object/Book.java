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
public class Book {
    private Number idBook;
    private String name;
    private String author;
    private String company;
    private Number year;
    private String type;
    private String country;
    private Number number;
    private Number value;
    private Number remaining;
    private Number editor;
    private byte[] image;
    
    public void setBook(Number idBook, String name, String author, String company, Number year, String type, String country, Number number, Number value, Number remaining, Number editor) {
        this.idBook = idBook;
        this.name = name;
        this.author = author;
        this.company = company;
        this.year = year;
        this.type = type;
        this.country = country;
        this.number = number;
        this.value = value;
        this.remaining = remaining;
        this.editor = editor;
    }
    
    public void setIdBook(Number idBook) {
        this.idBook = idBook;
    }
    
    public Number getIdBook() {
        return this.idBook;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setYear(Number year) {
        this.year = year;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public void setRemaining(Number remaining) {
        this.remaining = remaining;
    }

    public void setEditor(Number editor) {
        this.editor = editor;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public String getCompany() {
        return this.company;
    }
    
    public Number getYear() {
        return this.year;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    public Number getNumber() {
        return this.number;
    }
    
    public Number getValue() {
        return this.value;
    }
    
    public Number getRemaining() {
        return this.remaining;
    }
    
    public Number getEditor() {
        return this.editor;
    }
    
    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return this.image;
    }
}
