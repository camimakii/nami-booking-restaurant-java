package es.nami.booking.restaurant.api.data.restaurant;

import es.nami.booking.restaurant.client.data.RestaurantGroup;
import es.nami.booking.restaurant.client.data.RestaurantGroupRepository;
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
class RestaurantGroupDataRestControllerTest {

    private static final String API_ROOT = "/api/restaurant-group";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantGroupRepository restaurantGroupRepository;

    private static RestaurantGroup restaurantGroupCreated;

//    @Test
//    @Order(1)
//    @DisplayName("POST Create New RestaurantGroup")
//    public void createNewRestaurantGroup() throws Exception {
//        // SETUP REQUEST
//        RestaurantGroup restaurantGroupToCreate = new RestaurantGroup();
//        restaurantGroupToCreate.setName("group_name");
//        String request = JsonUtil.toJson(restaurantGroupToCreate);
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
//        restaurantGroupCreated = JsonUtil.fromJson(jsonResponse, RestaurantGroup.class);
//
//        // SETUP MISSING ID IN FORMER ENTITY TO HAVE GOOD COMPARISON
//        restaurantGroupToCreate.setId(restaurantGroupCreated.getId());
//
//        // ASSERTIONS TO ENSURE RETURNED OBJECT AND PERSISTED OBJECT ARE THE SAME
//        assertEquals(restaurantGroupToCreate, restaurantGroupCreated);
//        assertEquals(restaurantGroupToCreate, restaurantGroupRepository.findById(restaurantGroupCreated.getId()).get());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("GET Retrieve RestaurantGroup by ID")
//    public void findRestaurantGroupById() throws Exception {
//
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(get(API_ROOT + "/" + restaurantGroupCreated.getId()))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // SETUP RESPONSE
//        String jsonResponse = result.getResponse().getContentAsString();
//        RestaurantGroup resultRestaurantGroup = JsonUtil.fromJson(jsonResponse, RestaurantGroup.class);
//
//        // ASSERTIONS TO ENSURE THE RETURNED OBJECT IS CORRECTLY THE ONE WE EXPECT
//        assertTrue(restaurantGroupCreated.equals(resultRestaurantGroup));
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("GET Retrieve non-existing RestaurantGroup by ID")
//    public void findRestaurantGroupById_notExisting() throws Exception {
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
//                RestaurantGroupDataService.ENTITY_NAME,
//                wrongId);
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("GET Retrieve all RestaurantGroups")
//    public void findAllRestaurantGroups() throws Exception {
//        // CALL ENDPOINT
//        MvcResult result = mockMvc.perform(get(API_ROOT + "/all"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // SETUP RESPONSE
//        String jsonResponse = result.getResponse().getContentAsString();
//        List<RestaurantGroup> resultRestaurantGroup = JsonUtil.fromJsonArray(jsonResponse, RestaurantGroup.class);
//
//        // ASSERTIONS THAT THE EXPECTED OBJECT IS PART OF THE LIST
//        assertTrue(resultRestaurantGroup.contains(restaurantGroupCreated));
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("PUT Update a RestaurantGroup")
//    public void updateRestaurantGroup() throws Exception {
//
//        String newName = "abc";
//        restaurantGroupCreated.setName(newName);
//
//        String request = JsonUtil.toJson(restaurantGroupCreated);
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
//        restaurantGroupCreated = JsonUtil.fromJson(jsonResponse, RestaurantGroup.class);
//
//        // ASSERTIONS THAT THE RETURNED OBJECT AND THE PERSISTED OBJECT ARE WELL UPDATED
//        assertEquals(newName, restaurantGroupCreated.getName());
//        assertEquals(newName, restaurantGroupRepository.findById(restaurantGroupCreated.getId()).get().getName());
//
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("PUT Update a non-existing RestaurantGroup")
//    public void updateRestaurantGroup_notExisting() throws Exception {
//        long wrongId = 999;
//        RestaurantGroup fakeRestaurantGroup = new RestaurantGroup();
//        fakeRestaurantGroup.setId(wrongId);
//
//        String request = JsonUtil.toJson(fakeRestaurantGroup);
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
//
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("DELETE a RestaurantGroup")
//    public void deleteRestaurantGroupCreated() throws Exception {
//
//        // CALL ENDPOINT
//        mockMvc.perform(delete(API_ROOT + "/" + restaurantGroupCreated.getId()))
//                .andExpect(status().isNoContent());
//
//        // ASSERTIONS THAT THE EXPECTED OBJECT DOESN'T EXIST ANYMORE
//        // TODO: when implemented, test that the other informations are also deleted
//        assertTrue(restaurantGroupRepository.findById(restaurantGroupCreated.getId()).isEmpty());
//
//    }
//
//    @Test
//    @Order(7)
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
//                RestaurantGroupDataService.ENTITY_NAME,
//                wrongId);
//    }

}