package com.ra.report.service;

import com.ra.report.dto.model.FileDB;
import com.ra.report.dto.model.ScheduleDTO;

import java.util.List;

public interface ScheduleService {

    FileDB filmReport(List<ScheduleDTO> filmSchedules);
}
