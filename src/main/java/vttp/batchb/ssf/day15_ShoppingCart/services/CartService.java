package vttp.batchb.ssf.day15_ShoppingCart.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batchb.ssf.day15_ShoppingCart.models.Cart;
import vttp.batchb.ssf.day15_ShoppingCart.models.User;
import vttp.batchb.ssf.day15_ShoppingCart.repositories.CartRepository;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepo;

    public String addCartToUser(String username, Cart cart){

        String cartid = UUID.randomUUID().toString().substring(0,6);
        cart.setCartid(cartid);
        cart.setCount(0);

        cartRepo.insertCartForUser(username, cart);
        return cartid;
    }

    public Optional<Set<String>> getCartsForUser(String username){
        return cartRepo.getCartsForUser(username);
    }

    public void updateCartForUser(String username, Cart cart){

        cartRepo.updateCartForUser(username, cart);
        
    }


}
