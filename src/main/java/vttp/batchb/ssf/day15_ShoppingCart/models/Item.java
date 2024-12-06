package vttp.batchb.ssf.day15_ShoppingCart.models;

public class Item {

    private String item;
    private Integer quantity;

    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String itemString(){
        String result = this.getItem() + ":" + this.getQuantity();
        return result;
    }
}
