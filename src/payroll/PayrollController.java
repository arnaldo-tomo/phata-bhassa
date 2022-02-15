package payroll;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static payroll.PayrollController.round;

/*
A logica do sistema
Name*
ID*
Salario Bruto*
Basico = Bruto / 100 * 60
Renda = Bruto / 100 * 30
Medico = Bruto / 100 * 10
Por dia = Basico / 26
Por hora = Por Dia / 8
Hora Extra*
Over Time Pay = Hora Extra * pagar por hora * 2
salario = salario brutp + hora extra

*/

public class PayrollController implements Initializable {
    
    @FXML
    private Button btnDelete, btnSave, btnClear, btnReport;
    
    @FXML
    private TextField tx_name, tx_id, tx_gross, tx_basic, tx_house_rent, tx_medical,
                      tx_per_day, tx_per_hour, tx_over_time, tx_over_time_pay, tx_payable;
    @FXML
    private TableView<PayrollTable> table;
    
    @FXML
    private TableColumn<PayrollTable, String> tc_id, tc_name, tc_gross, tc_basic, tc_house_rent, tc_medical,
                                              tc_per_day, tc_per_hour, tc_over_time, tc_over_time_pay, tc_payable;
    
    ObservableList<PayrollTable> data = FXCollections.observableArrayList();
    
 
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public void calculateValues() {
        
        double gross, over_time;
        
        if(tx_gross.getText().equals("") || tx_gross.getText().equals("0") || Double.parseDouble(tx_gross.getText()) < 0){
            gross = 0;
        }else{
            gross = Double.parseDouble(tx_gross.getText());
        }
        
        if(tx_over_time.getText().equals("") || Double.parseDouble(tx_over_time.getText()) < 0){
            over_time = 0;
        }else{
            over_time = Double.parseDouble(tx_over_time.getText());
        }
        
        double basic = (gross / 100) * 60; // Basico = 60% de bruto
        basic = round(basic, 2);
        tx_basic.setText(String.valueOf(basic));
        
        double house_rent = (gross / 100) * 30; // renda de casa = 30%  bruto
        house_rent = round(house_rent, 2);
        tx_house_rent.setText(String.valueOf(house_rent));
        
        double medical = (gross / 100) * 10; // Medico = 10%  bruto
        medical = round(medical, 2);
        tx_medical.setText(String.valueOf(medical));
        
        double per_day = basic / 26; // por dia = Basico / 26
        per_day = round(per_day, 2);
        tx_per_day.setText(String.valueOf(per_day));
        
        double per_hour = per_day / 8; // por hora = por dia / 8
        per_hour = round(per_hour, 2);
        tx_per_hour.setText(String.valueOf(per_hour));
        
        double over_time_pay = over_time * per_hour * 2; // hora extra = hora extra * por dia * 2
        over_time_pay = round(over_time_pay, 2);
        tx_over_time_pay.setText(String.valueOf(over_time_pay));
        
        double payable = basic + over_time_pay; // 
        payable = round(payable, 2);
        tx_payable.setText(String.valueOf(payable));
    }
    
    @FXML
    public void onButtonSave(ActionEvent event) {
        
      
        
        if(tx_gross.getText().equals("") || tx_gross.getText().equals("0") || Double.parseDouble(tx_gross.getText()) < 0){
            tx_gross.setText("0.0");
        }
        
        if(tx_over_time.getText().equals("") || Double.parseDouble(tx_over_time.getText()) < 0){
            tx_over_time.setText("0.0");
        }
        
        calculateValues();
        
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = Payroll.conn.createStatement();
            
            String sql = "INSERT INTO `payroll` VALUES('" + tx_id.getText() + "', '" + tx_name.getText() + 
                                                      "', '" + tx_gross.getText() + "', '" + tx_over_time.getText() + "');";
            
            stmt.execute(sql);
            Reload();
        }
        catch (SQLException ex){
            
            if(ex.getMessage().contains("Duplicate")){
                try {
                    stmt = Payroll.conn.createStatement();
                    String sql = "UPDATE `payroll` SET `name` = '" + tx_name.getText() + "', `gross` = '" + tx_gross.getText() + 
                                                   "', `over` = '" + tx_over_time.getText() + "' WHERE `id` = '" + tx_id.getText() + "';";
                    stmt.execute(sql);
                    Reload();
                } catch(SQLException ex2) {
                    System.out.println("SQLException: " + ex2.getMessage());
                    System.out.println("SQLState: " + ex2.getSQLState());
                    System.out.println("VendorError: " + ex2.getErrorCode());
                }
            }
            
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignorar

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignorar

                stmt = null;
            }
        }
        
        onButtonClear();
    }
    
    @FXML
    public void Reload(){
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = Payroll.conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM payroll");
            data.clear();
            
            while(rs.next()){

                String id = rs.getString("id");
                String name = rs.getString("name");
                String gross = rs.getString("gross");
                String over = rs.getString("over");
                
                System.out.println(id + ", " + name + ", " + gross + ", " + over);
                
                PayrollTable pt = new PayrollTable(id, name, gross, over);
                data.add(pt);
            }
        }
        catch (SQLException ex){
            //  erros da cena
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignorar

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignorar

                stmt = null;
            }
        }

    }
    
    public void onTableClick() {
        tx_id.setText(table.getSelectionModel().getSelectedItem().getId());
        tx_name.setText(table.getSelectionModel().getSelectedItem().getName());
        tx_gross.setText(table.getSelectionModel().getSelectedItem().getGross());
        tx_over_time.setText(table.getSelectionModel().getSelectedItem().getOverTime());
        calculateValues();
        
        btnDelete.setDisable(false);
        btnReport.setDisable(false);
        tx_id.setEditable(false);
    }
    
    @FXML
    public void onButtonClear() {
        tx_id.setText("");
        tx_name.setText("");
        tx_gross.setText("");
        tx_over_time.setText("");
        calculateValues();
        
        btnDelete.setDisable(true);
        btnReport.setDisable(true);
        tx_id.setEditable(true);
    }
    
    @FXML
    public void onButtonDelete() {
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = Payroll.conn.createStatement();
            stmt.execute("DELETE FROM payroll WHERE id = " + tx_id.getText());
        } catch (SQLException ex) {
            // ignore
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignorar

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignorar

                stmt = null;
            }
        }
        
        onButtonClear();
        Reload();
        btnDelete.setDisable(true);
        tx_id.setEditable(true);
    }
    
    @FXML
    public void onButtonReport()
    {
        String id = table.getSelectionModel().getSelectedItem().getId();
        String name = table.getSelectionModel().getSelectedItem().getName();
        String gross = table.getSelectionModel().getSelectedItem().getGross();
        String over_time = table.getSelectionModel().getSelectedItem().getOverTime();
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Report.fxml"));
            
            // Creacao de um  controller de instancia
            ReportController controller = new ReportController(id, name, gross, over_time);
            
            // colocar em  FXMLLoader
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 500, 400);
            Stage stage = new Stage();
            stage.setTitle("Relatorio");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            System.out.println("Maning Nice Correu o pograma!");
        } catch (IOException e) {
            // ignore
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tc_gross.setCellValueFactory(new PropertyValueFactory<>("gross"));
        tc_basic.setCellValueFactory(new PropertyValueFactory<>("basic"));
        tc_house_rent.setCellValueFactory(new PropertyValueFactory<>("house_rent"));
        tc_medical.setCellValueFactory(new PropertyValueFactory<>("medical"));
        tc_per_day.setCellValueFactory(new PropertyValueFactory<>("per_day"));
        tc_per_hour.setCellValueFactory(new PropertyValueFactory<>("per_hour"));
        tc_over_time.setCellValueFactory(new PropertyValueFactory<>("over_time"));
        tc_over_time_pay.setCellValueFactory(new PropertyValueFactory<>("over_time_pay"));
        tc_payable.setCellValueFactory(new PropertyValueFactory<>("payable"));
        table.setItems(data);
        
        Reload();
    }

}

