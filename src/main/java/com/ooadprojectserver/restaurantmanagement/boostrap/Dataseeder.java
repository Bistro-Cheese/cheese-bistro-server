package com.ooadprojectserver.restaurantmanagement.boostrap;

import com.github.javafaker.Faker;
import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.FoodStatus;
import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderTable;
import com.ooadprojectserver.restaurantmanagement.model.order.TableStatus;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Schedule;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Shift;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Role;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.repository.food.CompositionRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.IngredientRepository;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.ScheduleRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Dataseeder implements ApplicationListener<ContextRefreshedEvent>, CommandLineRunner {
    private final Faker faker;
    private final RoleRepository roleRepository;
    private final ScheduleRepository scheduleRepository;
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final InventoryRepository inventoryRepository;
    private final CompositionRepository compositionRepository;
    private final OrderTableRepository orderTableRepository;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
//        this.loadRoles();
//        this.loadCategory();
//        this.loadSchedules();
//        this.loadIngredient();
//        this.loadInventory();
//        this.loadOrderTable();
    }

    @Override
    public void run(String... args) throws ParseException {
//        try {
//            this.createListFood();
//            this.updateFoodStatus(foodRepository.findAll());
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void loadRoles() {
        List<Role> roles = new ArrayList<>(
                List.of(
                        new Role(1, "STAFF"),
                        new Role(2, "MANAGER"),
                        new Role(3, "OWNER")
                )
        );
        roles.forEach(role -> {
            Optional<Role> optionalRole = roleRepository.findById(role.getId());
            optionalRole.ifPresentOrElse(System.out::println, () -> roleRepository.save(role));
        });
    }

    private void loadSchedules() {
        List<Schedule> schedules = new ArrayList<>(
                List.of(
                        new Schedule(1, DayOfWeek.MONDAY, Shift.MORNING),
                        new Schedule(2, DayOfWeek.MONDAY, Shift.AFTERNOON),
                        new Schedule(3, DayOfWeek.MONDAY, Shift.NIGHT),
                        new Schedule(4, DayOfWeek.TUESDAY, Shift.MORNING),
                        new Schedule(5, DayOfWeek.TUESDAY, Shift.AFTERNOON),
                        new Schedule(6, DayOfWeek.TUESDAY, Shift.NIGHT),
                        new Schedule(7, DayOfWeek.WEDNESDAY, Shift.MORNING),
                        new Schedule(8, DayOfWeek.WEDNESDAY, Shift.AFTERNOON),
                        new Schedule(9, DayOfWeek.WEDNESDAY, Shift.NIGHT),
                        new Schedule(10, DayOfWeek.THURSDAY, Shift.MORNING),
                        new Schedule(11, DayOfWeek.THURSDAY, Shift.AFTERNOON),
                        new Schedule(12, DayOfWeek.THURSDAY, Shift.NIGHT),
                        new Schedule(13, DayOfWeek.FRIDAY, Shift.MORNING),
                        new Schedule(14, DayOfWeek.FRIDAY, Shift.AFTERNOON),
                        new Schedule(15, DayOfWeek.FRIDAY, Shift.NIGHT),
                        new Schedule(16, DayOfWeek.SATURDAY, Shift.MORNING),
                        new Schedule(17, DayOfWeek.SATURDAY, Shift.AFTERNOON),
                        new Schedule(18, DayOfWeek.SATURDAY, Shift.NIGHT),
                        new Schedule(19, DayOfWeek.SUNDAY, Shift.MORNING),
                        new Schedule(20, DayOfWeek.SUNDAY, Shift.AFTERNOON),
                        new Schedule(21, DayOfWeek.SUNDAY, Shift.NIGHT)
                )
        );
        schedules.forEach(schedule -> {
            Optional<Schedule> optionalSchedule = scheduleRepository.findById(schedule.getId());
            optionalSchedule.ifPresentOrElse(System.out::println, () -> scheduleRepository.save(schedule));
        });
    }

    private void loadCategory() {
        List<Category> categories = new ArrayList<>(
                List.of(
                        new Category(1, "Appetizer"),
                        new Category(2, "Main Course"),
                        new Category(3, "Dessert"),
                        new Category(4, "Drink")
                )
        );
        categories.forEach(category -> {
            Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
            optionalCategory.ifPresentOrElse(System.out::println, () -> categoryRepository.save(category));
        });
    }

    private void loadIngredient() {
        List<Ingredient> ingredients = new ArrayList<>(
                List.of(
                        new Ingredient(1L, "Bread", 1),
                        new Ingredient(2L, "Cheese", 1)
                )
        );
        ingredients.forEach(ingredient -> {
            Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(ingredient.getName());
            optionalIngredient.ifPresentOrElse(System.out::println, () -> ingredientRepository.save(ingredient));
        });
    }

    private void loadInventory() {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(
                    new FileReader("./src/main/resources/data/inventory.json")
            );
        } catch (net.minidev.json.parser.ParseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<Inventory> list = new ArrayList<>();

        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            Long id = Long.valueOf((Integer) jsonObject.get("ingredient_id"));
            Double quantity = Double.valueOf((Integer) jsonObject.get("quantity"));
            Ingredient ingredient = ingredientRepository.findById(id).orElseThrow();
            list.add(
                    Inventory.builder()
                            .ingredient(ingredient)
                            .quantity(quantity)
                            .build()
            );
        }
        inventoryRepository.saveAll(list);
    }

    private void loadOrderTable() {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(
                    new FileReader("./src/main/resources/data/table.json")
            );
        } catch (net.minidev.json.parser.ParseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<OrderTable> orderTables = new ArrayList<>();
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            Integer tableNumber = (Integer) jsonObject.get("table_number");
            Integer seatNumber = (Integer) jsonObject.get("seat_number");
            orderTables.add(
                    OrderTable.builder()
                            .tableNumber(tableNumber)
                            .seatNumber(seatNumber)
                            .tableStatus(TableStatus.EMPTY)
                            .nameCustomer(null)
                            .build()
            );
        }
        orderTableRepository.saveAll(orderTables);
    }

    private void createListFood() throws FileNotFoundException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(
                    new FileReader("./src/main/resources/data/menu.json")
            );
        } catch (net.minidev.json.parser.ParseException e) {
            throw new RuntimeException(e);
        }

        for (Object food : jsonArray) {
            JSONObject foodObject = (JSONObject) food;
            String name = (String) foodObject.get("name");
            String description = (String) foodObject.get("description");
            Integer category = (Integer) foodObject.get("category");

            Food newFood = createFood(name, description, category);
            foodRepository.save(newFood);

            JSONArray ingredients = (JSONArray) foodObject.get("ingredients");
            for (Object ingredient : ingredients) {
                JSONObject ingredientObject = (JSONObject) ingredient;

                Long id = Long.valueOf((Integer) ingredientObject.get("ingredient_id"));
                Integer portion = (Integer) ingredientObject.get("portion");

                Ingredient existedIngredient = ingredientRepository.findById(id).orElseThrow();
                compositionRepository.save(
                        Composition.builder()
                                .food(newFood)
                                .ingredient(existedIngredient)
                                .portion(portion)
                                .build()
                );
            }
        }
    }

    private Food createFood(String name, String description, Integer category_id) {
        Category category = categoryRepository.findById(category_id).orElseThrow();
        UUID food_id = UUID.randomUUID();
        return new Food(
                food_id,
                name,
                category,
                description,
                faker.internet().image(),
                (long) faker.number().numberBetween(50000, 1000000),
                FoodStatus.DRAFT.getValue(),
                new Date(),
                new Date()
        );
    }

    private void updateFoodStatus(List<Food> foodList) {
        for (Food food : foodList) {
            boolean isOutOfStock = false;
            List<Composition> compositionList = compositionRepository.findByFood(food.getId());
            if (compositionList.isEmpty()) {
                foodRepository.updateStatusAndLastModifiedDateById(
                        FoodStatus.DRAFT.getValue(),
                        new Date(),
                        food.getId()
                );
                food.setStatus(FoodStatus.DRAFT.getValue());
                continue;
            }
            for (Composition composition : compositionList) {
                Long ingredient_id = composition.getIngredient().getId();
                Double quantity = inventoryRepository.findByIngredient_Id(ingredient_id).getQuantity();
                if (composition.getPortion() > quantity) {
                    isOutOfStock = true;
                    break;
                }
            }
            if (isOutOfStock) {
                foodRepository.updateStatusAndLastModifiedDateById(
                        FoodStatus.OUT_OF_STOCK.getValue(),
                        new Date(),
                        food.getId()
                );
                food.setStatus(FoodStatus.OUT_OF_STOCK.getValue());
            } else {
                foodRepository.updateStatusAndLastModifiedDateById(
                        FoodStatus.AVAILABLE.getValue(),
                        new Date(),
                        food.getId()
                );
                food.setStatus(FoodStatus.AVAILABLE.getValue());
            }
        }
    }
}





