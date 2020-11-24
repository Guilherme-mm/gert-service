package msh.gert.db.filter.sql;

import msh.gert.db.filter.QueryComponent;

public class SQLFilterCondition implements QueryComponent{
    private String column;
    private String operand;
    private Object value;
    private String logicalOperand;

    public SQLFilterCondition(String column, String operand, Object value, String logicalOperand) {
        this.column = column;
        this.operand = operand;
        this.value = value;
        this.logicalOperand = logicalOperand;
    }

    @Override
    public String getSQL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.column)
                    .append(" ")
                    .append(this.operand)
                    .append(" ");

        if(this.value instanceof String) {
            stringBuilder.append("'")
                        .append((String)this.value)
                        .append("'");
        } else if(this.value instanceof Integer || this.value instanceof Float || this.value instanceof Double){
            stringBuilder.append(this.value);
        }

        if(this.logicalOperand != null) {
            stringBuilder.append(" ")
                        .append(this.logicalOperand);
        }

        return stringBuilder.toString();
    }
}
