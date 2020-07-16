package com.imooc.o2o.dao;

import com.imooc.o2o.entity.WechatAuth;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WechatAuthDao {
	/**
	 * 通过openId查询对应本平台的微信帐号
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);

	/**
	 * 添加对应本平台的微信帐号
	 * 
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);

}
