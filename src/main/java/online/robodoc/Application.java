package online.robodoc;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@Push(PushMode.AUTOMATIC)
@SpringBootApplication
@Theme("default")
public class Application implements AppShellConfigurator
{

    @Bean
    public Clock clock()
    {
        return Clock.systemDefaultZone(); // You can also use Clock.systemUTC()
    }

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

}
