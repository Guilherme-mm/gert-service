package msh.gert.db;

import msh.gert.db.exceptions.DBException;
import msh.gert.db.filter.QueryFilter;

public interface DataCollection<T> {
    //Data management methods
    public T get(QueryFilter filter) throws DBException, IllegalArgumentException;
    public boolean create(T param) throws DBException;
    
    //Utilitary methods
    public QueryFilter getFilter(T param);
}
