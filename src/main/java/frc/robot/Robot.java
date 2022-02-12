// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorSensorV3.RawColor;
import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.Rev2mDistanceSensor.Port;
import com.revrobotics.Rev2mDistanceSensor.RangeProfile;
import com.revrobotics.Rev2mDistanceSensor.Unit;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  /**
   * Change the I2C port below to match the connection of your color sensor
   */
  private final I2C.Port i2cPort = I2C.Port.kMXP;
  //private boolean flag;
  private static final byte[] port1 = { (byte) 1 };  // single byte array to configure mux for port 1  0b00000001
  private static final byte[] port2 = { (byte) 2 };  // single byte array to configure mux for port 2  0b00000010
  private static final byte[] port3 = { (byte) 4 };  // single byte array to configure mux for port 3  0b00000100
  
  int m_counter = 0;
  private Rev2mDistanceSensor distMXP;
  private ColorSensorV3 m_colorSensor, m_colorSensor2;

  private final I2C m_mux = new I2C(i2cPort, 0x70);  //  construct I2C mux with default address is 0x70


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_mux.writeBulk(port1);  // connect mux port 1 before constructing color sensor
    m_colorSensor = new ColorSensorV3(i2cPort);

    m_mux.writeBulk(port2);  // connect mux port 2 before constructing color sensor
    m_colorSensor2 = new ColorSensorV3(i2cPort);

    m_mux.writeBulk(port3);  // connect mux port 3 before constructing distance sensor
    distMXP = new Rev2mDistanceSensor(Port.kMXP,Unit.kInches,RangeProfile.kDefault);
    distMXP.setAutomaticMode(true);
    }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    SmartDashboard.putNumber("m_counter", m_counter++);  // counter to check that the loop is running

    /**
     * The method GetColor() returns a normalized color value from the sensor and can be
     * useful if outputting the color to an RGB LED or similar. To
     * read the raw color, use GetRawColor().
     * 
     * The color sensor works best when within a few inches from an object in
     * well lit conditions (the built in LED is a big help here!). The farther
     * an object is the more light from the surroundings will bleed into the 
     * measurements and make it difficult to accurately determine its color.
     */
    
    m_mux.writeBulk(port1);  // connect mux to port 1
    
    RawColor detectedColor = m_colorSensor.getRawColor();
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);

    double IR = m_colorSensor.getIR();
    SmartDashboard.putNumber("IR", IR);

    int proximity = m_colorSensor.getProximity();
    SmartDashboard.putNumber("Proximity", proximity);

    m_mux.writeBulk(port2);  // connectd mux to port 2
    RawColor detectedColor_2 = m_colorSensor2.getRawColor();
    SmartDashboard.putNumber("Red_2", detectedColor_2.red);
    SmartDashboard.putNumber("Green_2", detectedColor_2.green);
    SmartDashboard.putNumber("Blue_2", detectedColor_2.blue);

    double IR_2 = m_colorSensor.getIR();
    SmartDashboard.putNumber("IR_2", IR_2);

    int proximity_2 = m_colorSensor.getProximity();
    SmartDashboard.putNumber("Proximity_2", proximity_2);

    m_mux.writeBulk(port3);  // connectd mux to port 3
    if(distMXP.isRangeValid()) {
      SmartDashboard.putNumber("Range MXP", distMXP.getRange());
      SmartDashboard.putNumber("Timestamp MXP", distMXP.getTimestamp());
    }
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */

   
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
