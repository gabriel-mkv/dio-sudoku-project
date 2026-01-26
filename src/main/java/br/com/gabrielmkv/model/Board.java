package br.com.gabrielmkv.model;

import java.util.Collection;
import java.util.List;

import static br.com.gabrielmkv.model.GameStatusEnum.COMPLETE;
import static br.com.gabrielmkv.model.GameStatusEnum.INCOMPLETE;
import static br.com.gabrielmkv.model.GameStatusEnum.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;

public class Board {
    
    private final List<List<Space>> spaces = new ArrayList<>();

    public Board(int size) {
        initializeEmptyGrid(size);
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus(){
        if (spaces.stream()
                    .flatMap(Collection::stream)
                    .noneMatch(s -> !s.isFixed() && nonNull(s.getActualNum()))){
            return NON_STARTED;
        }

        return spaces.stream()
                    .flatMap(Collection::stream)
                    .anyMatch(s -> isNull(s.getActualNum())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors(){
        if (getStatus() == NON_STARTED){
            return false;
        }

        return spaces.stream()
                        .flatMap(Collection::stream)
                        .anyMatch(s -> nonNull(s.getActualNum()) && !s.getActualNum().equals(s.getExpectedNum()));
    }

    public boolean changeValue(final int col, final int row, final int value){
        var space = spaces.get(col).get(row);

        if (space.isFixed()){
            return false;
        }

        space.setActualNum(value);
        return true;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);

        if (space.isFixed()){
            return false;
        }

        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(col -> col.forEach(row -> row.clearSpace()));
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus() == COMPLETE;
    }

    private void initializeEmptyGrid(int size){
        for (int i = 0; i < size; i++){
            List<Space> row = new ArrayList<>();

            for (int j = 0; j < size; j++) {
                row.add(new Space(0, false));
            }

            this.spaces.add(row);
        }
    }

    private static boolean isValid(List<List<Space>> spaces, int row, int col, int value) {
        int sizeBoard = spaces.size();
        int side = (int) Math.sqrt(sizeBoard);
        int startRow = (row / side) * side;
        int startCol = (col / side) * side;

        for (int i = 0; i < sizeBoard; i++) {
            if (spaces.get(row).get(i).getActualNum() == value) {
                return false;
            }

            if (spaces.get(i).get(col).getActualNum() == value) {
                return false;
            }

            if (spaces.get(startRow + (i / side)).get(startCol + (i % side)).getActualNum() == value) {
                return false;
            }
        }
        
        return true;
    }
}
