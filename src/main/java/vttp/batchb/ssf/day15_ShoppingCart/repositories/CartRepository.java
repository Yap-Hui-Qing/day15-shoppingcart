package vttp.batchb.ssf.day15_ShoppingCart.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import vttp.batchb.ssf.day15_ShoppingCart.models.Cart;
import vttp.batchb.ssf.day15_ShoppingCart.models.User;

@Repository
public class CartRepository {
    
    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    // add a new cart to a user's set of carts
    // sadd fred abc123:7
    // sadd fred xyz456:10
    public void insertCartForUser(String username, Cart cart){

        SetOperations<String, String> setOps = template.opsForSet();

        setOps.add(username, cart.getCartid() + ":" + cart.getCount());
    }

    // smembers fred
    public Optional<Set<String>> getCartsForUser(String username){

        SetOperations<String, String> setOps = template.opsForSet();

        Set<String> carts = setOps.members(username);

        if (carts.isEmpty() || carts == null)
            return Optional.empty();

        return Optional.of(carts);
    }

    public int updateCartForUser(String username, Cart cart){

        SetOperations<String, String> setOps = template.opsForSet();

        // retrieve the old count 
        // srem abc123:7
        Optional<Set<String>> optCarts = getCartsForUser(username);
        if (optCarts.isPresent()){
            Set<String> userCarts = optCarts.get();
            for (String entry : userCarts){
                if (entry.startsWith(cart.getCartid() + ":")){
                    setOps.remove(username, entry);
                    break;
                }
            }
        }

        List<String> items = cart.getItems();
        int itemCount = 0;
        for (String item : items){
            itemCount += Integer.parseInt(item.split(":")[1]);
        }

        cart.setCount(itemCount);

        setOps.add(username, cart.getCartid() + ":" + itemCount);

        return itemCount;
    }
}
