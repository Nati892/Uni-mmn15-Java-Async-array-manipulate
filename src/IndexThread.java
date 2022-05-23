public class IndexThread extends Thread {

    private int index;//the threads index
    private IndexThread[] arr;//the array
    private int value;//thread's value
    private boolean LeftCheckedMe;
    private boolean RightCheckedMe;

    public IndexThread(int index, IndexThread[] arr, int value) {
        this.index = index;
        this.arr = arr;
        this.value = value;
        this.LeftCheckedMe = false;
        this.RightCheckedMe = false;
    }


    @Override
    public void run() {
        super.run();

        //   System.out.println("thread " + this.index + " started");
        boolean checkedOthers = false;
        int leftIndex = ((this.index - 1) % arr.length + arr.length) % arr.length;//the array is cyclic
        int rightIndex = ((this.index + 1) % arr.length + arr.length) % arr.length;//the array is cyclic

        int leftVal;
        int rightVal;

        //solved the dining philosopher problem with the following solution modded to this case
        //"An even philosopher should pick the right chopstick and then the left chopstick while an odd philosopher should pick the left chopstick and then the right chopstick."

        if (index % 2 == 1) {

            leftVal = arr[leftIndex].getValue(this.index);
            arr[leftIndex].gotValue();

            rightVal = arr[rightIndex].getValue(this.index);
            arr[rightIndex].gotValue();

        } else {

            rightVal = arr[rightIndex].getValue(this.index);
            arr[rightIndex].gotValue();

            leftVal = arr[leftIndex].getValue(this.index);
            arr[leftIndex].gotValue();
        }

        while (!checkedOthers) {//make sure I change the value only if both near threads checked this thread's value
            if (LeftCheckedMe && RightCheckedMe) {

                if (leftVal > this.value && rightVal > this.value) {
                    this.value++;
                } else if (leftVal < this.value && rightVal < this.value) {
                    this.value--;
                }

                checkedOthers = true;
            }
        }

    }

    /**
     * this function returns the thread's value and is synchronized
     * @return int - thread's value
     */
    //only used by main thread for printing the values in the end
    public int getValue() {
        return this.value;
    }

    public synchronized int getValue(int checkerIndex) {

        if (checkerIndex == (((this.index + 1) % arr.length + arr.length) % arr.length)) {//the array is cyclic
            this.RightCheckedMe = true;
        }

        if (checkerIndex == (((this.index - 1) % arr.length + arr.length) % arr.length)) {//the array is cyclic
            this.LeftCheckedMe = true;
        }
        return this.value;
    }

    /**
     * should be called after the thread got his value
     */
    public synchronized void gotValue() {
        notifyAll();
    }

}
