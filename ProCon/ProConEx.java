package ProCon;

import java.util.LinkedList;
import java.util.Queue;

public class ProConEx {
	public static void main(String args[]) {
		Queue<Integer> que = new LinkedList<Integer>();
		Thread proTh = new Producer(que);
		Thread conTh = new Consumer(que, 1);
		proTh.start();
		conTh.start();
		try {
			proTh.join();
			conTh.interrupt();
		} catch (InterruptedException e) {
		}
		System.out.println(" 생산자 소비자 패턴 종료 ");
	}
}

// 생산자 스레드
class Producer extends Thread {
	Queue<Integer> q;

	public Producer(Queue<Integer> q) {
		this.q = q;
	}

	public void run() {
		int item; // 상품
		for (int i = 0; i < 100; i++) { // 상품 100개 생산
			item = i;
			synchronized (q) {
				q.offer(item); // 상품을 큐에 삽입
				System.out.println("생산 : " + item);
				q.notify(); // 대기 중인 소비자에게 알림
			}
			try {
				Thread.sleep((int) (Math.random() * 10));
			} catch (InterruptedException e) {
			}
		}
	}
}

// 소비자 스레드
class Consumer extends Thread {
	Queue<Integer> q;
	int number;

	public Consumer(Queue<Integer> q, int i) {
		this.q = q;
		number = i;
	}

	public void run() {
		int item; // 상품
		int totalItem = 0; // 내가 소비한 총 상품 개수
		while (!Thread.currentThread().isInterrupted()) {
			synchronized (q) {
				if (q.peek() == null) { // 큐에 상품이 하나도 없으면
					try {
						System.out.println("\t 소비자" + number + " 대기");
						q.wait(); // 대기
					} catch (InterruptedException e) {
						System.out.println("\t 총 소비 개수 : " + totalItem);
						Thread.currentThread().interrupt();
					}
				} else {
					item = q.poll(); // 큐의 첫번째 상품 가져오기
					System.out.println("\t 소비 : " + item);
					totalItem++; // 총 소비 개수 증가
				}
			}
		}
	}
}