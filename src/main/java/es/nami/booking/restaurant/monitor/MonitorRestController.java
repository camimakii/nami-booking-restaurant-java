package es.nami.booking.restaurant.monitor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@Slf4j
public class MonitorRestController {

    @GetMapping("isAlive")
    public ResponseEntity<String> isAlive() {
        return new ResponseEntity<>("Alive", HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Json> testGet(
            HttpServletRequest request
    ) {
        Json json = new Json();
        json.setOk(true);
        json.setName("Test");
        return ResponseEntity.ok(json);
    }

    @PostMapping("/test")
    public ResponseEntity<Json> testPost(
            @RequestBody Json json,
            HttpServletRequest request
    ) {
        if (json.isOk()) {
            return ResponseEntity.ok(json);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Data
    public static class Json {
        private String name;
        private boolean isOk;
    }

}
