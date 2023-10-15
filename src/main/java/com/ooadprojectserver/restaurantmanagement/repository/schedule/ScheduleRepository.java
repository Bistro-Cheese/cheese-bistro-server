package com.ooadprojectserver.restaurantmanagement.repository.schedule;

import com.ooadprojectserver.restaurantmanagement.model.schedule.Schedule;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Schedule findByDayAndShift(DayOfWeek day, Shift shift);
}
