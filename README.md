JavaSlice
=========

Java Slice is an API that provides unified operations similar to Python slice operator to the Java language, on string, arrays and lists.

> String text = "Hello, World";
> slice(text, -5, end); // "World"

## Why?

It is not difficult to write code that takes a slice of a string, an array or a list in Java. The problem is that these operations have different names, are in different classes and packages and all behaves slightly differently from each others.

For example, the proper way to take a slice copy of an array is to use the method Arrays.copyOfRange(). For List, you would have to use the method List.subList() and then create a new List out of it, because subList actually returns a slice view instead of doing a shallow copy. For String, it is confusing: String.substring() produced a new String that acted like a view on a part of the original string up until Java 6, but starting with Java 7 it was modified to do a shallow copy. Since Java String are immutable, this could be considered equivalent, but in reality it has an impact in term of performance.

This behaviour is confusing and just makes not only the beginner but also the intermediate programmer go back and forth in the Javadoc to see what are the idiosyncrasies of each implementation. Getting a part of a String or a List being a fairly common operation, it can make one loss a lot of productivity on the long term.

The beauty of the Python slice operator [] is that it acts in a predictable way and with a uniform notation for both strings, lists and tuples objects, which makes its usage effortless. This is what is the goal with this API, with the help of static imports.

Java Slice also supports the other features of Python slice operator, like providing negative indices to count from the end of the array, and the "extended slice" operations that comes with a third parameter.

## How?

The Java Slice API is very lightweight, it consist simply in one class with static methods for slicing. To make things simple, one can just use a static import to use the "slice" methods without further qualifications.

> import static import static org.nnamrak.util.Slice.slice;

Then using slice methods is straigthforward on any array, list or string:

> String str = "Hello";
> slice(str, 1, 3); // "el";
>
> // ...
>
> List<Character> list = Arrays.asList('a', 'b', 'c', 'd');
> slice(list, 1, 3)); // ['b', 'c' ]
>
> // ...
>
> int[] array = new int[] { 9, 8, 7, 6);
> slice(array, 1, 3)); // [ 8, 7 ]

## Why a slice copy instead of a slice view? Why not parallel Go slice operator?

The next version of Java Slice is going to provide an API for Go-like slice views as well.

##Python hasn't invented Slice operator, X has it before.
##Language X's implementation of the slice operation is better.

Of course array slicing existed long before Python. Wikipedia lists [18 languages][http://en.wikipedia.org/wiki/Array_slicing] providing some kind of slicing, starting with Fortran in 1966, although with different definitions of what array slicing is. Java Slice originally intended to implement Python's flavor of it. It is my plan to implement Go's flavor too in the form of some sliceView methods(). I think it should cover most needs, but feel free to fork and do pull requests to add functionalities that could be complementary.