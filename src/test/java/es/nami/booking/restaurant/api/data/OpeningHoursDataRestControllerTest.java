package es.nami.booking.restaurant.api.data;

import es.nami.booking.restaurant.opening.service.OpeningHoursDataService;
import es.nami.booking.restaurant.client.service.RestaurantDataService;
import es.nami.booking.restaurant.opening.data.OpeningHours;
import es.nami.booking.restaurant.opening.data.OpeningHoursRepository;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.data.RestaurantGroup;
import es.nami.booking.restaurant.client.data.RestaurantGroupRepository;
import es.nami.booking.restaurant.client.data.RestaurantRepository;
import es.nami.booking.restaurant.error.ErrorCode;
import es.nami.booking.restaurant.util.ErrorAssertUtil;
import es.nami.booking.restaurant.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OpeningHoursDataRestControllerTest {

    private static final String API_ROOT = "/api/opening-hours";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantGroupRepository restaurantGroupRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private OpeningHoursRepository openingHoursRepository;

    private static OpeningHours openingHoursCreated;

//    @Test
//    @Order(1)
//    @DisplayName("POST Create New OpeningHours")
//    public void createNewOpeningHours() throws Exception {
//        // SETUP REQUEST
//        RestaurantGroup group = new RestaurantGroup();
//        group.setName("name");
//        group = restaurantGroupRepository.save(group);
//        Restaurant restaurant = new Restaurant();
//        restaurant.setRestaurantGroup(group);
//        restaurant.setName("restau_name");
//        restaurant = restaurantRepository.save(restaurant);
//        OpeningHours openingHoursToCreate = new OpeningHours();
//        openingHoursToCreate.setRestaurant(restaurant);
//        openingHoursToCreate.setOpen(true);
//        openingHoursToCreate.setDayOfWeek(DayOfWeek.MONDAY);
//        openingHoursToCreate.setDurationInMinutes(2 * 60); // 2 hours
//        openingHoursToCreate.setStartTime(LocalTime.of(10, 30));
//
//        String request = JsonUtil.toJson(openingHoursToCreate);
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(post(API_ROOT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        // SETUP RESPONSE
//        String jsonResponse = result.getResponse().getContentAsString();
//        openingHoursCreated = JsonUtil.fromJson(jsonResponse, OpeningHours.class);
//
//        // SETUP MISSING ID IN FORMER ENTITY TO HAVE GOOD COMPARISON
//        openingHoursToCreate.setId(openingHoursCreated.getId());
//
//        // ASSERTIONS TO ENSURE RETURNED OBJECT AND PERSISTED OBJECT ARE THE SAME
//        assertEquals(openingHoursToCreate, openingHoursCreated);
//        assertEquals(openingHoursToCreate, openingHoursRepository.findById(openingHoursCreated.getId()).get());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("GET Retrieve all OpeningHours of Restaurant")
//    public void findAllOpeningHoursOfRestaurants() throws Exception {
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(
//                        get(API_ROOT + "/all")
//                                .param("restaurantId", openingHoursCreated.getRestaurant().getId().toString())
//                )
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // SETUP RESPONSE
//        String jsonResponse = result.getResponse().getContentAsString();
//        List<OpeningHours> resultRestaurantGroup = JsonUtil.fromJsonArray(jsonResponse, OpeningHours.class);
//
//        // ASSERTIONS THAT THE EXPECTED OBJECT IS PART OF THE LIST
//        assertTrue(resultRestaurantGroup.contains(openingHoursCreated));
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("GET Retrieve all OpeningHours of non-existing Restaurant")
//    public void findAllOpeningHoursOfRestaurant_notExisting() throws Exception {
//        String wrongId = "999";
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(
//                        get(API_ROOT + "/all")
//                                .param("restaurantId", wrongId)
//                )
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        // ASSERT ERROR AND MESSAGE IS THE ONE EXPECTED
//        ErrorAssertUtil.assertErrorResponse(
//                ErrorCode.NOT_FOUND,
//                result,
//                RestaurantDataService.ENTITY_NAME,
//                wrongId);
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("PUT Update a OpeningHours")
//    public void updateOpeningHours() throws Exception {
//        DayOfWeek dayUpdated = DayOfWeek.SUNDAY;
//        int durationUpdated = 4 * 60;
//        openingHoursCreated.setDurationInMinutes(durationUpdated);
//        openingHoursCreated.setDayOfWeek(dayUpdated);
//
//        String request = JsonUtil.toJson(openingHoursCreated);
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(put(API_ROOT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // SETUP RESPONSE
//        String jsonResponse = result.getResponse().getContentAsString();
//        openingHoursCreated = JsonUtil.fromJson(jsonResponse, OpeningHours.class);
//
//        // ASSERTIONS THAT THE RETURNED OBJECT AND THE PERSISTED OBJECT ARE WELL UPDATED
//        assertEquals(dayUpdated, openingHoursCreated.getDayOfWeek());
//        assertEquals(dayUpdated, openingHoursRepository.findById(openingHoursCreated.getId()).get().getDayOfWeek());
//        assertEquals(durationUpdated, openingHoursCreated.getDurationInMinutes());
//        assertEquals(durationUpdated, openingHoursRepository.findById(openingHoursCreated.getId()).get().getDurationInMinutes());
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("PUT Update a non-existing OpeningHours")
//    public void updateOpeningHours_notExisting() throws Exception {
//        long wrongId = 999;
//        OpeningHours fake = new OpeningHours();
//        fake.setId(wrongId);
//
//        String request = JsonUtil.toJson(fake);
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(put(API_ROOT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        // ASSERT ERROR AND MESSAGE IS THE ONE EXPECTED
//        ErrorAssertUtil.assertErrorResponse(
//                ErrorCode.NOT_FOUND,
//                result,
//                OpeningHoursDataService.ENTITY_NAME,
//                wrongId + "");
//
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("DELETE a OpeningHours")
//    public void deleteOpeningHoursCreated() throws Exception {
//
//        // CALL ENDPOINT
//        mockMvc.perform(delete(API_ROOT + "/" + openingHoursCreated.getId()))
//                .andExpect(status().isNoContent());
//
//        // ASSERTIONS THAT THE EXPECTED OBJECT DOESN'T EXIST ANYMORE
//        assertTrue(openingHoursRepository.findById(openingHoursCreated.getId()).isEmpty());
//
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("DELETE a non-existing OpeningHours")
//    public void deleteOpeningHoursCreated_notExisting() throws Exception {
//        String wrongId = "999";
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(delete(API_ROOT + "/" + wrongId))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        // ASSERT ERROR AND MESSAGE IS THE ONE EXPECTED
//        ErrorAssertUtil.assertErrorResponse(
//                ErrorCode.NOT_FOUND,
//                result,
//                OpeningHoursDataService.ENTITY_NAME,
//                wrongId);
//    }

}