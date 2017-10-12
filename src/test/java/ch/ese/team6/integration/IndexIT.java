package ch.ese.team6.integration;
/*
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITConfig.class)
public class IndexIT {
	
	@Autowired
	private WebDriver webDriver;
	
	@Value("${server.port}")
	private int serverPort;
	
	@Test void visitIndexPage() throws Exception {
		
		webDriver.get("http://localhost:8080/");
		WebElement working = webDriver.findElement(By.name("Hello, World!"));
		
		Assert.assertThat(working.getText(), is(equalTo("Hello, World!")));
	}
	
}
*/