package com.kavi.pbc.live.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [AppProperties::class])
class PbcApiApplicationTests {

	@Test
	fun contextLoads() {
	}

}
