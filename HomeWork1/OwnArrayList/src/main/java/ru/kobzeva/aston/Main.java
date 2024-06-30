package ru.kobzeva.aston;

import ru.kobzeva.aston.classes.ArrayList;
import ru.kobzeva.aston.classes.LinkedList;
import ru.kobzeva.aston.interfaces.List;

import java.util.Comparator;

/**
 * Demonstration of how classes work
 *
 * @author Кобзева Виолетта
 */
//TODO Create tests!!!
public class Main
{
    public static void main( String[] args )
    {
        /**
         * Comparator for strings. Sorts strings by length
         */
        class StringLengthComparator implements Comparator<String>
        {
            public int compare (String obj1, String obj2)
            {
                return obj1.length() - obj2.length();
            }
        }

        StringLengthComparator c = new StringLengthComparator();

        /**
         * Demonstration of ArrayList methods
         */

        ArrayList<String> arrayList0 = new ArrayList<>();
        arrayList0.add("1");
        arrayList0.add(0, "13");
        arrayList0.add(1, "1");
        arrayList0.add(2, "12");
        arrayList0.add(0, "1234");
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("123");
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("123");
        //arrayList2.add(1, "1"); //ERROR: IndexOutOfBoundsException.
        arrayList2.add(0, "1");
        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add("1");
        arrayList3.add(0, "123");
        arrayList3.add(1, "12");
        ArrayList<String> arrayList4 = new ArrayList<>();
        arrayList4.add("1");
        arrayList4.add(0, "123");
        arrayList4.add(1, "12");
        arrayList4.add("1");
        ArrayList<String> arrayList5 = new ArrayList<>();
        arrayList5.add("1");
        arrayList5.add(0, "13");
        arrayList5.add(1, "1");
        arrayList5.add(2, "12");
        arrayList5.add(0, "123");
        //arrayList1.get(1); //ERROR: IndexOutOfBoundsException.

        printAllArrayList(arrayList0, arrayList1, arrayList2, arrayList3, arrayList4, arrayList5);

        System.out.println(arrayList0.remove("13"));
        System.out.println(arrayList0.remove("13"));
        printAllArrayList(arrayList0);
        arrayList0.remove(0);
        printAllArrayList(arrayList0);
        arrayList0.remove("1");
        printAllArrayList(arrayList0);
        arrayList0.remove(1);
        printAllArrayList(arrayList0);
        arrayList0.clear();
        printAllArrayList(arrayList0);

        arrayList0.sort(c);
        arrayList1.sort(c);
        arrayList2.sort(c);
        arrayList3.sort(c);
        arrayList4.sort(c);
        arrayList5.sort(c);

        printAllArrayList(arrayList0, arrayList1, arrayList2, arrayList3, arrayList4, arrayList5);


        /**
         * Demonstration of LinkedList methods
         */
        System.out.println("*******Print LinkedList*******");
        LinkedList<String> linkedList0 = new LinkedList<>();
        linkedList0.add("1");
        linkedList0.add(0, "13");
        linkedList0.add(1, "12");
        linkedList0.addFirst("1234");
        linkedList0.addLast("1");
        LinkedList<String> linkedList1 = new LinkedList<>();
        linkedList1.add("123");
        LinkedList<String> linkedList2 = new LinkedList<>();
        linkedList2.add("123");
        //linkedList2.add(1, "1"); //ERROR: IndexOutOfBoundsException.
        linkedList2.add(0, "1");
        LinkedList<String> linkedList3 = new LinkedList<>();
        linkedList3.add("1");
        linkedList3.add(0, "123");
        linkedList3.add(1, "12");
        LinkedList<String> linkedList4 = new LinkedList<>();
        linkedList4.add("1");
        linkedList4.addFirst("123");
        linkedList4.add(1, "12");
        linkedList4.addLast("1");
        LinkedList<String> linkedList5 = new LinkedList<>();
        linkedList5.add("1");
        linkedList5.add(0, "13");
        linkedList5.add(1, "1");
        linkedList5.add(2, "12");
        linkedList5.add(0, "123");
        //linkedList1.get(1); //ERROR: IndexOutOfBoundsException.

        printAllArrayList(linkedList0, linkedList1, linkedList2, linkedList3, linkedList4, linkedList5);

        /**
         *
         */

        System.out.println(linkedList0.remove("13"));
        System.out.println(linkedList0.remove("13"));
        printAllArrayList(linkedList0);
        linkedList0.removeFirst();
        printAllArrayList(linkedList0);
        linkedList0.remove("1");
        printAllArrayList(linkedList0);
        linkedList0.remove(1);
        printAllArrayList(linkedList0);
        linkedList0.add("123");
        linkedList0.add(0,"123");
        printAllArrayList(linkedList0);
        linkedList0.removeLast();
        printAllArrayList(linkedList0);
        linkedList0.clear();
        printAllArrayList(linkedList0);

        linkedList0.sort(c);
        linkedList1.sort(c);
        linkedList2.sort(c);
        linkedList3.sort(c);
        linkedList4.sort(c);
        linkedList5.sort(c);

        printAllArrayList(linkedList0, linkedList1, linkedList2, linkedList3, linkedList4, linkedList5);
    }

    public static <E> void printAllArrayList(List<E>... argsList) {
        for(int i = 0; i < argsList.length; i++) {
            System.out.print("List" + i + ": ");
            for(int j = 0; j < argsList[i].size(); j++) {
                System.out.print(argsList[i].get(j) + " ");
            }
            System.out.println();
        }
    }
}
