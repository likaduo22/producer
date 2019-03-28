package com.ushine.homestay.homestayserver.service.impl;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushine.common.dao.IBaseDao;
import com.ushine.homestay.common.HomestayException;
import com.ushine.homestay.po.Homestay;
import com.ushine.homestay.service.IHomestayService;

/**
 * 
 * @author zq
 *
 */
@Service
public class HomestayServiceImpl implements IHomestayService {
	
	private static Log log = LogFactory.getLog(HomestayServiceImpl.class);
	
	@Autowired
	private IBaseDao<Homestay, String> dao;
	
	/**
	 * 添加民宿注册，详细地址一样注册不成功
	 */
	@Override
	public void insert(Homestay homestay) {
		if (homestay == null) {
			log.error("民宿注册异常：民宿信息为空");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM,"民宿注册异常：民宿信息为空");
		}
		Object[] param = new Object[]{homestay.getAddress()};
		String hql = " from " + Homestay.class.getName() + " where address = ?";
		List<Homestay> list = dao.findByHql(hql, param);
		if (!(list == null || list.size() == 0)) {
			log.error("民宿注册异常：该地址已经注册过");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM, "民宿注册异常：该地址已经注册过");
		}
		String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
		homestay.setId(uuid);
		dao.save(homestay);
		
		
		Properties props = new Properties();
		props.put("bootstrap.servers",
				"192.168.227.159:9092,192.168.227.160:9092,192.168.227.158:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 5242880);
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer","com.ushine.homestay.common.EncodeingKafka");

		Producer<String, Object> producer = new KafkaProducer<String, Object>(
				props);

		for (int i = 0; i < 1; i++) {
			
			producer.send(new ProducerRecord<String, Object>("demoKafka21", homestay));

		}
		producer.close();
	}
		


	@Override
	public List<Homestay> queryByUid(String uid) {
		if (uid == null || "".equals(uid)) {
			log.error("民宿查寻异常：用户id为空");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM, "民宿查寻异常：用户id为空");
		}
		Object[] param = new Object[]{uid};
		List<Homestay> homestays = dao.findByHql(" from "+Homestay.class.getName()+" where user_id = ?", param);
		return homestays;
	}

	@Override
	public void modify(Homestay homestay) {
		if (homestay == null) {
			log.error("民宿修改异常：民宿信息为空");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM,"民宿修改异常：民宿信息为空");
		}
		String id = homestay.getId();
		Homestay homestays = dao.findById(Homestay.class, id);
		if (homestays == null) {
			log.error("民宿查询异常：名宿不存在");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_NULL,"民宿查询异常：名宿不存在");
		}
		homestays.setAddress(homestay.getAddress());
		homestays.setCompanyName(homestay.getCompanyName());
		homestays.setCreateTime(homestay.getCreateTime());
		homestays.setFlag(homestay.getFlag());
		homestays.setIdCardId(homestay.getIdCardId());
		homestays.setIdType(homestay.getIdType());
		homestays.setImage(homestay.getImage());
		homestays.setIntroduce(homestay.getIntroduce());
		homestays.setMobile(homestay.getMobile());
		homestays.setPhone(homestay.getPhone());
		homestays.setState(homestay.getState());
		homestays.setUserId(homestay.getUserId());
		dao.update(homestays);
	}

	@Override
	public void delByID(String id) {
		if (id == null || "".equals(id)) {
			log.error("删除民宿异常：民宿id为空");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM, "删除民宿异常：民宿id为空");
		}
		/*Object[] param = new Object[]{id};
		dao.deleteByHql(" delete from " + HomestayTest.class + " where id = ?", param);*/
		dao.deleteById(Homestay.class, id);
	}

	@Override
	public Homestay queryById(String id) {
		if (id == null || "".equals(id)) {
			log.error("查询民宿异常：民宿id为空");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM,"查询民宿异常：民宿id为空");
		}
		Homestay homestay = dao.findById(Homestay.class, id);
		return homestay;
	}

	@Override
	public void updateStatus(String id, int status) {
		if (id == null || "".equals(id)) {
			log.error("修改民宿状态异常：民宿id为空");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM, "修改民宿状态异常：民宿id为空");
		}
		Homestay homestay = dao.findById(Homestay.class, id);
		if (homestay == null) {
			log.error("修改民宿状态异常：民宿不存在");
			throw new HomestayException(HomestayException.IErrorCode.ERROR_CODE_INVALID_PARAM, "修改民宿状态异常：民宿不存在");
		}
		homestay.setState(status);
		dao.update(homestay);
	}

	@Override
	public List<Homestay> queryByStatus(int status) {
		Object[] param = new Object[]{status};
		String hql = " from " + Homestay.class.getName() + " where status=?";
		List<Homestay> list = dao.findByHql(hql, param);
		return list;
	}

}
