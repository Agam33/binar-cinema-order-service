package com.ra.order.service;

import com.ra.order.dto.model.FileDB;
import com.ra.order.dto.model.ScheduleDTO;

import java.util.List;

public interface ScheduleService {

    FileDB filmReport(List<ScheduleDTO> filmSchedules);
}
