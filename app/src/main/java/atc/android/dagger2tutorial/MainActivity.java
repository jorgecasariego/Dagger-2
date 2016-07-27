package atc.android.dagger2tutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import atc.android.dagger2tutorial.Interface.DaggerVehicleComponent;
import atc.android.dagger2tutorial.Interface.VehicleComponent;
import atc.android.dagger2tutorial.Model.Vehicle;
import atc.android.dagger2tutorial.Module.VehicleModule;

public class MainActivity extends AppCompatActivity {

    private Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VehicleComponent component =  DaggerVehicleComponent.builder().vehicleModule(new VehicleModule()).build();

        vehicle = component.provideVehicle();

        Toast.makeText(this, String.valueOf(vehicle.getSpeed()), Toast.LENGTH_LONG).show();
    }
}
