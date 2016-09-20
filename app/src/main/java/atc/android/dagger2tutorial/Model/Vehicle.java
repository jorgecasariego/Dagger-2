package atc.android.dagger2tutorial.Model;

import javax.inject.Inject;

/**
 * Created by jorgecasariego on 27/7/16.
 *
 * In this class, you can see that I'm not creating a new object of the Motor class,
 * even though I'm using its methods. In a real world application, this class should have more
 * methods and attributes, but let's keep it simple for now.
 */
public class Vehicle {
    private Motor motor;

    /**
     * We can use the @Inject annotation to request dependencies in the constructor, fields, or methods.
     * In this case, I'm keeping the injection in the constructor.
     *
     * Con Inject marcamos los lugares donde queremos inyectar el objeto. En este caso queremos
     * inyectar el objeto Motor en el constructor de Vehiculo
     * @param motor
     */
    @Inject
    public Vehicle(Motor motor){
        this.motor = motor;
    }

    public void increaseSpeed(int value){
        motor.accelerate(value);
    }

    public void stop(){
        motor.brake();
    }

    public int getSpeed(){
        return motor.getRpm();
    }

}
