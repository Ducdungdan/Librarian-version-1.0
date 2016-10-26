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
public class RentBook {
    private Number idRent;
    private Number idBook;
    private Number idUser;
    private String day_borrow;
    private String day_return;
    private Number rental;

    public Number getIdRent() {
        return idRent;
    }

    public Number getIdBook() {
        return idBook;
    }

    public Number getIdUser() {
        return idUser;
    }

    public String getDay_borrow() {
        return day_borrow;
    }

    public String getDay_return() {
        return day_return;
    }

    public Number getRental() {
        return rental;
    }

    public void setIdRent(Number idRent) {
        this.idRent = idRent;
    }

    public void setIdBook(Number idBook) {
        this.idBook = idBook;
    }

    public void setIdUser(Number idUser) {
        this.idUser = idUser;
    }

    public void setDay_borrow(String day_borrow) {
        this.day_borrow = day_borrow;
    }

    public void setDay_return(String day_return) {
        this.day_return = day_return;
    }

    public void setRental(Number rental) {
        this.rental = rental;
    }
    
}
