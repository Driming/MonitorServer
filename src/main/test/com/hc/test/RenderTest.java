package com.hc.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:spring-mybatis.xml"})  
public class RenderTest {
	@Test
	public void testSate(){
		System.out.println(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(
				1514736360000l);
		try {
			java.util.Date parse = format.parse("2018-05-06 09:09:00");
			System.out.println(parse.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(format.format(date));
	}
	
	//WebSocketSendService
	
	@Test
	public void testRegisterCore() throws IOException{
	/*	CollectionTaskTimePointStatus status = new CollectionTaskTimePointStatus();
		status.setCtid("112");
		status.setTimesave(11111L);
		status.setTimedelay(122222L);
		status.setTimecurrent(24L);
		collectionMonitorDao.upsertCollectionTaskTimePointStatus(status);*/
	}

	
}