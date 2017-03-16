import java.util.Iterator;

interface MyList<T> extends Iterable<T> {
    int getCount();
    T get(int i);  
    void set(int i, T item);    
    void add(T item);  
    void insert(int i, T item);
    void removeAt(int i);   
    <U> MyList<U> map(Mapper<T, U> f); 
}

interface Mapper<X, Y> {
    Y call(X a);
}