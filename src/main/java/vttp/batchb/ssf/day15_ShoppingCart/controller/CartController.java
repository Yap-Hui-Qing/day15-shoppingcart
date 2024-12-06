package vttp.batchb.ssf.day15_ShoppingCart.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.batchb.ssf.day15_ShoppingCart.models.Cart;
import vttp.batchb.ssf.day15_ShoppingCart.models.User;
import vttp.batchb.ssf.day15_ShoppingCart.services.CartService;
import vttp.batchb.ssf.day15_ShoppingCart.services.ItemService;

@Controller
@RequestMapping
public class CartController {

    private static final Logger logger = Logger.getLogger(CartController.class.getName());

    @Autowired
    private CartService cartSvc;
    @Autowired
    private ItemService itemSvc;
    
    @GetMapping(path = {"/", "/index.html"})
    public String getIndex(Model model){

        model.addAttribute("user", new User());
        return "index";

    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute User user, BindingResult bindings, Model model, HttpSession sess){

        if (bindings.hasErrors()){
            return "index";
        }

        String username = (String) sess.getAttribute("username");
        if (username == null){
            username = user.getUsername();
            sess.setAttribute("username", username);
        }

        // retrieve cart ids
        Optional<Set<String>> opt = cartSvc.getCartsForUser(username);

        // check if there are any carts
        Set<String> carts = opt.orElse(Collections.emptySet());

        model.addAttribute("username", username);
        model.addAttribute("carts", carts);

        return "cart-list";

    }

    @GetMapping("/login")
    public String getCarts(Model model, HttpSession sess){

        String username = (String) sess.getAttribute("username");

        // retrieve cart ids
        Optional<Set<String>> opt = cartSvc.getCartsForUser(username);
        Set<String> carts = opt.orElse(Collections.emptySet());

        model.addAttribute("username", username);
        model.addAttribute("carts", carts);
        return "cart-list";
    }
    

    @PostMapping("/login/addCart")
    public String postCart(Model model, HttpSession sess){

        Cart cart = new Cart();
        sess.setAttribute("currentcart", cart);

        String username = (String) sess.getAttribute("username");
        model.addAttribute("username", username);

        String cartid = cartSvc.addCartToUser(username, cart);
        model.addAttribute("cartid", cartid);
        sess.setAttribute("currentcartid", cartid);
    
        // retrieve cart items
        Optional<Set<String>> opt = itemSvc.getItems(cartid);
        Set<String> items = opt.orElse(Collections.emptySet());
        model.addAttribute("items", items);

        return "cart-info";
    }

    @GetMapping("/login/{cartid}")
    public String getCartItems(@PathVariable String cartid, Model model, HttpSession sess){
       
        String username = (String) sess.getAttribute("username");
        model.addAttribute("username", username);

        sess.setAttribute("currentcartid", cartid);

        Optional<Set<String>> opt = itemSvc.getItems(cartid);
        Set<String> items = opt.orElse(Collections.emptySet());
        model.addAttribute("items", items);

        model.addAttribute("cartid", cartid);

        return "cart-info";
    }

    @PostMapping("/login/addItem")
    public String addItem(Model model, @RequestBody MultiValueMap<String, String> form, HttpSession sess){

        String cartid = form.getFirst("cartid");
        // get items in cart
        Optional<Cart> optcart = itemSvc.getCart(cartid);
        if (optcart.isPresent()){
            Cart cart = optcart.get();

            sess.setAttribute("cart", cart);
            itemSvc.insertItems(cart, form.getFirst("item"), Integer.parseInt(form.getFirst("quantity")));
        
            String username = (String) sess.getAttribute("username");
            model.addAttribute("username", username);

            List<String> items = cart.getItems();

        // String cartid = currentcart.getCartid();
        // logger.info("cartid: %s\n".formatted(cartid));
        // Optional<Set<String>> opt = itemSvc.getItems(cartid);
        // Set<String> items = opt.orElse(Collections.emptySet());
        
            model.addAttribute("items", items);
            
        } else {
            Cart cart = new Cart();
            cart.setCartid(cartid);
            cart.setCount(0);
            cart.setItems(new LinkedList<>());

            sess.setAttribute("cart", cart);
            itemSvc.insertItems(cart, form.getFirst("item"), Integer.parseInt(form.getFirst("quantity")));
        
            String username = (String) sess.getAttribute("username");
            model.addAttribute("username", username);

            List<String> items = cart.getItems();

            model.addAttribute("items", items);
        }

        model.addAttribute("cartid", cartid);
        return "cart-info";
    }

    @GetMapping("/saveCart")
    public String saveCart(Model model, HttpSession sess){

        String cartid = (String) sess.getAttribute("currentcartid");
        Optional<Cart> optcart = itemSvc.getCart(cartid);
        if (optcart.isPresent()){
            Cart cart = optcart.get();

            String username = (String) sess.getAttribute("username");

            // update cart info
            cartSvc.updateCartForUser(username, cart);

            // retrieve cart ids
            Optional<Set<String>> opt = cartSvc.getCartsForUser(username);
            Set<String> carts = opt.orElse(Collections.emptySet());

            model.addAttribute("username", username);
            model.addAttribute("carts", carts);
        } else {
            Cart cart = new Cart();
            cart.setCartid(cartid);
            cart.setCount(0);
            cart.setItems(new LinkedList<>());

            String username = (String) sess.getAttribute("username");

            // update cart info
            cartSvc.updateCartForUser(username, cart);

            // retrieve cart ids
            Optional<Set<String>> opt = cartSvc.getCartsForUser(username);
            Set<String> carts = opt.orElse(Collections.emptySet());

            model.addAttribute("username", username);
            model.addAttribute("carts", carts);
        }

        sess.removeAttribute("currentcartid");
        
        return "cart-list";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession sess, Model model){
        sess.invalidate();
        model.addAttribute("user", new User());
        return "index";
    }
}
