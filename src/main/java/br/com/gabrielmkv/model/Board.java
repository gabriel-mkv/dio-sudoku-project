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
    
    private final int size;
    private final List<List<Space>> spaces = new ArrayList<>();

    public Board(int size) {
        this.size = size;
        initializeEmptyGrid(size);
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public int getSize() {
        return size;
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
        for (int col = 0; col < size; col++){
            List<Space> column = new ArrayList<>();

            for (int row = 0; row < size; row++) {
                column.add(new Space(0, false));
            }

            this.spaces.add(column);
        }
    }
}
