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
		System.out.println(" ������ �Һ��� ���� ���� ");
	}
}

// ������ ������
class Producer extends Thread {
	Queue<Integer> q;

	public Producer(Queue<Integer> q) {
		this.q = q;
	}

	public void run() {
		int item; // ��ǰ
		for (int i = 0; i < 100; i++) { // ��ǰ 100�� ����
			item = i;
			synchronized (q) {
				q.offer(item); // ��ǰ�� ť�� ����
				System.out.println("���� : " + item);
				q.notify(); // ������� �Һ��ڿ��� �˸�
			}
			try {
				Thread.sleep((int) (Math.random() * 10));
			} catch (InterruptedException e) {
			}
		}
	}
}

// �Һ��� ������
class Consumer extends Thread {
	Queue<Integer> q;
	int number;

	public Consumer(Queue<Integer> q, int i) {
		this.q = q;
		number = i;
	}

	public void run() {
		int item; // ��ǰ
		int totalItem = 0; // ���� �Һ��� �� ��ǰ ����
		while (!Thread.currentThread().isInterrupted()) {
			synchronized (q) {
				if (q.peek() == null) { // ť�� ��ǰ�� �ϳ��� ������
					try {
						System.out.println("\t �Һ���" + number + " ���");
						q.wait(); // ���
					} catch (InterruptedException e) {
						System.out.println("\t �� �Һ� ���� : " + totalItem);
						Thread.currentThread().interrupt();
					}
				} else {
					item = q.poll(); // ť�� ù��° ��ǰ ��������
					System.out.println("\t �Һ� : " + item);
					totalItem++; // �� �Һ� ���� ����
				}
			}
		}
	}
}