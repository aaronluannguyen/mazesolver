package mazes.generators.maze;

import java.util.Random;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
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
        ISet<Wall> untouchableWalls = maze.getUntouchableWalls();
        
        Random rand = new Random();
        ISet<Wall> randomWalls = new ChainedHashSet<Wall>();
        for (Wall wall : walls) {
            if (!untouchableWalls.contains(wall)) {
                double randDist = rand.nextDouble();
                wall.setDistance(randDist);
                randomWalls.add(wall);
                wall.resetDistanceToOriginal();    
            }
        }
        
        Graph<Room, Wall> graph = new Graph<Room, Wall>(rooms, randomWalls);
        ISet<Wall> mstWalls = graph.findMinimumSpanningTree();
        
        return mstWalls;
    }
}
