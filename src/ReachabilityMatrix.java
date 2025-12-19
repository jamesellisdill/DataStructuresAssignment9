// Name:		Ellis Dill
// Class:	    CS 3305/W01
// Term:		Fall 2025
// Instructor:  S. Perry
// Assignment:  9
// IDE Name:	IntelliJ

import java.util.Scanner;

public class ReachabilityMatrix
{
    public static void main(String[] args)
    {
        // Creating variables
        Boolean isRunning = true;
        Scanner input = new Scanner(System.in);
        int[][] adjacencyMatrix = null;
        int[][] reachabilityMatrix = null;
        String menu = """
                ------MAIN MENU------
                1. Enter graph data
                2. Print outputs
                3. Exit program

                Enter option number:\s""";

        // While loop to keep the menu showing
        while (isRunning)
        {
            System.out.print(menu);

            String menuOption = input.nextLine();

            switch (menuOption)
            {
                // Enter graph data
                case "1":
                    adjacencyMatrix = enterGraphData();
                    break;
                // Print outputs
                case "2":
                    try
                    {
                        printMatrix(adjacencyMatrix);
                        System.out.println();
                        reachabilityMatrix = printReachabilityMatrix(adjacencyMatrix);
                        System.out.println();
                        printDegrees(adjacencyMatrix);
                        System.out.println();
                        printOutDegrees(adjacencyMatrix);
                        System.out.println();
                        printSelfLoops(adjacencyMatrix);
                        System.out.println();
                        printCycles(adjacencyMatrix);
                        System.out.println();
                        printPathsOfLength1(adjacencyMatrix);
                        System.out.println();
                        printPathsOfLengthN(adjacencyMatrix);
                        System.out.println();
                        printPathsOfLength1ToN(reachabilityMatrix);
                        System.out.println();
                        printCyclesOfLength1ToN(reachabilityMatrix);
                        System.out.println();
                        break;
                    }
                    catch (NullPointerException e)  // Exception handling so users must enter 1 first
                    {
                        System.out.print("Error: Please enter graph data. (Option 1)\n");
                        break;
                    }
                // Exit program
                case "3":
                    isRunning = false;
                    break;
                default:
                    System.out.print("\nInvalid option. Try again.\n");
            }

            // Extra whitespace for readability
            System.out.println();
        }
    }

    // Allows the user to enter the number of nodes and their reachability
    public static int[][] enterGraphData()
    {
        Scanner input = new Scanner(System.in);
        int value;
        int numberOfNodes = 0;
        final int MAX_SIZE = 5;

        // Enter number of nodes
        while (true)
        {
            System.out.print("Enter the number of nodes in the graph (no more than 5): ");

            numberOfNodes = input.nextInt();

            if (numberOfNodes <= MAX_SIZE) break;

            System.out.print("Size must be no more than 5.\n");
        }

        // Create adjacency matrix with number of nodes
        int[][] adjacencyMatrix = new int[numberOfNodes][numberOfNodes];

        // Add 0 or 1 to each index (up to user)
        for (int i = 0; i < numberOfNodes; i++)
        {
            for (int j = 0; j <numberOfNodes; j++)
            {
                System.out.printf("\nEnter A1[%d,%d]: ", i, j);
                value = input.nextInt();
                adjacencyMatrix[i][j] = value;
            }
        }
        
        return adjacencyMatrix;
    }

    // Print out the input matrix
    public static void printMatrix(int[][] adjacencyMatrix)
    {
        int size = adjacencyMatrix.length;

        System.out.print("Input Matrix\n");

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                System.out.print(adjacencyMatrix[i][j] + "  ");
            }

            System.out.print("\n");
        }
    }

    // Compute and print out the graph reachability matrix
    public static int[][] printReachabilityMatrix(int[][] adjacencyMatrix)
    {
        int size = adjacencyMatrix.length;
        int[][] sum = new int[size][size];  // A + A^2 + A^3
        int[][] current = adjacencyMatrix;  // start with A^1

        for (int p = 1; p <= size; p++) {

            // Add current power into the sum
            for (int i = 0; i < size; i++)
                {
                for (int j = 0; j < size; j++)
                {
                    sum[i][j] += current[i][j];
                }
            }

            // Compute the next A^(p+1)
            current = multiplyMatrix(current, adjacencyMatrix);
        }

        // Print reachability matrix
        System.out.print("Reachability Matrix\n");

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                System.out.print(sum[i][j] + "  ");
            }

            System.out.print("\n");
        }

        return sum; // Return the reachability matrix for later methods
    }

    // Multiply the matrices to find the reachability matrix
    public static int[][] multiplyMatrix(int[][] A, int[][] B)
    {
        int n = A.length;
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int sum = 0;
                for (int k = 0; k < n; k++)
                {
                    sum += A[i][k] * B[k][j];
                }
                result[i][j] = sum;
            }
        }

    return result;
}

    // Compute and print out the In-degree of each node of the graph
    public static void printDegrees(int[][] adjacencyMatrix)
    {
        int size = adjacencyMatrix.length;

        System.out.print("In-degrees:\n");

        for (int i = 0; i < size; i++)
        {
            int sum = 0;
            for (int j = 0; j < size; j++)
            {
                sum += adjacencyMatrix[j][i];
            }
            System.out.printf("Node %d in-degree is %d\n", (i + 1), sum);
        }
    }

    // Compute and print out the Out-degree for each node of the graph
    public static void printOutDegrees(int[][] adjacencyMatrix)
    {
        int size = adjacencyMatrix.length;

        System.out.print("Out-degrees:\n");

        for (int i = 0; i < size; i++)
        {
            int sum = 0;
            for (int j = 0; j < size; j++)
            {
                sum += adjacencyMatrix[i][j];
            }
            System.out.printf("Node %d out-degree is %d\n", (i + 1), sum);
        }
    }

    // Compute and print out the total number of loops (also known as self-loops) in the graph
    public static void printSelfLoops(int[][] adjacencyMatrix)
    {
        int size = adjacencyMatrix.length;
        int sum = 0;

        for (int i = 0; i < size; i++)
        {
            if (adjacencyMatrix[i][i] == 1)
            {
                sum++;
            }
        }

        System.out.printf("Total number of self-loops: %d", sum);
    }

    // Compute and print out the total number of cycles of length N edges (N is the number of nodes in the inputted graph)
    public static void printCycles(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;

        // Make a copy of A to begin exponentiation
        int[][] power = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, power[i], 0, n);
        }

        // Multiply A by itself (n-1) more times to compute A^n
        for (int p = 1; p < n; p++) {
            power = multiplyMatrix(power, adjacencyMatrix);
        }

        // Sum diagonal entries (closed walks of length n)
        int totalCycles = 0;
        for (int i = 0; i < n; i++) {
            totalCycles += power[i][i];
        }

        System.out.print("Total number of cycles of length " + n + " edges: " + totalCycles);
    }

    // Compute and print out the total number of paths of length 1 edge
    public static void printPathsOfLength1(int[][] adjacencyMatrix)
    {
        int size = adjacencyMatrix.length;
        int sum = 0;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (adjacencyMatrix[i][j] == 1)
                {
                    sum++;
                }
            }
        }

        System.out.printf("Total number of paths of length 1 edge: %d", sum);
    }

    // Compute and print out the total number of paths of length N edges (N is the number of nodes in the inputted graph)
    public static void printPathsOfLengthN(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;

        // Compute A^n just like in the cycle method
        int[][] power = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, power[i], 0, n);
        }

        for (int p = 1; p < n; p++) {
            power = multiplyMatrix(power, adjacencyMatrix);
        }

        // Sum ALL entries (all walks of length n)
        int totalPaths = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                totalPaths += power[i][j];
            }
        }

        System.out.print("Total number of paths of length " + n + " edges: " + totalPaths);
    }

    // Compute and print out the total number of paths of length 1 to N edges (N is the number of nodes in the inputted graph)
    public static void printPathsOfLength1ToN(int[][] reachabilityMatrix)
    {
        int size = reachabilityMatrix.length;
        int sum = 0;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                sum += reachabilityMatrix[i][j];
            }
        }

        System.out.printf("Total number of cycles of length 1 to %d edges: %d", size, sum);
    }

    // Compute and print out the total number of cycles length 1 to N edges (N is the number of nodes in the inputted graph)
    public static void printCyclesOfLength1ToN(int[][] reachabilityMatrix)
    {
        int size = reachabilityMatrix.length;
        int sum = 0;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (i == j) sum += reachabilityMatrix[i][j];
            }
        }

        System.out.printf("Total number of cycles of length 1 to %d edges: %d", size, sum);
    }
}