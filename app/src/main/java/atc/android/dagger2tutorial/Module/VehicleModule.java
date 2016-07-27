package atc.android.dagger2tutorial.Module;

import javax.inject.Singleton;

import atc.android.dagger2tutorial.Model.Motor;
import atc.android.dagger2tutorial.Model.Vehicle;
import dagger.Module;
import dagger.Provides;

/**
 * Created by jorgecasariego on 27/7/16.
 *
 * Dagger 2 exposes a number of special annotations:

     @Module for the classes whose methods provide dependencies
     @Provides for the methods within @Module classes
     @Inject to request a dependency (a constructor, a field, or a method)
     @Component is a bridge interface between modules and injection

     Vehicle needs Motor to work properly. That is why we need to create two providers,
     one for Motor (the independent model) and another one for Vehicle (indicating its dependency).

     Every provider (or method) must have the @Provides annotation and the class must have the
     @Module annotation.

     The @Singleton annotation indicates that there will be only one instance of the object.
 */

@Module
public class VehicleModule {

    @Provides @Singleton
    Motor provideMotor(){
        return new Motor();
    }

    @Provides @Singleton
    Vehicle provideVehicle(){
        return new Vehicle(new Motor());
    }
}
