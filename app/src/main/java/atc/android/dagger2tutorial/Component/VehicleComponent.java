package atc.android.dagger2tutorial.Component;

import javax.inject.Singleton;

import atc.android.dagger2tutorial.Model.Vehicle;
import atc.android.dagger2tutorial.Module.VehicleModule;
import dagger.Component;

/**
 * Created by jorgecasariego on 27/7/16.
 *
 * The connection between the provider of dependencies,
 * @Module, and the classes requesting them through @Inject is made using @Component
 *
 * Creamos una interfaz utilizando @Component el cual contiene todos los lugares donde
 * usaremos el objeto que queremos inyectar.
 *
 * En este caso el lugar donde inyectaremos el objeto Vehiculo va a ser VehicleModulo
 */
@Singleton
@Component(modules = {VehicleModule.class})
public interface VehicleComponent {
    Vehicle provideVehicle();
}
