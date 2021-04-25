package com.wanderrful.baewoo.repository

import com.wanderrful.baewoo.dao.Word
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WordRepository : ReactiveCrudRepository<Word, String>