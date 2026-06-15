package org.jenkinsci.plugin.matrixconfigsorter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.matrix.MatrixConfiguration;
import org.junit.jupiter.api.Test;

public class ShortestBuildFirstSorterTest {

    @Test
    public void sortsByEstimatedDurationAscending() {
        ShortestBuildFirstSorter sorter = new ShortestBuildFirstSorter();

        MatrixConfiguration fast = config(10L, "fast");
        MatrixConfiguration slow = config(30L, "slow");

        assertTrue(sorter.compare(fast, slow) < 0);
        assertTrue(sorter.compare(slow, fast) > 0);
    }

    @Test
    public void breaksTiesByDisplayNameAscending() {
        ShortestBuildFirstSorter sorter = new ShortestBuildFirstSorter();

        MatrixConfiguration alpha = config(10L, "alpha");
        MatrixConfiguration beta = config(10L, "beta");

        assertTrue(sorter.compare(alpha, beta) < 0);
        assertTrue(sorter.compare(beta, alpha) > 0);
    }

    private static MatrixConfiguration config(long estimatedDuration, String displayName) {
        MatrixConfiguration configuration = mock(MatrixConfiguration.class);
        when(configuration.getEstimatedDuration()).thenReturn(estimatedDuration);
        when(configuration.getDisplayName()).thenReturn(displayName);
        return configuration;
    }
}
