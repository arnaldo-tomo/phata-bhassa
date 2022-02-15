package payroll;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import javafx.scene.control.Alert;

public class Payroll extends Application {
    
    static Connection conn = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/database";
            String username = "root";
            String password = "";

            try {
                conn = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
             
            }
            
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("nao foi econtrad o driver database!", e);
        }
        
        Parent root = FXMLLoader.load(getClass().getResource("Payroll.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Phata-Bhassa - Sistema de folha de pagamento");
        stage.show();
           }

    public static void main(String[] args) {
        launch(args);
    }
}