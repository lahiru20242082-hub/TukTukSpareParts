// Author: Lahiru - CM1601 - Part 2b - Search Fixed
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class Main extends Application {

    private TableView<SparePart> table;
    private ObservableList<SparePart> dataList;

    @Override
    public void start(Stage stage) {
        List<SparePart> loaded = FileParser.loadInventory("TukTukSpareParts/inventory_legacy.txt");
        if (loaded.isEmpty()) loaded = FileParser.loadInventory("inventory_legacy.txt");
        if (loaded.isEmpty()) loaded = FileParser.loadInventory("src/inventory_legacy.txt");
        if (loaded.isEmpty()) loaded = FileParser.loadInventory("inventory_cleaned.txt");
        dataList = FXCollections.observableArrayList(loaded);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or code...");

        FilteredList<SparePart> filtered = new FilteredList<>(dataList, p -> true);

        // FIXED: Added search listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filtered.setPredicate(part -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return part.getCode().toLowerCase().contains(lower) ||
                        part.getName().toLowerCase().contains(lower) ||
                        part.getBrand().toLowerCase().contains(lower) ||
                        part.getCategory().toLowerCase().contains(lower);
            });
        });

        table = new TableView<>();
        table.setItems(filtered); // FIXED: was dataList, now filtered

        TableColumn<SparePart, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<SparePart, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        TableColumn<SparePart, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<SparePart, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<SparePart, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<SparePart, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        table.getColumns().addAll(codeCol, nameCol, brandCol, priceCol, qtyCol, catCol);

        HBox topBox = new HBox(10, searchField);
        VBox root = new VBox(10, topBox, table);
        root.setStyle("-fx-padding: 10;");

        Scene scene = new Scene(root, 850, 450);
        stage.setTitle("TukTukSpareParts - Search Fixed");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}