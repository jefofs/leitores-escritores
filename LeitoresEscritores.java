import java.util.concurrent.Semaphore;

class ReaderWritersProblem {

    static Semaphore bloqLeitor = new Semaphore(1);
    static Semaphore bloqEscritor = new Semaphore(1);
    static int contaLeitor = 0;

    static class Read extends Thread {
        @Override
        public void run() {
            try {
                //Acquire Section
                bloqLeitor.acquire();
                contaLeitor++;
                if (contaLeitor == 1) {
                    bloqEscritor.acquire();
                }
                bloqLeitor.release();

                //Reading section
                System.out.println("Leitor "+Thread.currentThread().getName() + " Está lendo");
                Thread.sleep(1500);
                System.out.println("Leitor "+Thread.currentThread().getName() + " terminou de ler");

                //Releasing section
                bloqLeitor.acquire();
                contaLeitor--;
                if(contaLeitor == 0) {
                    bloqEscritor.release();
                }
                bloqLeitor.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                bloqEscritor.acquire();
                System.out.println("Escritor "+Thread.currentThread().getName() + " está escrevendo");
                Thread.sleep(2500);
                System.out.println("Escritor "+Thread.currentThread().getName() + " terminou de escrever");
                bloqEscritor.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Read read = new Read();
        Write write = new Write();
        Thread t1 = new Thread(read);
        t1.setName("thread1");
        Thread t2 = new Thread(read);
        t2.setName("thread2");
        Thread t3 = new Thread(write);
        t3.setName("thread3");
        Thread t4 = new Thread(read);
        t4.setName("thread4");
        t1.start();
        t3.start();
        t2.start();
        t4.start();
    }
}
