//  Part 1 - Table View
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        table = new TableView<>();
        table.setItems(dataList);
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
        VBox root = new VBox(table);
        Scene scene = new Scene(root, 800, 400);
        stage.setTitle("TukTukSpareParts - Part 1");
        stage.setScene(scene);
        stage.show();
        System.out.println("Loaded " + dataList.size() + " parts");
    }
    public static void main(String[] args) { launch(args); }
}