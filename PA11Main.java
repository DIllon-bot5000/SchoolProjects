/*
Author: Dillon Barr
Assigment: PA 11 - Traveling Salesperson
Date: Fall 2019
Course: CSC 210, Software Development
Purpose: This program reads in a graph and ultimately displays the cost associated
with visiting all the "cities" in the graph as well as the travel order. There are 3 different ways
to accomplish this, a heuristic approach, recursive backtracking and a custom approach to recursive
backtracking with a little more pruning. There is also a time command that runs all the methods and displays
how long each took to run.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PA11Main {
    public static void main(String[] args) {

        DGraph graph = createGraph(args);
        if (args[1].equals("HEURISTIC")) {
            Trip trip = heuristic(graph);
            System.out.println(trip.toString(graph));
        }
        if (args[1].equals("BACKTRACK")) {
            Trip trip = recursive(graph);
            System.out.println(trip.toString(graph));
        }
        if (args[1].equals("MINE")) {
            Trip trip = mine(graph);
            System.out.println(trip.toString(graph));
        }

        if (args[1].equals("TIME")) {
            long start = System.nanoTime();
            Trip trip = heuristic(graph);
            long end = System.nanoTime();
            long dur = (end - start) / 1000000;
            System.out.println("heuristic: cost = " + trip.tripCost(graph)
                    + ", " + dur + " milliseconds");

            start = System.nanoTime();
            Trip trip1 = mine(graph);
            end = System.nanoTime();
            dur = (end - start) / 1000000;
            System.out.println("mine: cost = " + trip1.tripCost(graph)
                    + ", " + dur + " milliseconds");

            start = System.nanoTime();
            Trip trip2 = recursive(graph);
            end = System.nanoTime();
            dur = (end - start) / 1000000;
            System.out.println("backtracking: cost = " + trip2.tripCost(graph)
                    + ", " + dur + " milliseconds");
        }

    }

    public static Trip heuristic(DGraph graph) {
        // This method contains the heuristic approach to the traveling
        // salesperson problem.
        // The first city is selected as the current and each subsequent city is
        // chosen solely based on
        // if it is available and the immediate closest.
        Trip trip = new Trip(graph.getNumNodes());
        int current = graph.getNode(1);
        int temp = current;
        trip.chooseNextCity(current);
        for (int k = 2; k <= graph.getNumNodes(); k++) {
            List<Integer> list = graph.getNeighbors(current);
            double cost = 10000000000000000000000000.0;
            for (int neigh : list) {
                if (trip.isCityAvailable(neigh)
                        && graph.getWeight(current, neigh) <= cost) {
                    cost = graph.getWeight(current, neigh);
                    temp = neigh;
                }
            }
            current = temp;
            trip.chooseNextCity(current);

        }
        return trip;
    }

    public static Trip recursive(DGraph graph) {
        // This method begins the recursive backtracking approach. It sets the
        // current trip starting point to city 1 and creates a min trip that is
        // just
        // the available cities in sequential order. Then the recursive function
        // is called to
        // find the lowest cost path.
        Trip soFar = new Trip(graph.getNumNodes());
        int current = graph.getNode(1);
        soFar.chooseNextCity(current);
        Trip min = new Trip(graph.getNumNodes());
        for (int i = 1; i <= graph.getNumNodes(); i++) {
            min.chooseNextCity(i);
        }
        recursiveHelp(graph, soFar, min);
        return min;
    }

    public static void recursiveHelp(DGraph graph, Trip soFar, Trip min) {
        // This method finds the lowest costing path between the start point and
        // end by searching
        // through all available options until the lowest is found and then
        // returned. The base case is if
        // all the cities have been visited, if the current trip is lower than
        // the current min trip, it
        // becomes the new min trip and this is repeated until the lowest is
        // found.
        if (soFar.citiesLeft().size() == 0) {
            double cost = soFar.tripCost(graph);
            if (cost < min.tripCost(graph)) {
                min.copyOtherIntoSelf(soFar);
                return;
            }
        }
        if (soFar.tripCost(graph) < min.tripCost(graph)) {
            for (int i : soFar.citiesLeft()) {
                if (soFar.isCityAvailable(i)) {
                    soFar.chooseNextCity(i);
                    recursiveHelp(graph, soFar, min);
                }
                soFar.unchooseLastCity();
            }
        }
    }

    public static Trip mine(DGraph graph) {
        // This method begins the recursive backtracking approach with a
        // modification of my own. It sets the
        // current trip starting point to city 1 and creates a min trip that is
        // just the available cities in sequential order. Then the recursive
        // function
        // is called to find the lowest cost path.
        Trip soFar = new Trip(graph.getNumNodes());
        int current = graph.getNode(1);
        soFar.chooseNextCity(current);
        Trip min = new Trip(graph.getNumNodes());
        for (int i = 1; i <= graph.getNumNodes(); i++) {
            min.chooseNextCity(i);
        }
        mineHelp(graph, soFar, min);
        return min;
    }

    public static void mineHelp(DGraph graph, Trip soFar, Trip min) {
        // This method finds the lowest costing path between the start point and
        // end by searching
        // through all available options until the lowest is found and then
        // returned. The base case is if
        // all the cities have been visited, if the current trip is lower than
        // the current min trip, it
        // becomes the new min trip and this is repeated until the lowest is
        // found. I added an additional check prior to the recursive call to
        // cut down on calls but unfortunately it didn't help speed.
        if (soFar.citiesLeft().size() == 0
                && soFar.tripCost(graph) < min.tripCost(graph)) {
                min.copyOtherIntoSelf(soFar);
            }

        if (soFar.tripCost(graph) < min.tripCost(graph)) {
            for (int i : soFar.citiesLeft()) {
                if (soFar.isCityAvailable(i)) {
                    soFar.chooseNextCity(i);
                    if (soFar.tripCost(graph) < min.tripCost(graph)) {
                        recursiveHelp(graph, soFar, min);
                    }
                }
                soFar.unchooseLastCity();
            }
        }
    }
    public static DGraph createGraph(String[] args) {

        // This method reads in the command line argument containing the graph
        // and then stores the graph in a DGraph object.
        Scanner file = null;
        ArrayList<String[]> list = new ArrayList<String[]>();

        try {
            file = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = file.nextLine().toString();
        boolean comment = true;
        while (comment) {
            line = file.nextLine().toString();
            comment = line.startsWith("%");
        }
        list.add(line.split(" "));

        while (file.hasNextLine()) {
            String[] lines = file.nextLine().split(" +");
            list.add(lines);
        }

        int rows = Integer.valueOf(list.get(0)[0]);
        int columns = Integer.valueOf(list.get(0)[1]);
        int nonZero = Integer.valueOf(list.get(0)[2]);
        DGraph ya = new DGraph(nonZero);

        for (int i = 1; i < list.size(); i++) {
            ya.addEdge(Integer.valueOf(list.get(i)[0]),
                    Integer.valueOf(list.get(i)[1]),
                    Double.valueOf(list.get(i)[2]));
        }
        return ya;
    }
}
