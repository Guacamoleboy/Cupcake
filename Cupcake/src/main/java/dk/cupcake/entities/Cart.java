// Package
package dk.cupcake.entities;

// Imports
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.isMergeableWith(item)) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public double getTotal() {
        double total = 0.0;
        for (CartItem item : items) total += item.getLineTotal();
        return total;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : items) count += item.getQuantity();
        return count;
    }
}


