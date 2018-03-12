package mazes.generators.maze;

import java.util.Random;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.exceptions.NotYetImplementedException;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        ISet<Room> rooms = maze.getRooms();
        ISet<Wall> walls = maze.getWalls();
        ISet<Wall> randomWalls = new ChainedHashSet<Wall>();
        ISet<Wall> untouchableWalls = maze.getUntouchableWalls();
        ISet<Wall> toRemove = new ChainedHashSet<Wall>();
        Random rand = new Random(); 
                
        for(Wall wall : walls) {
            if(!untouchableWalls.contains(wall)) {
                double randDist = (rand.nextDouble()) * 100;                              
                wall.setDistance(randDist);                
                randomWalls.add(wall);                                                             
            }
        }
        Graph<Room, Wall> graph = new Graph<Room, Wall>(rooms, randomWalls);
        ISet<Wall> mstWalls = graph.findMinimumSpanningTree();
        
        for(Wall wall : randomWalls) {
            wall.resetDistanceToOriginal();
        }
        
        return mstWalls;
    }
}
