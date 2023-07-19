package com.example.town_application.service;

import com.example.town_application.model.dto.motionDTOs.MotionStateDTO;
import com.example.town_application.repository.MotionStateRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotionStateService {
    private final MotionStateRepository motionStateRepository;
    private final ModelMapper modelMapper;
    public List<MotionStateDTO> getAllMotionStates() {
        return motionStateRepository.findAll().stream()
                .map(mapItem -> modelMapper.map(mapItem, MotionStateDTO.class))
                .collect(Collectors.toList());
    }

}
