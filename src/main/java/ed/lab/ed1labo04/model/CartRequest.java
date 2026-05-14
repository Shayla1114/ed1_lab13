package ed.lab.ed1labo04.model;

import java.util.List;

public class CartRequest {
    private List<ItemRequest> items;

    public List<ItemRequest> getItems() { return items; }
    public void setItems(List<ItemRequest> items) { this.items = items; }
}