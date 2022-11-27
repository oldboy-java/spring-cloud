package com.thoughtmechanix.authentication.controller;

import com.thoughtmechanix.authentication.security.OauthClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * 客户端管理
 */
@RestController
@RequestMapping("/clients")
public class ClientDetailsController {

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    /**
     * 查询客户端列表
     * @return
     */
    @GetMapping
    public ResponseEntity<OauthClientDetails> listClients(){
        return new ResponseEntity(jdbcClientDetailsService.listClientDetails(), HttpStatus.OK);
    }

    /**
     * 添加客户端
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addClient(@RequestBody  OauthClientDetails clientDetails){
        jdbcClientDetailsService.addClientDetails(clientDetails);
        return new ResponseEntity( HttpStatus.OK);
    }


    /**
     * 删除客户端
     * @return
     */
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable("clientId") String clientId){
        jdbcClientDetailsService.removeClientDetails(clientId);
        return new ResponseEntity( HttpStatus.OK);
    }

    /**
     * 获取单个客户端
     * @return
     */
    @GetMapping("/{clientId}")
    public ResponseEntity<OauthClientDetails>  loadClient(@PathVariable("clientId")String clientId){
        return new ResponseEntity(jdbcClientDetailsService.loadClientByClientId(clientId), HttpStatus.OK);
    }


    /**
     * 更新客户端
     * @return
     */
    @PutMapping
    public ResponseEntity<?> updateClient(@RequestBody  OauthClientDetails clientDetails){
        jdbcClientDetailsService.updateClientDetails(clientDetails);
        return new ResponseEntity( HttpStatus.OK);
    }

}
