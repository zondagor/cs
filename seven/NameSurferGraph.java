package com.shpp.p2p.cs.ldebryniuk.assignment7;

/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NameSurferGraph extends GCanvas
        implements NameSurferConstants, ComponentListener {

    private final ArrayList<NameSurferEntry> graphs = new ArrayList<>();
    private final Color[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK};

    /**
     * Creates a new NameSurferGraph object that displays the data.
     */
    public NameSurferGraph() {
        addComponentListener(this);
        // You fill in the rest //
    }


    /**
     * Clears the list of name surfer entries stored inside this class.
     */
    public void clear() {
        graphs.clear();
        update();
    }


    /* Method: addEntry(entry) */

    /**
     * Adds a new NameSurferEntry to the list of entries on the display.
     * Note that this method does not actually draw the graph, but
     * simply stores the entry; the graph is drawn by calling update.
     */
    public void addEntry(NameSurferEntry entry) {
        graphs.add(entry);
        update();
    }


    /**
     * Updates the display image by deleting all the graphical objects
     * from the canvas and then reassembling the display according to
     * the list of entries. Your application must call update after
     * calling either clear or addEntry; update is also called whenever
     * the size of the canvas changes.
     */
    public void update() {
        removeAll();
        final Font LABEL_FONT = new Font("Serif", Font.BOLD, 18);
        final double DECADE_VERTICAL_OFFSET = APPLICATION_HEIGHT * 0.83;
        final int DISTANCE_BETWEEN_LINES = APPLICATION_WIDTH / NDECADES;
        final double DISTANCE_BETWEEN_GRAPH_EDGES = 500;
        final double GRAPH_UPPER_EDGE = APPLICATION_HEIGHT * 0.04;
        final double GRAPH_LOWER_EDGE = GRAPH_UPPER_EDGE + DISTANCE_BETWEEN_GRAPH_EDGES;
//        final double GRAPH_LOWER_EDGE = APPLICATION_HEIGHT * 0.78;
        int decade = START_DECADE;
        double x = 2.0; // pixels

        // add two edges for the graphs
        add(new GLine(0, GRAPH_UPPER_EDGE, APPLICATION_WIDTH, GRAPH_UPPER_EDGE));
        add(new GLine(0, GRAPH_LOWER_EDGE, APPLICATION_WIDTH, GRAPH_LOWER_EDGE));

        for (int i = 0; i < NDECADES; i++) {
            add(new GLine(x, 0, x, APPLICATION_HEIGHT));

            GLabel decadeLabel = new GLabel(String.valueOf(decade), x, DECADE_VERTICAL_OFFSET);
            decadeLabel.setFont(LABEL_FONT);
            add(decadeLabel);

            x += DISTANCE_BETWEEN_LINES;
            decade += 10;
        }

        // add graphs
        int colorIndex = 0;
        for (int i = 0; i < graphs.size(); i++) {
            double x2 = 2.0; // pixels
            NameSurferEntry entry = graphs.get(i);

            for (int j = 0; j < NDECADES - 1; j++) { // draw a graph
                double currentRank = entry.getRank(j);
                double currentRankPercent = currentRank * DISTANCE_BETWEEN_GRAPH_EDGES / MAX_RANK;
                double nextRankPercent = entry.getRank(j + 1) * DISTANCE_BETWEEN_GRAPH_EDGES / MAX_RANK;

                if (currentRankPercent == 0) currentRankPercent = DISTANCE_BETWEEN_GRAPH_EDGES;
                if (nextRankPercent == 0) nextRankPercent = DISTANCE_BETWEEN_GRAPH_EDGES;

                double y = GRAPH_UPPER_EDGE + currentRankPercent;

                String rankName = entry.getName() + " " + (currentRank == 0 ? "*" : currentRank);
                GLabel rankLabel = new GLabel(rankName, x2, y);
                rankLabel.setColor(colors[colorIndex]);
                add(rankLabel);

                GLine line = new GLine(x2, y, x2 + DISTANCE_BETWEEN_LINES, GRAPH_UPPER_EDGE + nextRankPercent);
                line.setColor(colors[colorIndex]);
                add(line);

                x2 += DISTANCE_BETWEEN_LINES;
            }

            colorIndex++;
            if (colorIndex == 4) colorIndex = 0;
        }
    }


    /* Implementation of the ComponentListener interface */
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        update();
    }

    public void componentShown(ComponentEvent e) {
    }
}