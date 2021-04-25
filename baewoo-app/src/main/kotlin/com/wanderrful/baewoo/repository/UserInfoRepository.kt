package com.wanderrful.baewoo.repository

import com.wanderrful.baewoo.dao.UserInfo
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserInfoRepository : ReactiveCrudRepository<UserInfo, String>
