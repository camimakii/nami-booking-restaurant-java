package es.nami.booking.restaurant.api;

import es.nami.booking.restaurant.data.RestaurantGroup;
import es.nami.booking.restaurant.data.RestaurantGroupRepository;
import es.nami.booking.restaurant.exception.ErrorCode;
import es.nami.booking.restaurant.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class RestaurantGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantGroupRepository restaurantGroupRepository;


//    @Test
//    public void getGroupById_ExistingId_ShouldReturnGroup() throws Exception {
//        final String name = "name";
//        RestaurantGroup group = new RestaurantGroup();
//        group.setName(name);
//        group = restaurantGroupRepository.save(group);
//        mockMvc.perform(get("/api/restaurant_group/" + group.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(
//                        JsonUtil.toJson(group)));
//    }
//
//    @Test
//    public void getGroupById_NonExistingId_ShouldReturn404() throws Exception {
//        mockMvc.perform(get("/api/restaurant_group/" + 0)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json(
//                        JsonUtil.toJson(new ErrorJson(ErrorCode.NOT_FOUND))));
//    }

}