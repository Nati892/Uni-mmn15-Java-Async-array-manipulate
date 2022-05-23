import java.util.Scanner;

public class ArrayManipulate {
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        runProgram();
    }


    private static void runProgram() {
        System.out.println("Hi, Please enter the threads array size");
        int arrSize = inputPositiveNum(2);
        System.out.println("Please enter the number of rounds to do");
        int roundsAmount = inputPositiveNum(1);

        IndexThread[] threadsArr = new IndexThread[arrSize];

        initArray(threadsArr);
        printArray(threadsArr);

        //iterate array of threads m times
        for (int i = 0; i < roundsAmount; i++) {
            mIterate(threadsArr);
        }
        System.out.println("**************** Done ***************");
    }


    private static void initArray(IndexThread[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new IndexThread(i, arr, (int) (Math.random() * 100) + 1);
        }

    }

    private static void mIterate(IndexThread[] arr) {

        for (int i = 0; i < arr.length; i++) {//remake array of threads
            arr[i] = new IndexThread(i, arr, arr[i].getValue());
        }

        for (int i = 0; i < arr.length; i++) {//restart threads
            arr[i].start();
        }

        for (int i = 0; i < arr.length; i++) {//wait for threads to finish
            try {
                arr[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printArray(arr);
    }

    //get an int from the user
    private static int inputPositiveNum(int min) {
        int res = 0;
        boolean good_input = false;
        System.out.println("Please enter an integer of minimum " + min);
        while (!good_input) {
            try {
                res = Integer.parseInt(scanner.next());
                if (res >= min) good_input = true;
                else System.out.println("The number must be a minimum of " + min + "please re enter");
            } catch (Exception e) {
                System.out.println("Error - Please re-enter integer");
            }
        }
        return res;
    }

    //a beautiful print of the array's numbers
    private static void printArray(IndexThread[] arr) {
        if (arr.length > 0) System.out.print("[" + arr[0].getValue());
        for (int i = 1; i < arr.length; i++) {
            System.out.print(",");
            if (arr[i].getValue() < 10) System.out.print(" ");
            if (arr[i].getValue() < 100) System.out.print(" ");
            System.out.print(arr[i].getValue());
            arr[i].gotValue();
        }
        System.out.println("]");
    }

}
