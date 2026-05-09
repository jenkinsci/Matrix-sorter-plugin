package org.jenkinsci.plugin.matrixconfigsorter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.matrix.MatrixConfiguration;
import org.junit.jupiter.api.Test;

public class LongestBuildFirstSorterTest {

    @Test
    public void sortsByEstimatedDurationDescending() {
        LongestBuildFirstSorter sorter = new LongestBuildFirstSorter();

        MatrixConfiguration longRun = config(30L, "longRun");
        MatrixConfiguration shortRun = config(10L, "shortRun");

        assertTrue(sorter.compare(longRun, shortRun) < 0);
        assertTrue(sorter.compare(shortRun, longRun) > 0);
    }

    @Test
    public void breaksTiesByDisplayNameDescending() {
        LongestBuildFirstSorter sorter = new LongestBuildFirstSorter();

        MatrixConfiguration alpha = config(10L, "alpha");
        MatrixConfiguration beta = config(10L, "beta");

        assertTrue(sorter.compare(beta, alpha) < 0);
        assertTrue(sorter.compare(alpha, beta) > 0);
    }

    private static MatrixConfiguration config(long estimatedDuration, String displayName) {
        MatrixConfiguration configuration = mock(MatrixConfiguration.class);
        when(configuration.getEstimatedDuration()).thenReturn(estimatedDuration);
        when(configuration.getDisplayName()).thenReturn(displayName);
        return configuration;
    }
}
