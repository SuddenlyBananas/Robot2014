/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.raiderrobotics;

import edu.wpi.first.wpilibj.*;

/***************************************************************************
The two left motors are connected to victors on ports 1 and 3
The two right motors are connected for victors on ports 2 and 4
*****************************************************************************/

public class RobotMain extends IterativeRobot {

    final static int ARCADE = 1;
    final static int TANK = 2;

    //create object references
    Joystick leftStick, rightStick;
    RobotDrive driveTrain1, driveTrain2;
    Victor victor1, victor2, victor3, victor4;
    Jaguar jag1;
    DigitalInput limitSwitch;
    //the JoystickButton class does not exist in our Java FRC plugins!
    // JoystickButton stickLBtn1, stickLBtn2; 

    //global variables
    private int driveState = ARCADE;

    //create global objects here
    public void robotInit() {
        victor1 = new Victor(1);
        victor2 = new Victor(2);
        victor3 = new Victor(3);
        victor4 = new Victor(4);
        jag1 = new Jaguar(5); //for the arm
        /*** do the following lines do anything? 
        victor1.enableDeadbandElimination(true);
        victor2.enableDeadbandElimination(true);
        victor3.enableDeadbandElimination(true);
        victor4.enableDeadbandElimination(true);
        jag1.enableDeadbandElimination(true);
        ***/
        
        //reversed the motor to fix the left and right joystick
        driveTrain1 = new RobotDrive(victor2, victor1);
        driveTrain2 = new RobotDrive(victor4, victor3);
        
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        //stickLBtn1 = new JoystickButton(stickL, 1);
        //stickLBtn2 = new JoystickButton(stickL, 2);
        limitSwitch = new DigitalInput(5);
    }

    public void teleopInit() {
        Watchdog.getInstance().feed();
    }

    // called at 50Hz (every 20ms). This method must not take more than 20ms to complete!
    public void teleopPeriodic() {
        // feed the watchdog
        Watchdog.getInstance().feed();

        normalDrive();
        //publicDrive();
        
       sendSmartDashboard();

        
        //check for button press to switch mode. Use two buttons to prevent bounce.
        //if (joyLeftBtn2.get()) driveState = ARCADE;
        //if (joyLeftBtn3.get()) driveState = TANK;
        //Since joystick button is not loaded ....
        boolean button2 = leftStick.getRawButton(2);
        boolean button3 = leftStick.getRawButton(3);
        if (button2) driveState = ARCADE;
        if (button3) driveState = TANK;
    }

    public void autonomousInit() {
        //chassis.setSafetyEnabled(false); // or better yet: feed the watchdog regularly
    }

    public void autonomousPeriodic() {}

    public void autonomousDisabled() {
        //turn off motors here
    }
    
    // drive the robot normally
    private void normalDrive() {
        if (driveState == ARCADE) {
            driveTrain1.arcadeDrive(leftStick);
            driveTrain2.arcadeDrive(leftStick);
        } else {
            driveTrain1.tankDrive(leftStick, rightStick);
            driveTrain2.tankDrive(leftStick, rightStick);
        }
    }
    
    // square the inputs (while preserving the sign) to increase fine control while permitting full power
    double squareInputs(double input) {
        if (input >= 0.0) {
            input = (input * input);
        } else {
            input = -(input * input);
        }
        return input;
    }
    
    //limit values so that they are always between -1.0 and +1.0
    public double limit(double num) {
        if (num > 1.0) return 1.0;
        if (num < -1.0) return -1.0;
        return num;
    }
    
    public void sendSmartDashboard(){
        //smartDashboardSetup-CURRENTLY UNTESTED
        SmartDashboard.putNumber("joyRx", rightStick.getX());
        SmartDashboard.putNumber("joyLx", leftStick.getX());
        SmartDashboard.putNumber("joyRy", rightStick.getY());
        SmartDashboard.putNumber("joyLy", leftStick.getY());
        SmartDashboard.putBoolean("btn2",leftStick.getRawButton(2));
        SmartDashboard.putBoolean("btn3",leftStick.getRawButton(3));
        
        SmartDashboard.putNumber("vic1",victor1.get());
        SmartDashboard.putNumber("vic2",victor2.get());
        SmartDashboard.putNumber("vic3",victor3.get());
        SmartDashboard.putNumber("vic4",victor4.get());
        SmartDashboard.putNumber("jagArm",jag1.get());
        if(driveState==ARCADE){    
            SmartDashboard.putString("driveState","Arcade");
        }else{
            SmartDashboard.putString("driveState","Tank Drive");
        }
        SmartDashboard.putBoolean("limitSwitch",limitSwitch.get());
    }
}

