package msh.gert.db.filter.sql;

import java.util.ArrayList;
import java.util.List;

import msh.gert.db.filter.QueryComponent;
import msh.gert.db.filter.QueryFilter;

public class SQLFilter implements QueryComponent, QueryFilter{
    private List<QueryComponent> components;

    public SQLFilter(){
        this.components = new ArrayList<QueryComponent>();
    }
    
    public boolean addComponent(QueryComponent component) {
        return components.add(component);
    }

    @Override
    public String getSQL() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        for (QueryComponent queryComponent : components) {
            stringBuilder.append(queryComponent.getSQL())
                        .append(" ");
        }

        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public String toString(){
        return this.getSQL();
    }
}
