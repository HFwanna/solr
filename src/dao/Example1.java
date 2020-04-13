package dao;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.sun.org.apache.bcel.internal.generic.NEW;

import sun.misc.Unsafe;

class Example1 extends Thread {
	 
	private A a;
	private B b;
	public Example1(A a,B b) {
		this.a = a;
		this.b = b;
	}

    public static void main(String args[]) throws Exception {
    	Unsafe unsafe = Unsafe.getUnsafe();
    	A a = new A();
    	B b = new B();
    	new Example1(a,b).start();
    	b.getA(a);
    }
 
    public void run() {
    	System.out.println("进入分线程");
    	a.getA(b);
    }
}

class A{
	public int a = 3;
	public int b = 7;
	public synchronized int getA(B b) {
		System.out.println(Thread.currentThread().getName() + "进入A");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b.get();
		System.out.println("A:");
		return a;
	}
	public synchronized void get() {
		
	}
}

class B extends A{
	public synchronized int getA(A a) {
		System.out.println("进入B");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a.get();
		return 1;
	}
	public synchronized void get() {
		
	}
}
