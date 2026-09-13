public class CartItem {
    private SparePart part;
    private int quantity;

    public CartItem(SparePart part, int quantity) {
        this.part = part;
        this.quantity = quantity;
    }

    public SparePart getPart() { return part; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { this.quantity = q; }

    public double getSubTotal() {
        return part.getPrice() * quantity;
    }

    public double getDiscountedTotal() {
        double total = getSubTotal();
        if (quantity >= 3) {
            total = total * 0.95;
        }
        return total;
    }
}