package com.david.completesudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author David
 */
public class SudokuGenerator {

    private static final String DEFAULT_GAME = "puzzler.txt";

    public static final String RANDOM = "Random";
    public static final String DIABOLICAL = "Diabolical";
    public static final String EASY = "Easy";
    public static final String HARD = "Hard";
    public static final String MEDIUM = "Medium";
    public static final String ULTRA_EASY = "Ultra Easy";


    private int sudokuNumber;
    private List<int[][]> sudokus;
    private List<String> difficulties;
    private Map<String,ArrayList<Integer>> index;

    public SudokuGenerator() throws Exception{
        this(DEFAULT_GAME);
    }

    public SudokuGenerator(String path) throws Exception{
        this(new FileReader(path));
    }

    public SudokuGenerator(InputStreamReader in) {
        sudokuNumber = 0;
        sudokus = new ArrayList<>();
        difficulties = new ArrayList<>();
        index = new HashMap<>();
        index.put(ULTRA_EASY, new ArrayList<Integer>());
        index.put(EASY, new ArrayList<Integer>());
        index.put(MEDIUM, new ArrayList<Integer>());
        index.put(HARD, new ArrayList<Integer>());
        index.put(DIABOLICAL, new ArrayList<Integer>());

        //from file
        try (BufferedReader br = new BufferedReader(in)) {
            String line;
            int count = 0;
            while((line = br.readLine()) != null){
                if (line.contains("G")) {
                    String[] metaData = line.split(" ");
                    if (metaData.length > 2) {
                        difficulties.add(metaData[2]);
                    }
                    char[][] initialBoard = new char[9][9];
                    for (int i = 0; i < 9; i++) {
                        line = br.readLine();
                        initialBoard[i] = line.toCharArray();
                    }
                    int[][] grid = new int[9][9];
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            grid[i][j] = Character.getNumericValue(initialBoard[i][j]);
                        }
                    }
                    sudokus.add(grid);
                    if (difficulties.size() > count &&  index.containsKey(difficulties.get(count))) {
                        index.get(difficulties.get(count)).add(count);
                    }
                    count++;
                }
            }
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + DEFAULT_GAME + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '" + DEFAULT_GAME + "'");
        }

    }

    public Sudoku getSudoku(String difficulty) {
        if (difficulty.equals(RANDOM)) {
            return new Sudoku(sudokus.get(sudokuNumber++));
        }
        else if (index.containsKey(difficulty)) {
            Random rand = new Random();
            List<Integer> ids = index.get(difficulty);
            return new Sudoku(sudokus.get(ids.get(rand.nextInt(ids.size()))));
        }
        return null;
    }

    public List<int[][]> getTestSudokus() {
        return sudokus;
    }
}
