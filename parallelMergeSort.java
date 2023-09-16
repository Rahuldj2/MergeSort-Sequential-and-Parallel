import java.util.Arrays;

public class parallelMergeSort {
    public static void ParallelMergeSort(int[] arr, int numThreads, int lo, int hi) {
        if (numThreads <= 1) {
            mergeSort(arr, lo, hi);
        } else {
            int mid = lo + (hi - lo) / 2;

            // Create two threads for sorting each subarray
            Thread leftThread = new Thread(() -> ParallelMergeSort(arr, numThreads / 2, 0, mid));
            Thread rightThread = new Thread(() -> ParallelMergeSort(arr, numThreads / 2, mid + 1, hi));

            leftThread.start();
            rightThread.start();

            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                // Handle exception
            }

            // Merge the two sorted subarrays
            merge(arr, lo, hi, mid);
        }
    }

    public static void merge(int[] arr, int lo, int hi, int mid) {
        int n1 = mid - lo + 1;
        int n2 = hi - mid;

        int[] larr = new int[n1];
        int[] rarr = new int[n2];
        int i = 0;
        int j = 0;
        int k = 0;
        for (i = 0; i < n1; i++) {
            larr[i] = arr[i + lo];
        }
        k = 0;
        for (i = 0; i < n2; i++) {
            rarr[i] = arr[mid + 1 + i];
        }

        i = 0;
        j = 0;
        k = lo;
        while (i < n1 && j < n2) {
            if (larr[i] < rarr[j]) {
                arr[k] = larr[i];
                i++;
            } else {
                arr[k] = rarr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = larr[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = rarr[j];
            j++;
            k++;
        }
    }

    public static void mergeSort(int[] arr, int lo, int hi) {
        if (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            mergeSort(arr, lo, mid);
            mergeSort(arr, mid + 1, hi);
            merge(arr, lo, hi, mid);
        }
    }

    public static void main(String[] args) {
        // int[] arr = { 5, 2, 9, 1, 5, 6, 3, 4, 8, 0, 87, 67, 12, 34, 56, 23 };
        int[] arr = new int[10000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (1 + Math.random() * 1000); // Random integers between 0 and 9999
        }
        int numThreads = Runtime.getRuntime().availableProcessors(); // Number of CPU cores
        System.out.println(numThreads);
        long startTime = System.nanoTime();
        ParallelMergeSort(arr, numThreads, 0, arr.length - 1);
        long endTime = System.nanoTime();
        // System.out.println(Arrays.toString(arr));
        System.out.println("TIME TAKEN: " + (endTime - startTime));
    }
}
