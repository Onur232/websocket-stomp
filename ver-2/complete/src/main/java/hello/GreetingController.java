package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {
	/*
	 * connectionCount'ı saymazdan evvel ui'da her send yapıldığında greeting metodu tetiklendiğinden her seferinde
	 * tüm isimleri ekrana bastırıyordu. Tek isim ekrana bassın istiyorsan, yani isim değişssin istiyorsan aşağıdaki gibi
	 * olmalı.
	 */

	private int connectionCount=0;

	@Autowired
	private SimpMessagingTemplate template;
	
	private HelloMessage message;

	@MessageMapping("/hello")
	// @SendTo("/topic/greetings")
	public void greeting(HelloMessage message) throws Exception {
		connectionCount++;
		 this.message=message;
		while (true) {
			if (connectionCount>1) {
				connectionCount--;
				break;
			}
			System.out.println("sending message");
			Thread.sleep(1000); // simulated delay
			template.convertAndSend("/topic/greetings", greetingHelper(this.message));
			// greetingHelper(message);
		}
	}

	// eğer bir kere bağlantı sağlandıysa bu metot çalışssın
	public Greeting greetingHelper(HelloMessage message) throws InterruptedException {
		System.out.println("in helper");
		return new Greeting("Hello, " + message.getName() + "!");
	}

}
