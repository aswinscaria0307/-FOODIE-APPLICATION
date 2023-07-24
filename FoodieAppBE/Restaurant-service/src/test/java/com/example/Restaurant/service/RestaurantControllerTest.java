package com.example.Restaurant.service;

import com.example.Restaurant.service.Repository.RestaurantRepository;
import com.example.Restaurant.service.Service.IRestaurantService;
import com.example.Restaurant.service.controller.RestaurantController;
import com.example.Restaurant.service.domain.FoodItems;
import com.example.Restaurant.service.domain.Restaurant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private IRestaurantService iRestaurantService;
    @InjectMocks
    private RestaurantController restaurantController;

    @Autowired
    private MockMvc mockMvc;
    private Restaurant restaurant;
    private FoodItems foodItems;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        foodItems = new FoodItems(20, "pizza", 500,"", "https://img.freepik.com/free-photo/mixed-pizza-with-various-ingridients_140725-3790.jpg?w=2000", 5);
        List<FoodItems> foodItemsList = new ArrayList<>();
        foodItemsList.add(foodItems);
        restaurant = new Restaurant(501, "The Spice Factory", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cmVzdGF1cmFudHxlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80", "Bangalore", 5.0, foodItemsList);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @AfterEach
    public void tearDown(){
        restaurant=null;
        foodItems=null;

    }

    @Test
    public void testAddRestaurantIfSuccess() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "adminRole");
        System.out.println(convertToJson(restaurant));
        when(iRestaurantService.addRestaurant(restaurant)).thenReturn(restaurant);
        mockMvc.perform(
                post("/api/v1/restaurant/addRestaurant")
                        .requestAttr("attr2","adminRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(restaurant)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).addRestaurant(restaurant);
    }

    @Test
    public void testAddRestaurantIfFailure() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "userRole");
        System.out.println(convertToJson(restaurant));
        mockMvc.perform(
                        post("/api/v1/restaurant/addRestaurant")
                                .requestAttr("attr2","userRole")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(restaurant)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(0)).addRestaurant(restaurant);

    }
    @Test
    public void testGetAllRestaurants() throws Exception {
        when(iRestaurantService.getAllRestaurant()).thenReturn(List.of(restaurant));
        mockMvc.perform((
                get("/api/v1/restaurant/getRestaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(restaurant))))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).getAllRestaurant();

    }
    @Test
    public void testGetLocation() throws Exception {
        System.out.println(convertToJson(restaurant));
        when(iRestaurantService.getRestaurantByLocation(restaurant.getLocation())).thenReturn(restaurant);
        mockMvc.perform(
                get("/api/v1/restaurant/getLocation/{location}",restaurant.getLocation())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(restaurant)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).getRestaurantByLocation(restaurant.getLocation());
    }

    @Test
    public void testGetRating() throws Exception{
        System.out.println(convertToJson(restaurant));
        when(iRestaurantService.getByRating(restaurant.getRating())).thenReturn(restaurant);
        mockMvc.perform(
                get("/api/v1/restaurant/getRating/{rating}",restaurant.getRating())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(restaurant)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).getByRating(restaurant.getRating());
    }

    @Test
    public void testUpdateRestaurant() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "adminRole");
        System.out.println(convertToJson(restaurant));
        when(iRestaurantService.updateRestaurant(restaurant,restaurant.getRestaurantId())).thenReturn(restaurant);
        mockMvc.perform(
                put("/api/v1/restaurant/update/{id}",restaurant.getRestaurantId())
                        .requestAttr("attr2","adminRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(restaurant)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).updateRestaurant(restaurant,restaurant.getRestaurantId());
    }

    @Test
    public void testUpdateRestaurantIfFailure() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2","userRole");
        System.out.println(convertToJson(restaurant));
        mockMvc.perform(
                put("/api/v1/restaurant/update/{id}",restaurant.getRestaurantId())
                        .requestAttr("attr2","userRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(restaurant)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(0)).updateRestaurant(restaurant,restaurant.getRestaurantId());
    }

    @Test
    public void testDeleteRestaurantIfSuccess() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2","adminRole");
        System.out.println(convertToJson(restaurant));
        when(iRestaurantService.deleteById(restaurant.getRestaurantId())).thenReturn(true);
        mockMvc.perform(
                delete("/api/v1/restaurant/delete/{id}",restaurant.getRestaurantId())
                        .requestAttr("attr2","adminRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(restaurant)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).deleteById(restaurant.getRestaurantId());
    }

    @Test
    public void testDeleteRestaurantIfFailure() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2","userRole");
        System.out.println(convertToJson(restaurant));
        mockMvc.perform(
                        delete("/api/v1/restaurant/delete/{id}",restaurant.getRestaurantId())
                                .requestAttr("attr2","userRole")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(restaurant)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(0)).deleteById(restaurant.getRestaurantId());
    }

    @Test
    public void testAddAllItemsIfSuccess() throws Exception {
        int restaurantId = 1;
        List<FoodItems> foodItemsList = new ArrayList<>();
        foodItemsList.add(new FoodItems(21, "Burger", 100,"", "https://img.freepik.com/free-photo/mixed-pizza-with-various-ingridients_140725-3790.jpg?w=2000", 4.5));
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "adminRole");
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        mockMvc.perform(post("/api/v1/restaurant/addItem/{id}", restaurantId)
                        .requestAttr("attr2", "adminRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(foodItemsList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    public void testAddAllItemsIfFailure() throws Exception {
        int restaurantId = 1;
        List<FoodItems> foodItemsList = new ArrayList<>();
        foodItemsList.add(new FoodItems(21, "Burger", 100,"", "https://img.freepik.com/free-photo/mixed-pizza-with-various-ingridients_140725-3790.jpg?w=2000", 4.5));
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "userRole");
        mockMvc.perform(post("/api/v1/restaurant/addItem/{id}", restaurantId)
                        .requestAttr("attr2", "userRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(foodItemsList)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        verify(restaurantRepository, times(0)).findById(restaurantId);
        verify(restaurantRepository, times(0)).save(restaurant);
    }

//    /api/v1/restaurant/getItems/{id}
    @Test
    public void testGetAllItems() throws Exception{
        when(iRestaurantService.getItems(restaurant.getRestaurantId())).thenReturn(List.of(foodItems));
        mockMvc.perform(
                get("/api/v1/restaurant/getItems/{id}",restaurant.getRestaurantId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(foodItems)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).getItems(restaurant.getRestaurantId());
    }

//    /api/v1/restaurant/updateItem/{restaurantId}

    @Test
    public void testUpdateAllFoodItemsIfSuccess() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "adminRole");
        when(iRestaurantService.updateFoodItem(restaurant.getRestaurantId(),foodItems)).thenReturn(restaurant);
        mockMvc.perform(
                put("/api/v1/restaurant/updateItem/{restaurantId}",restaurant.getRestaurantId())
                        .requestAttr("attr2","adminRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(foodItems)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).updateFoodItem(restaurant.getRestaurantId(),foodItems);
    }

    @Test
    public void testUpdateAllFoodItemsIfFailure() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "userRole");
        mockMvc.perform(
                        put("/api/v1/restaurant/updateItem/{restaurantId}",restaurant.getRestaurantId())
                                .requestAttr("attr2","userRole")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(foodItems)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(0)).updateFoodItem(restaurant.getRestaurantId(),foodItems);
    }

//    /api/v1/restaurant/deleteItem/{restaurantId}
    @Test
    public void testDeleteFoodItemsIfSuccess() throws Exception{
//        int restaurantId = 1;
//        List<FoodItems> foodItemsList = new ArrayList<>();
//        foodItemsList.add(new FoodItems(21, "Burger", 100, "https://img.freepik.com/free-photo/mixed-pizza-with-various-ingridients_140725-3790.jpg?w=2000", 4.5));
//        Restaurant restaurant = new Restaurant();
//        restaurant.setRestaurantId(restaurantId);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "adminRole");
        when(iRestaurantService.deleteFoodItem(restaurant.getRestaurantId(),foodItems)).thenReturn(true);
        mockMvc.perform(
                post("/api/v1/restaurant/deleteItem/{restaurantId}",restaurant.getRestaurantId())
                        .requestAttr("attr2","adminRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(foodItems)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(1)).deleteFoodItem(restaurant.getRestaurantId(),foodItems);
    }
    @Test
    public void testDeleteFoodItemsIfFailure() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr2", "userRole");
        mockMvc.perform(
                        post("/api/v1/restaurant/deleteItem/{restaurantId}",restaurant.getRestaurantId())
                                .requestAttr("attr2","userRole")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(foodItems)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
        verify(iRestaurantService,times(0)).deleteFoodItem(restaurant.getRestaurantId(),foodItems);
    }


    public static String convertToJson(final Object object){
        String result="";
        ObjectMapper mapper = new ObjectMapper();
        try {
           result= mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
