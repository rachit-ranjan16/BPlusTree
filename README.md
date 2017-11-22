# BPlusTree
BPlus Tree Data Structure Implementation in Java

## Abstract
- Stores Pairs in (key,value) form
- Multiple pairs with the same key allowed
- Suported Operations
  - Insert(key,value)
  - Search (key) : returns all values associated with the key
  - Search (key1,key2): returns (all key value pairs) such that key1 <= key <= key2.

## Design
![Could not display. Check class_dig/img/ClassDiagram.png](/img/ClassDiagram.png?raw=true "Class Diagram")

## I/O
### Input Format
First Line contains order of the tree.
  3
  Insert(3.55,Value1)
  Insert(4.01,Value10)
  Insert(39.56,Value2)
  Insert(-3.95,Value23)
  Insert(-3.91,Value54609)
  Insert(3.55,Value67)
  Insert(0.02,Value98)
  Search(3.55)
  Search(-3.91,30.96)
  Insert(3.26,Value56089)
  Insert(121.56,Value1234)
  Insert(-109.23,Value43234)
  Search(3.71)

  All Strings are case and space sensitive

### Output Format
  (-3.91,Value54609) //Search
  (-3.91,Value54609), (0.02,Value98), (3.55,Value1 ), (3.55,Value67), (4.01, Value10) //Search Range
  Null

## Execution
- Currently executed on from the terminal using the provided **makefile**
    `$>make`
     `$>java treesearch input_file.txt`
- Alternatively all the java files in `~/src/main/java/` can be compiled individually and executed using the above command

## Remarks
- Degree 3 B+Trees are functional without any hiccups
- Higher Degree B+Trees inserts are prone to failure
- Working on squashing the bugs and packagaing as a jar
