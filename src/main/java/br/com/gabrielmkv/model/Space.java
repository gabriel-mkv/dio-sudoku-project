package br.com.gabrielmkv.model;

public class Space {
    
    private Integer actualNum;
    private final int expectedNum;
    private final boolean fixed;

    public Space(int expectedNum, boolean fixed) {
        this.expectedNum = expectedNum;
        this.fixed = fixed;
        if (fixed) {
            actualNum = expectedNum;
        }
    }

    public Integer getActualNum() {
        return actualNum;
    }

    public void setActualNum(final Integer actualNum){
        if (fixed) return;
        this.actualNum = actualNum;
    }

    public int getExpectedNum() {
        return expectedNum;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void clearSpace(){
        setActualNum(null);
    }
}
