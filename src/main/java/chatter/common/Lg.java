package chatter.common;

/**
 * Created by c0s on 16-4-20.
 */
public class Lg {
    private StringBuilder stringBuilder = new StringBuilder();
    private Class tClass;
    public void log(String string) {
        System.err.println(Thread.currentThread().getName() + " " + stringBuilder.toString() + " :" + string);
    }

    public <T> Lg(Class<T> tClass) {
        this.tClass = tClass;
        stringBuilder.append(tClass.getSimpleName());
    }
}
