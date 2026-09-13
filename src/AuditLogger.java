import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditLogger {
    private String fileName;

    public AuditLogger(String fileName) {
        this.fileName = fileName;
    }

    public void log(String action) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            String time = LocalDateTime.now().toString();
            writer.write(time + " - " + action + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}