package com.pranav.estudy.seeder;

import com.pranav.estudy.model.Subject;
import com.pranav.estudy.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public void run(String... args) {
        // Only seed if database is empty
        if (subjectRepository.count() == 0) {
            System.out.println("Seeding study data into MongoDB...");
            subjectRepository.saveAll(Arrays.asList(
                buildOOP(), buildDSA(), buildOS(), buildCN(), buildDBMS()
            ));
            System.out.println("Study data seeded successfully!");
        } else {
            System.out.println("Study data already exists. Skipping seed.");
        }
    }

    // ==================== OOP ====================
    private Subject buildOOP() {
        Subject s = new Subject();
        s.setSubject("OOP");
        s.setYoutubeLinks(Arrays.asList(
            "https://www.youtube.com/watch?v=BSVKUk58K6U"
        ));

        s.setTopics(Arrays.asList(
            topic("What is OOP?",
                "Object-Oriented Programming (OOP) is a programming paradigm that organizes software design around data (objects) rather than functions and logic. An object is an instance of a class that contains both data (fields) and behavior (methods).",
                "Dog dog = new Dog(); dog.bark();",
                "class Dog {\n  String name;\n  void bark() {\n    System.out.println(name + \" says Woof!\");\n  }\n}\npublic class Main {\n  public static void main(String[] args) {\n    Dog d = new Dog();\n    d.name = \"Tommy\";\n    d.bark();\n  }\n}"),

            topic("Encapsulation",
                "Encapsulation is the process of wrapping data (variables) and methods together as a single unit. It hides internal implementation details from the outside world using private access modifiers and exposes only necessary parts via public getters/setters. This protects data integrity.",
                "private String name; public String getName() { return name; }",
                "class BankAccount {\n  private double balance;\n  public void deposit(double amount) {\n    if(amount > 0) balance += amount;\n  }\n  public double getBalance() {\n    return balance;\n  }\n}"),

            topic("Inheritance",
                "Inheritance allows a child class to acquire properties and behaviors of a parent class using the 'extends' keyword. It promotes code reuse. Java supports single and multilevel inheritance but NOT multiple inheritance through classes (only through interfaces).",
                "class Dog extends Animal { }",
                "class Animal {\n  String name;\n  void eat() {\n    System.out.println(name + \" is eating\");\n  }\n}\nclass Dog extends Animal {\n  void bark() {\n    System.out.println(name + \" is barking\");\n  }\n}\npublic class Main {\n  public static void main(String[] args) {\n    Dog d = new Dog();\n    d.name = \"Tommy\";\n    d.eat();\n    d.bark();\n  }\n}"),

            topic("Polymorphism",
                "Polymorphism means 'one name, many forms'. In Java it occurs in two ways: Compile-time (Method Overloading) and Runtime (Method Overriding). It allows one interface to be used for different data types or objects.",
                "void draw(Circle c) and void draw(Square s) — same method name, different behavior",
                "// Runtime Polymorphism\nclass Shape {\n  void draw() { System.out.println(\"Drawing shape\"); }\n}\nclass Circle extends Shape {\n  @Override\n  void draw() { System.out.println(\"Drawing circle\"); }\n}\nclass Square extends Shape {\n  @Override\n  void draw() { System.out.println(\"Drawing square\"); }\n}\npublic class Main {\n  public static void main(String[] args) {\n    Shape s = new Circle(); // upcasting\n    s.draw(); // Drawing circle\n  }\n}"),

            topic("Abstraction",
                "Abstraction means hiding implementation details and showing only essential features. In Java, abstraction is achieved through Abstract Classes and Interfaces. An abstract class can have both abstract and concrete methods. Interface has only abstract methods (before Java 8).",
                "abstract class Vehicle { abstract void start(); }",
                "abstract class Vehicle {\n  abstract void start();\n  void stop() { System.out.println(\"Vehicle stopped\"); }\n}\nclass Car extends Vehicle {\n  @Override\n  void start() { System.out.println(\"Car started with key\"); }\n}\nclass Bike extends Vehicle {\n  @Override\n  void start() { System.out.println(\"Bike started with kick\"); }\n}"),

            topic("Method Overloading",
                "Method Overloading is defining multiple methods with the same name but different parameters (type, number, or order) in the same class. It is resolved at compile time — also called Static Polymorphism or Compile-time Polymorphism.",
                "add(int a, int b) and add(double a, double b)",
                "class Calculator {\n  int add(int a, int b) { return a + b; }\n  double add(double a, double b) { return a + b; }\n  int add(int a, int b, int c) { return a + b + c; }\n}"),

            topic("Method Overriding",
                "Method Overriding is redefining a method in a child class that already exists in the parent class with the same name, return type, and parameters. It is resolved at runtime — also called Dynamic Polymorphism or Runtime Polymorphism. Use @Override annotation.",
                "Child class provides its own implementation of parent's method",
                "class Animal {\n  void sound() { System.out.println(\"Animal makes sound\"); }\n}\nclass Cat extends Animal {\n  @Override\n  void sound() { System.out.println(\"Cat says Meow\"); }\n}"),

            topic("Interface",
                "An interface is a blueprint of a class that contains only abstract methods (Java 7) and constants. From Java 8, interfaces can have default and static methods. A class implements an interface using 'implements' keyword. One class can implement multiple interfaces.",
                "class Dog implements Animal, Pet { }",
                "interface Flyable {\n  void fly();\n}\ninterface Swimmable {\n  void swim();\n}\nclass Duck implements Flyable, Swimmable {\n  public void fly() { System.out.println(\"Duck flies\"); }\n  public void swim() { System.out.println(\"Duck swims\"); }\n}"),

            topic("Abstract Class vs Interface",
                "Abstract Class: can have abstract + concrete methods, constructors, instance variables, single inheritance. Interface: all methods abstract (Java 7), no constructors, only constants, multiple inheritance. Use abstract class when classes share common code. Use interface for capability contracts.",
                "abstract class Shape vs interface Drawable",
                "// Use abstract class for IS-A relationship\nabstract class Animal {\n  String name;\n  abstract void sound();\n  void breathe() { System.out.println(\"Breathing\"); }\n}\n// Use interface for CAN-DO relationship\ninterface Trainable {\n  void train();\n}"),

            topic("Constructor",
                "A constructor is a special method called when an object is created. It has the same name as the class and no return type. Types: Default Constructor (no args), Parameterized Constructor, Copy Constructor. Constructor chaining uses this() or super().",
                "Student s = new Student(\"Pranav\", 21);",
                "class Student {\n  String name;\n  int age;\n  // Default constructor\n  Student() { name = \"Unknown\"; age = 0; }\n  // Parameterized constructor\n  Student(String name, int age) {\n    this.name = name;\n    this.age = age;\n  }\n  // Copy constructor\n  Student(Student s) {\n    this.name = s.name;\n    this.age = s.age;\n  }\n}"),

            topic("this and super keyword",
                "'this' refers to the current class object. Used to differentiate between instance variables and parameters, call current class constructor (this()), or pass current object. 'super' refers to the parent class. Used to call parent class methods (super.method()), constructor (super()), or access parent fields.",
                "this.name = name; super.display();",
                "class Animal {\n  String name;\n  Animal(String name) { this.name = name; }\n  void display() { System.out.println(\"Animal: \" + name); }\n}\nclass Dog extends Animal {\n  String breed;\n  Dog(String name, String breed) {\n    super(name); // calls Animal constructor\n    this.breed = breed;\n  }\n  void display() {\n    super.display(); // calls Animal's display\n    System.out.println(\"Breed: \" + breed);\n  }\n}"),

            topic("Static keyword",
                "Static means belonging to the class rather than any instance. Static variables are shared among all objects. Static methods can be called without creating an object. Static blocks run once when class loads. Cannot use 'this' or access instance variables inside static methods.",
                "Math.max(a, b) — static method called without object",
                "class Counter {\n  static int count = 0; // shared among all objects\n  String name;\n  Counter(String name) {\n    this.name = name;\n    count++;\n  }\n  static int getCount() { return count; }\n}\npublic class Main {\n  public static void main(String[] args) {\n    new Counter(\"A\");\n    new Counter(\"B\");\n    System.out.println(Counter.getCount()); // 2\n  }\n}"),

            topic("final keyword",
                "final variable: value cannot be changed (constant). final method: cannot be overridden by child class. final class: cannot be inherited (e.g. String class is final). Use final for security and immutability.",
                "final double PI = 3.14159;",
                "final class ImmutableClass {\n  final int value;\n  ImmutableClass(int v) { this.value = v; }\n  // value cannot be changed after assignment\n}\nclass Parent {\n  final void show() { System.out.println(\"Cannot override\"); }\n}")
        ));

        s.setQuestions(Arrays.asList(
            qna("What are the 4 pillars of OOP?", "Encapsulation, Inheritance, Polymorphism, Abstraction."),
            qna("Difference between Overloading and Overriding?", "Overloading: same method name, different parameters, same class, compile-time. Overriding: same method name, same parameters, different classes (parent-child), runtime."),
            qna("Can we override static methods?", "No. Static methods belong to the class, not objects. They can be hidden (method hiding) but not overridden."),
            qna("What is the difference between Abstract Class and Interface?", "Abstract class can have concrete methods, constructors, instance variables. Interface has only abstract methods (pre-Java 8), no constructors. A class can extend only one abstract class but implement multiple interfaces."),
            qna("What is constructor chaining?", "Calling one constructor from another using this() (same class) or super() (parent class). Must be the first statement in the constructor."),
            qna("Why is multiple inheritance not supported in Java through classes?", "To avoid the Diamond Problem — ambiguity when two parent classes have the same method. Java solves this through interfaces."),
            qna("What is the difference between == and .equals()?", "== compares object references (memory addresses). .equals() compares object content/values. For String comparison always use .equals()."),
            qna("What is an immutable class?", "A class whose objects cannot be modified after creation. Make all fields private final, no setters, only getters. Example: String class in Java."),
            qna("What is tight coupling vs loose coupling?", "Tight coupling: classes are highly dependent on each other. Loose coupling: classes are independent, changes in one don't affect others. Interfaces help achieve loose coupling."),
            qna("What is the use of 'super' keyword?", "To call parent class constructor (super()), to call parent class methods (super.methodName()), to access parent class variables when overridden in child class.")
        ));
        return s;
    }

    // ==================== DSA ====================
    private Subject buildDSA() {
        Subject s = new Subject();
        s.setSubject("DSA");
        s.setYoutubeLinks(Arrays.asList(
            "https://www.youtube.com/watch?v=rZ41y93P2Qo"
        ));

        s.setTopics(Arrays.asList(
            topic("Arrays",
                "An array is a collection of elements of the same data type stored in contiguous memory locations. Arrays have fixed size. Access is O(1) by index. Insertion/deletion in middle is O(n). Used when size is known and random access is needed.",
                "int[] arr = {1, 2, 3, 4, 5};",
                "// Common array operations\nint[] arr = {5, 3, 1, 4, 2};\n// Find max\nint max = arr[0];\nfor(int i : arr) if(i > max) max = i;\n// Reverse array\nfor(int i=0, j=arr.length-1; i<j; i++, j--) {\n  int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;\n}\n// Two pointer technique\nint left = 0, right = arr.length - 1;"),

            topic("Linked List",
                "A linked list is a linear data structure where elements (nodes) are stored at random memory locations and connected via pointers. Types: Singly (next pointer), Doubly (next + prev), Circular. Insertion/Deletion: O(1) at head. Search: O(n). No random access.",
                "Node: [data | next] -> [data | next] -> null",
                "class Node {\n  int data;\n  Node next;\n  Node(int data) { this.data = data; this.next = null; }\n}\nclass LinkedList {\n  Node head;\n  void insertAtHead(int data) {\n    Node newNode = new Node(data);\n    newNode.next = head;\n    head = newNode;\n  }\n  void display() {\n    Node curr = head;\n    while(curr != null) {\n      System.out.print(curr.data + \" -> \");\n      curr = curr.next;\n    }\n  }\n}"),

            topic("Stack",
                "Stack is a linear data structure following LIFO (Last In First Out) principle. Operations: push (add), pop (remove), peek (top element). Used in: function call stack, undo operations, expression evaluation, backtracking. Time complexity O(1) for all operations.",
                "Stack: [1, 2, 3] -> push(4) -> [1, 2, 3, 4] -> pop() -> [1, 2, 3]",
                "import java.util.Stack;\nStack<Integer> stack = new Stack<>();\nstack.push(1);\nstack.push(2);\nstack.push(3);\nSystem.out.println(stack.pop()); // 3\nSystem.out.println(stack.peek()); // 2\nSystem.out.println(stack.isEmpty()); // false"),

            topic("Queue",
                "Queue is a linear data structure following FIFO (First In First Out) principle. Operations: enqueue (add at rear), dequeue (remove from front), peek. Types: Simple Queue, Circular Queue, Priority Queue, Deque. Used in: BFS, scheduling, print queue.",
                "Queue: front [1, 2, 3] rear -> enqueue(4) -> [1, 2, 3, 4]",
                "import java.util.LinkedList;\nimport java.util.Queue;\nQueue<Integer> q = new LinkedList<>();\nq.offer(1); q.offer(2); q.offer(3);\nSystem.out.println(q.poll()); // 1 (removes front)\nSystem.out.println(q.peek()); // 2 (just views front)"),

            topic("Binary Search Tree (BST)",
                "BST is a tree where each node has at most 2 children. Left child < parent < Right child. Operations: Insert, Delete, Search all O(log n) average, O(n) worst case. Inorder traversal of BST gives sorted output.",
                "         50\n        /  \\\n      30    70\n     /  \\  /  \\\n    20  40 60  80",
                "class BST {\n  class Node { int data; Node left, right; Node(int d) { data=d; } }\n  Node root;\n  Node insert(Node root, int data) {\n    if(root == null) return new Node(data);\n    if(data < root.data) root.left = insert(root.left, data);\n    else if(data > root.data) root.right = insert(root.right, data);\n    return root;\n  }\n  void inorder(Node root) {\n    if(root == null) return;\n    inorder(root.left);\n    System.out.print(root.data + \" \");\n    inorder(root.right);\n  }\n}"),

            topic("Bubble Sort",
                "Bubble Sort repeatedly compares adjacent elements and swaps them if they are in wrong order. After each pass, the largest element bubbles to the end. Time: O(n²) worst/average, O(n) best (already sorted with optimization). Space: O(1). Stable sort.",
                "Pass 1: [5,3,1,4] -> [3,1,4,5], Pass 2: [1,3,4,5]",
                "void bubbleSort(int[] arr) {\n  int n = arr.length;\n  for(int i = 0; i < n-1; i++) {\n    boolean swapped = false;\n    for(int j = 0; j < n-i-1; j++) {\n      if(arr[j] > arr[j+1]) {\n        int temp = arr[j];\n        arr[j] = arr[j+1];\n        arr[j+1] = temp;\n        swapped = true;\n      }\n    }\n    if(!swapped) break; // already sorted\n  }\n}"),

            topic("Merge Sort",
                "Merge Sort divides array in half recursively, sorts each half, then merges them. Uses Divide and Conquer strategy. Time: O(n log n) always. Space: O(n). Stable sort. Best for large datasets and linked lists.",
                "[5,3,1,4] -> [5,3] [1,4] -> [3,5] [1,4] -> [1,3,4,5]",
                "void mergeSort(int[] arr, int left, int right) {\n  if(left < right) {\n    int mid = (left + right) / 2;\n    mergeSort(arr, left, mid);\n    mergeSort(arr, mid+1, right);\n    merge(arr, left, mid, right);\n  }\n}\nvoid merge(int[] arr, int l, int m, int r) {\n  // create temp arrays, copy, merge back\n}"),

            topic("Quick Sort",
                "Quick Sort picks a pivot element and partitions array such that elements less than pivot go left, greater go right. Recursively sorts both partitions. Time: O(n log n) average, O(n²) worst. Space: O(log n). Not stable. Fastest in practice.",
                "Pivot=4: [3,1] 4 [5,7] -> sort each side",
                "void quickSort(int[] arr, int low, int high) {\n  if(low < high) {\n    int pi = partition(arr, low, high);\n    quickSort(arr, low, pi-1);\n    quickSort(arr, pi+1, high);\n  }\n}\nint partition(int[] arr, int low, int high) {\n  int pivot = arr[high];\n  int i = low - 1;\n  for(int j = low; j < high; j++) {\n    if(arr[j] < pivot) { i++; int t=arr[i]; arr[i]=arr[j]; arr[j]=t; }\n  }\n  int t=arr[i+1]; arr[i+1]=arr[high]; arr[high]=t;\n  return i+1;\n}"),

            topic("Binary Search",
                "Binary Search finds an element in a SORTED array by repeatedly dividing the search space in half. Compare target with middle element — if equal found, if less search left half, if greater search right half. Time: O(log n). Space: O(1) iterative.",
                "Search 7 in [1,3,5,7,9]: mid=5, go right -> mid=7, found!",
                "int binarySearch(int[] arr, int target) {\n  int left = 0, right = arr.length - 1;\n  while(left <= right) {\n    int mid = left + (right - left) / 2;\n    if(arr[mid] == target) return mid;\n    else if(arr[mid] < target) left = mid + 1;\n    else right = mid - 1;\n  }\n  return -1; // not found\n}"),

            topic("Time and Space Complexity",
                "Time Complexity measures how runtime grows with input size. Space Complexity measures memory usage. Big O notation: O(1) constant, O(log n) logarithmic, O(n) linear, O(n log n) linearithmic, O(n²) quadratic, O(2^n) exponential. Always analyze worst case.",
                "O(1) < O(log n) < O(n) < O(n log n) < O(n²) < O(2^n)",
                "// O(1) - constant\nint getFirst(int[] arr) { return arr[0]; }\n// O(n) - linear\nint sum(int[] arr) { int s=0; for(int x:arr) s+=x; return s; }\n// O(n²) - quadratic\nvoid print2D(int[][] mat) {\n  for(int[] row: mat)\n    for(int x: row)\n      System.out.print(x);\n}"),

            topic("Graph and BFS/DFS",
                "Graph is a collection of nodes (vertices) and edges. Types: Directed/Undirected, Weighted/Unweighted. BFS (Breadth First Search): uses Queue, level by level traversal, finds shortest path. DFS (Depth First Search): uses Stack/Recursion, goes deep first. Both O(V+E).",
                "BFS: visit level by level. DFS: go as deep as possible first.",
                "// BFS using Queue\nvoid bfs(int start, List<List<Integer>> adj) {\n  boolean[] visited = new boolean[adj.size()];\n  Queue<Integer> q = new LinkedList<>();\n  visited[start] = true;\n  q.offer(start);\n  while(!q.isEmpty()) {\n    int node = q.poll();\n    System.out.print(node + \" \");\n    for(int neighbor : adj.get(node)) {\n      if(!visited[neighbor]) {\n        visited[neighbor] = true;\n        q.offer(neighbor);\n      }\n    }\n  }\n}")
        ));

        s.setQuestions(Arrays.asList(
            qna("Difference between Array and Linked List?", "Array: fixed size, contiguous memory, O(1) access, O(n) insert/delete. Linked List: dynamic size, random memory, O(n) access, O(1) insert/delete at head."),
            qna("What is the difference between Stack and Queue?", "Stack: LIFO (Last In First Out) — push/pop from same end. Queue: FIFO (First In First Out) — enqueue at rear, dequeue from front."),
            qna("When to use BFS vs DFS?", "BFS: find shortest path, level-order traversal. DFS: detect cycles, topological sort, maze problems, when solution is far from root."),
            qna("What is the best sorting algorithm?", "Depends on use case. Merge Sort: stable, O(n log n) always, good for linked lists. Quick Sort: fastest in practice, O(n log n) average. Counting Sort: O(n) for small integer ranges."),
            qna("What is recursion?", "A function calling itself to solve a smaller subproblem. Every recursive function needs: base case (stopping condition) and recursive case (smaller problem). Uses call stack internally."),
            qna("Difference between Linear Search and Binary Search?", "Linear: works on unsorted array, O(n). Binary: requires sorted array, O(log n). Binary is much faster for large datasets."),
            qna("What is Dynamic Programming?", "DP solves complex problems by breaking into overlapping subproblems and storing results (memoization/tabulation) to avoid recomputation. Used in: Fibonacci, Knapsack, Longest Common Subsequence."),
            qna("What is a Hash Table?", "Data structure that maps keys to values using a hash function. Average O(1) for insert, delete, search. Collision handled by chaining or open addressing. Used in HashMap in Java.")
        ));
        return s;
    }

    // ==================== OS ====================
    private Subject buildOS() {
        Subject s = new Subject();
        s.setSubject("OS");
        s.setYoutubeLinks(Arrays.asList(
            "https://www.youtube.com/watch?v=vBURTt97EkA"
        ));

        s.setTopics(Arrays.asList(
            topic("What is an Operating System?",
                "An Operating System (OS) is system software that manages hardware resources and provides services to application programs. It acts as an interface between user and hardware. Functions: Process Management, Memory Management, File System Management, Device Management, Security.",
                "Windows, Linux, macOS, Android are operating systems",
                "// OS is not written in Java but concepts:\n// Process: running program\n// Thread: lightweight process\n// Memory: RAM management\n// File System: how files are stored/retrieved"),

            topic("Process vs Thread",
                "Process: independent program in execution with its own memory space (PCB, stack, heap, code, data). Thread: smallest unit of execution within a process, shares memory with other threads of same process. Process creation is heavy, thread creation is lightweight. Threads communicate via shared memory, processes via IPC.",
                "Browser = 1 process, each tab = 1 thread (roughly)",
                "// Java Thread example\nclass MyThread extends Thread {\n  public void run() {\n    System.out.println(\"Thread running: \" + Thread.currentThread().getName());\n  }\n}\npublic class Main {\n  public static void main(String[] args) {\n    MyThread t1 = new MyThread();\n    MyThread t2 = new MyThread();\n    t1.start();\n    t2.start();\n  }\n}"),

            topic("CPU Scheduling Algorithms",
                "CPU Scheduling decides which process gets CPU next. Algorithms: FCFS (First Come First Serve) - simple, no preemption. SJF (Shortest Job First) - minimum avg waiting time. Round Robin - time quantum, fair for all. Priority Scheduling - higher priority first. SRTF - preemptive SJF.",
                "Round Robin with quantum=2: P1(4ms), P2(3ms), P3(1ms) -> P1(2), P2(2), P3(1), P1(2), P2(1)",
                "// Key terms:\n// Burst Time: CPU time required by process\n// Waiting Time: time spent in ready queue\n// Turnaround Time: completion - arrival time\n// Response Time: first response - arrival time\n// Throughput: processes completed per unit time"),

            topic("Deadlock",
                "Deadlock occurs when a group of processes are blocked, each waiting for a resource held by another process in the group. Four necessary conditions (Coffman): Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait. Prevention: break any one condition. Detection: Resource Allocation Graph. Recovery: process termination or resource preemption.",
                "P1 holds R1, waits for R2. P2 holds R2, waits for R1. -> Deadlock!",
                "// Deadlock prevention - break circular wait:\n// Assign order to resources, always acquire in order\n// Thread 1: lock A then B\n// Thread 2: lock A then B (same order = no deadlock)\n// Banker's Algorithm - deadlock avoidance"),

            topic("Memory Management and Paging",
                "Memory management allocates RAM to processes. Techniques: Contiguous allocation (simple but fragmentation), Paging (fixed size pages, no external fragmentation), Segmentation (variable size, logical division). Paging: logical address = page number + offset. Physical address found via page table.",
                "Page Size = 4KB, Logical Address = Page 2, Offset 100 -> Physical frame 5, offset 100",
                "// Virtual Memory allows processes to use more memory than RAM\n// Pages swapped between RAM and disk (swap space)\n// Page Fault: page not in RAM, OS loads it from disk\n// Thrashing: too many page faults, system spends more time swapping than executing"),

            topic("Semaphore and Mutex",
                "Semaphore: integer variable used for process synchronization. Binary Semaphore (0 or 1) = Mutex. Counting Semaphore allows multiple processes. Operations: wait(P) decrements, signal(V) increments. Mutex: mutual exclusion lock, only the thread that locked it can unlock it. Used to prevent race conditions.",
                "Semaphore S=1: P1 does wait(S)->S=0, P2 does wait(S)->blocks until P1 does signal(S)",
                "// Java Semaphore\nimport java.util.concurrent.Semaphore;\nSemaphore s = new Semaphore(1);\ns.acquire(); // wait - P operation\n// critical section\ns.release(); // signal - V operation\n\n// Java synchronized (mutex)\nsynchronized(this) {\n  // only one thread at a time\n}"),

            topic("Virtual Memory and Page Replacement",
                "Virtual Memory allows a process to use more memory than physically available by using disk space as extension of RAM. Page Replacement Algorithms decide which page to swap out: FIFO (oldest page out), LRU (Least Recently Used), Optimal (replace page used farthest in future - theoretical). LRU is best practical algorithm.",
                "3 frames, pages: 1,2,3,4,1,2 -> FIFO causes 6 page faults",
                "// Page replacement key concepts:\n// Page Fault: referenced page not in memory\n// Belady's Anomaly: more frames = more page faults (FIFO only)\n// Working Set: set of pages a process is currently using\n// Thrashing: process spends more time paging than executing"),

            topic("File System",
                "File system manages how data is stored and retrieved. Components: File (named collection of data), Directory (organizes files), Path (location of file). File operations: create, read, write, delete, open, close. Allocation methods: Contiguous, Linked, Indexed. File attributes: name, type, size, permissions, timestamps.",
                "/home/pranav/projects/estudy/README.md — absolute path",
                "// File permissions in Linux: rwxrwxrwx\n// r=read(4), w=write(2), x=execute(1)\n// chmod 755 file.txt\n// owner=7(rwx), group=5(rx), others=5(rx)\n// Inode: data structure storing file metadata")
        ));

        s.setQuestions(Arrays.asList(
            qna("What is the difference between process and thread?", "Process: independent execution unit with own memory space. Thread: lightweight unit within a process sharing memory. Thread creation faster, communication easier but sharing causes synchronization issues."),
            qna("What are the four conditions for deadlock?", "1. Mutual Exclusion: resource held by one process at a time. 2. Hold and Wait: process holds one resource and waits for another. 3. No Preemption: resource cannot be forcibly taken. 4. Circular Wait: circular chain of processes waiting."),
            qna("What is the difference between paging and segmentation?", "Paging: fixed size pages, no external fragmentation, internal fragmentation possible. Segmentation: variable size segments based on logical division (code, stack, heap), no internal fragmentation, external fragmentation possible."),
            qna("What is a race condition?", "When multiple threads access shared data simultaneously and the result depends on the order of execution. Prevented using synchronization mechanisms like mutex, semaphore, synchronized blocks."),
            qna("What is context switching?", "The process of saving the state (PCB) of a currently running process and loading the state of another process. Allows multitasking but has overhead cost."),
            qna("Difference between preemptive and non-preemptive scheduling?", "Preemptive: OS can interrupt a running process and allocate CPU to another (Round Robin, SRTF). Non-preemptive: process runs until it completes or blocks voluntarily (FCFS, SJF)."),
            qna("What is thrashing?", "When a process spends more time swapping pages in/out of memory than executing. Occurs when too many processes compete for too few frames. Solution: reduce degree of multiprogramming.")
        ));
        return s;
    }

    // ==================== CN ====================
    private Subject buildCN() {
        Subject s = new Subject();
        s.setSubject("CN");
        s.setYoutubeLinks(Arrays.asList(
            "https://www.youtube.com/watch?v=JFF2vAaN6gA"
        ));

        s.setTopics(Arrays.asList(
            topic("OSI Model",
                "OSI (Open Systems Interconnection) Model has 7 layers for network communication. Each layer has specific functions and communicates with adjacent layers. Mnemonic: 'Please Do Not Throw Sausage Pizza Away' (Physical, Data Link, Network, Transport, Session, Presentation, Application).",
                "7-Application, 6-Presentation, 5-Session, 4-Transport, 3-Network, 2-Data Link, 1-Physical",
                "// OSI Layers and protocols:\n// 7. Application: HTTP, FTP, SMTP, DNS\n// 6. Presentation: SSL/TLS, JPEG, MPEG\n// 5. Session: NetBIOS, RPC\n// 4. Transport: TCP, UDP\n// 3. Network: IP, ICMP, Router\n// 2. Data Link: MAC, Ethernet, Switch\n// 1. Physical: Cables, Hubs, Bits"),

            topic("TCP/IP Model",
                "TCP/IP Model has 4 layers and is the practical model used in the internet. Application Layer (HTTP, FTP, DNS), Transport Layer (TCP, UDP), Internet Layer (IP, ICMP), Network Access Layer (Ethernet, WiFi). TCP/IP is simpler than OSI and actually implemented.",
                "4-Application, 3-Transport, 2-Internet, 1-Network Access",
                "// TCP 3-way handshake:\n// Client -> SYN -> Server\n// Server -> SYN-ACK -> Client\n// Client -> ACK -> Server\n// Connection established!\n\n// TCP 4-way termination:\n// FIN -> ACK -> FIN -> ACK"),

            topic("TCP vs UDP",
                "TCP (Transmission Control Protocol): connection-oriented, reliable, ordered delivery, error checking, flow control, congestion control, slower. Used for: HTTP, FTP, email, file transfer. UDP (User Datagram Protocol): connectionless, unreliable, no ordering, faster, no handshake. Used for: video streaming, gaming, DNS, VoIP.",
                "TCP: like registered mail (guaranteed delivery). UDP: like broadcast (fast, no guarantee)",
                "// TCP features:\n// - 3-way handshake\n// - Sequence numbers\n// - Acknowledgements\n// - Retransmission on loss\n// - Flow control (sliding window)\n\n// UDP features:\n// - No connection setup\n// - No acknowledgement\n// - Checksum only\n// - Much lower overhead"),

            topic("IP Addressing and Subnetting",
                "IPv4: 32-bit address (4 octets), e.g., 192.168.1.1. Classes: A(1-127), B(128-191), C(192-223), D(multicast), E(research). Private ranges: 10.x.x.x, 172.16-31.x.x, 192.168.x.x. Subnet mask divides IP into network and host parts. CIDR notation: 192.168.1.0/24 means 24 bits for network.",
                "192.168.1.100/24: Network=192.168.1.0, Host range=.1-.254, Broadcast=.255",
                "// Subnetting formula:\n// Hosts per subnet = 2^(host bits) - 2\n// /24 = 256-2 = 254 hosts\n// /25 = 128-2 = 126 hosts\n// /26 = 64-2 = 62 hosts\n\n// IPv6: 128-bit, e.g., 2001:0db8:85a3::8a2e:0370:7334"),

            topic("HTTP and HTTPS",
                "HTTP (HyperText Transfer Protocol): application layer protocol for web communication. Methods: GET (retrieve), POST (create), PUT (update), DELETE (remove), PATCH (partial update). Status codes: 1xx(info), 2xx(success), 3xx(redirect), 4xx(client error), 5xx(server error). HTTPS = HTTP + SSL/TLS encryption.",
                "GET /api/subjects HTTP/1.1 -> 200 OK with JSON data",
                "// Common HTTP Status Codes:\n// 200 OK\n// 201 Created\n// 301 Moved Permanently\n// 400 Bad Request\n// 401 Unauthorized\n// 403 Forbidden\n// 404 Not Found\n// 500 Internal Server Error\n// 503 Service Unavailable"),

            topic("DNS",
                "DNS (Domain Name System) translates human-readable domain names to IP addresses. Hierarchy: Root DNS -> TLD DNS (.com, .org) -> Authoritative DNS -> Local DNS Cache. Process: browser checks cache -> OS checks hosts file -> query local DNS -> recursive resolution. DNS uses UDP port 53.",
                "www.google.com -> 142.250.80.46",
                "// DNS Record Types:\n// A Record: domain -> IPv4\n// AAAA Record: domain -> IPv6\n// CNAME: alias -> canonical name\n// MX: mail server\n// NS: name server\n// TXT: text info (SPF, DKIM)\n// TTL: Time To Live (cache duration)"),

            topic("Router vs Switch vs Hub",
                "Hub: physical layer, broadcasts to all ports, half-duplex, creates collision domain. Switch: data link layer, uses MAC table, forwards to specific port, creates separate collision domains, full-duplex. Router: network layer, uses IP addresses, connects different networks, routing table, NAT, firewall.",
                "Hub: 1 collision domain. Switch: N collision domains. Router: connects networks.",
                "// Network devices summary:\n// Repeater: amplifies signal (Layer 1)\n// Hub: multi-port repeater (Layer 1)\n// Bridge: connects 2 LANs (Layer 2)\n// Switch: multi-port bridge with MAC table (Layer 2)\n// Router: connects networks using IP (Layer 3)\n// Gateway: connects different protocols (Layer 4-7)"),

            topic("Protocols and Ports",
                "Protocols are rules for communication. Each service uses a specific port number. Well-known ports: 0-1023. Registered: 1024-49151. Dynamic: 49152-65535.",
                "HTTP:80, HTTPS:443, FTP:21, SSH:22, SMTP:25, DNS:53, MySQL:3306, MongoDB:27017",
                "// Important Protocols:\n// HTTP: 80 (web)\n// HTTPS: 443 (secure web)\n// FTP: 20/21 (file transfer)\n// SSH: 22 (secure shell)\n// Telnet: 23 (remote login)\n// SMTP: 25 (send email)\n// DNS: 53 (name resolution)\n// DHCP: 67/68 (IP assignment)\n// POP3: 110 (receive email)\n// IMAP: 143 (email access)")
        ));

        s.setQuestions(Arrays.asList(
            qna("What is the difference between TCP and UDP?", "TCP: reliable, connection-oriented, ordered, slower. UDP: unreliable, connectionless, faster. TCP for file transfer, UDP for video streaming."),
            qna("Explain TCP 3-way handshake?", "1. Client sends SYN. 2. Server responds SYN-ACK. 3. Client sends ACK. Connection established. Ensures both sides are ready to communicate."),
            qna("What is the difference between OSI and TCP/IP model?", "OSI: 7 layers, theoretical reference model. TCP/IP: 4 layers, practical implementation used in internet. TCP/IP combines OSI's top 3 layers into Application and bottom 2 into Network Access."),
            qna("What happens when you type www.google.com in browser?", "1. Browser checks DNS cache. 2. OS checks hosts file. 3. DNS query to resolve IP. 4. TCP 3-way handshake with server. 5. HTTP GET request. 6. Server responds with HTML. 7. Browser renders page."),
            qna("What is the difference between a router and a switch?", "Switch: Layer 2, uses MAC addresses, connects devices within same network. Router: Layer 3, uses IP addresses, connects different networks, determines best path."),
            qna("What is NAT?", "Network Address Translation converts private IP addresses to public IP. Allows multiple devices to share one public IP. Types: Static NAT, Dynamic NAT, PAT (Port Address Translation/IP Masquerading)."),
            qna("What is a subnet mask?", "Subnet mask determines which part of IP is network and which is host. /24 = 255.255.255.0 means first 24 bits are network. Used to divide large networks into smaller subnets.")
        ));
        return s;
    }

    // ==================== DBMS ====================
    private Subject buildDBMS() {
        Subject s = new Subject();
        s.setSubject("DBMS");
        s.setYoutubeLinks(Arrays.asList(
            "https://www.youtube.com/watch?v=kBdlM6hNDAE"
        ));

        s.setTopics(Arrays.asList(
            topic("What is DBMS?",
                "Database Management System is software that manages databases. It provides an interface to store, retrieve, update, and delete data. Advantages over file system: data redundancy control, data integrity, concurrent access, backup and recovery, security. Examples: MySQL, PostgreSQL, Oracle, MongoDB.",
                "Instead of Excel files, use MySQL for structured data management",
                "// DBMS vs File System:\n// DBMS: data independence, ACID, concurrency, relationships\n// File System: simple but redundancy, no relationships, no ACID\n\n// Types of DBMS:\n// Relational (RDBMS): MySQL, PostgreSQL, Oracle\n// NoSQL: MongoDB, Cassandra, Redis\n// NewSQL: Google Spanner, CockroachDB"),

            topic("ACID Properties",
                "ACID ensures reliable database transactions. Atomicity: transaction is all-or-nothing (complete or rollback). Consistency: database moves from one valid state to another. Isolation: concurrent transactions don't interfere. Durability: committed transactions survive system failures (written to disk).",
                "Bank transfer: debit A + credit B must both succeed or both fail (Atomicity)",
                "// ACID in SQL:\nBEGIN TRANSACTION;\n  UPDATE accounts SET balance = balance - 1000 WHERE id = 1;\n  UPDATE accounts SET balance = balance + 1000 WHERE id = 2;\nCOMMIT; -- both succeed\n-- or\nROLLBACK; -- both fail (atomicity)"),

            topic("Normalization",
                "Normalization organizes database to reduce redundancy and improve integrity. 1NF: atomic values, no repeating groups. 2NF: 1NF + no partial dependency (non-key attributes depend on full primary key). 3NF: 2NF + no transitive dependency. BCNF: stronger 3NF. Higher normal forms reduce redundancy but increase joins.",
                "Unnormalized: Student(ID, Name, Course1, Course2) -> 1NF: separate rows per course",
                "-- 1NF violation: multiple values in one column\n-- Bad: courses = 'Math, Science, English'\n-- Good: separate row for each course\n\n-- 2NF violation: partial dependency\n-- Bad: (StudentID, CourseID) -> StudentName (depends only on StudentID)\n-- Good: move StudentName to Students table\n\n-- 3NF violation: transitive dependency\n-- Bad: StudentID -> ZipCode -> City\n-- Good: separate ZipCode-City into another table"),

            topic("SQL Commands - DDL, DML, DCL, TCL",
                "DDL (Data Definition): CREATE, ALTER, DROP, TRUNCATE — structure. DML (Data Manipulation): SELECT, INSERT, UPDATE, DELETE — data. DCL (Data Control): GRANT, REVOKE — permissions. TCL (Transaction Control): COMMIT, ROLLBACK, SAVEPOINT — transactions.",
                "DDL: CREATE TABLE. DML: INSERT INTO. DCL: GRANT SELECT. TCL: COMMIT",
                "-- DDL\nCREATE TABLE students (id INT PRIMARY KEY, name VARCHAR(50));\nALTER TABLE students ADD email VARCHAR(100);\nDROP TABLE students;\n\n-- DML\nINSERT INTO students VALUES (1, 'Pranav', 'p@gmail.com');\nSELECT * FROM students WHERE id = 1;\nUPDATE students SET name = 'PK' WHERE id = 1;\nDELETE FROM students WHERE id = 1;\n\n-- DCL\nGRANT SELECT ON students TO user1;\nREVOKE SELECT ON students FROM user1;"),

            topic("SQL Joins",
                "Joins combine rows from multiple tables based on related columns. INNER JOIN: only matching rows from both tables. LEFT JOIN: all rows from left + matching from right (NULL if no match). RIGHT JOIN: all from right + matching from left. FULL JOIN: all rows from both tables. CROSS JOIN: cartesian product.",
                "Students INNER JOIN Courses ON Students.courseId = Courses.id",
                "-- INNER JOIN\nSELECT s.name, c.courseName\nFROM students s\nINNER JOIN courses c ON s.courseId = c.id;\n\n-- LEFT JOIN (all students even without course)\nSELECT s.name, c.courseName\nFROM students s\nLEFT JOIN courses c ON s.courseId = c.id;\n\n-- Self Join (manager-employee)\nSELECT e.name, m.name as manager\nFROM employees e\nJOIN employees m ON e.managerId = m.id;"),

            topic("Keys in DBMS",
                "Primary Key: unique identifier for each row, cannot be NULL. Foreign Key: references primary key of another table, maintains referential integrity. Candidate Key: minimal set of attributes to uniquely identify a row. Composite Key: primary key with multiple columns. Super Key: set of attributes uniquely identifying a row (superset of candidate key). Unique Key: unique but can have NULL.",
                "Students(StudentID-PK, Email-Unique, DeptID-FK references Department)",
                "-- Primary Key\nCREATE TABLE students (\n  id INT PRIMARY KEY AUTO_INCREMENT,\n  email VARCHAR(100) UNIQUE NOT NULL\n);\n-- Foreign Key\nCREATE TABLE enrollments (\n  id INT PRIMARY KEY,\n  studentId INT,\n  courseId INT,\n  FOREIGN KEY (studentId) REFERENCES students(id),\n  FOREIGN KEY (courseId) REFERENCES courses(id)\n);"),

            topic("Indexing",
                "Index is a data structure that improves data retrieval speed at the cost of extra storage and slower writes. Types: Primary Index (on primary key), Secondary Index (on non-key), Clustered Index (data physically sorted by index), Non-clustered Index (separate from data). B-Tree is most common index structure.",
                "Without index: scan all rows O(n). With index: O(log n) search.",
                "-- Create index\nCREATE INDEX idx_email ON students(email);\n\n-- Composite index\nCREATE INDEX idx_name_dept ON students(name, deptId);\n\n-- Drop index\nDROP INDEX idx_email ON students;\n\n-- EXPLAIN to check if index is used\nEXPLAIN SELECT * FROM students WHERE email = 'p@gmail.com';"),

            topic("Transactions and Isolation Levels",
                "Transaction: sequence of operations treated as single unit. Isolation levels control visibility of uncommitted changes: READ UNCOMMITTED (dirty read possible), READ COMMITTED (no dirty read), REPEATABLE READ (no non-repeatable read, MySQL default), SERIALIZABLE (no phantom read, strictest). Higher isolation = less concurrency.",
                "Transfer 1000 from A to B = one transaction (debit + credit)",
                "-- Transaction\nSTART TRANSACTION;\nUPDATE account SET balance = balance - 1000 WHERE id = 1;\nUPDATE account SET balance = balance + 1000 WHERE id = 2;\nCOMMIT;\n\n-- Set isolation level\nSET TRANSACTION ISOLATION LEVEL REPEATABLE READ;\n\n-- Savepoint\nSAVEPOINT sp1;\n-- some operations\nROLLBACK TO sp1; -- rollback to savepoint")
        ));

        s.setQuestions(Arrays.asList(
            qna("What are ACID properties?", "Atomicity: all or nothing. Consistency: valid state to valid state. Isolation: concurrent transactions independent. Durability: committed data persists after failure."),
            qna("What is normalization and why is it needed?", "Normalization organizes database to reduce redundancy and improve integrity. 1NF removes repeating groups, 2NF removes partial dependencies, 3NF removes transitive dependencies. Prevents update/insert/delete anomalies."),
            qna("Difference between DELETE, TRUNCATE, DROP?", "DELETE: DML, removes specific rows, can rollback, WHERE clause. TRUNCATE: DDL, removes all rows, cannot rollback, faster. DROP: DDL, removes entire table structure and data."),
            qna("What is the difference between HAVING and WHERE?", "WHERE filters rows before grouping (works on individual rows). HAVING filters groups after GROUP BY (works on aggregated data). WHERE cannot use aggregate functions, HAVING can."),
            qna("What is a view in SQL?", "Virtual table based on result of a SELECT query. Simplifies complex queries, provides security (show only specific columns), doesn't store data physically (except materialized views)."),
            qna("What is the difference between clustered and non-clustered index?", "Clustered: data physically sorted by index, only one per table, faster for range queries. Non-clustered: separate structure with pointer to data, multiple allowed per table."),
            qna("What is a stored procedure?", "Precompiled SQL code stored in database. Advantages: code reuse, reduced network traffic, better performance, security. Can accept parameters and return values."),
            qna("What is referential integrity?", "Ensures foreign key values always refer to existing primary key values. Prevents orphan records. Enforced by FOREIGN KEY constraints with ON DELETE CASCADE/RESTRICT options.")
        ));
        return s;
    }

    // ==================== Helper methods ====================
    private Subject.Topic topic(String name, String theory, String example, String code) {
        Subject.Topic t = new Subject.Topic();
        t.setTopicName(name);
        t.setTheory(theory);
        t.setExample(example);
        t.setCode(code);
        t.setDiagrams(List.of());
        return t;
    }

    private Subject.QnA qna(String q, String a) {
        Subject.QnA qna = new Subject.QnA();
        qna.setQuestion(q);
        qna.setAnswer(a);
        return qna;
    }
}
