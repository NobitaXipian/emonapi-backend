package com.xipian.emonapiinterface;

import com.xipian.emonapiclientsdk.client.EmonApiClient;
import com.xipian.emonapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@SpringBootTest
class EmonapiInterfaceApplicationTests {

    @Resource
    private EmonApiClient emonApiClient;

    @Test
    void contextLoads() {
        String result = emonApiClient.getNameByGet("xipian");
        User user = new User();
        user.setUsername("xipian");
        String usernameByPost = emonApiClient.getUsernameByPost(user);
        System.out.println(result);
        System.out.println(usernameByPost);
    }

}
