package frc.robot.commands;

import frc.robot.constants.Constants;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.SwerveTrackingSubsystem;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class AutonAlignmentCommand extends TeleopSwerve{
    private SwerveTrackingSubsystem t_Tracking;
    private Swerve s_Swerve;
    private double direction;

    public AutonAlignmentCommand(SwerveTrackingSubsystem t_Tracking, Swerve s_Swerve, double direction){
        super(s_Swerve, () -> 0, () -> 0, () -> t_Tracking.getRotationAuton(0), () -> false);
        addRequirements(s_Swerve, t_Tracking);
        this.t_Tracking = t_Tracking;
        this.s_Swerve = s_Swerve;
        this.direction = direction;
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("Auton State", "Beginging Alignment");
        t_Tracking.setPipelineSpeaker();     
    }


    @Override
    public void execute() {
        if (t_Tracking.id == -1){
            s_Swerve.drive(
                new Translation2d(0, 0).times(Constants.Swerve.maxSpeed), 
                (0.2 * direction) * Constants.Swerve.maxAngularVelocity, 
                false, 
                true
            );
        } else {
            super.execute();
        }
    }

    @Override
    public void end(boolean interrupted) {
        s_Swerve.drive(
            new Translation2d(0, 0).times(Constants.Swerve.maxSpeed), 
            0 * Constants.Swerve.maxAngularVelocity, 
            false, 
            true
        );
        SmartDashboard.putString("Auton State", "Alignment Done");
    }

    @Override
    public boolean isFinished(){
        if (t_Tracking.id != -1){
            if (Math.abs(t_Tracking.x) <= 3){
                return true;
            } else{
                return false;
            }
        } else {
            return false;
        }
    }
}