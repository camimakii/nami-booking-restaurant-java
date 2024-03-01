package es.nami.booking.restaurant.api.data;

import es.nami.booking.restaurant.client.service.RestaurantDataService;
import es.nami.booking.restaurant.opening.service.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.opening.data.SpecialOpeningHours;
import es.nami.booking.restaurant.opening.data.SpecialOpeningHoursRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpecialOpeningHoursDataRestControllerTest {

    private static final String API_ROOT = "/api/special-opening-hours";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantGroupRepository restaurantGroupRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private SpecialOpeningHoursRepository specialOpeningHoursRepository;

    private static SpecialOpeningHours specialOpeningHoursCreated;

    @Test
    @Order(1)
    @DisplayName("POST Create New SpecialOpeningHours")
    public void createNewSpecialOpeningHours() throws Exception {
        // SETUP REQUEST
        RestaurantGroup group = new RestaurantGroup();
        group.setName("name");
        group = restaurantGroupRepository.save(group);
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantGroup(group);
        restaurant.setName("restau_name");
        restaurant = restaurantRepository.save(restaurant);
        SpecialOpeningHours specialOpeningHoursToCreate = new SpecialOpeningHours();
        specialOpeningHoursToCreate.setRestaurant(restaurant);
        specialOpeningHoursToCreate.setDurationInMinutes(2 * 60); // 2 hours
        specialOpeningHoursToCreate.setStartDateTime(LocalDateTime.of(2000, 1, 1, 10, 30));

        String request = JsonUtil.toJson(specialOpeningHoursToCreate);

        // CALL ENDPOINT
        MvcResult result = mockMvc.perform(post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn();

        // SETUP RESPONSE
        String jsonResponse = result.getResponse().getContentAsString();
        specialOpeningHoursCreated = JsonUtil.fromJson(jsonResponse, SpecialOpeningHours.class);

        // SETUP MISSING ID IN FORMER ENTITY TO HAVE GOOD COMPARISON
        specialOpeningHoursToCreate.setId(specialOpeningHoursCreated.getId());

        // ASSERTIONS TO ENSURE RETURNED OBJECT AND PERSISTED OBJECT ARE THE SAME
        assertEquals(specialOpeningHoursToCreate, specialOpeningHoursCreated);
        assertEquals(specialOpeningHoursToCreate, specialOpeningHoursRepository.findById(specialOpeningHoursCreated.getId()).get());
    }

    @Test
    @Order(2)
    @DisplayName("GET Retrieve all SpecialOpeningHours of Restaurant")
    public void findAllSpecialOpeningHoursOfRestaurants() throws Exception {
        // CALL ENDPOINT
        MvcResult result = mockMvc.perform(
                        get(API_ROOT + "/all")
                                .param("restaurantId", specialOpeningHoursCreated.getRestaurant().getId().toString())
                )
                .andExpect(status().isOk())
                .andReturn();

        // SETUP RESPONSE
        String jsonResponse = result.getResponse().getContentAsString();
        List<SpecialOpeningHours> resultRestaurantGroup = JsonUtil.fromJsonArray(jsonResponse, SpecialOpeningHours.class);

        // ASSERTIONS THAT THE EXPECTED OBJECT IS PART OF THE LIST
        assertTrue(resultRestaurantGroup.contains(specialOpeningHoursCreated));
    }

    @Test
    @Order(3)
    @DisplayName("GET Retrieve all SpecialOpeningHours of non-existing Restaurant")
    public void findAllOpeningHoursOfRestaurant_notExisting() throws Exception {
        String wrongId = "999";

        // CALL ENDPOINT
        MvcResult result = mockMvc.perform(
                        get(API_ROOT + "/all")
                                .param("restaurantId", wrongId)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        // ASSERT ERROR AND MESSAGE IS THE ONE EXPECTED
        ErrorAssertUtil.assertErrorResponse(
                ErrorCode.NOT_FOUND,
                result,
                RestaurantDataService.ENTITY_NAME,
                wrongId);
    }

    @Test
    @Order(4)
    @DisplayName("PUT Update a SpecialOpeningHours")
    public void updateSpecialOpeningHours() throws Exception {
        LocalDateTime dayUpdated = LocalDateTime.now();
        int durationUpdated = 4 * 60;
        specialOpeningHoursCreated.setDurationInMinutes(durationUpdated);
        specialOpeningHoursCreated.setStartDateTime(dayUpdated);

        String request = JsonUtil.toJson(specialOpeningHoursCreated);

        // CALL ENDPOINT
        MvcResult result = mockMvc.perform(put(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        // SETUP RESPONSE
        String jsonResponse = result.getResponse().getContentAsString();
        specialOpeningHoursCreated = JsonUtil.fromJson(jsonResponse, SpecialOpeningHours.class);

        // ASSERTIONS THAT THE RETURNED OBJECT AND THE PERSISTED OBJECT ARE WELL UPDATED
        assertEquals(dayUpdated.getDayOfMonth(), specialOpeningHoursCreated.getStartDateTime().getDayOfMonth());
        assertEquals(dayUpdated.getHour(), specialOpeningHoursCreated.getStartDateTime().getHour());
        assertEquals(dayUpdated.getDayOfMonth(), specialOpeningHoursRepository.findById(specialOpeningHoursCreated.getId()).get().getStartDateTime().getDayOfMonth());
        assertEquals(dayUpdated.getHour(), specialOpeningHoursRepository.findById(specialOpeningHoursCreated.getId()).get().getStartDateTime().getHour());
        assertEquals(durationUpdated, specialOpeningHoursCreated.getDurationInMinutes());
        assertEquals(durationUpdated, specialOpeningHoursRepository.findById(specialOpeningHoursCreated.getId()).get().getDurationInMinutes());
    }

    @Test
    @Order(5)
    @DisplayName("PUT Update a non-existing SpecialOpeningHours")
    public void updateSpecialOpeningHours_notExisting() throws Exception {
        long wrongId = 999;
        SpecialOpeningHours fake = new SpecialOpeningHours();
        fake.setId(wrongId);

        String request = JsonUtil.toJson(fake);

        // CALL ENDPOINT
        MvcResult result = mockMvc.perform(put(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andReturn();

        // ASSERT ERROR AND MESSAGE IS THE ONE EXPECTED
        ErrorAssertUtil.assertErrorResponse(
                ErrorCode.NOT_FOUND,
                result,
                SpecialOpeningHoursDataService.ENTITY_NAME,
                wrongId + "");

    }

    @Test
    @Order(6)
    @DisplayName("DELETE a SpecialOpeningHours")
    public void deleteSpecialOpeningHoursCreated() throws Exception {

        // CALL ENDPOINT
        mockMvc.perform(delete(API_ROOT + "/" + specialOpeningHoursCreated.getId()))
                .andExpect(status().isNoContent());

        // ASSERTIONS THAT THE EXPECTED OBJECT DOESN'T EXIST ANYMORE
        assertTrue(specialOpeningHoursRepository.findById(specialOpeningHoursCreated.getId()).isEmpty());

    }

    @Test
    @Order(7)
    @DisplayName("DELETE a non-existing SpecialOpeningHours")
    public void deleteOpeningHoursCreated_notExisting() throws Exception {
        String wrongId = "999";

        // CALL ENDPOINT
        MvcResult result = mockMvc.perform(delete(API_ROOT + "/" + wrongId))
                .andExpect(status().isNotFound())
                .andReturn();

        // ASSERT ERROR AND MESSAGE IS THE ONE EXPECTED
        ErrorAssertUtil.assertErrorResponse(
                ErrorCode.NOT_FOUND,
                result,
                SpecialOpeningHoursDataService.ENTITY_NAME,
                wrongId);
    }

}