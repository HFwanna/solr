package dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;

/**
 * reentrantlock用于替代synchronized
 * 由于m1锁定this,只有m1执行完毕的时候,m2才能执行
 * 这里是复习synchronized最原始的语义
 * 
 * 使用reentrantlock可以完成同样的功能
 * 需要注意的是，必须要必须要必须要手动释放锁（重要的事情说三遍）
 * 使用syn锁定的话如果遇到异常，jvm会自动释放锁，但是lock必须手动释放锁，因此经常在finally中进行锁的释放
 * 
 * 使用reentrantlock可以进行“尝试锁定”tryLock，这样无法锁定，或者在指定时间内无法锁定，线程可以决定是否继续等待
 * 
 * 使用ReentrantLock还可以调用lockInterruptibly方法，可以对线程interrupt方法做出响应，
 * 在一个线程等待锁的过程中，可以被打断
 * 
 * ReentrantLock还可以指定为公平锁
 * 
 * @author mashibing
 */

import java.util.concurrent.locks.ReentrantLock;

import com.sun.javafx.image.IntPixelAccessor;

//算法题目
public class T2 extends Thread {
		
    public static void main(String[] args) {
    	int[] a = {1,1,2,2,3,4,4};
    	System.out.println(findNumber(a));
    	System.out.println(aplusb(1,3));
    	System.out.println(shuixianhua(4));;
    }
    
    //查找数组中不重复的数字
    static int findNumber(int[] a) {
    	int since = 0;
    	for (int i : a) {
    		since++;
    		int count = 1;
			for (int j = since; j < a.length; j++) {
				if(i == a[j]) {
					int temp = a[j];
					a[j] = a[since];
					a[since] = temp;
					break;
				}
				if(++count == a.length - j) {
					return i;
				}
			}
		}
    	return -1;
    }
    
    //位操作计算a+b
    static int aplusb(int a, int b) {
        // write your code here
        int a_ = a^b;
        int b_ = (a&b)<<1;
        while(b_ != 0){
            a_ = a^b;
            b_ = (a&b)<<1;
            a = a_;
            b = b_;
        }
        return a_;
    }
    //水仙花数    1634 = 1^4 + 6^4 + 3^4 + 4^4 = 1634
    static List<Integer> shuixianhua(int n) {
    	ArrayList<Integer> list = new ArrayList<>();
    	int start = (int) Math.pow(10,n-1);
        int end = 0;
        for(int i = 0 ; i < n; i++){
            end += 9 * Math.pow(10,i);
        }
        for(;start <= end;start++){
//            int sum = (int) Math.pow(start&1, n);
        	int sum = 0;
        	int temp = start;
            for(int i = 1; i <= n; i++){
//                sum += (int)Math.pow((start >> i)&1, n);
            	sum += Math.pow((int)(temp/Math.pow(10, n - i)) & 15, n);
            	temp = temp - (int)(temp/Math.pow(10, n - i)) * (int)Math.pow(10, n - i);
            }
            if(sum == start) {
            	list.add(start);
            }
        }
        return list;
    }
    
    //链表反转
	  public class ListNode {
	      int val;
	      ListNode next;
	      ListNode(int x) {
	          val = x;
	          next = null;
	      }
	  }
     

    public class Solution {
        /**
         * @param head: n
         * @return: The new head of reversed linked list.
         */
        public ListNode reverse(ListNode head) {
            // write your code here
            ListNode temp = head;
            int index = 0;
            //第一次循环到末尾
            while(temp.next!=null){
                index++;
                temp = temp.next;
            }
            int[] is = new int[index+1];
            for(int i = 0; i< is.length; i++){
                is[i] = head.val;
                head = head.next;
            }
            temp = head;
            for(int i =0; i<is.length; i++){
                head = new ListNode(is[i]);
                head.next = temp;
                temp = head;
            }
            return head;
            //方法二：原地反转
            /*ListNode current = head;
            ListNode next = null;
            ListNode previous = null;
            //最后判定条件
            while(current.next !=null){
                next = current.next;
                current.next = previous;
                previous = current;
                current = next;
            }
            current.next = previous;
            return current;*/
                
                
        }
    }
    
    
}

