public class IndexThread extends Thread {
    private boolean running;
    private boolean EndThread;
    private boolean gettingChecked;
    public int index;//the threads index TODO private
    private IndexThread[] arr;//the array
    private int value;
    private boolean LeftCheckedMe;
    private boolean RightCheckedMe;

    public IndexThread(int index, IndexThread[] arr) {
        this.running = true;
        this.EndThread = false;
        this.index = index;
        this.arr = arr;
        this.value = (int) (Math.random() * 100) + 1;
        this.LeftCheckedMe = false;
        this.RightCheckedMe = false;
        this.gettingChecked = false;
    }


    @Override
    public void run() {
        super.run();
        while (!EndThread) {
            if (running) {
                System.out.println("thread " + this.index + " started");
                boolean checkedOthers = false;
                int leftIndex = ((this.index - 1) % arr.length + arr.length) % arr.length;
                int rightIndex = ((this.index + 1) % arr.length + arr.length) % arr.length;

                int leftVal;
                int rightVal;

                if (index % 2 == 1) {
                    System.out.println("thread  " + this.index + "checking " + leftIndex);
                    leftVal = arr[leftIndex].getValue(this.index);
                    arr[leftIndex].gotValue();

                    System.out.println("thread  " + this.index + "checking " + rightIndex);
                    rightVal = arr[rightIndex].getValue(this.index);
                    arr[rightIndex].gotValue();
                } else {


                    System.out.println("thread  " + this.index + "checking " + rightIndex);
                    rightVal = arr[rightIndex].getValue(this.index);
                    arr[rightIndex].gotValue();

                    System.out.println("thread  " + this.index + "checking " + leftIndex);
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

                System.out.println("thread " + this.index + " ended");
            }
            running = false;
        }

    }


    public synchronized int getValue() {
        while (gettingChecked) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        gettingChecked = true;
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

    public void endProcess() {
        this.EndThread = true;
    }

    public  boolean getRunning() {
        return this.running;
    }


    public synchronized void reset(){
        this.LeftCheckedMe = false;
        this.RightCheckedMe = false;
        this.gettingChecked = false;
        notifyAll();
    }
    public synchronized void setRunning(boolean running) {
        System.out.println("restarting "+index);
        this.running = running;
    }
}
