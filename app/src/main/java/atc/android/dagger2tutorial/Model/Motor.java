package atc.android.dagger2tutorial.Model;

/**
 * Created by jorgecasariego on 27/7/16.
 *
 * This class has only one attribute called rpm, which I'm going to modify through the accelerate
 * and brake methods. And I'll check the current value using the getRpm method.
 */
public class Motor {
    private int rpm;

    public Motor(){
        this.rpm = 0;
    }

    public int getRpm(){
        return rpm;
    }

    public void accelerate(int value){
        rpm = rpm + value;
    }

    public void brake(){
        rpm = 0;
    }
}
