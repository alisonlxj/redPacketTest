package com.alison.redpackets.service;

import com.alison.redpackets.dao.RedPackeDao;
import com.alison.redpackets.dao.UserRedPacketDao;
import com.alison.redpackets.pojo.RedPacket;
import com.alison.redpackets.pojo.UserRedPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service("userRedPacketService")
public class UserRedPacketServiceImpl implements UserRedPacketService {

    @Autowired
    UserRedPacketDao userRedPacketDao = null;

    @Autowired
    RedPackeDao redPackeDao = null;

    private static final int FAILED = 0;


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grabRedPacket(Long redPacketId, Long userId) {
        RedPacket redPacket = redPackeDao.getRedPacket(redPacketId);
        if(redPacket == null){
            return FAILED;
        }
        if(redPacket.getStock() > 0){
            redPackeDao.decreaceRedPacket(redPacketId);
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setRedPacketId(redPacketId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setUserId(userId);
            userRedPacket.setNote("抢红包 " + redPacketId);
            int result = userRedPacketDao.grabRedPacket(userRedPacket);
            return result;
        }

        return FAILED;
    }

}
