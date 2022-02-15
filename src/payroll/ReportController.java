package payroll;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import static payroll.PayrollController.round;


public class ReportController implements Initializable {
        
    private String id, name, gross, over_time;
    
    @FXML
    private AnchorPane root;
    
    @FXML
    private Button btnPrint;
    
    @FXML
    private Label tx_name, tx_id, tx_gross, tx_basic, tx_house_rent, tx_medical, tx_per_day, tx_per_hour, tx_over_time, tx_over_time_pay, tx_payable;
    
    public ReportController(String id, String name, String gross, String over_time) {
        this.id = id;
        this.name = name;
        this.gross = gross;
        this.over_time = over_time;
    }
    
    public void calculateValues() {
        
        tx_id.setText(id);
        tx_name.setText(name);
        tx_gross.setText(gross);
        tx_over_time.setText(over_time);
        
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
        
        // Basico = 60% do Grosso
        double basic = (gross / 100) * 60; 
        basic = round(basic, 2);
        tx_basic.setText(String.valueOf(basic) + " MZN");
        
        // renda  = 30% do Grosso
        double house_rent = (gross / 100) * 30; 
        house_rent = round(house_rent, 2);
        tx_house_rent.setText(String.valueOf(house_rent) + "  MZN");
        
        // Medico = 10% of Grosso
        double medical = (gross / 100) * 10; 
        medical = round(medical, 2);
        tx_medical.setText(String.valueOf(medical) + "  MZN");
        
        // Por dia = Basico / 26
        double per_day = basic / 26; 
        per_day = round(per_day, 2);
        tx_per_day.setText(String.valueOf(per_day) + "  MZN");
        
        // Por Hora = Por dia / 8
        double per_hour = per_day / 8; 
        per_hour = round(per_hour, 2);
        tx_per_hour.setText(String.valueOf(per_hour) + "  MZN");
        
         // Total da Hora Extra = Hora extra * Pagar por hora * 2
        double over_time_pay = over_time * per_hour * 2;
        over_time_pay = round(over_time_pay, 2);
        tx_over_time_pay.setText(String.valueOf(over_time_pay) + "  MZN");
        
        // Apagar = Basico +  Total da Hora Extra
        double payable = basic + over_time_pay; 
        payable = round(payable, 2);
        tx_payable.setText(String.valueOf(payable) + "  MZN");
        
        tx_gross.setText(gross + "  MZN");
        tx_over_time.setText(over_time + " Horas");
    }
    
    @FXML
    private void onButtonPrint() {
        btnPrint.setVisible(false);
        print(root);
    }
         
    
    private void print(Node node) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(node.getScene().getWindow())){
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }
        btnPrint.setVisible(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        calculateValues();
    }    
    
}
