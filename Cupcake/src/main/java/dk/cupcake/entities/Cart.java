// Package
package dk.cupcake.entities;

// Imports
import java.util.ArrayList;
import java.util.List;

public class Cart {

    // Attributes
    private final List<CartItem> items = new ArrayList<>();

    // _______________________________________________________________

    public List<CartItem> getItems() {
        return items;
    }

    // _______________________________________________________________

    public void addItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.isMergeableWith(item)) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    // _______________________________________________________________

    public double getTotal() {
        double total = 0.0;
        for (CartItem item : items) total += item.getLineTotal();
        return total;
    }

    // _______________________________________________________________

    public int getItemCount() {
        int count = 0;
        for (CartItem item : items) count += item.getQuantity();
        return count;
    }

} // Cart end