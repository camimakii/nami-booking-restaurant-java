package es.nami.booking.restaurant.api.data.restaurant;

import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.data.RestaurantGroupRepository;
import es.nami.booking.restaurant.client.data.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestaurantDataRestControllerTest {

    private static final String API_ROOT = "/api/restaurant";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantGroupRepository restaurantGroupRepository;

    private static Restaurant restaurantCreated;

//    @Test
//    @Order(1)
//    @DisplayName("POST Create New Restaurant")
//    public void createNewRestaurant() throws Exception {
//        // SETUP REQUEST
//        RestaurantGroup group = new RestaurantGroup();
//        group.setName("name");
//        group = restaurantGroupRepository.save(group);
//        Restaurant restaurantToCreate = new Restaurant();
//        restaurantToCreate.setName("restaurant_name");
//        restaurantToCreate.setRestaurantGroup(group);
//        String request = JsonUtil.toJson(restaurantToCreate);
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
//        restaurantCreated = JsonUtil.fromJson(jsonResponse, Restaurant.class);
//
//        // SETUP MISSING ID IN FORMER ENTITY TO HAVE GOOD COMPARISON
//        restaurantToCreate.setId(restaurantCreated.getId());
//
//        // ASSERTIONS TO ENSURE RETURNED OBJECT AND PERSISTED OBJECT ARE THE SAME
//        assertEquals(restaurantToCreate, restaurantCreated);
//        assertEquals(restaurantToCreate, restaurantRepository.findById(restaurantCreated.getId()).get());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("GET Retrieve Restaurant by ID")
//    public void findRestaurantById() throws Exception {
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(get(API_ROOT + "/" + restaurantCreated.getId()))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // SETUP RESPONSE
//        String jsonResponse = result.getResponse().getContentAsString();
//        Restaurant resultRestaurant = JsonUtil.fromJson(jsonResponse, Restaurant.class);
//
//        // ASSERTIONS TO ENSURE THE RETURNED OBJECT IS CORRECTLY THE ONE WE EXPECT
//        assertTrue(restaurantCreated.equals(resultRestaurant));
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("GET Retrieve non-existing Restaurant by ID")
//    public void findRestaurantById_notExisting() throws Exception {
//        String wrongId = "999";
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(get(API_ROOT + "/" + wrongId))
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
//    @DisplayName("GET Retrieve all Restaurants of a RestaurantGroup")
//    public void findAllRestaurantsOfGroup() throws Exception {
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(
//                        get(API_ROOT + "/all")
//                                .param("restaurantGroupId", restaurantCreated.getRestaurantGroup().getId().toString())
//                )
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // SETUP RESPONSE
//        String jsonResponse = result.getResponse().getContentAsString();
//        List<Restaurant> resultRestaurantGroup = JsonUtil.fromJsonArray(jsonResponse, Restaurant.class);
//
//        // ASSERTIONS THAT THE EXPECTED OBJECT IS PART OF THE LIST
//        assertTrue(resultRestaurantGroup.contains(restaurantCreated));
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("GET Retrieve all Restaurants of a non-existing RestaurantGroup")
//    public void findAllRestaurantsOfGroup_notExistingGroup() throws Exception {
//        String wrongId = "999";
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(
//                        get(API_ROOT + "/all")
//                                .param("restaurantGroupId", wrongId)
//                )
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        // ASSERT ERROR AND MESSAGE IS THE ONE EXPECTED
//        ErrorAssertUtil.assertErrorResponse(
//                ErrorCode.NOT_FOUND,
//                result,
//                RestaurantGroupDataService.ENTITY_NAME,
//                wrongId);
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("PUT Update a Restaurant")
//    public void updateRestaurant() throws Exception {
//        String newName = "abc";
//        restaurantCreated.setName(newName);
//
//        String request = JsonUtil.toJson(restaurantCreated);
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
//        restaurantCreated = JsonUtil.fromJson(jsonResponse, Restaurant.class);
//
//        // ASSERTIONS THAT THE RETURNED OBJECT AND THE PERSISTED OBJECT ARE WELL UPDATED
//        assertEquals(newName, restaurantCreated.getName());
//        assertEquals(newName, restaurantRepository.findById(restaurantCreated.getId()).get().getName());
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("PUT Update a non-existing Restaurant")
//    public void updateRestaurant_notExisting() throws Exception {
//        long wrongId = 999;
//        Restaurant fakeRestaurant = new Restaurant();
//        fakeRestaurant.setId(wrongId);
//        fakeRestaurant.setRestaurantGroup(restaurantCreated.getRestaurantGroup());
//
//        String request = JsonUtil.toJson(fakeRestaurant);
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
//                RestaurantDataService.ENTITY_NAME,
//                wrongId + "");
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("DELETE a Restaurant")
//    public void deleteRestaurantGroupCreated() throws Exception {
//        // CALL ENDPOINT
//        mockMvc.perform(delete(API_ROOT + "/" + restaurantCreated.getId()))
//                .andExpect(status().isNoContent());
//
//        // ASSERTIONS THAT THE EXPECTED OBJECT DOESN'T EXIST ANYMORE
//        // TODO: when implemented, test that the other informations are also deleted
//        assertTrue(restaurantRepository.findById(restaurantCreated.getId()).isEmpty());
//    }
//
//    @Test
//    @Order(9)
//    @DisplayName("DELETE a non-existing Restaurant")
//    public void deleteRestaurantGroupCreated_notExisting() throws Exception {
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
//                RestaurantDataService.ENTITY_NAME,
//                wrongId);
//    }

}