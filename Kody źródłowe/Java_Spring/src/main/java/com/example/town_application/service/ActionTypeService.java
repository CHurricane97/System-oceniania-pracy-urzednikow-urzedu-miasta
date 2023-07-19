package com.example.town_application.service;

import com.example.town_application.model.dto.actionDTOs.ActionTypeDTO;
import com.example.town_application.repository.ActionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActionTypeService {
    private final ActionTypeRepository actionTypeRepository;
    private final ModelMapper modelMapper;

    public List<ActionTypeDTO> getAllActionTypes() {
        return actionTypeRepository.findAll().stream()
                .map(mapItem -> modelMapper.map(mapItem, ActionTypeDTO.class))
                .collect(Collectors.toList());
    }
}
