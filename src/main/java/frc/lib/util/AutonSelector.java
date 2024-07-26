package frc.lib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutonSelector {
    
    /**
   * Get a list of all auto names in the project
   *
   * @return List of all auto names
   */
  public static List<String> getAllAutoNames() {
    File[] autoFiles = new File(Filesystem.getDeployDirectory(), "pathplanner/autos").listFiles();

    if (autoFiles == null) {
      return new ArrayList<>();
    }

    return Stream.of(autoFiles)
        .filter(file -> !file.isDirectory())
        .map(File::getName)
        .filter(name -> name.endsWith(".auto"))
        .map(name -> name.substring(0, name.lastIndexOf(".")))
        .collect(Collectors.toList());
  }

    /**
   * Create and populate a sendable chooser with all PathPlannerAutos in the project
   *
   * @param defaultAutoName The name of the auto that should be the default option. If this is an
   *     empty string, or if an auto with the given name does not exist, the default option will be
   *     Commands.none()
   * @return SendableChooser populated with all autos
   */
  public static SendableChooser<Command> buildAutoChooserAlliance(String defaultAutoName) {
    if (!AutoBuilder.isConfigured()) {
      throw new RuntimeException(
          "AutoBuilder was not configured before attempting to build an auto chooser");
    }

    SendableChooser<Command> chooser = new SendableChooser<>();
    List<String> autoNames = getAllAutoNames();

    PathPlannerAuto defaultOption = null;
    List<PathPlannerAuto> options = new ArrayList<>();

    for (String autoName : autoNames) {
      PathPlannerAuto auto = new PathPlannerAuto(autoName);

      if (!defaultAutoName.isEmpty() && defaultAutoName.equals(autoName)) {
        defaultOption = auto;
      } else if (autoName.contains("Dont Move")){
        options.add(auto);
      } else if ((DriverStation.getAlliance().get() == Alliance.Blue) && autoName.contains("Blue")){
        options.add(auto);
      } else {
        options.add(auto);
      }
    }

    if (defaultOption == null) {
      chooser.setDefaultOption("None", Commands.none());
    } else {
      chooser.setDefaultOption(defaultOption.getName(), defaultOption);
    }

    options.forEach(auto -> chooser.addOption(auto.getName(), auto));

    return chooser;
  }
}
