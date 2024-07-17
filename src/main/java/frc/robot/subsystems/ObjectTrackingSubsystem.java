package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.TeleopSwerve;

public class ObjectTrackingSubsystem extends SubsystemBase {
    // double angle;
    double x ;
    double id ;
    double y ;
    double area ;
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-intake");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tid = table.getEntry("tid");
    NetworkTableEntry pipeline = table.getEntry("pipeline");
    public ObjectTrackingSubsystem(){
    }
    @Override
    public void periodic() {
        x = tx.getDouble(0.0);
        id = tid.getDouble(0.0);
        y = ty.getDouble(0.0);
        area = ta.getDouble(0.0);
        SmartDashboard.putNumber("distance", getDistance());
        SmartDashboard.putNumber("limelightx", x);
        SmartDashboard.putNumber("limelighty", y);
        SmartDashboard.putNumber("limelighta", area);
        SmartDashboard.putNumber("limelightid", id);
    }
    
    public Command AimAtSpeaker(Swerve s_Swerve, DoubleSupplier translationSup, DoubleSupplier strafeSup, BooleanSupplier robotCentricSup) {
        Command setPipelineCommand = this.run(
            () -> pipeline.setDouble(0)
            );
            setPipelineCommand.addRequirements(this);
            // angle = getRotation(offset);
            Command rotateSwerveCommand = new TeleopSwerve(
                s_Swerve,
                translationSup,
                strafeSup,
                () -> getRotation(0),
                robotCentricSup 
            );
        return setPipelineCommand.andThen(rotateSwerveCommand);
    }
    
    public double getDistance(){
        double area = ta.getDouble(0.0);
        double oneSide = Math.sqrt(area);
        double distance = 5.37/oneSide;
        return distance;
    }

    public double getRotation(double targetAngle){
        // System.out.println("id: " + id);
        if (id <= 0){
            System.out.println("id is 0");
            return 0;
        }
        else{
            return (tx.getDouble(0.0)-targetAngle)*-0.04;
        }
    }
}