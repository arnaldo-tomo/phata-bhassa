package payroll;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static payroll.PayrollController.round;

public class PayrollTable {

        public final SimpleStringProperty tc_name, tc_id, tc_gross, tc_basic, tc_house_rent, tc_medical, tc_per_day, tc_per_hour, tc_over_time, tc_over_time_pay, tc_payable;

        PayrollTable(String id, String name, String gross, String overtime){
            this.tc_id = new SimpleStringProperty(id);
            this.tc_name = new SimpleStringProperty(name);
            this.tc_gross = new SimpleStringProperty(gross);
            this.tc_over_time = new SimpleStringProperty(overtime);

            // Basico = 60% de salario bruto
            double basic = (Double.parseDouble(tc_gross.get()) / 100) * 60; 
            basic = round(basic, 2);
            this.tc_basic = new SimpleStringProperty(String.valueOf(basic));
            
            // casa = 30% salrio bruto
            double house_rent = (Double.parseDouble(tc_gross.get()) / 100) * 30; 
            house_rent = round(house_rent, 2);
            this.tc_house_rent = new SimpleStringProperty(String.valueOf(house_rent));
            
             // Medico = 10% Salario bruto
            double medical = (Double.parseDouble(tc_gross.get()) / 100) * 10;
            medical = round(medical, 2);
            this.tc_medical = new SimpleStringProperty(String.valueOf(medical));
            
            // por dia = Basico / 26
            double per_day = basic / 26; 
            per_day = round(per_day, 2);
            this.tc_per_day = new SimpleStringProperty(String.valueOf(per_day));
            
            // por hora = Por dia / 8
            double per_hour = per_day / 8; 
            per_hour = round(per_hour, 2);
            this.tc_per_hour = new SimpleStringProperty(String.valueOf(per_hour));
            
            // Hora extra= hora extra * por dia * 2
            double over_time_pay = Double.parseDouble(tc_over_time.get()) * per_hour * 2; 
            over_time_pay = round(over_time_pay, 2);
            this.tc_over_time_pay = new SimpleStringProperty(String.valueOf(over_time_pay));
            
            // salario = Basico + hora extra
            double payable = basic + over_time_pay; 
            payable = round(payable, 2);
            this.tc_payable = new SimpleStringProperty(String.valueOf(payable));
        }
        
        public final String getId() { return tc_id.getValue(); }
        public final String getName() { return tc_name.getValue(); }
        public final String getGross() { return tc_gross.getValue(); }
        public final String getOverTime() { return tc_over_time.getValue(); }
        
        public final StringProperty idProperty() { return tc_id; }
        public final StringProperty nameProperty() { return tc_name; }
        public final StringProperty grossProperty() { return tc_gross; }
        public final StringProperty basicProperty() { return tc_basic; }
        public final StringProperty house_rentProperty() { return tc_house_rent; }
        public final StringProperty medicalProperty() { return tc_medical; }
        public final StringProperty per_dayProperty() { return tc_per_day; }
        public final StringProperty per_hourProperty() { return tc_per_hour; }
        public final StringProperty over_timeProperty() { return tc_over_time; }
        public final StringProperty over_time_payProperty() { return tc_over_time_pay; }
        public final StringProperty payableProperty() { return tc_payable; }
} 