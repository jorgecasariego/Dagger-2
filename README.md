# Dagger-2

Dependency injection is a software design pattern focused on making our 
applications loosely coupled, extensible, and maintainable.

This project show how to handle dependency injection using Dagger 2.

**Introduction**

When you have an object that needs or depends on another object to do its work, you have a dependency. Dependencies can be solved by letting the dependent object create the dependency or asking a factory object to make one. In the context of dependency injection, however, the dependencies are supplied to the class that needs the dependency to avoid the need for the class itself to create them. 


**Step 1 - buil.gradle (App)**

    // 1. Add plugin https://bitbucket.org/hvisser/android-apt
    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:2.1.2'
            classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'

            // NOTE: Do not place your application dependencies here; they belong
            // in the individual module build.gradle files
        }
    }

    allprojects {
        repositories {
            jcenter()
        }
    }

    task clean(type: Delete) {
        delete rootProject.buildDir
    }


**Step 2 - buil.gradle (Module)**

Open build.gradle in your project's app folder and modify it as shown below.

    apply plugin: 'com.android.application'

    // 2. Apply plugin
    apply plugin: 'com.neenbedankt.android-apt'

    android {
        compileSdkVersion 24
        buildToolsVersion "23.0.3"

        defaultConfig {
            applicationId "atc.android.dagger2tutorial"
            minSdkVersion 10
            targetSdkVersion 24
            versionCode 1
            versionName "1.0"
        }
        buildTypes {
            release {
                minifyEnabled false
                proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            }
        }
    }

    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])
        testCompile 'junit:junit:4.12'
        compile 'com.android.support:appcompat-v7:24.0.0'

        // 3. Add Dagger dependencies
        compile 'com.google.dagger:dagger:2.0.2'
        apt 'com.google.dagger:dagger-compiler:2.0.2'
        provided 'org.glassfish:javax.annotation:10.0-b28' // 4. for additional annotations required outside Dagger
    }

After updating Dagger's configuration, you can synchronize the project 

**Implementing Dagger 2**

Step 1: Identify Dependent Objects

For this tutorial, I'm going to work with two classes, Vehicle and Motor. 
Motor is the independent class and Vehicle is the dependent class. 

I'm going to start creating this model within a new package called model.

**Motor.java**

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

**Vehicle.java**

    package atc.android.dagger2tutorial.Model;

    /**
    * Created by jorgecasariego on 27/7/16.
    *
    * In this class, you can see that I'm not creating a new object of the Motor class,
    * even though I'm using its methods. In a real world application, this class should have more
    * methods and attributes, but let's keep it simple for now.
    */
    public class Vehicle {
        private Motor motor;

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

**Create @Module Class**

Now we create a class with the @Module annotation. 

This class is going to provide the objects we will need with its 
dependencies satisfied. For this, we have to create a new package 
(just to keep it in order), name it **module** and add a new class 
inside it as follows:

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


**Request Dependencies in Dependent Objects**

Now that we have the providers for our different models, we need to 
request them. Just as Vehicle needs Motor, you have to add 
the @Inject annotation in the Vehicle constructor as follows:

    @Inject
    public Vehicle(Motor motor){
        this.motor = motor;
    }


**Connecting @Modules With @Inject**

The connection between the provider of dependencies, 
@Module, and the classes requesting them through @Inject 
is made using @Component, which is an interface:

    package atc.android.dagger2tutorial.Interface;

    import javax.inject.Singleton;

    import atc.android.dagger2tutorial.Model.Vehicle;
    import atc.android.dagger2tutorial.Module.VehicleModule;
    import dagger.Component;

    /**
    * Created by jorgecasariego on 27/7/16.
    *
    * The connection between the provider of dependencies,
    * @Module, and the classes requesting them through @Inject is made using @Component
    */
    @Singleton
    @Component(modules = {VehicleModule.class})
    public interface VehicleComponent {
        Vehicle provideVehicle();
    }



Next to the @Component annotation, you have to specify which modules 
are going to be used—in this case I use VehicleModule, which we created 
earlier. If you need to use more modules, then just add them using a 
comma as a separator.

Within the interface, add methods for every object you need and it will 
automatically give you one with all its dependencies satisfied. In this 
case, I only need a Vehicle object, which is why there is only one method.

**Using @Component Interface to Obtain Objects**

Now that we have every connection ready, we have to obtain an instance 
of this interface and invoke its methods to obtain the object you need. 

I'm going to implement it in the onCreate method in the MainActivity 
as follows:

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

            Toast.makeText(this, String.valueOf(vehicle.getSpeed()), Toast.LENGTH_SHORT).show();
        }
    }

You can see that the magic takes place on line 23. 
You are only asking for one object of the class Vehicle and the library
is the one in charge of satisfying all the dependencies this object 
needs. Again you can see there is no new instantiation of any other 
object—everything is managed by the library.

**Conclusion**

Dependency injection is a pattern that you will have to implement 
sooner or later in your own applications. With Dagger 2, you have an 
easy-to-use library to implement it. I hope you found this tutorial useful,
 and don't forget to share it if you liked it.















