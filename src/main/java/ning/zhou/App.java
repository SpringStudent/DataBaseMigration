package ning.zhou;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        ApplicationContextHolder.APPLICATION_CONTEXT = applicationContext;
        applicationContext.setConfigLocation("applicationContext.xml");
        applicationContext.refresh();
        applicationContext.start();
    }
}
