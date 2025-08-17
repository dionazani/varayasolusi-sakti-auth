package org.varayasolusi.saktiauth;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping/")
public class PingController {

	@PersistenceContext
	private EntityManager em;
	@PostMapping("")
	public ResponseEntity<String> sayPing() {

		String sql = "select 1";
		var result = (int) this.em.createNativeQuery(sql).getSingleResult();
		return ResponseEntity.ok("PING");
	}
	
}
