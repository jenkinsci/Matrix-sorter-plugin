package org.jenkinsci.plugin.matrixconfigsorter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.matrix.AxisList;
import hudson.matrix.Combination;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixProject;
import hudson.matrix.TextAxis;
import hudson.util.FormValidation;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class AxisOrderSorterTest {

    @Test
    public void fallbackToLastAxisWhenAxisNamesEmpty() {
        AxisOrderSorter sorter = new AxisOrderSorter();
        AxisList axes = new AxisList(
                new TextAxis("BROWSER", "chrome", "firefox"), new TextAxis("PLATFORM", "linux", "windows"));

        MatrixConfiguration linux = config(axes, mapOf("BROWSER", "firefox", "PLATFORM", "linux"), "linux");
        MatrixConfiguration windows = config(axes, mapOf("BROWSER", "chrome", "PLATFORM", "windows"), "windows");

        assertTrue(sorter.compare(linux, windows) < 0, "Expected linux before windows based on last axis order");
    }

    @Test
    public void sortsBySingleConfiguredAxis() {
        AxisOrderSorter sorter = new AxisOrderSorter();
        sorter.setAxisNames("BROWSER");

        AxisList axes = new AxisList(
                new TextAxis("BROWSER", "chrome", "firefox"), new TextAxis("PLATFORM", "linux", "windows"));

        MatrixConfiguration chrome = config(axes, mapOf("BROWSER", "chrome", "PLATFORM", "windows"), "chrome");
        MatrixConfiguration firefox = config(axes, mapOf("BROWSER", "firefox", "PLATFORM", "linux"), "firefox");

        assertTrue(
                sorter.compare(chrome, firefox) < 0, "Expected chrome before firefox based on configured BROWSER axis");
    }

    @Test
    public void sortsByMultipleConfiguredAxesInPriorityOrder() {
        AxisOrderSorter sorter = new AxisOrderSorter();
        sorter.setAxisNames("BROWSER, PLATFORM");

        AxisList axes = new AxisList(
                new TextAxis("BROWSER", "chrome", "firefox"), new TextAxis("PLATFORM", "linux", "windows"));

        MatrixConfiguration c1 = config(axes, mapOf("BROWSER", "chrome", "PLATFORM", "windows"), "c1");
        MatrixConfiguration c2 = config(axes, mapOf("BROWSER", "chrome", "PLATFORM", "linux"), "c2");

        assertTrue(sorter.compare(c2, c1) < 0, "Expected PLATFORM to break ties after BROWSER");
    }

    @Test
    public void ignoresSpacesAndEmptyTokensInAxisNames() {
        AxisOrderSorter sorter = new AxisOrderSorter();
        sorter.setAxisNames(" BROWSER , , PLATFORM ");

        AxisList axes = new AxisList(
                new TextAxis("BROWSER", "chrome", "firefox"), new TextAxis("PLATFORM", "linux", "windows"));

        MatrixConfiguration linux = config(axes, mapOf("BROWSER", "chrome", "PLATFORM", "linux"), "linux");
        MatrixConfiguration windows = config(axes, mapOf("BROWSER", "chrome", "PLATFORM", "windows"), "windows");

        assertTrue(sorter.compare(linux, windows) < 0, "Expected PLATFORM comparison after trimmed/filtered tokens");
    }

    @Test
    public void validateFailsForUnknownAxisNames() {
        AxisOrderSorter sorter = new AxisOrderSorter();
        sorter.setAxisNames("UNKNOWN");

        MatrixProject project = mock(MatrixProject.class);
        when(project.getAxes()).thenReturn(new AxisList(new TextAxis("BROWSER", "chrome", "firefox")));

        try {
            sorter.validate(project);
            fail("Expected FormValidation error for unknown axis name");
        } catch (FormValidation expected) {
            assertEquals(Messages.AxisOrderSorter_Validation_UnknownAxisNames("UNKNOWN"), expected.getMessage());
        }
    }

    private static MatrixConfiguration config(AxisList axes, Map<String, String> values, String displayName) {
        MatrixProject project = mock(MatrixProject.class);
        when(project.getAxes()).thenReturn(axes);

        Combination combination = mock(Combination.class);
        when(combination.get(org.mockito.ArgumentMatchers.anyString()))
                .thenAnswer(invocation -> values.get(invocation.getArgument(0)));

        MatrixConfiguration config = mock(MatrixConfiguration.class);
        when(config.getParent()).thenReturn(project);
        when(config.getCombination()).thenReturn(combination);
        when(config.getDisplayName()).thenReturn(displayName);
        return config;
    }

    private static Map<String, String> mapOf(String k1, String v1, String k2, String v2) {
        Map<String, String> values = new HashMap<>();
        values.put(k1, v1);
        values.put(k2, v2);
        return values;
    }
}
