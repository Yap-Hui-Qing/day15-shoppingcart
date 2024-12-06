package vttp.batchb.ssf.day15_ShoppingCart.models;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class User {

    @NotEmpty(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    @Size(min = 3, message = "Username cannot be less than 3 characters")
    private String username;
    private List<String> carts;

    public List<String> getCarts() {
        return carts;
    }

    public void setCarts(List<String> carts) {
        this.carts = carts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
