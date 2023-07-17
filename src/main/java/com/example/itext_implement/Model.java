package com.example.itext_implement;

public class Model {
    private String[][] rowPayee;
    private String totalPayamt;
    private String totalPayamtThai;
    public String[][] getRowPayee() {
        return rowPayee;
    }
    public void setRowPayee(String[][] rowPayee) {
        this.rowPayee = rowPayee;
    }
    public String getTotalPayamt() {
        return totalPayamt;
    }
    public void setTotalPayamt(String totalPayamt) {
        this.totalPayamt = totalPayamt;
    }
    public String getTotalPayamtThai() {
        return totalPayamtThai;
    }
    public void setTotalPayamtThai(String totalPayamtThai) {
        this.totalPayamtThai = totalPayamtThai;
    }
}
