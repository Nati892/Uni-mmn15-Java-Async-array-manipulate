public class IndexThread extends Thread {

    private boolean gettingChecked;
    public int index;//the threads index TODO private
    private IndexThread[] arr;//the array
    private int value;
    private boolean LeftCheckedMe;
    private boolean RightCheckedMe;

    public IndexThread(int index, IndexThread[] arr, int value) {
        this.index = index;
        this.arr = arr;
        this.value = value;
        this.LeftCheckedMe = false;
        this.RightCheckedMe = false;
        this.gettingChecked = false;
    }


    @Override
    public void run() {
        super.run();

        //   System.out.println("thread " + this.index + " started");
        boolean checkedOthers = false;
        int leftIndex = ((this.index - 1) % arr.length + arr.length) % arr.length;
        int rightIndex = ((this.index + 1) % arr.length + arr.length) % arr.length;

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

        while (!checkedOthers) {
            if (LeftCheckedMe && RightCheckedMe) {

                if (leftVal > this.value && rightVal > this.value) {
                    this.value++;
                } else if (leftVal < this.value && rightVal < this.value) {
                    this.value--;
                }

                checkedOthers = true;
            }
        }

        //  System.out.println("thread " + this.index + " ended");
    }


    public int getValue() {
        return this.value;
    }

    public synchronized int getValue(int checkerIndex) {


        if (checkerIndex == (((this.index + 1) % arr.length + arr.length) % arr.length)) {
            this.RightCheckedMe = true;
        }

        if (checkerIndex == (((this.index - 1) % arr.length + arr.length) % arr.length)) {
            this.LeftCheckedMe = true;
        }
        return this.value;
    }


    public synchronized void gotValue() {
        this.gettingChecked = false;
        notifyAll();
    }

}
