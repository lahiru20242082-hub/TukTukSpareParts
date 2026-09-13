import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(SparePart part, int quantity) {
        for (CartItem item : items) {
            if (item.getPart().getId().equals(part.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(part, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;
        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (CartItem item : items) {
            total += item.getDiscountedTotal();
            String cat = item.getPart().getCategory();
            if (cat.equals("Engine")) hasEngine = true;
            if (cat.equals("Electrical")) hasElectrical = true;
        }

        if (hasEngine && hasElectrical) {
            total = total * 0.90;
        }
        return total;
    }

    public void clear() {
        items.clear();
    }
}