package com.ooadprojectserver.restaurantmanagement.repository.schedule;

import com.ooadprojectserver.restaurantmanagement.model.schedule.Shift;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Timekeeping;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimekeepingRepository extends JpaRepository<Timekeeping, Long> {
    @Query("select t from Timekeeping t where t.manager.username = ?1 and t.schedule.day = ?2 and t.schedule.shift = ?3")
    List<Timekeeping> findManagerSchedule(String username, DayOfWeek day, Shift shift);

    @Query("select t from Timekeeping t where t.staff.username = ?1")
    List<Timekeeping> findStaffSchedule(String username);

    @Query("select t from Timekeeping t where t.staff.username = ?1 and t.schedule.day = ?2 and t.schedule.shift = ?3")
    Optional<Timekeeping> findStaffSchedule(String username, DayOfWeek day, Shift shift);

    @Transactional
    @Modifying
    @Query("delete from Timekeeping t where t.manager = ?1")
    void deleteByManager(Manager manager);

    @Transactional
    @Modifying
    @Query("update Timekeeping t set t.workDate = ?1, t.status = ?2 where t.id = ?3")
    void updateWorkDateAndStatusById(LocalDateTime workDate, Integer status, Long id);

    @Transactional
    @Modifying
    @Query("delete from Timekeeping t where t.staff = ?1")
    void deleteByStaff(Staff staff);
}
