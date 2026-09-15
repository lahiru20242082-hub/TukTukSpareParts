import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private TableView<SparePart> table;
    private ObservableList<SparePart> dataList;
    private Cart cart = new Cart();
    private InventoryManager inventoryManager = new InventoryManager();
    private AuditLogger logger = new AuditLogger("audit.log");
    private Label cartLabel = new Label("Cart: 0 items - Total: Rs. 0.00");
    private List<Dealer> allDealers;

    @Override
    public void start(Stage stage) {
        // Load inventory - ROOT folder is your structure
        List<SparePart> loaded = FileParser.loadInventory("inventory_legacy.txt");
        if (loaded.isEmpty()) loaded = FileParser.loadInventory("TukTukSpareParts/inventory_legacy.txt");
        if (loaded.isEmpty()) loaded = FileParser.loadInventory("src/inventory_legacy.txt");
        if (loaded.isEmpty()) loaded = FileParser.loadInventory("inventory_cleaned.txt");

        for (SparePart p : loaded) inventoryManager.addPart(p);
        dataList = FXCollections.observableArrayList(loaded);

        // Load dealers - FIXED for dealers_legacy.txt in ROOT
        allDealers = DealerManager.loadDealers("dealers_legacy.txt");
        if (allDealers.isEmpty()) allDealers = DealerManager.loadDealers("TukTukSpareParts/dealers_legacy.txt");
        if (allDealers.isEmpty()) allDealers = DealerManager.loadDealers("src/dealers_legacy.txt");
        if (allDealers.isEmpty()) allDealers = DealerManager.loadDealers("dealers.txt");

        if (allDealers.isEmpty()) {
            allDealers = new ArrayList<>();
            allDealers.add(new Dealer("D001", "Colombo Auto Parts", "0771234567", "Colombo"));
            allDealers.add(new Dealer("D002", "Kandy Tuk Center", "0772345678", "Kandy"));
            allDealers.add(new Dealer("D003", "Galle Motors", "0773456789", "Galle"));
            allDealers.add(new Dealer("D004", "Jaffna Spares", "0774567890", "Jaffna"));
            allDealers.add(new Dealer("D005", "Negombo Auto", "0775678901", "Negombo"));
            System.out.println("Dealers file not found - using dummy data");
        }

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or code...");

        FilteredList<SparePart> filtered = new FilteredList<>(dataList, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filtered.setPredicate(part -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return part.getName().toLowerCase().contains(lower) ||
                        part.getCode().toLowerCase().contains(lower) ||
                        part.getCategory().toLowerCase().contains(lower);
            });
        });

        table = new TableView<>(filtered);
        TableColumn<SparePart, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setPrefWidth(80);
        TableColumn<SparePart, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        TableColumn<SparePart, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<SparePart, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<SparePart, Integer> qtyCol = new TableColumn<>("Stock");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        table.getColumns().addAll(codeCol, nameCol, brandCol, priceCol, qtyCol);

        Button addToCartBtn = new Button("Add to Cart");
        Button viewCartBtn = new Button("View Cart & Checkout");
        Button viewDealersBtn = new Button("View Dealers");
        Button randomDealersBtn = new Button("Get 4 Random Dealers");
        Button sortDealersBtn = new Button("Sort by Location (Bubble Sort)");

        addToCartBtn.setOnAction(e -> {
            SparePart selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (inventoryManager.isAvailable(selected.getCode(), 1)) {
                    cart.addItem(selected, 1);
                    logger.log("Added to cart: " + selected.getCode());
                    updateCartLabel();
                    showAlert("Added: " + selected.getName());
                } else {
                    showAlert("Out of stock!");
                }
            } else {
                showAlert("Select a part first!");
            }
        });

        viewCartBtn.setOnAction(e -> showCartWindow());
        viewDealersBtn.setOnAction(e -> showDealersWindow(allDealers, "All Dealers - from dealers_legacy.txt"));
        randomDealersBtn.setOnAction(e -> {
            List<Dealer> randomFour = DealerManager.getRandomFour(allDealers);
            showDealersWindow(randomFour, "4 Random Dealers");
            logger.log("Viewed 4 random dealers");
        });
        sortDealersBtn.setOnAction(e -> {
            DealerManager.sortByLocation(allDealers);
            showDealersWindow(allDealers, "Dealers Sorted by Location (Bubble Sort)");
            logger.log("Sorted dealers by location using bubble sort");
        });

        HBox menuBar = new HBox(10, addToCartBtn, viewCartBtn, viewDealersBtn, randomDealersBtn, sortDealersBtn);
        menuBar.setPadding(new Insets(10));
        menuBar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white;");

        VBox root = new VBox(10, searchField, table, cartLabel, menuBar);
        root.setPadding(new Insets(10));
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("TukTuk Spare Parts - Main Menu");
        stage.setScene(scene);
        stage.show();
        logger.log("Application started");
    }

    private void updateCartLabel() {
        cartLabel.setText("Cart: " + cart.getItems().size() + " items - Total: Rs. " + String.format("%.2f", cart.getTotal()) + " (5% discount on qty>=3)");
    }

    private void showCartWindow() {
        if (cart.getItems().isEmpty()) {
            showAlert("Cart is empty!");
            return;
        }
        StringBuilder sb = new StringBuilder("CART:\n\n");
        for (CartItem item : cart.getItems()) {
            sb.append(item.getPart().getCode()).append(" - ").append(item.getPart().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = Rs.").append(String.format("%.2f", item.getDiscountedTotal())).append("\n");
        }
        sb.append("\nTOTAL: Rs.").append(String.format("%.2f", cart.getTotal()));
        TextArea area = new TextArea(sb.toString());
        area.setEditable(false);
        Button checkout = new Button("Checkout - Deduct Stock");
        checkout.setOnAction(ev -> {
            for (CartItem item : cart.getItems()) {
                inventoryManager.deductStock(item.getPart().getCode(), item.getQuantity());
            }
            logger.log("Checkout completed - Total: " + cart.getTotal());
            cart.clear();
            updateCartLabel();
            table.refresh();
            showAlert("Checkout successful! Stock deducted.");
        });
        VBox box = new VBox(10, area, checkout);
        box.setPadding(new Insets(10));
        Stage s = new Stage();
        s.setScene(new Scene(box, 450, 350));
        s.setTitle("Cart");
        s.show();
    }

    private void showDealersWindow(List<Dealer> dealers, String title) {
        if (dealers == null || dealers.isEmpty()) {
            showAlert("No dealers found! Check dealers_legacy.txt");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Dealer d : dealers) {
            sb.append(d.toString()).append("\n");
        }
        TextArea area = new TextArea(sb.toString());
        area.setEditable(false);
        VBox box = new VBox(area);
        box.setPadding(new Insets(10));
        Stage s = new Stage();
        s.setScene(new Scene(box, 550, 400));
        s.setTitle(title);
        s.show();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}

