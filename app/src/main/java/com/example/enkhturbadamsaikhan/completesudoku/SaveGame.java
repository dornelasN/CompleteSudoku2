package com.example.enkhturbadamsaikhan.completesudoku;

import java.util.List;
import java.util.Map;

public class SaveGame {
    private String name;
    private String difficulty;
    private String status;

    private int score;
    private String answers;
    private String errors;
    private List<Boolean> hints;

    private long currentTime;
    private long elapsed;

    private String highlighted;

    private List<Integer> value;
    private String given;
    private String possible;

    private List<Map<String, Map<String, String>>> undo;
    private List<Map<String, Map<String, String>>> redo;

    public long getCurrentTime() {
        return currentTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public long getElapsed() {
        return elapsed;
    }

    public String getGiven() {
        return given;
    }

    public String getHighlighted() {
        return highlighted;
    }

    public String getName() {
        return name;
    }

    public String getPossible() {
        return possible;
    }

    public List<Map<String, Map<String, String>>> getRedo() {
        return redo;
    }

    public int getScore() {
        return score;
    }

    public String getErrors() {
        return errors;
    }

    public String getAnswers() {
        return answers;
    }

    public String getStatus() {
        return status;
    }

    public List<Map<String, Map<String, String>>> getUndo() {
        return undo;
    }

    public List<Integer> getValue() {
        return value;
    }

    public List<Boolean> getHints() {
        return hints;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public void setHighlighted(String highlighted) {
        this.highlighted = highlighted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPossible(String possible) {
        this.possible = possible;
    }

    public void setRedo(List<Map<String, Map<String, String>>> redo) {
        this.redo = redo;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUndo(List<Map<String, Map<String, String>>> undo) {
        this.undo = undo;
    }

    public void setValue(List<Integer> value) {
        this.value = value;
    }

    public void setHints(List<Boolean> hints) {
        this.hints = hints;
    }

}