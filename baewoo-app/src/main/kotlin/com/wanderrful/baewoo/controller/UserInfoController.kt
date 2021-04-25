package com.wanderrful.baewoo.controller

import com.wanderrful.baewoo.dao.UserInfo
import com.wanderrful.baewoo.repository.UserInfoRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${routes.v1.userInfo}")
class UserInfoController(private val userInfoRepository: UserInfoRepository) {

    @GetMapping
    fun userGet(@RequestParam id: String) = userInfoRepository.findById(id)

    @PostMapping
    fun userPost(@RequestBody userInfo: UserInfo) = userInfoRepository.save(userInfo)

    @DeleteMapping
    fun userDelete(@RequestParam id: String) = userInfoRepository.deleteById(id)

}
