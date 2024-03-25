// package ProCon;

import java.util.LinkedList;
import java.util.Queue;

public class ProCon {
	public static void main(String args[]) {
		Queue<Integer> que = new LinkedList<Integer>();
		Thread proTh = new Producer(que);
		// 소비자 스레드를 3개 생성
		Thread conTh1 = new Consumer(que, 1);
		Thread conTh2 = new Consumer(que, 2);
		Thread conTh3 = new Consumer(que, 3);
		// 소비자 스레드3의 우선 순위를 10으로 설정
		conTh3.setPriority(Thread.MAX_PRIORITY);
		proTh.start();
		conTh1.start();
		conTh2.start();
		conTh3.start();
		try {
			// 3초 동안 block
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		// 모든 스레드를 종료시킴
		proTh.interrupt();
		conTh1.interrupt();
		conTh2.interrupt();
		conTh3.interrupt();
		System.out.println(" 생산자 소비자 패턴 종료 ");
	}
}

// 생산자 스레드
class Producer extends Thread {
	Queue<Integer> q;

	public Producer(Queue<Integer> q) {
		this.q = q;
	}

	@Override
	public void run() {
		int item = 0; // 상품
		while (!Thread.currentThread().isInterrupted()) { // 상품을 무한히 생산
			synchronized (q) {
				q.offer(item); // 상품을 큐에 삽입
				System.out.println("생산 : " + item);
				q.notify(); // 대기 중인 소비자에게 알림
			}
			try {
				Thread.sleep((int) (Math.random() * 10));
			} catch (InterruptedException e) {
				// interrupt를 받으면 다시 스스로를 interrupt
				Thread.currentThread().interrupt();
			}
			item++;
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

	@Override
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
						// 각 소비자 스레드는 자신의 총 소비 개수를 표시
						// 각 소비자 스레드는 자신의 스레드 우선 순위를 표시
						System.out.println("\t 소비자 " + number
								+ "의 총 소비 개수 : " + totalItem
								+ ", 스레드 우선 순위 : " + Thread.currentThread().getPriority());
						// interrupt를 받으면 다시 스스로를 interrupt
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