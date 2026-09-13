import java.io.FileWriter;
import java.io.IOException;

public class AuditLogger {
    private String fileName;

    public AuditLogger(String fileName) {
        this.fileName = fileName;
    }

    public void log(String action) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(action + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}