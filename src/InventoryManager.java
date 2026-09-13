import java.util.HashMap;

public class InventoryManager {
    private HashMap<String, SparePart> parts;

    public InventoryManager() {
        parts = new HashMap<String, SparePart>();
    }

    public void addPart(SparePart part) {
        parts.put(part.getId(), part);
    }

    public SparePart getPart(String id) {
        return parts.get(id);
    }
    public boolean isAvailable(String id, int qty) {
        if (parts.containsKey(id)) {
            SparePart p = parts.get(id);
            if (p.getStock() >= qty) {
                return true;
            }
        }
        return false;
    }

    public void deductStock(String id, int qty) {
        if (isAvailable(id, qty)) {
            SparePart p = parts.get(id);
            p.setStock(p.getStock() - qty);
        }
    }
}