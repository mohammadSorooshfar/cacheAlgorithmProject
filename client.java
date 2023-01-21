
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

public class client {
  public static int framesCount = 0;
  public static int neededFrames = 0;
  public static int fifo[];
  public static int fifoFaults = 0;

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
    fifo = new int[input];
    framesCount = input;
    for (int i = 0; i < framesCount; i++)
      fifo[i] = -1;
    input = socketIn.readInt();
    while (input != 0) {
      System.out.println(input);
      fifo(input);
      System.out.println("fifo:[");
      for (int i = 0; i < framesCount; i++)
        System.out.println(fifo[i] + ",");
      System.out.println("]");
      input = socketIn.readInt();
    }
    System.out.println("Faults: " + fifoFaults);
  }

}