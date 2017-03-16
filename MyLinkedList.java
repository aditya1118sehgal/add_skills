import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.NoSuchElementException;
import java.lang.annotation.Annotation;

@MyAnnotation ( author="aditya" )
class MyLinkedList<T> implements MyList<T> {
	
	private static boolean debug = false;
	private String threadId;
	private static boolean debugIsTurnedOn() {
		return debug;
	}
	private static void debug(String s){
		if(debugIsTurnedOn()){
			System.out.print(s);
		}
	}
	private static void debugln(String s){
		debug(s);
		debug("\n");
	}
	
	public MyLinkedList(String threadId) {
		this();
		this.threadId="["+threadId+"] ";
	}
	
	

	private void myPrint(String s) {
		myPrintln(s+"\n");
	}
	private void myPrintln(String s) {
		System.out.println(this.threadId + s);
	}
	
    protected int size;
    protected Node<T> first;
    
    @MyAnnotation (inner=true)
    protected  class Node<U> {
        public Node<U> next;
        public U item;

        public Node(U item) {
        	Class<MyLinkedList.Node> obj = MyLinkedList.Node.class;
        	if (obj.isAnnotationPresent(MyAnnotation.class)) {
    		Annotation annotation = obj.getAnnotation(MyAnnotation.class);
    		MyAnnotation myAnnotation = (MyAnnotation) annotation;
    		boolean isInner = myAnnotation.inner();
    		
    		myPrintln(String.format("%ncreated %s class Node, Author :%s", (isInner?"inner":"top level" ),myAnnotation.author()));
    	}
            this.item = item;
        }
    }

    public MyLinkedList() {
    	threadId= "";
        first = null;
        size = 0;
    }
    @MyAnnotation ( author="aditya", inner=true )
    private class MyLinkedListIterator implements Iterator<T> {
    Node<T> next;             

		public MyLinkedListIterator() {
			Class<MyLinkedList.MyLinkedListIterator> obj = MyLinkedList.MyLinkedListIterator.class;
			if (obj.isAnnotationPresent(MyAnnotation.class)) {
				Annotation annotation = obj.getAnnotation(MyAnnotation.class);
				MyAnnotation myAnnotation = (MyAnnotation) annotation;
				String myAuthor = myAnnotation.author();
				System.out.printf("%ncreated %s MyLinkedListIterator, Author :%s\n, ", myAnnotation.inner()?"":"inner class" ,myAuthor);
			}
			this.next = first;
		}
		
		public T next() {
		  if (hasNext()) {
			return next.item;
		  } 
		  else 
			throw new NoSuchElementException();
		}
		
		public boolean hasNext() {
		  return this.next != null;
		}
	
		public void remove() {
		  throw new UnsupportedOperationException();
		}
  }
    @Override
    public int getCount() {
        return size;
    }

    @Override
    public T get(int i) {
        int j = 0;
    	Node<T> curr = this.first;
    	while(j < i) {
    		j ++;
    		curr = curr.next;
    	}
    	return curr.item;
    }

    @Override
    public void set(int i, T item) {
    	int j = 0;
    	Node<T> curr = this.first;
    	while(j < i) {
    		j ++;
    		curr = curr.next;
    	}
    	curr.item = item;

    }

    public void add(T item) {
        insert(size, item);
    }

    public void insert(int i, T item) {
    	this.size += 1;
    	if( (this.first == null) || (i == 0)) {
    		this.first = new Node<T>(item);
    		this.first.next = null;
    		return ;
    	}
    	Node<T> curr = first;
    	for(int j = 0; j < (i-1); j ++ ){
    		curr = curr.next;
    	}
    	curr.next = new Node<T>(item);
    }

    @Override
    public void removeAt(int i) {
    	int j = 0;
    	if(!(this.getCount()==0)) {
			if(i == 0){
				this.first = (this.first).next;
			}
			else {
				Node<T> curr = this.first;
				while(j++ < (i-1)) {
					curr = curr.next;
				}
				Node<T> toSetAs = (curr.next);
				toSetAs = toSetAs.next;
				curr.next = toSetAs;
			}
			size --;
		}
    }

    @Override
    public <U> MyList<U> map(Mapper<T, U> f) {
    	MyLinkedList<U> myList = new MyLinkedList<U>();
    	for (T t : this) 
    		myList.add(f.call(t));
    	return myList;
        
    }

    public boolean equals(Object that) {
        return equals((MyList<T>)that);
    }

    public boolean equals(MyList<T> that) {
        return this == that;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyLinkedListIterator();
    }

    public Collection<T> toCollection() {
    	Node<T> curr = first;
    	Collection<T> collection = new ArrayList<T>();
    	for(int i = 0; i < this.size; i ++) {
    		T item = curr.item;
    		curr = curr.next;
    		collection.add(item);
    	}
    	return collection;
    }
    
    public void printItems() {
    	StringBuilder sb = new StringBuilder("start-->");
    	this.toCollection().forEach(item -> sb.append("{"+item.toString() + "}-->"));
    	sb.append("end");
    	myPrintln("List ["+sb.toString()+"]");
    }
    
    public static void main(String[] args) {
    	Class<MyLinkedList> obj = MyLinkedList.class;
    	if (obj.isAnnotationPresent(MyAnnotation.class)) {
    		Annotation annotation = obj.getAnnotation(MyAnnotation.class);
    		MyAnnotation myAnnotation = (MyAnnotation) annotation;
    		String myAuthor = myAnnotation.author();
    		System.out.printf("%nAuthor :%s\n", myAuthor);
    	}
    	
    	new Thread("INTEGER"){
        public void run(){
          System.out.println("spawned thread ["+getName()+"]") ;
          MyLinkedList<Integer> intList = new MyLinkedList<Integer>(getName());
          intList.add(new Integer(1));
          intList.add(new Integer(2));
          intList.add(new Integer(3));
          intList.printItems();
          System.out.println("ended thread ["+getName()+"]") ;
        }
      }.start();
      
      new Thread("STRING"){
        public void run(){
          System.out.println("spawned thread ["+getName()+"]") ;
          MyLinkedList<String> intList = new MyLinkedList<String>(getName());
          intList.add("a");
          intList.add("b");
          intList.add("c");
          intList.add("d");
          intList.add("e");
          intList.printItems();
          System.out.println("ended thread ["+getName()+"]") ;
        }
      }.start();
      
    }
}