package com.example.springbootwithmariadb.controller;

import com.example.springbootwithmariadb.model.Bruh;
import com.example.springbootwithmariadb.model.BruhDto;
import com.example.springbootwithmariadb.model.BruhDtoNoId;
import com.example.springbootwithmariadb.repository.BruhRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("bruhs")
public class BruhController {

    private final BruhRepository bruhRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BruhController(BruhRepository bruhRepository, ModelMapper modelMapper) {
        this.bruhRepository = bruhRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<Bruh> createBruh(BruhDtoNoId bruhDtoNoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bruhRepository.save(modelMapper.map(bruhDtoNoId, Bruh.class)));
    }

    @GetMapping
    public ResponseEntity<List<BruhDto>> getAllBruhs() {
        return ResponseEntity.ok()
                .body(bruhRepository.findAll().stream()
                        .map(b -> modelMapper.map(b, BruhDto.class))
                        .toList()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BruhDto> getBruhById(@PathVariable long id) {
        Optional<Bruh> optionalBruh = bruhRepository.findById(id);

        return ResponseEntity.of(optionalBruh.map(bruh -> modelMapper.map(bruh, BruhDto.class)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BruhDto> deleteBruhById(@PathVariable long id) {
        Optional<Bruh> optionalBruh = bruhRepository.findById(id);
        if (optionalBruh.isPresent()) {
            bruhRepository.deleteById(id);
            return ResponseEntity.of(optionalBruh.map(bruh -> modelMapper.map(bruh, BruhDto.class)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BruhDto> updateBruh(@RequestBody BruhDtoNoId bruhDto, @PathVariable long id) {
        Bruh bruhUpdate = modelMapper.map(bruhDto, Bruh.class);
        Optional<Bruh> bruhToUpdate = bruhRepository.findById(id);
        return ResponseEntity.of(bruhToUpdate.map((Bruh btu) -> {
                    bruhUpdate.setId(btu.getId());
                    return bruhUpdate;
                })
                .map(bruhRepository::save)
                .map(bruh -> modelMapper.map(bruh, BruhDto.class)));
    }
}
