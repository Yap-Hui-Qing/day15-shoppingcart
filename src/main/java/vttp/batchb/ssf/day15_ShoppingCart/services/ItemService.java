package vttp.batchb.ssf.day15_ShoppingCart.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batchb.ssf.day15_ShoppingCart.models.Cart;
import vttp.batchb.ssf.day15_ShoppingCart.models.Item;
import vttp.batchb.ssf.day15_ShoppingCart.repositories.ItemRepository;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepo;

    public void insertItems(Cart cart, String item, int quantity){

        itemRepo.insertItems(cart, item, quantity);

    }

    public Optional<Set<String>> getItems(String cartid){
        return itemRepo.getItemsInCart(cartid);
    }

    public Optional<Cart> getCart(String cartid){
        return itemRepo.getCartById(cartid);
    }


}
