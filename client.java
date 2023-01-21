
// import java.io.*;
// import java.util.*;

// class FIFO {

//   public static void main(String args[]) throws IOException {
//     int n;
//     int f;
//     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//     System.out.println("Enter the number of FRAMES :");
//     f = Integer.parseInt(br.readLine());
//     int fifo[] = new int[f];
//     System.out.println("Enter the number of INPUTS :");
//     n = Integer.parseInt(br.readLine());
//     int inp[] = new int[n];
//     System.out.println("Enter INPUT:");
//     for (int i = 0; i < n; i++)
//       inp[i] = Integer.parseInt(br.readLine());

//     for (int i = 0; i < f; i++)
//       fifo[i] = -1;
//     int Fault = 0;
//     int j = 0;
//     boolean check;
//     for (int i = 0; i < n; i++) {
//       check = false;

//       for (int k = 0; k < f; k++)
//         if (fifo[k] == inp[i]) {
//           check = true;
//         }
//       if (check == false) {
//         fifo[j] = inp[i];
//         j++;
//         if (j >= f)
//           j = 0;
//         Fault = Fault + 1;
//       }

//     }
//     System.out.println("FAULT:" + Fault);
//   }

// }

// class GFG {
// public static void main(String[] args) throws IOException {

// int n;
// int f;

// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
// System.out.println("Enter the number of FRAMES :");
// f = Integer.parseInt(br.readLine());
// System.out.println("Enter the number of INPUTS :");
// n = Integer.parseInt(br.readLine());
// int inp[] = new int[n];
// System.out.println("Enter INPUT:");
// for (int i = 0; i < n; i++)
// inp[i] = Integer.parseInt(br.readLine());
// System.out.println("Page Faults = "
// + pageFaults(n, f, inp));
// }

// static int pageFaults(int N, int C, int pages[]) {
// // a queue to maintain page frames
// Queue<Integer> q = new LinkedList<>();

// // starting with 0 page faults
// int pageFault = 0;
// for (int i = 0; i < N; i++) {

// // if queue is empty or the current page is
// // absent in the page frame then page fault
// // occurs
// if (q.isEmpty() || !q.contains(pages[i])) {
// if (q.size() == C) // if frame is full we remove the
// // least recently used page
// q.poll(); // removing the front element
// // which is not replaced for
// // long time

// q.add(pages[i]); // now we add the page to
// // frame
// pageFault++; // incrementing page faults
// } else {
// q.remove(pages[i]); // if the page already exists
// // we'll remove it from
// // previous position and add
// // it to the end of the queue
// q.add(pages[i]);
// }

// }
// return pageFault;
// }
// }

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class client {
  public static int framesCount = 0;
  public static int neededFrames = 0;
  public static int fifo[];
  public static int fifoFaults = 0;
  public static int lruFaults = 0;
  public static int secondChanceFaults = 0;
  public static boolean second_chance[];
  public static int pageNumberSecondChance[];
  public static int pointerSecondChance = 0;
  public static Queue<Integer> lruQueue = new LinkedList<>();

  public static void fifo(int input) {
    boolean check = false;
    for (int k = 0; k < framesCount; k++) {
      if (fifo[k] == input) {
        check = true;
      }
    }
    if (!check) {
      fifo[neededFrames] = input;
      neededFrames++;
      if (neededFrames >= framesCount)
        neededFrames = 0;
      fifoFaults++;
    }
  }

  public static void LRU(int input) {
    if (lruQueue.isEmpty() || !lruQueue.contains(input)) {
      if (lruQueue.size() == framesCount)
        lruQueue.poll();
      lruQueue.add(input);
      lruFaults++;
    } else {
      lruQueue.remove(input);
      lruQueue.add(input);
    }

  }

  // If page found, updates the second chance bit to true
  static boolean findAndUpdate(int x) {
    for (int i = 0; i < framesCount; i++) {
      if (pageNumberSecondChance[i] == x) {
        second_chance[i] = true;
        return true;
      }
    }
    return false;
  }

  static int replaceAndUpdate(int x) {
    while (true) {
      if (!second_chance[pointerSecondChance]) {
        pageNumberSecondChance[pointerSecondChance] = x;
        return (pointerSecondChance + 1) % framesCount;
      }
      second_chance[pointerSecondChance] = false;
      pointerSecondChance = (pointerSecondChance + 1) % framesCount;
    }
  }

  static void secondChance(int input) {
    if (!findAndUpdate(input)) {
      pointerSecondChance = replaceAndUpdate(input);
      secondChanceFaults++;
    }

  }

  public static void main(String[] args) throws Exception {

    String host = "localhost";
    int port = 8080;
    Socket s = new Socket(host, port);
    DataInputStream socketIn = null;
    try {
      socketIn = new DataInputStream(s.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
    int input = socketIn.readInt();
    second_chance = new boolean[input];
    pageNumberSecondChance = new int[input];
    Arrays.fill(pageNumberSecondChance, -1);
    fifo = new int[input];
    framesCount = input;
    for (int i = 0; i < framesCount; i++)
      fifo[i] = -1;
    input = socketIn.readInt();
    int x = 2;
    while (x != 24) {
      System.out.println(input);
      fifo(input);
      LRU(input);
      secondChance(input);
      System.out.println("fifo:[");
      for (int i = 0; i < framesCount; i++)
        System.out.println(fifo[i] + ",");
      System.out.println("]");
      System.out.println("LRU:[");
      for (Object o : lruQueue) {
        System.out.println(o);
      }
      System.out.println("]");
      input = socketIn.readInt();
      x++;
    }
    System.out.println("Fifo Faults: " + fifoFaults);
    System.out.println("LRU Faults: " + lruFaults);
    System.out.println("second chance Faults: " + secondChanceFaults);
  }

}

// Java program to find largest in an array
// without conditional/bitwise/ternary/ operators
// and without library functions.
// import java.util.*;
// import java.io.*;

// class secondChance {
// public static void main(String args[]) throws IOException {
// String reference_string = "";
// int frames = 0;
// reference_string = "7 0 1 2 0 3 0 4 2 3 0 3 0 3 2 1 2 0 1 7 0 1";
// frames = 3;
// printHitsAndFaults(reference_string, frames);

// }

// // If page found, updates the second chance bit to true
// static boolean findAndUpdate(int x, int arr[],
// boolean second_chance[], int frames)

// {
// int i;

// for (i = 0; i < frames; i++) {

// if (arr[i] == x) {
// // Mark that the page deserves a second chance
// second_chance[i] = true;

// // Return 'true', that is there was a hit
// // and so there's no need to replace any page
// return true;
// }
// }

// // Return 'false' so that a page for replacement is selected
// // as he reuested page doesn't exist in memory
// return false;

// }

// // Updates the page in memory and returns the pointer
// static int replaceAndUpdate(int x, int arr[],
// boolean second_chance[], int frames, int pointer) {
// while (true) {

// // We found the page to replace
// if (!second_chance[pointer]) {
// // Replace with new page
// arr[pointer] = x;

// // Return updated pointer
// return (pointer + 1) % frames;
// }

// // Mark it 'false' as it got one chance
// // and will be replaced next time unless accessed again
// second_chance[pointer] = false;

// // Pointer is updated in round robin manner
// pointer = (pointer + 1) % frames;
// }
// }

// static void printHitsAndFaults(String reference_string,
// int frames) {
// int pointer, i, l, x, pf;

// // initially we consider frame 0 is to be replaced
// pointer = 0;

// // number of page faults
// pf = 0;

// // Create a array to hold page numbers
// int arr[] = new int[frames];

// // No pages initially in frame,
// // which is indicated by -1
// Arrays.fill(arr, -1);

// // Create second chance array.
// // Can also be a byte array for optimizing memory
// boolean second_chance[] = new boolean[frames];

// // Split the string into tokens,
// // that is page numbers, based on space
// String str[] = reference_string.split(" ");

// // get the length of array
// l = str.length;

// for (i = 0; i < l; i++) {

// x = Integer.parseInt(str[i]);

// // Finds if there exists a need to replace
// // any page at all
// if (!findAndUpdate(x, arr, second_chance, frames)) {
// // Selects and updates a victim page
// pointer = replaceAndUpdate(x, arr,
// second_chance, frames, pointer);

// // Update page faults
// pf++;
// }
// }

// System.out.println("Total page faults were " + pf);
// }
// }