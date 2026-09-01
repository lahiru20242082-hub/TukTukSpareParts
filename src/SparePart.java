public class SparePart {
    private String code;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String dateAdded;

    public SparePart(String code, String name, String brand, double price, int quantity, String category, String dateAdded) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getDateAdded() { return dateAdded; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return code + " - " + name + " (" + quantity + " in stock)";
    }
}