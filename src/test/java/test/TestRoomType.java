package test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ushine.homestay.po.RoomType;
import com.ushine.homestay.service.IRoomTypeService;

/**
 * @author CHL
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/applicationContext.xml"})//加载配置文件
public class TestRoomType {

	@Autowired
	private IRoomTypeService Irts;
	
	@Test
	public void test(){
		RoomType rm  = new RoomType();
		/*rm.setBedType(1);
		rm.setDefaultPrice(2000);
		rm.setHomestayId("1");
		rm.setId("1");
		rm.setNumber(1);
		rm.setPhoto("img");
		rm.setRoomName("kaka");
		rm.setRoomType(1);
		Irts.addRoomType(rm);*/
	
		/*List<RoomType> queryAllRoomType = Irts.queryAllRoomType(1);
		for(int i = 0 ;i < queryAllRoomType.size() ; i++){
			System.out.println(queryAllRoomType.get(i).getRoomName());
		}*/
	
		
		/*Irts.deleteRoomType("1");*/
		
		
		rm.setBedType(1);
		rm.setDefaultPrice(20000);
		rm.setHomestayId("1");
		rm.setId("1");
		rm.setNumber(1);
		rm.setPhoto("imghgh");
		rm.setRoomName("kaka");
		rm.setRoomType(1);
		
		Irts.updateRoomType(rm);
	}
	
}
