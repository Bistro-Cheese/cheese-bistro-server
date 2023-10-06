package com.ooadprojectserver.restaurantmanagement.boostrap;

import com.github.javafaker.Faker;
import com.ooadprojectserver.restaurantmanagement.constant.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.Address;
import com.ooadprojectserver.restaurantmanagement.model.Category;
import com.ooadprojectserver.restaurantmanagement.model.Role;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.Owner;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.RoleRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.ManagerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.OwnerRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Dataseeder implements ApplicationListener<ContextRefreshedEvent>, CommandLineRunner {
    private final Faker faker;
    private final PasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(Dataseeder.class);


    private final RoleRepository roleRepository;
    private final FoodRepository foodRepository;
    private final OwnerRepository ownerRepository;
    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;

    private final List<String> listCategory = new ArrayList<>(
            List.of(
                    "appetizer",
                    "dessert",
                    "drink",
                    "main Course"
            )
    );

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        logger.info("Loading Roles and Categories");
        this.loadRoles();
        this.LoadCategory();
    }

    @Override
    public void run(String... args) throws ParseException {
        logger.info("Loading Food");
        this.createListFakeFood();

        logger.info("Loading Owner");
        this.createListOwner();

        logger.info("Loading Manager");
        this.createListManager();

        logger.info("Loading Staff");
        this.createListStaff();
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

    private void LoadCategory() {
        List<Category> categories = new ArrayList<>(
                List.of(
                        new Category(UUID.randomUUID(), "drink"),
                        new Category(UUID.randomUUID(), "main Course"),
                        new Category(UUID.randomUUID(), "appetizer"),
                        new Category(UUID.randomUUID(), "dessert")
                )
        );
        categories.forEach(category -> {
            Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
            optionalCategory.ifPresentOrElse(System.out::println, () -> categoryRepository.save(category));
        });
    }

    private Category randomCategory() {
        Random categoryName = new Random();
        String randomCategoryName = this.listCategory.get(
                categoryName.nextInt(
                        this.listCategory.size()
                )
        );
        Optional<Category> category = categoryRepository.findByName(randomCategoryName);
        return category.orElse(null);
    }

    private void createListFakeFood() {
        List<Food> listFood = Stream.generate(() ->
                new Food(
                        UUID.randomUUID(),
                        faker.name().name(),
                        randomCategory(),
                        faker.lorem().characters(100),
                        faker.number().numberBetween(100, 400),
                        faker.food().ingredient(),
                        BigDecimal.valueOf(faker.number().numberBetween(50000, 1000000)),
                        faker.number().numberBetween(1, 3),
                        new Date(),
                        new Date()
                )
        ).limit(100).collect(Collectors.toList());
        foodRepository.saveAll(listFood);
    }

    private void createListOwner() throws ParseException {
        String longSdob = "22-09-2003";
        String tuanSdob = "18-07-2003";
        String nhatSdob = "21-02-2003";
        Date longDob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(longSdob);
        Date tuanDob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(tuanSdob);
        Date nhatDob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(nhatSdob);

        ownerRepository.save(Owner.ownerBuilder()
                .username("longowner")
                .firstName("Long")
                .lastName("Tran")
                .dateOfBirth(longDob)
                .password(passwordEncoder.encode("longtran123"))
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .role(3)
                .status(1)
                .address(
                        addressRepository.save(
                                Address.builder()
                                        .addressLine("123 Hoang Dieu 2")
                                        .city("Long An")
                                        .region("Southern")
                                        .build()
                        )
                )
                .licenseBusiness("Restaurant Business License")
                .branch("Thu Duc")
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .enabled(Objects.equals(1, AccountStatus.ACTIVE_STATUS.getValue()))
                .build());

        ownerRepository.save(Owner.ownerBuilder()
                .username("tuanowner")
                .firstName("Tuan")
                .lastName("Nguyen")
                .dateOfBirth(tuanDob)
                .password(passwordEncoder.encode("tuannguyen123"))
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .role(3)
                .status(1)
                .address(
                        addressRepository.save(
                                Address.builder()
                                        .addressLine("124 Hoang Dieu 2")
                                        .city("Quang Tri")
                                        .region("Central")
                                        .build()
                        )
                )
                .licenseBusiness("Restaurant Business License")
                .branch("Di An")
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .enabled(Objects.equals(1, AccountStatus.ACTIVE_STATUS.getValue()))
                .build());

        ownerRepository.save(Owner.ownerBuilder()
                .username("nhatowner")
                .firstName("Nhat")
                .lastName("Nguyen")
                .dateOfBirth(nhatDob)
                .password(passwordEncoder.encode("nhatnguyen123"))
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .role(3)
                .status(1)
                .address(
                        addressRepository.save(
                                Address.builder()
                                        .addressLine("125 Hoang Dieu 2")
                                        .city("Da Nang")
                                        .region("Central")
                                        .build()
                        )
                )
                .licenseBusiness("Restaurant Business License")
                .branch("Binh Thanh")
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .enabled(Objects.equals(1, AccountStatus.ACTIVE_STATUS.getValue()))
                .build());
    }

    private void createListManager() {
        List<Manager> managerList = Stream.generate(() ->
                managerRepository.save(
                        Manager.managerBuilder()
                        .username(faker.name().username())
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dateOfBirth(faker.date().birthday())
                        .password(faker.internet().password())
                        .phoneNumber(faker.phoneNumber().phoneNumber())
                        .role(2)
                        .status(1)
                        .address(
                                addressRepository.save(
                                        Address.builder()
                                                .addressLine(faker.address().streetAddress())
                                                .city(faker.address().cityName())
                                                .region(faker.address().country())
                                                .build()
                                )
                        )
                        .certificationManagement("Restaurant Management Certification")
                        .experiencedYear(String.valueOf(faker.number().numberBetween(1, 5)))
                        .foreignLanguage(faker.nation().language())
                        .createdDate(new Date())
                        .lastModifiedDate(new Date())
                        .enabled(Objects.equals(1, AccountStatus.ACTIVE_STATUS.getValue()))
                        .build())
        ).limit(10).toList();
    }

    private void createListStaff() {
        List<Staff> staffList = Stream.generate(() ->
                staffRepository.save(Staff.staffBuilder()
                        .username(faker.name().username())
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dateOfBirth(faker.date().birthday())
                        .password(faker.internet().password())
                        .phoneNumber(faker.phoneNumber().phoneNumber())
                        .role(1)
                        .status(1)
                        .address(
                                addressRepository.save(
                                        Address.builder()
                                                .addressLine(faker.address().streetAddress())
                                                .city(faker.address().cityName())
                                                .region(faker.address().country())
                                                .build()
                                )
                        )
                        .academicLevel("university")
                        .foreignLanguage(faker.nation().language())
                        .createdDate(new Date())
                        .lastModifiedDate(new Date())
                        .enabled(Objects.equals(1, AccountStatus.ACTIVE_STATUS.getValue()))
                        .build())
        ).limit(50).toList();
    }
}
