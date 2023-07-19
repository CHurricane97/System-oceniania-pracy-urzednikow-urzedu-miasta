package com.example.town_application.service;

import com.example.town_application.utility.MessageResponse;
import com.example.town_application.utility.requests.utility.AddMotionTypeRequest;
import com.example.town_application.model.MotionType;
import com.example.town_application.model.dto.motionDTOs.MotionTypeDTO;
import com.example.town_application.repository.MotionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotionTypeService {
    private final MotionTypeRepository motionTypeRepository;
    private final ModelMapper modelMapper;
    public List<MotionTypeDTO> getAllMotionTypes() {
        return motionTypeRepository.findAll().stream()
                .map(mapItem -> modelMapper.map(mapItem, MotionTypeDTO.class))
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> addMotionType( AddMotionTypeRequest addMotionTypeRequest) {

        if (motionTypeRepository.existsByTypeAllIgnoreCase(addMotionTypeRequest.getType())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Typ wniosku już w bazie"));
        }
        MotionType motionType =new MotionType(addMotionTypeRequest.getType());
        motionTypeRepository.save(motionType);
        return ResponseEntity.ok(new MessageResponse("Typ wniosku dodany"));
    }


}
