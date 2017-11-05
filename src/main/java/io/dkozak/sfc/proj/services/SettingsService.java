package io.dkozak.sfc.proj.services;

import lombok.Data;

@Data
public class SettingsService {

    public static final int DEFAULT_GRAPH_MINIMUM_X = 0;
    public static final int DEFAULT_GRAPH_MAXIMUM_X = 100;

    private int graphMinimumX = DEFAULT_GRAPH_MINIMUM_X;
    private int graphMaximumX = DEFAULT_GRAPH_MAXIMUM_X;
}
