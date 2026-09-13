import java.util.HashMap;

public class InventoryManager {
    private HashMap<String, SparePart> parts;

    public InventoryManager() {
        parts = new HashMap<String, SparePart>();
    }

    public void addPart(SparePart part) {
        parts.put(part.getId(), part);
    }
}