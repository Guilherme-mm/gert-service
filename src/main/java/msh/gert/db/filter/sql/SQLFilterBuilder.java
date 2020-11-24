package msh.gert.db.filter.sql;

import msh.gert.db.filter.QueryComponent;

public class SQLFilterBuilder {
    private SQLFilter filter;

    public SQLFilterBuilder() {
        this.filter = new SQLFilter();
    }

    public SQLFilterBuilder addCondition(String column, String operand, Object value, String logicalOperand) {
        QueryComponent condition = new SQLFilterCondition(column, operand, value, logicalOperand);
        this.filter.addComponent(condition);
        return this;
    }

    public SQLFilter build() {
        return this.filter;
    }
}
