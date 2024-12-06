package vttp.batchb.ssf.day15_ShoppingCart.repositories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import vttp.batchb.ssf.day15_ShoppingCart.models.Cart;
import vttp.batchb.ssf.day15_ShoppingCart.models.Item;

@Repository
public class ItemRepository {
    
    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    // add items to a cart in redis
    // sadd abc123 apple:2
    // sadd abc123 orange:3
    // sadd abc123 grapes:5
    public void insertItems(Cart cart, String item, int quantity){

        SetOperations<String, String> setOps = template.opsForSet();

        setOps.add(cart.getCartid(), item + ":" + quantity);

        List<String> items = cart.getItems();
        if (items == null){
            items = new LinkedList<>();
        }
        items.add(item + ":" + quantity);
        cart.setItems(items);
    }

    // retrieve all items in a cart
    // smembers abc123
    public Optional<Set<String>> getItemsInCart(String cartid){

        SetOperations<String, String> setOps = template.opsForSet();

        Set<String> items = setOps.members(cartid);

        if (items.isEmpty() || items == null)
            return Optional.empty();

        return Optional.of(items);
    }

    public Optional<Cart> getCartById(String cartid){
        SetOperations<String, String> setOps = template.opsForSet();
        Set<String> items = setOps.members(cartid);

        if (items == null || items.isEmpty())
            return Optional.empty();

        // construct the cart object
        Cart cart = new Cart();
        cart.setCartid(cartid);
        cart.setItems(new ArrayList<>(items));
        
        // calculate count
        int count = items.stream()
                    .mapToInt(item -> Integer.parseInt(item.split(":")[1]))
                    .sum();
        cart.setCount(count);

        return Optional.of(cart);
    }

}
