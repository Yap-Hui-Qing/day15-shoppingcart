package vttp.batchb.ssf.day15_ShoppingCart.models;

import java.util.List;

public class Cart {
    
    private String cartid;

    @Override
    public String toString() {
        return "Cart [cartid=" + cartid + ", count=" + count + ", items=" + items + "]";
    }
    
    public String getCartid() {
        return cartid;
    }
    public void setCartid(String cartid) {
        this.cartid = cartid;
    }

    private Integer count;

    public void setCount(Integer count) {
        this.count = count;
    }
    public Integer getCount() {
        return count;
    }

    private List<String> items;

    public List<String> getItems() {
        return items;
    }
    public void setItems(List<String> items) {
        this.items = items;
    }

    public String cartString(){
        String result = this.getCartid() + ":" + this.getCount();
        return result;
    }

}
