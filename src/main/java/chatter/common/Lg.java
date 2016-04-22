package chatter.common;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by c0s on 16-4-20.
 */
public class Lg {
    private Class tClass;
    private static BlockingQueue<String> workQueue = new LinkedBlockingQueue<String>(200);
    private static Writer output = new OutputStreamWriter(System.err);
    private static Thread workThread;
    private static volatile boolean active;

    private ThreadLocal<SimpleDateFormat> FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        }
    };

    static class LoggerClosed extends RuntimeException {

    }

    public boolean log(String string) {
        //format : time thread className output message
        if (!active) {
            throw new LoggerClosed();
        }
        String s = FORMAT.get().format(new Date()) +
                " " +
                Thread.currentThread().getName() +
                " " +
                tClass.getSimpleName() +
                " " +
                string;

        try {
            workQueue.put(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static class LogWriter implements Runnable {
        @Override
        public void run() {
            String str;
            while (true) {
                if (!active && workQueue.size() == 0) {
                    break;
                }
                try {
                    str = workQueue.take();
                    output.write(str);
                    output.flush();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("LogWriter exit");
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        active = true;
        workThread = new Thread(new LogWriter());
        workThread.start();
    }

    public static void closeLogger() {
        active = false;
    }

    public <T> Lg(Class<T> tClass) {
        this.tClass = tClass;
    }

    public static void main(String[] args) throws InterruptedException {
        Lg lg = new Lg(PingMessage.class);
        lg.log("hahaha");
        Lg.closeLogger();
    }
}
