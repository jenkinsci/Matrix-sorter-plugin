package org.jenkinsci.plugin.matrixconfigsorter;

import hudson.Extension;
import hudson.matrix.Axis;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixConfigurationSorter;
import hudson.matrix.MatrixConfigurationSorterDescriptor;
import hudson.matrix.MatrixProject;
import hudson.util.FormValidation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Sort configurations {@link MatrixConfiguration} of matrix job {@link MatrixProject}
 * by order of values in configured axes (or by the last axis when not configured).
 */
@Extension
public class AxisOrderSorter extends MatrixConfigurationSorter {
    private String axisNames;

    @Override
    public int compare(MatrixConfiguration configuration1, MatrixConfiguration configuration2) {
        int compare = 0;
        List<Axis> projectAxes = configuration1.getParent().getAxes();
        if (!projectAxes.isEmpty()) {
            List<Axis> axesForSorting = getAxesForSorting(projectAxes);
            for (Axis axis : axesForSorting) {
                List<String> axisValues = axis.getValues();
                compare = Integer.compare(
                        axisValues.indexOf(configuration1.getCombination().get(axis.getName())),
                        axisValues.indexOf(configuration2.getCombination().get(axis.getName())));
                if (compare != 0) {
                    break;
                }
            }
        }
        if (compare == 0) {
            compare = configuration1.getDisplayName().compareTo(configuration2.getDisplayName());
        }
        return compare;
    }

    @DataBoundConstructor
    public AxisOrderSorter() {}

    @DataBoundSetter
    public void setAxisNames(String axisNames) {
        this.axisNames = axisNames;
    }

    public String getAxisNames() {
        return axisNames;
    }

    private List<Axis> getAxesForSorting(List<Axis> projectAxes) {
        List<String> configuredAxisNames = getConfiguredAxisNames();
        if (configuredAxisNames.isEmpty()) {
            return Collections.singletonList(projectAxes.get(projectAxes.size() - 1));
        }

        Map<String, Axis> axisByName = new HashMap<>();
        for (Axis axis : projectAxes) {
            axisByName.put(axis.getName(), axis);
        }

        List<Axis> axesForSorting = new ArrayList<>();
        for (String axisName : configuredAxisNames) {
            Axis axis = axisByName.get(axisName);
            if (axis != null) {
                axesForSorting.add(axis);
            }
        }
        if (axesForSorting.isEmpty()) {
            return Collections.singletonList(projectAxes.get(projectAxes.size() - 1));
        }
        return axesForSorting;
    }

    private List<String> getConfiguredAxisNames() {
        if (axisNames == null || axisNames.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(axisNames.split(","))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .toList();
    }

    @Override
    public void validate(MatrixProject p) throws FormValidation {
        if (p.getAxes().isEmpty()) {
            throw FormValidation.error(Messages.AxisOrderSorter_Validation_NeedsAxis());
        }

        if (axisNames == null || axisNames.trim().isEmpty()) {
            return;
        }

        Set<String> projectAxisNames =
                p.getAxes().stream().map(Axis::getName).collect(Collectors.toCollection(HashSet::new));
        List<String> invalidAxisNames = getConfiguredAxisNames().stream()
                .filter(axisName -> !projectAxisNames.contains(axisName))
                .toList();
        if (!invalidAxisNames.isEmpty()) {
            throw FormValidation.error(
                    Messages.AxisOrderSorter_Validation_UnknownAxisNames(String.join(", ", invalidAxisNames)));
        }
    }

    @Extension
    public static class DescriptorImpl extends MatrixConfigurationSorterDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.AxisOrderSorter_DisplayName();
        }
    }
}
