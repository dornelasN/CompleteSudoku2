package com.example.enkhturbadamsaikhan.completesudoku;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.david.completesudoku.Cell;
import com.david.completesudoku.Sudoku;
import com.david.completesudoku.SudokuGame;
import com.david.completesudoku.SudokuGame.*;
import com.david.completesudoku.SudokuModel;

import com.example.david.testsudoku.GameActivity;
import com.example.david.testsudoku.inputmethod.InputMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class FirebaseModel implements SudokuModel {

    private static final String TAG = "Firebase";

    private DatabaseReference ref;

    public FirebaseModel(String reference) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(reference).child("sudokus");
        String key = db.push().getKey();
        ref = db.child(key);
    }

    public FirebaseModel(DatabaseReference ref) {
        this.ref = ref;
    }

    public static SaveGame createSaveGame(SudokuGame sg) {
        SaveGame game = new SaveGame();

        Sudoku sudoku = sg.getSudoku();
        int length = sudoku.getLength();

        game.setName(sg.getName());
        game.setDifficulty(sg.getDifficulty());
        game.setStatus(sg.getStatus());

        game.setScore(sg.getScore());
        game.setAnswers(boolArray2String(sg.getAnswers()));

        game.setErrors(bool2dArray2String(sg.getErrors()));
        List<Boolean> hints = new ArrayList<Boolean>();
        boolean[] old = sg.getHints();
        for (int i = 0; i < old.length; i++) {
            hints.add(old[i]);
        }
        game.setHints(hints);

        game.setCurrentTime(sg.getCurrentTime());
        game.setElapsed(sg.getElapsed());

        game.setHighlighted(bool2dArray2String(sg.getHighlighted()));

        ArrayList<Integer> value = new ArrayList<>(length*length);
        boolean[] given = new boolean[length*length];
        boolean[][] possible = new boolean[length*length][length];
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                int id = i*length+j;
                Cell c = sudoku.getCell(i, j);
                value.add(c.getValue());
                given[id] = c.isGiven();
                possible[id] = c.getPossibilities();
            }
        }

        game.setValue(value);
        game.setGiven(boolArray2String(given));
        game.setPossible(bool2dArray2String(possible));

        List<Map<String, Map<String, String>>> undo = getListFromActions(sg.getUndo());
        List<Map<String, Map<String, String>>> redo = getListFromActions(sg.getRedo());
        game.setUndo(undo);
        game.setRedo(redo);

        return game;
    }

    private static List<Map<String, Map<String, String>>> getListFromActions(Deque<ActionPair> stack) {
        List<Map<String, Map<String, String>>> map = new ArrayList<>();
        Iterator<ActionPair> it = stack.iterator();
        while (it.hasNext()) {
            ActionPair ap = it.next();
            Map<String, Map<String, String>> pair = new HashMap<>();
            Map<String, String> action = getMapFromAction(ap.getAction());
            pair.put("action", action);
            action = getMapFromAction(ap.getReverse());
            pair.put("reverse", action);
            map.add(pair);
        }
        return map;
    }

    private static Map<String, String> getMapFromAction(Action action) {
        Map<String, String> map = new HashMap<>();
        if (action instanceof SetCellAction) {
            SetCellAction a = (SetCellAction)action;
            map.put("name", "SetCellAction");
            map.put("targetI", String.valueOf(a.getTargetI()));
            map.put("targetJ", String.valueOf(a.getTargetJ()));
            map.put("value", String.valueOf(a.getValue()));
        } else if (action instanceof SetPossibilityAction) {
            SetPossibilityAction a = (SetPossibilityAction)action;
            map.put("name", "SetPossibilityAction");
            map.put("targetI", String.valueOf(a.getTargetI()));
            map.put("targetJ", String.valueOf(a.getTargetJ()));
            map.put("value", String.valueOf(a.getValue()));
            map.put("possible", String.valueOf(a.isPossible()));
        } else if (action instanceof FillCellAction) {
            FillCellAction a = (FillCellAction)action;
            map.put("name", "FillCellAction");
            map.put("targetI", String.valueOf(a.getTargetI()));
            map.put("targetJ", String.valueOf(a.getTargetJ()));
            map.put("value", String.valueOf(a.getValue()));
            map.put("possibles", Arrays.toString(a.getPossibles()).replaceAll("[,\\[\\]]", ""));
        } else if (action instanceof SetHighlightedAction) {
            SetHighlightedAction a = (SetHighlightedAction)action;
            map.put("name", "SetHighlightedAction");
            map.put("targets", Arrays.toString(a.getTargets().toArray()).replaceAll("[,\\[\\]]", ""));
            map.put("isIsHighlighted", String.valueOf(a.isIsHighlighted()));
        } else if (action instanceof ShowPossibilitiesAction) {
            map.put("name", "ShowPossibilitiesAction");
        } else if (action instanceof RemovePossibilitiesAction) {
            map.put("name", "RemovePossibilitiesAction");
        } else if (action instanceof FillPossibilitiesAction) {
            FillPossibilitiesAction a = (FillPossibilitiesAction)action;
            map.put("name", "FillPossibilitiesAction");
            map.put("possibles", bool2dArray2String(a.getPossibles()));
        }
        return map;
    }

    public static SudokuGame createSudokuGame(SaveGame sg) {
        int length = (int)Math.sqrt(sg.getValue().size());
        List<Integer> a = sg.getValue();
        int[] value = new int[a.size()];
        for (int i = 0; i < value.length; i++) {
            value[i] = a.get(i);
        }

        boolean[] given = string2BoolArray(sg.getGiven(), length*length);
        boolean[][] possible = string2Bool2dArray(sg.getPossible(), length*length, length);

        boolean[][] highlighted = string2Bool2dArray(sg.getHighlighted(), length, length);

        boolean[] answers = string2BoolArray(sg.getAnswers(), length*length);
        boolean[][] errors = string2Bool2dArray(sg.getErrors(), length*length, length);

        List<Boolean> b = sg.getHints();
        boolean[] hints = new boolean[b.size()];
        for (int i = 0; i < hints.length; i++) {
            hints[i] = b.get(i);
        }

        SudokuGame game = new SudokuGame(new Sudoku(value, given, possible),
                highlighted, sg.getCurrentTime(), sg.getElapsed(), sg.getName(),
                sg.getDifficulty(), sg.getStatus(), sg.getScore(), answers, errors, hints);
        if (sg.getUndo() != null) {
            game.setUndo(getActionsFromList(sg.getUndo(), game));
        }
        if (sg.getRedo() != null) {
            game.setRedo(getActionsFromList(sg.getRedo(), game));
        }
        return game;
    }

    private static Deque<ActionPair> getActionsFromList(List<Map<String, Map<String, String>>> list, SudokuGame game) {
        Deque<ActionPair> stack = new ArrayDeque<>();
        Iterator<Map<String, Map<String, String>>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Map<String, String>> map = it.next();
            Action action = getActionFromMap(map.get("action"), game);
            Action reverse = getActionFromMap(map.get("reverse"), game);
            stack.add(game.new ActionPair(action, reverse));
        }
        return stack;
    }

    private static Action getActionFromMap(Map<String, String> map, SudokuGame game) {
        Action a = null;
        String name = map.get("name");
        if (name.equals("SetCellAction")) {
            a = game.new SetCellAction(Integer.parseInt(map.get("targetI")),
                    Integer.parseInt(map.get("targetJ")),
                    Integer.parseInt(map.get("value")));
        } else if (name.equals("SetPossibilityAction")) {
            a = game.new SetPossibilityAction(Integer.parseInt(map.get("targetI")),
                    Integer.parseInt(map.get("targetJ")),
                    Integer.parseInt(map.get("value")),
                    Boolean.parseBoolean(map.get("possible")));
        } else if (name.equals("FillCellAction")) {
            String[] bools = map.get("possibles").split(" ");
            boolean[] possibles = new boolean[bools.length];
            for (int i = 0; i < possibles.length; ++i) {
                possibles[i] = Boolean.parseBoolean(bools[i]);
            }
            a = game.new FillCellAction(Integer.parseInt(map.get("targetI")),
                    Integer.parseInt(map.get("targetJ")),
                    Integer.parseInt(map.get("value")), possibles);
        } else if (name.equals("SetHighlightedAction")) {
            String[] ints = map.get("targets").split(" ");
            List<Integer> targets = new ArrayList<>(ints.length);
            for (int i = 0; i < ints.length; ++i) {
                targets.add(Integer.parseInt(ints[i]));
            }
            a = game.new SetHighlightedAction(targets,
                    Boolean.parseBoolean(map.get("isIsHighlighted")));
        } else if (name.equals("ShowPossibilitiesAction")) {
            a = game.new ShowPossibilitiesAction();
        } else if (name.equals("RemovePossibilitiesAction")) {
            a = game.new RemovePossibilitiesAction();
        } else if (name.equals("FillPossibilitiesAction")) {
            int length = game.getLength();
            a = game.new FillPossibilitiesAction(string2Bool2dArray(map.get("possibles"), length*length, length));
        }
        return a;
    }

    private static String boolArray2String(boolean[] array) {
        BitSet bs = new BitSet(array.length);
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                bs.set(i);
            }
        }
        return Base64.encodeToString(bs.toByteArray(), Base64.DEFAULT);
    }

    private static String bool2dArray2String(boolean[][] array) {
        BitSet bs = new BitSet(array.length*array[0].length);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j]) {
                    bs.set(i*array[i].length+j);
                }
            }
        }
        return Base64.encodeToString(bs.toByteArray(), Base64.DEFAULT);
    }

    private static boolean[] string2BoolArray(String string, int length) {
        BitSet bs = BitSet.valueOf(Base64.decode(string, Base64.DEFAULT));
        boolean[] array = new boolean[length];
        for (int i = 0; i < length; i++) {
            if (i == bs.length())
                return array;
            array[i] = bs.get(i);
        }
        return array;
    }

    private static boolean[][] string2Bool2dArray(String string, int length, int length2) {
        BitSet bs = BitSet.valueOf(Base64.decode(string, Base64.DEFAULT));
        boolean[][] array = new boolean[length][length2];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length2; j++) {
                if (i*length2+j == bs.length())
                    return array;
                array[i][j] = bs.get(i*length2+j);
            }
        }
        return array;
    }

    @Override
    public void saveGame(SudokuGame sudokuGame, Object param) {
        final Handler saveHandler = (Handler) param;
        SaveGame sg = createSaveGame(sudokuGame);
        ref.setValue(sg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    saveHandler.sendEmptyMessage(GameActivity.SAVE_SUCCESS);
                } else {
                    Log.d(TAG, task.getException().getMessage());
                    saveHandler.sendEmptyMessage(GameActivity.SAVE_FAILURE);
                }
            }
        });
    }

    private SudokuGame temp;

    //synchronous
    @Override
    public SudokuGame loadGame(Object param) {
        final Semaphore semaphore = new Semaphore(0);
        temp = null;
        try {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    try {
                        temp = createSudokuGame(dataSnapshot.getValue(SaveGame.class));
                    } catch (Exception e) {
                        Log.d(TAG, "error constructing game from save: "+e.getMessage());
                    }
                    semaphore.release();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                    semaphore.release();
                }
            });
            semaphore.acquire();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return temp;
    }
}
