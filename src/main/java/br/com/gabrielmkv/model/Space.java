package br.com.gabrielmkv.model;

public class Space {
    
    private Integer actualNum;
    private int expectedNum;
    private boolean fixed;

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

    public void setExpectedNum(int expectedNum){
        this.expectedNum = expectedNum;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed){
        this.fixed = fixed;
        this.actualNum = fixed ? expectedNum : null;
    }

    public void clearSpace(){
        setActualNum(null);
    }
}
