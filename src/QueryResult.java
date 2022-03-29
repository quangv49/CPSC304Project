package src;

public class QueryResult {
    private String[] colNames;
    private Object[][] tuples;

    public QueryResult(int colNum, int tupleNum) {
        colNames = new String[colNum];
        tuples = new Object[tupleNum][colNum];
    }

    public QueryResult(String[] colNames, Object[][] tuples) {
        setColNames(colNames);
        setTuples(tuples);
    }

    public void setColName(int idx, String name) {
        colNames[idx-1] = name;
    }

    public void setTupleEntry(int tupleIdx, int colIdx, String name) {
        tuples[tupleIdx - 1][colIdx - 1] = name;
    }

    public void setColNames(String[] colNames) {
        this.colNames = colNames;
    }

    public void setTuples(Object[][] tuples) {
        this.tuples = tuples;
    }

    public String[] getColNames() {return colNames;}

    public Object[][] getTuples() {return tuples;}

    public StringBuilder asString() {
        StringBuilder temp = new StringBuilder();
        int numCol = colNames.length;
        int numTuples = tuples.length;

        for (int i = 1; i <= numCol; i++) {
            temp.append(String.format("%-50s", colNames[i-1]));
            if (i == numCol) temp.append("\n");
        }

        for (int i = 1; i <= numTuples; i++) {
            for (int j = 1; j <= numCol; j++) {
                temp.append(String.format("%-50s", tuples[i-1][j-1]));
                if (j == numCol) temp.append("\n");
            }
        }

        return temp;}
}
