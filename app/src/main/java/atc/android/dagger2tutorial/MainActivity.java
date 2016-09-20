package atc.android.dagger2tutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import atc.android.dagger2tutorial.Component.DaggerVehicleComponent;
import atc.android.dagger2tutorial.Component.VehicleComponent;
import atc.android.dagger2tutorial.Model.Vehicle;
import atc.android.dagger2tutorial.Module.VehicleModule;

public class MainActivity extends AppCompatActivity {

    private Vehicle vehicle;
    private TextView speedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedValue = (TextView) findViewById(R.id.current_speed_value);

        VehicleComponent component =  DaggerVehicleComponent.builder().vehicleModule(new VehicleModule()).build();

        vehicle = component.provideVehicle();

        speedValue.setText(String.valueOf(vehicle.getSpeed()));
    }

    public void callBrake(View v){
        vehicle.stop();
        speedValue.setText(String.valueOf(vehicle.getSpeed()));
    }

    public void callIncrease(View v){
        vehicle.increaseSpeed(10);
        speedValue.setText(String.valueOf(vehicle.getSpeed()));
    }
}
